package distribuidora.scrapping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@PostMapping
	ProductEffectDto createUpdateProductEffect(@RequestBody ProductEffectDto dto) throws Exception {
		return productEffectService.createUpdateProductEffect(dto);
	}
	
	@DeleteMapping("/{id}")
	Integer deleteProductEffect(@PathVariable Integer id) {
		return productEffectService.deleteProductEffectById(id);
	}
	
	// label
	@GetMapping("/label")
	List<LabelDto> getLabels() {
		return productEffectService.getLabels();
	}
	@PostMapping("/label")
	LabelDto createUpdateLabel(@RequestBody LabelDto dto) throws Exception {
		return productEffectService.createUpdateLabel(dto);
	}
	
	@DeleteMapping("/label/{id}")
	Integer deleteLabel(@PathVariable Integer id) throws Exception {
		return productEffectService.deleteLabelById(id);
	}

}
