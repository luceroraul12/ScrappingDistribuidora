package distribuidora.scrapping.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.dto.ProductEffectParams;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.entities.Label;
import distribuidora.scrapping.entities.ProductEffect;
import distribuidora.scrapping.repositories.postgres.LabelRepository;
import distribuidora.scrapping.repositories.postgres.ProductEffectRepository;

@Service
public class ProductEffectServiceImpl implements ProductEffectService {

	@Autowired
	ProductEffectRepository productEffectRepository;

	@Autowired
	LabelRepository labelRepository;

	@Autowired
	UsuarioService userService;

	@Override
	public List<Label> getLabels() {
		Client client = userService.getCurrentClient();
		return labelRepository.findLabelsByClientId(client.getId());
	}

	@Override
	public List<ProductEffect> getProductEffectsByParams(
			ProductEffectParams params) {
		// tengo que buscar los productos asociados a esos labels
		List<Integer> productIds = productEffectRepository
				.findProductIdsByLabelIds(params.getLabelIds());
		List<ProductEffect> result = productEffectRepository
				.findProductEffectByProductIds(productIds);
		return result;
	}

	@Override
	public void createUpdateLabel(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createUpdateProductEffect(Object object) {
		// TODO Auto-generated method stub

	}

}
