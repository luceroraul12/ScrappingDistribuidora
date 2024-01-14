package distribuidora.scrapping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.services.OrderService;

@RestController
@RequestMapping("/order/")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("create")
	OrderDto createOrder(@RequestBody OrderDto dto) throws Exception {
		return orderService.createOrder(dto);
	}

	@PostMapping("update")
	OrderDto updateOrder(@RequestBody OrderDto dto) throws Exception {
		return orderService.updateOrder(dto);
	}

	@PostMapping("confirm/{orderId}")
	OrderDto confirmOrder(@PathVariable Integer orderId) throws Exception {
		return orderService.confirmOrder(orderId);
	}

	@PostMapping("finalize/{orderId}")
	OrderDto finalizeOrder(@PathVariable Integer orderId) throws Exception {
		return orderService.finalizeOrder(orderId);
	}

	@PostMapping("delete/{orderId}")
	OrderDto deleteOrder(@PathVariable Integer orderId) throws Exception {
		return orderService.deleteOrder(orderId);
	}
}