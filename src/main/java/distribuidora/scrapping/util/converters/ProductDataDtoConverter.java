package distribuidora.scrapping.util.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import distribuidora.scrapping.dto.ProductDataDto;
import distribuidora.scrapping.entities.ProductoInterno;
import distribuidora.scrapping.entities.ProductoInternoStatus;
import distribuidora.scrapping.util.CalculatorUtil;

@Component
public class ProductDataDtoConverter
		extends
			Converter<ProductoInternoStatus, ProductDataDto> {

	@Autowired
	CalculatorUtil calculatorUtil;

	@Autowired
	LookupValueDtoConverter lookupValueDtoConverter;

	@Override
	public ProductDataDto toDto(ProductoInternoStatus entidad) {
		ProductDataDto dto = new ProductDataDto();
		ProductoInterno p = entidad.getProductoInterno();
		dto.setId(p.getId());
		dto.setName(p.getNombre());
		dto.setDescription(p.getDescripcion());
		dto.setStock(entidad.getHasStock());
		dto.setOnlyUnit(entidad.getIsUnit());
		dto.setPriceList(calculatorUtil.calculatePriceList(p));
		dto.setCategory(lookupValueDtoConverter.toDto(p.getLvCategoria()));
		return dto;
	}

	@Override
	public ProductoInternoStatus toEntidad(ProductDataDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
