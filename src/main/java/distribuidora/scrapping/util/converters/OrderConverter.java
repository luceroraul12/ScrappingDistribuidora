package distribuidora.scrapping.util.converters;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.entities.ProductoInternoStatus;
import distribuidora.scrapping.entities.customer.Order;

@Component
public class OrderConverter extends Converter<Order, OrderDto>{
	
	@Autowired
	ProductCustomerDtoConverter orderHasProductConverter;

	@Override
	public OrderDto toDto(Order entidad) {
		OrderDto dto = new OrderDto();
		dto.setDate(entidad.getDate());
		dto.setId(entidad.getId());
		if (entidad.getClient() != null) 
			dto.setStoreCode(entidad.getClient().getName());	
		if (entidad.getCustomer() != null)
			dto.setUsername(entidad.getCustomer().getUsername());
		if (CollectionUtils.isNotEmpty(entidad.getOrderHasProducts())) {
			List<ProductoInternoStatus> relations = entidad.getOrderHasProducts().stream().map(p -> p.getProduct().getProdInternoStatus()).toList();
			dto.setProducts(orderHasProductConverter.toDtoList(relations));
		}
		dto.setStatus(entidad.getStatus());
		return dto;
	}

	@Override
	public Order toEntidad(OrderDto dto) {
//		Order e = new Order();
//		e.setDate(dto.getDate());
//		e.setId(dto.getId());
//		return e;
		return null;
	}

}
