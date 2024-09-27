package distribuidora.scrapping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distribuidora.scrapping.dto.LabelDto;
import distribuidora.scrapping.dto.ProductEffectDto;
import distribuidora.scrapping.dto.ProductEffectParams;
import distribuidora.scrapping.services.ProductEffectService;

@RestController
@RequestMapping("/product-effect")
public class ProductEffectController {

	@Autowired
	ProductEffectService productEffectService;

	@GetMapping
	List<ProductEffectDto> getProductEffectsByLabelIds(
			@RequestBody ProductEffectParams params) {
		return productEffectService.getProductEffectsByParams(params);
	}
	@GetMapping("/label")
	List<LabelDto> getLabels() {
		return productEffectService.getLabels();
	}
	@PostMapping("/label")
	LabelDto createUpdateLabel(@RequestBody LabelDto dto) throws Exception {
		return productEffectService.createUpdateLabel(dto);
	}

}
