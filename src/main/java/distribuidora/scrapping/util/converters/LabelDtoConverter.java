package distribuidora.scrapping.util.converters;

import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.LabelDto;
import distribuidora.scrapping.entities.Label;

@Component
public class LabelDtoConverter extends Converter<Label, LabelDto> {

	@Override
	public LabelDto toDto(Label entidad) {
		LabelDto dto = new LabelDto();
		dto.setId(entidad.getId());
		dto.setName(entidad.getName());
		dto.setDescription(entidad.getDescription());
		return dto;
	}

	@Override
	public Label toEntidad(LabelDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
