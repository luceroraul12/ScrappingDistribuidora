package distribuidora.scrapping.util.converters;

import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.SimpleProductDto;
import distribuidora.scrapping.entities.ProductoInterno;

@Component
public class SimpleProductDtoConverter extends Converter<ProductoInterno, SimpleProductDto>{

	@Override
	public SimpleProductDto toDto(ProductoInterno entidad) {
		SimpleProductDto dto = new SimpleProductDto();
		dto.setId(entidad.getId());
		dto.setName(entidad.getNombre());
		dto.setDescription(entidad.getDescripcion());
		return dto;
	}

	@Override
	public ProductoInterno toEntidad(SimpleProductDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
