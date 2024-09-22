package distribuidora.scrapping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distribuidora.scrapping.dto.ProductEffectParams;
import distribuidora.scrapping.entities.Label;
import distribuidora.scrapping.entities.ProductEffect;
import distribuidora.scrapping.services.ProductEffectService;

@RestController
@RequestMapping("/product-effect")
public class ProductEffectController {
	
	@Autowired
	ProductEffectService productEffectService;
	
	@GetMapping()
	List<ProductEffect> getProductEffectsByLabelIds(@RequestBody ProductEffectParams params){
		return productEffectService.getProductEffectsByParams(params);
	}
	
	@GetMapping("/label")
	List<Label> getLabels(){
		return productEffectService.getLabels();
	}
}
