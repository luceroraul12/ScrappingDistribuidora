package distribuidora.scrapping.services;

import java.util.List;

import distribuidora.scrapping.dto.LabelDto;
import distribuidora.scrapping.dto.ProductEffectDto;
import distribuidora.scrapping.dto.ProductEffectParams;

public interface ProductEffectService {

	List<LabelDto> getLabels();

	List<ProductEffectDto> getProductEffectsByParams(
			ProductEffectParams params);

	LabelDto createUpdateLabel(LabelDto dto) throws Exception;

	ProductEffectDto createUpdateProductEffect(ProductEffectDto dto) throws Exception;

}
