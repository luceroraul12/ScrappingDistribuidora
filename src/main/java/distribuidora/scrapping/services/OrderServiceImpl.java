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
import distribuidora.scrapping.dto.ProductOrderDto;
import distribuidora.scrapping.entities.CategoryHasUnit;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.entities.LookupValor;
import distribuidora.scrapping.entities.ProductoInterno;
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
import distribuidora.scrapping.util.converters.OrderHasProductConverter;
import distribuidora.scrapping.util.converters.ProductHasStatusToProductOrderConverter;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ClientDataService clientDataService;

	@Autowired
	private InventorySystem inventorySystem;

	@Autowired
	private OrderHasProductConverter orderHasProductConverter;

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
		if(CollectionUtils.isNotEmpty(dto.getProducts()))
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
//				dto.setProducts(orderHasProductConverter.toDtoList(p));
				result.add(dto);
			});

		}
		return result;
	}

	@Override
	public OrderDto confirmOrder(Integer orderId) throws Exception {
		Order order = validateOrderExistAndActive(orderId);
		order.setStatus(Constantes.ORDER_STATUS_CONFIRMED);
		orderRepository.save(order);
		return orderConverter.toDto(order);
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
//		orderDto.setProducts(orderHasProductConverter.toDtoList(ohp));
		return orderDto;
	}

	@Override
	public OrderDto updateOrder(OrderDto dto) throws Exception {
		// Me fijo que exista el pedido
		Order order = validateOrderExistAndActive(dto.getId());
		// Busco los productos asociados anteriores y los elimino
		List<OrderHasProduct> ohps = orderHasProductRepository
				.findAllByOrderId(order.getId());
		orderHasProductRepository.deleteAll(ohps);
		// Asocio nuevamente los productos que me manda
//		List<OrderHasProduct> newOhps = configOrderHasProduct(order,
//				dto.getProducts());
//		// Persisto los nuevos productos
//		newOhps = orderHasProductRepository.saveAll(newOhps);
		// Convierto y retorno el dto
		OrderDto result = orderConverter.toDto(order);
//		result.setProducts(orderHasProductConverter.toDtoList(newOhps));
		return result;
	}

	private List<OrderHasProduct> configOrderHasProduct(Order order,
			List<ProductOrderDto> productsDto) throws Exception {
		// Me fijo los productos de la base
		List<Integer> productIds = productsDto.stream()
				.map(p -> p.getProductId()).toList();
		List<ProductoInterno> products = inventorySystem
				.getProductByIds(productIds);
		// Valido que la cantidad de productos enviados sea la misma a l aque
		// existen en la base de datos
		if (products.size() != productsDto.size())
			throw new Exception(
					"Alguno de los productos no existen en el sistema");
		List<OrderHasProduct> orderHasProducts = new ArrayList<>();
		for (ProductOrderDto ohpDto : productsDto) {
			OrderHasProduct ohp = orderHasProductConverter.toEntidad(ohpDto);
			ProductoInterno product = ohp.getProduct();
			ohp.setOrder(order);
			// Le agrego producto para que no tenga conflictos al momento de
			// hacer desconversion
			ohp.setProduct(product);
			orderHasProducts.add(ohp);
		}
		return orderHasProducts;
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

	@Override
	public List<OrderDto> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		orders.sort((a, b) -> b.getDate().compareTo(a.getDate()));
		List<Integer> orderIds = orders.stream().map(o -> o.getId()).toList();
		List<OrderDto> result = orderConverter.toDtoList(orders);
		// Busco todos los productos de cada orden
		List<OrderHasProduct> ohp = orderHasProductRepository
				.findAllByOrderId(orderIds.toArray(Integer[]::new));
		// Recorro cada uno de los pedidos y le agrego sus productos
		for (OrderDto o : result) {
			// Filtro sus productos
			List<OrderHasProduct> ohpSelected = ohp.stream()
					.filter(p -> p.getOrder().getId().equals(o.getId()))
					.toList();
			// Convierto y agrego
//			o.setProducts(orderHasProductConverter.toDtoList(ohpSelected));
		}
		return result;
	}

	@Override
	public List<ProductOrderDto> getProductOrders() {
		// Busco los productos
		List<ProductoInternoStatus> products = productoInternoStatusService
				.getAllEntities();
		// Busco los Lookup de las unidades
		List<CategoryHasUnit> categoryHasUnits = categoryHasUnitRepository
				.findAll();
		List<ProductOrderDto> result = new ArrayList<>();
		// Debo vincular las unidades al producto y colocarle el precio
		products.forEach(pis -> {
			// Identifico la categoria del producto
			LookupValor lvUnit = categoryHasUnits.stream()
					.filter(chu -> chu.getCategory().getId().equals(
							pis.getProductoInterno().getLvCategoria().getId()))
					.findFirst().orElse(null).getUnit();
			// Comienzo a convertir el dto
			ProductOrderDto dto = productHasStatusToProductOrderConverter
					.toDto(pis);
			// agrego los datos de la unidad de la categoria
			dto.setUnitName(lvUnit.getDescripcion());
			dto.setUnitValue(Double.parseDouble(lvUnit.getValor()));
			int unitPrice = (int) (calculatorUtil.calculateCustomerPrice(
					pis.getProductoInterno()) * dto.getUnitValue());
			dto.setUnitPrice(unitPrice);
			result.add(dto);
		});
		return result;
	}
}
