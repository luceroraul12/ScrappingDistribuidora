package distribuidora.scrapping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.dto.ProductDataDto;
import distribuidora.scrapping.services.OrderService;
import distribuidora.scrapping.services.internal.ProductoInternoStatusService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	ProductoInternoStatusService service;

	@Autowired
	OrderService orderService;

	@GetMapping("/products")
	public List<ProductDataDto> getProducts() {
		return service.getProductsForCustomer();
	}

	// @PostMapping("/order")
	// public OrderDto createOrder(@RequestBody OrderDto order) throws Exception
	// {
	// return orderService.createOrGetActualOrder();
	// }

	@GetMapping("/store/{storeCode}/username/{username}/orders")
	public List<OrderDto> getMyOrders(@PathVariable String storeCode,
			@PathVariable String username) {
		return orderService.getMyOrders(storeCode, username);
	}

}
