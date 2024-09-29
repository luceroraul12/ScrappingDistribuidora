package distribuidora.scrapping.util.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.ProductEffectDto;
import distribuidora.scrapping.entities.ProductEffect;

@Component
public class ProductEffectDtoConverter extends Converter<ProductEffect, ProductEffectDto> {
	
	@Autowired
	LookupValueDtoConverter lookupValueDtoConverter;
	
	@Autowired
	LabelDtoConverter labelDtoConverter;
	
	@Autowired
	SimpleProductDtoConverter simpleProductDtoConverter;

	@Override
	public ProductEffectDto toDto(ProductEffect entidad) {
		ProductEffectDto dto = new ProductEffectDto();
		dto.setId(entidad.getId());
		dto.setDescription(entidad.getDescription());
		dto.setProduct(simpleProductDtoConverter.toDto(entidad.getProduct()));
		dto.setType(lookupValueDtoConverter.toDto(entidad.getLvType()));
		dto.setLabel(labelDtoConverter.toDto(entidad.getLabel()));
		return dto;
	}

	@Override
	public ProductEffect toEntidad(ProductEffectDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
