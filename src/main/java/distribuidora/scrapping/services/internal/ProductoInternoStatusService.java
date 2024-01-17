package distribuidora.scrapping.services.internal;

import java.util.List;

import distribuidora.scrapping.dto.ProductCustomerDto;
import distribuidora.scrapping.dto.ProductoInternoStatusDto;
import distribuidora.scrapping.entities.ProductoInternoStatus;

public interface ProductoInternoStatusService {

	List<ProductoInternoStatusDto> getAll();

	List<ProductoInternoStatus> getAllEntities();
	
	ProductoInternoStatusDto update(ProductoInternoStatusDto dto);

	List<ProductCustomerDto> getProductsForCustomer();

	List<ProductoInternoStatus> getAllByProductIds(List<Integer> productIds);

	void saveAll(List<ProductoInternoStatus> productStatus);
}
