package distribuidora.scrapping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.services.OrderService;

@RestController
@RequestMapping("/order/")
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("")
	OrderDto createOrGetActualOrder() throws Exception {
		return orderService.createOrGetActualOrder();
	}

	@PostMapping("finalize/{orderId}")
	OrderDto finalizeOrder(@PathVariable Integer orderId) throws Exception {
		return orderService.finalizeOrder(orderId);
	}

	@DeleteMapping("{orderId}")
	OrderDto deleteOrder(@PathVariable Integer orderId) throws Exception {
		return orderService.deleteOrder(orderId);
	}
}
