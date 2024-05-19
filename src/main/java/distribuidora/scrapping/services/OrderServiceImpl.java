package distribuidora.scrapping.services;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.configs.Constantes;
import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.entities.ProductoInternoStatus;
import distribuidora.scrapping.entities.customer.Customer;
import distribuidora.scrapping.entities.customer.Order;
import distribuidora.scrapping.entities.customer.OrderHasProduct;
import distribuidora.scrapping.repositories.ClientHasUsersRepository;
import distribuidora.scrapping.repositories.CustomerRepository;
import distribuidora.scrapping.repositories.OrderHasProductRepository;
import distribuidora.scrapping.repositories.OrderRepository;
import distribuidora.scrapping.repositories.postgres.CategoryHasUnitRepository;
import distribuidora.scrapping.security.entity.UsuarioEntity;
import distribuidora.scrapping.services.internal.InventorySystem;
import distribuidora.scrapping.services.internal.ProductoInternoStatusService;
import distribuidora.scrapping.util.CalculatorUtil;
import distribuidora.scrapping.util.converters.OrderConverter;
import distribuidora.scrapping.util.converters.ProductHasStatusToProductOrderConverter;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ClientDataService clientDataService;

	@Autowired
	private InventorySystem inventorySystem;

	// @Autowired
	// private OrderHasProductConverter orderHasProductConverter;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderHasProductRepository orderHasProductRepository;

	@Autowired
	private OrderConverter orderConverter;

	@Autowired
	private ProductoInternoStatusService productoInternoStatusService;

	@Autowired
	private ProductHasStatusToProductOrderConverter productHasStatusToProductOrderConverter;

	@Autowired
	private CategoryHasUnitRepository categoryHasUnitRepository;

	@Autowired
	private CalculatorUtil calculatorUtil;

	@Autowired
	private ClientHasUsersRepository clientHasUsersRepository;

	@Autowired
	private UsuarioService userService;

	@Override
	public OrderDto createOrGetActualOrder() throws Exception {
		// Busco la tienda cliente
		// Busco el usuario
		// TODO: En proximas versiones hay que ver si es necesario diferenciar
		// el pedido en funcion del usuario logeado en la tienda
		Client client = userService.getCurrentClient();
		if (client == null)
			throw new Exception(
					"La tienda desde la que pregunta por pedidos no existe");

		Order order = orderRepository
				.findOrderPendingByClientId(client.getId());
		// En caso de no existir tengo que crearlo
		if (order == null) {
			order = new Order(client);
			order = orderRepository.save(order);
		}

		// Me fijo si tiene productos cargados
		OrderDto dto = orderConverter.toDto(order);
		// Le agrego las unidades / precio a los productos que faltaban
		if (CollectionUtils.isNotEmpty(dto.getProducts()))
			productoInternoStatusService.setDataToClientList(dto.getProducts());

		return dto;
	}

	private Client validateClient(OrderDto order) throws Exception {
		// Verifico si el usuario ya existe
		UsuarioEntity user = userService.getCurrentUser();
		Client client = clientHasUsersRepository.findByClientId(user.getId())
				.getClient();

		// En caso de que no exista lo voy a registrar
		if (client == null)
			throw new Exception("No existe la tienda solicitada");
		return client;
	}

	private Customer validateCustomer(OrderDto order) {
		// Verifico si el usuario ya existe
		Customer customer = customerRepository
				.findByUsername(order.getUsername());
		// En caso de que no exista lo voy a registrar
		if (customer == null) {
			customer = new Customer();
			customer.setUsername(order.getUsername());
			customer = customerRepository.save(customer);
		}
		return customer;
	}

	@Override
	public List<OrderDto> getMyOrders(String storeCode, String username) {
		List<OrderHasProduct> relations = orderHasProductRepository
				.findByStoreCodeAndUsername(storeCode, username);
		// Agrupo por orden y las ordeno por fecha
		TreeMap<Order, List<OrderHasProduct>> treeProductByOrder = new TreeMap<>(
				(a, b) -> b.getDate().compareTo(a.getDate()));
		treeProductByOrder.putAll(relations.stream()
				.collect(Collectors.groupingBy(r -> r.getOrder())));
		List<OrderDto> result = new ArrayList();
		if (!treeProductByOrder.isEmpty()) {
			treeProductByOrder.forEach((o, p) -> {
				OrderDto dto = new OrderDto();
				dto.setId(o.getId());
				dto.setUsername(o.getCustomer().getUsername());
				dto.setStoreCode(o.getClient().getName());
				dto.setDate(o.getDate());
				// dto.setProducts(orderHasProductConverter.toDtoList(p));
				result.add(dto);
			});

		}
		return result;
	}

	@Override
	public OrderDto finalizeOrder(Integer orderId) throws Exception {
		// Validar orden
		Order order = validateOrderExistAndActive(orderId);
		// Entrega de pedidos (simbolica de momento)
		// Finalizar orden
		order.setStatus(Constantes.ORDER_STATUS_FINALIZED);
		orderRepository.save(order);
		// Restar al stock actual de cada uno de los productos
		List<OrderHasProduct> ohps = orderHasProductRepository
				.findAllByOrderId(orderId);
		List<Integer> productIds = ohps.stream()
				.map(o -> o.getProduct().getId()).toList();
		// Busco los estados de cada uno de los productos
		List<ProductoInternoStatus> productStatus = productoInternoStatusService
				.getAllByProductIds(productIds);
		// Acceso rapido map de OrderHasProduct
		// Redujo stock
		for (ProductoInternoStatus pis : productStatus) {
			OrderHasProduct ohpSelected = ohps.stream()
					.filter(o -> o.getProduct().getId()
							.equals(pis.getProductoInterno().getId()))
					.findFirst().orElse(null);
			Double newAmount = pis.getAmount() - ohpSelected.getAmount();
			// Hago que el stock sea positivo o 0 cuando quiso ser stock
			// negativo
			newAmount = newAmount < 0 ? 0.0 : newAmount;
			pis.setAmount(newAmount);
		}
		// Persisto el nuevo Stock
		productoInternoStatusService.saveAll(productStatus);
		return orderConverter.toDto(order);
	}

	@Override
	public OrderDto deleteOrder(Integer orderId) throws Exception {
		// Busco el pedido
		Order order = validateOrderExistAndActive(orderId);
		// Cambio el estado y persisto
		order.setStatus(Constantes.ORDER_STATUS_INACTIVE);
		order = orderRepository.save(order);
		// Busco los productos para poder mostrarlos
		OrderDto orderDto = orderConverter.toDto(order);
		List<OrderHasProduct> ohp = orderHasProductRepository
				.findAllByOrderId(orderId);
		// orderDto.setProducts(orderHasProductConverter.toDtoList(ohp));
		return orderDto;
	}

	private Order validateOrderExistAndActive(Integer orderId)
			throws Exception {
		Order order = orderRepository.findById(orderId).orElse(null);
		// Me fijo que exista
		if (order == null)
			throw new Exception("La orden no existe");
		// Me fijo que no se encuentre eliminada previamente
		if (order.getStatus().equals(Constantes.ORDER_STATUS_INACTIVE))
			throw new Exception("El pedido ya se encuentra eliminado");
		return order;
	}

}
