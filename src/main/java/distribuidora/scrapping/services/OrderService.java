package distribuidora.scrapping.services;

import java.util.List;

import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.dto.ProductOrderDto;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.security.entity.UsuarioEntity;

public interface OrderService {

	/**
	 * Se fija si la tienda actual puede crear una nueva orden.
	 * Si existia una creada, va a retomarla
	 * @param order
	 * @return
	 * @throws Exception
	 */
	OrderDto createOrGetActualOrder() throws Exception;

	/**
	 * Es para obtener el historial de ordenes realizadas en cierta tienda
	 * @param storeCode
	 * @param username
	 * @return
	 */
	List<OrderDto> getMyOrders(String storeCode, String username);
	
	/**
	 * Es para que luego de {@link #createOrGetActualOrder(OrderDto)} la tienda pueda validar el pedido en precios, productos y cantidades
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	OrderDto confirmOrder(Integer orderId) throws Exception;
	
	/**
	 * Es para que luego de {@link #confirmOrder(OrderDto)} el cliene abone el dinero y se descuente las cantidades del producto
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	OrderDto finalizeOrder(Integer orderId) throws Exception;

	OrderDto deleteOrder(Integer orderId) throws Exception;

	OrderDto updateOrder(OrderDto dto) throws Exception;

	List<OrderDto> getAllOrders();
	
	List<ProductOrderDto> getProductOrders();
}
