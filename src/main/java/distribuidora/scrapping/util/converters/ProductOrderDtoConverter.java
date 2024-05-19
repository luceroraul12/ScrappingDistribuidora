package distribuidora.scrapping.util.converters;

import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.ProductOrderDto;
import distribuidora.scrapping.entities.customer.OrderHasProduct;

@Component
public class ProductOrderDtoConverter
		extends
			Converter<OrderHasProduct, ProductOrderDto> {
	


	@Override
	public ProductOrderDto toDto(OrderHasProduct entidad) {
		ProductOrderDto dto = new ProductOrderDto();
		dto.setId(entidad.getId());
		dto.setAmount(entidad.getAmount());
		dto.setData(null);
		return dto;
	}

	@Override
	public OrderHasProduct toEntidad(ProductOrderDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
