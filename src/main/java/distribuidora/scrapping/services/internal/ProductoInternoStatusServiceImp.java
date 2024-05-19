package distribuidora.scrapping.services.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.dto.ProductCustomerDto;
import distribuidora.scrapping.dto.ProductoInternoStatusDto;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.entities.LookupValor;
import distribuidora.scrapping.entities.ProductoInternoStatus;
import distribuidora.scrapping.repositories.postgres.CategoryHasUnitRepository;
import distribuidora.scrapping.repositories.postgres.ProductoInternoStatusRepository;
import distribuidora.scrapping.services.UsuarioService;
import distribuidora.scrapping.util.converters.LookupValueDtoConverter;
import distribuidora.scrapping.util.converters.ProductCustomerDtoConverter;
import distribuidora.scrapping.util.converters.ProductoInternoStatusConverter;

@Service
public class ProductoInternoStatusServiceImp
		implements
			ProductoInternoStatusService {

	@Autowired
	ProductoInternoStatusRepository repository;

	@Autowired
	CategoryHasUnitRepository categoryHasUnitRepository;

	@Autowired
	ProductoInternoStatusConverter converter;

	@Autowired
	ProductCustomerDtoConverter productCustomerDtoConverter;

	@Autowired
	LookupValueDtoConverter lookupValueDtoConverter;

	@Autowired
	UsuarioService userService;

	@Override
	public List<ProductoInternoStatusDto> getByClientId(Integer clientId)
			throws Exception {
		if (clientId == null) {
			throw new Exception("La tienda no existe");
		}
		List<ProductoInternoStatusDto> result = converter
				.toDtoList(repository.findByClientId(clientId));
		configLowAmount(result);
		return result;
	}

	@Override
	public ProductoInternoStatusDto update(ProductoInternoStatusDto dto) {
		ProductoInternoStatus entity = converter.toEntidad(dto);
		dto = converter.toDto(repository.save(entity));
		configLowAmount(dto);
		return dto;
	}

	@Override
	public List<ProductCustomerDto> getProductsForCustomer() {
		List<ProductoInternoStatus> entities = repository.findAll();
		List<ProductCustomerDto> dtos = productCustomerDtoConverter
				.toDtoList(entities);
		// Busco las relaciones de las categorias con las unidades
		Map<Integer, LookupValor> mapUnitByCategoryId = categoryHasUnitRepository
				.findAll().stream().collect(Collectors
						.toMap(r -> r.getCategory().getId(), r -> r.getUnit()));
		// Recorro cada dto para asignarle su unidad
		for (ProductCustomerDto d : dtos) {
			LookupValor unit = mapUnitByCategoryId
					.getOrDefault(d.getCategory().getId(), null);
			if (unit != null)
				d.setUnitType(lookupValueDtoConverter.toDto(unit));
		}
		return dtos;
	}

	@Override
	public List<ProductoInternoStatus> getAllByProductIds(
			List<Integer> productIds) {
		return repository.findAllByProductIds(productIds);
	}

	@Override
	public void saveAll(List<ProductoInternoStatus> productStatus) {
		repository.saveAll(productStatus);
	}

	/**
	 * Me fijo segun una constante si la cantidad de stock es poca o no. Es
	 * posible que esto cambie y base en otra cosa o utilice la base de datos
	 * 
	 * @param result
	 */
	private void configLowAmount(List<ProductoInternoStatusDto> result) {
		result.forEach(pis -> configLowAmount(pis));
	}

	/**
	 * Me fijo segun una constante si la cantidad de stock es poca o no. Es
	 * posible que esto cambie y base en otra cosa o utilice la base de datos
	 * 
	 * @param result
	 */
	private void configLowAmount(ProductoInternoStatusDto dto) {
		int minAmount = 2;
		dto.setLowAmount(dto.getAmount() < minAmount);
	}

	@Override
	public List<ProductoInternoStatus> getAllEntities() {
		return repository
				.findByClientId(userService.getCurrentClient().getId());
	}

	@Override
	public ProductCustomerDto getProductToOrderById(Integer productId) {
		Client client = userService.getCurrentClient();

		ProductoInternoStatus product = repository.findByProductId(productId,
				client.getId());
		ProductCustomerDto dto = productCustomerDtoConverter.toDto(product);
		setDataToClientList(Arrays.asList(dto));
		return dto;
	}

	@Override
	public void setDataToClientList(
			List<ProductCustomerDto> dtos) {
		// Busco las relaciones de las categorias con las unidades
		Map<Integer, LookupValor> mapUnitByCategoryId = categoryHasUnitRepository
				.findAll().stream().collect(Collectors
						.toMap(r -> r.getCategory().getId(), r -> r.getUnit()));
		dtos.forEach(dto -> {
			// Recorro cada dto para asignarle su unidad
			LookupValor unit = mapUnitByCategoryId
					.getOrDefault(dto.getCategory().getId(), null);
			if (unit != null) {
				dto.setUnitType(lookupValueDtoConverter.toDto(unit));
				Double percent = Double.parseDouble(unit.getValor());
				double result = dto.getPrice() * percent;
				dto.setPriceUnit((int) Math.round(result));
			}
		});
	}
}
