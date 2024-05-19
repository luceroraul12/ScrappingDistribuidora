package distribuidora.scrapping.util.converters;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.OrderDto;
import distribuidora.scrapping.entities.customer.Order;

@Component
public class OrderDtoConverter extends Converter<Order, OrderDto> {

	@Autowired
	ProductOrderDtoConverter productOrderDtoConverter;

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
			dto.setProducts(productOrderDtoConverter
					.toDtoList(entidad.getOrderHasProducts()));
		}
		dto.setStatus(entidad.getStatus());
		return dto;
	}

	@Override
	public Order toEntidad(OrderDto dto) {
		// Order e = new Order();
		// e.setDate(dto.getDate());
		// e.setId(dto.getId());
		// return e;
		return null;
	}

}
