package distribuidora.scrapping.util.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.ProductOrderDto;
import distribuidora.scrapping.entities.customer.OrderHasProduct;

@Component
public class ProductOrderDtoConverter
		extends
			Converter<OrderHasProduct, ProductOrderDto> {
	
	@Autowired
	ProductDataDtoConverter productDataDtoConverter;


	@Override
	public ProductOrderDto toDto(OrderHasProduct entidad) {
		ProductOrderDto dto = new ProductOrderDto();
		dto.setId(entidad.getId());
		dto.setAmount(entidad.getAmount());
		dto.setData(productDataDtoConverter.toDto(entidad.getProduct().getProdInternoStatus()));
		return dto;
	}

	@Override
	public OrderHasProduct toEntidad(ProductOrderDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
