package distribuidora.scrapping.services.internal;

import java.util.List;

import distribuidora.scrapping.dto.ProductDataDto;
import distribuidora.scrapping.dto.ProductoInternoStatusDto;
import distribuidora.scrapping.entities.ProductoInternoStatus;

public interface ProductoInternoStatusService {

	List<ProductoInternoStatusDto> getByClientId(Integer clientId) throws Exception;

	List<ProductoInternoStatus> getAllEntities();
	
	ProductoInternoStatusDto update(ProductoInternoStatusDto dto);

	List<ProductDataDto> getProductsForCustomer();

	List<ProductoInternoStatus> getAllByProductIds(List<Integer> productIds);

	void saveAll(List<ProductoInternoStatus> productStatus);

	ProductDataDto getProductToOrderById(Integer productId);
	
	void setDataToClientList(List<ProductDataDto> dtos);
}
