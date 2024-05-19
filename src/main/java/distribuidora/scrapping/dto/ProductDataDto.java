package distribuidora.scrapping.dto;

import lombok.Data;

@Data
public class ProductDataDto {
	private Integer id;
	private Boolean stock;
	private Boolean onlyUnit;
	private String name;
	private String description;
	private Integer priceList;
	private Integer priceUnit;
	private LookupValueDto category;
	private LookupValueDto unitType;
}
