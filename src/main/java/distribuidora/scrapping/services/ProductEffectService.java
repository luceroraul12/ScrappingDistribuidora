package distribuidora.scrapping.services;

import java.util.List;

import distribuidora.scrapping.dto.ProductEffectParams;
import distribuidora.scrapping.entities.Label;
import distribuidora.scrapping.entities.ProductEffect;

public interface ProductEffectService {

	List<Label> getLabels();
	
	List<ProductEffect> getProductEffectsByParams(ProductEffectParams params);
	
	void createUpdateLabel(Object object);
	
	void createUpdateProductEffect(Object object);
	
}
