package distribuidora.scrapping.dto;

import lombok.Data;

@Data
public class ProductOrderDto {
	private Integer id;
	private Double amount;
	private ProductDataDto data;
}
