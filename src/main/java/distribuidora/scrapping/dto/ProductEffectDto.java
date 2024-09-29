package distribuidora.scrapping.dto;

import lombok.Data;

@Data
public class ProductEffectDto {
	private Integer id;
	private LookupValueDto type;
	private LabelDto label;
	private SimpleProductDto product;
	private String description;
}
