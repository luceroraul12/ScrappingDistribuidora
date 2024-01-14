package distribuidora.scrapping.services.internal;

import java.util.List;

import org.springframework.stereotype.Service;

import distribuidora.scrapping.dto.CategoryHasUnitDto;
import distribuidora.scrapping.dto.ProductoInternoDto;
import distribuidora.scrapping.entities.DatosDistribuidora;

@Service
public interface InventorySystem {

	/**
	 * pide a la base la lista de productos especificos y la interna para luego realizar la actualizacion de precios y
	 * actualizar la base de datos
	 * @return numero de productos que fueron actualizado sus precios
	 * @see #actualizarPrecioConProductosEspecificos(List, List)
	 */
	int actualizarPreciosAutomatico();

	ProductoInternoDto crearProducto(ProductoInternoDto dto);
	
	ProductoInternoDto modificarProducto(ProductoInternoDto dto);
	
	List<ProductoInternoDto> eliminarProductos(List<Integer> productoInternoIds);

	List<ProductoInternoDto> getProductos();

	List<ProductoInternoDto> updateManyProducto(List<ProductoInternoDto> dtos);

	List<CategoryHasUnitDto> getCategoryDtoList();

	CategoryHasUnitDto updateCategoryHasUnit(CategoryHasUnitDto dto);

	List<DatosDistribuidora> getDistribuidoraStatus();
	
	void eliminarIndices();

	boolean existsProducts(List<Integer> productIds);
}
