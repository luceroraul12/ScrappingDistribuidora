package distribuidora.scrapping.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.dto.ExternalProductDto;
import distribuidora.scrapping.dto.LookupValueDto;
import distribuidora.scrapping.entities.ExternalProduct;
import distribuidora.scrapping.entities.LookupValor;
import distribuidora.scrapping.repositories.postgres.ExternalProductRepository;
import distribuidora.scrapping.services.general.LookupService;
import distribuidora.scrapping.util.converters.ExternalProductDtoConverter;
import io.jsonwebtoken.lang.Collections;

/**
 * Encargada de la collecion de productos finales.
 */
@Service
public class ExternalProductService {

	@Autowired
	ExternalProductRepository productoRepository;
	
	@Autowired
	LookupService lookupService;
	
	@Autowired
	ExternalProductDtoConverter externalProductDtoConverter;

	/**
	 * Actualiza los productos de cierta distribuidora. Almacena productos, en
	 * caso de que hayan existentes, los borra y guarda los nuevos
	 * 
	 * @param productos
	 *            productos que se quiere almacenar.
	 * @param distribuidora
	 *            distribuidora con la que se quiere trabajar.
	 */
	public void actualizarProductosPorDistribuidora(List<ExternalProduct> productos,
			String distribuidoraCodigo) {
		//TODO: Adaptar a SQL
		Date date = new Date();
		productos.forEach(p -> p.setDate(date));
		// Hago la validacion de productos existentes
		List<ExternalProduct> productExisted = getByDistribuidoraCodeAndProductCode(
				distribuidoraCodigo, null);
		List<ExternalProduct> productToUpdate = new ArrayList();
		List<ExternalProduct> productToNew = new ArrayList<>();
		LookupValor lvDistribuidora = lookupService.getLookupValueByCode(distribuidoraCodigo);
		for (ExternalProduct p : productos) {
			// Actualizo el lookup a cada producto
			p.setDistribuidora(lvDistribuidora);
			boolean isRepeated = false;
			for (ExternalProduct pE : productExisted) {
				if(p.getTitle().equals(pE.getTitle())) {
					// Actualizo al antiguo producto, los valores nuevos
					pE.setDate(date);
					pE.setPrice(p.getPrecioPorCantidadEspecifica());
					pE.setTitle(p.getTitle());
					// Agrego al listado
					productToUpdate.add(pE);
					isRepeated = true;
				}
			}
			if(!isRepeated)
				productToNew.add(p);
		}
		// En una unica lista meto todos los productos y los persisto
		productToNew.addAll(productToUpdate);
		productoRepository.saveAll(productToNew);
	}

	public Set<ExternalProductDto> getBySearch(String search) {
		List<String> values = Stream.of(search.split(" ")).toList();
		Set<ExternalProductDto> result = new HashSet<>();
		for (String v : values) {
			List<ExternalProductDto> innerResult = externalProductDtoConverter.toDtoList(this.productoRepository
					.findBySearch(v.toUpperCase()));
			if(CollectionUtils.isNotEmpty(innerResult))
				result.addAll(innerResult);
		}
		return result; 
	}

	public List<ExternalProduct> getByDistribuidoraCodeAndProductCode(
			String distribuidoraCodigo, String idReferencia) {
		return this.productoRepository.findByDistribuidoraCodeAndProductCode(
				distribuidoraCodigo, idReferencia);
	}

	public Integer countProductosByDistribuidoraCode(
			String distribuidoraCode) {
		return productoRepository.countExternalProductsByDistribuidoraCode(distribuidoraCode);
	}
}
