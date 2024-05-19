package distribuidora.scrapping.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import distribuidora.scrapping.dto.CategoryHasUnitDto;
import distribuidora.scrapping.dto.ProductoInternoDto;
import distribuidora.scrapping.dto.ProductoInternoStatusDto;
import distribuidora.scrapping.repositories.postgres.ProductoInternoRepository;
import distribuidora.scrapping.services.UsuarioService;
import distribuidora.scrapping.services.internal.InventorySystem;
import distribuidora.scrapping.services.internal.ProductoInternoStatusService;
import distribuidora.scrapping.services.pdf.PdfService;

@RestController()
@RequestMapping("/inventory-system/")
public class InventorySystemController {

	@Autowired
	ProductoInternoRepository repository;

	@Autowired
	InventorySystem service;

	@Autowired
	ProductoInternoStatusService productoInternoStatusService;

	@Autowired
	PdfService pdfService;

	@Autowired
	UsuarioService userService;

	@PostMapping(value = "create")
	ProductoInternoDto crearProducto(@RequestBody ProductoInternoDto dto) {
		return service.crearProducto(dto);
	}

	@PutMapping(value = "update")
	ProductoInternoDto modificarProducto(@RequestBody ProductoInternoDto dto)
			throws Exception {
		return service.modificarProducto(dto);
	}

	@PutMapping(value = "updates")
	List<ProductoInternoDto> updateManyProducto(
			@RequestBody List<ProductoInternoDto> dtos) throws Exception {
		return service.updateManyProducto(dtos);
	}

	@GetMapping
	List<ProductoInternoDto> getProductos() throws Exception {
		return service.getProductos();
	}

	@GetMapping("{productId}")
	ProductoInternoDto getProductById(
			@PathVariable("productId") Integer productId) throws Exception {
		return service.getProductById(productId);
	}

	@DeleteMapping(value = "delete")
	List<ProductoInternoDto> eliminarProductos(
			@RequestBody List<Integer> dtos) {
		return service.eliminarProductos(dtos);
	}

	@GetMapping("updateAll")
	List<ProductoInternoDto> actualizarAllProductos() throws Exception {
		service.actualizarPreciosAutomatico();
		return getProductos();
	}

	@GetMapping("pdf")
	void getPDF(HttpServletResponse response)
			throws DocumentException, IOException {
		pdfService.getPdfByClientId(response,
				userService.getCurrentClient().getId());
	}

	@GetMapping("status")
	List<ProductoInternoStatusDto> getStatus() throws Exception {
		return productoInternoStatusService
				.getByClientId(userService.getCurrentClient().getId());
	}

	@PutMapping("status")
	ProductoInternoStatusDto updateStatus(
			@RequestBody ProductoInternoStatusDto dto) {
		return productoInternoStatusService.update(dto);
	}

	@GetMapping("categories")
	List<CategoryHasUnitDto> getCategoryDtoList() {
		return service.getCategoryDtoList();
	}

	@PutMapping("categories")
	CategoryHasUnitDto updateCategoryHasUnit(
			@RequestBody CategoryHasUnitDto dto) {
		return service.updateCategoryHasUnit(dto);
	}
}
