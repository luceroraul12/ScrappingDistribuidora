package distribuidora.scrapping.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.dto.LabelDto;
import distribuidora.scrapping.dto.ProductEffectDto;
import distribuidora.scrapping.dto.ProductEffectParams;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.entities.Label;
import distribuidora.scrapping.entities.ProductEffect;
import distribuidora.scrapping.entities.ProductoInterno;
import distribuidora.scrapping.repositories.postgres.LabelRepository;
import distribuidora.scrapping.repositories.postgres.ProductEffectRepository;
import distribuidora.scrapping.services.general.LookupService;
import distribuidora.scrapping.services.internal.InventorySystem;
import distribuidora.scrapping.util.converters.LabelDtoConverter;
import distribuidora.scrapping.util.converters.ProductEffectDtoConverter;

@Service
public class ProductEffectServiceImpl implements ProductEffectService {

	@Autowired
	ProductEffectRepository productEffectRepository;

	@Autowired
	LabelRepository labelRepository;

	@Autowired
	UsuarioService userService;

	@Autowired
	LookupService lookupService;

	@Autowired
	InventorySystem inventorySystem;

	@Autowired
	LabelDtoConverter labelConverter;

	@Autowired
	ProductEffectDtoConverter productEffectConverter;

	@Override
	public List<LabelDto> getLabels() {
		Client client = userService.getCurrentClient();
		List<Label> result = labelRepository
				.findLabelsByClientId(client.getId());
		return labelConverter.toDtoList(result);
	}

	@Override
	public List<ProductEffectDto> getProductEffectsByParams(
			ProductEffectParams params) {
		List<Integer> labelIds = params != null ? params.getLabelIds() : null;
		Client client = userService.getCurrentClient();
		List<ProductEffect> result = productEffectRepository
				.findProductEffectByLabelIds(client.getId(), labelIds);
		return productEffectConverter.toDtoList(result);
	}

	@Override
	public LabelDto createUpdateLabel(LabelDto dto) throws Exception {
		if (dto == null)
			throw new Exception("Debe mandar la etiqueta a crear / modificar");

		Label label = null;
		Client client = userService.getCurrentClient();
		Label labelExisted = labelRepository
				.findByNameAndClientId(dto.getName(), client.getId());
		if (dto.getId() != null)
			label = labelRepository.findById(dto.getId()).orElse(null);
		else if (labelExisted != null) {
			label = labelExisted;
		} else {
			label = new Label();
			label.setClient(client);
		}

		label.setName(dto.getName());
		label.setDescription(dto.getDescription());

		label = labelRepository.save(label);
		return labelConverter.toDto(label);
	}

	@Override
	public ProductEffectDto createUpdateProductEffect(ProductEffectDto dto)
			throws Exception {
		if (dto == null)
			throw new Exception("Debe mandar el efecto a crear / modificar");

		ProductEffect productEffect = null;
		if (dto.getId() != null)
			productEffect = productEffectRepository.findById(dto.getId())
					.orElse(null);
		else {
			productEffect = new ProductEffect();

			productEffect.setLabel(labelRepository
					.findById(dto.getLabel().getId()).orElse(null));
			productEffect.setLvType(lookupService
					.getLookupValueByCode(dto.getType().getCode()));
			ProductoInterno product = inventorySystem
					.getProductByIds(Arrays.asList(dto.getProduct().getId())).stream()
					.findFirst().orElse(null);
			productEffect.setProduct(product);
		}
		productEffect.setDescription(dto.getDescription());

		productEffect = productEffectRepository.save(productEffect);

		return productEffectConverter.toDto(productEffect);
	}

	@Override
	public Integer deleteLabelById(Integer id) {
		labelRepository.deleteById(id);
		return id;
	}

	@Override
	public Integer deleteProductEffectById(Integer id) {
		productEffectRepository.deleteById(id);
		return id;
	}
	
	

}
