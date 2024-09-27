package distribuidora.scrapping.util.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.ProductEffectDto;
import distribuidora.scrapping.entities.ProductEffect;

@Component
public class ProductEffectDtoConverter extends Converter<ProductEffect, ProductEffectDto> {
	
	@Autowired
	LookupValueDtoConverter lookupValueDtoConverter;

	@Override
	public ProductEffectDto toDto(ProductEffect entidad) {
		ProductEffectDto dto = new ProductEffectDto();
		dto.setId(entidad.getId());
		dto.setDescription(entidad.getDescription());
		dto.setProductId(entidad.getProduct().getId());
		dto.setProductName(entidad.getProduct().getDescripcion());
		dto.setType(lookupValueDtoConverter.toDto(entidad.getLvType()));
		return dto;
	}

	@Override
	public ProductEffect toEntidad(ProductEffectDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
