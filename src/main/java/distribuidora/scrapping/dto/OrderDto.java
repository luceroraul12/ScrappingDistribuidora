package distribuidora.scrapping.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrderDto {
	private Integer id;
	private String username;
	private String storeCode;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date date;
	private List<ProductOrderDto> products;
	private String status;
}
