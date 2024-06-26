package distribuidora.scrapping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import distribuidora.scrapping.dto.DatosDistribuidoraDto;
import distribuidora.scrapping.entities.UpdateRequest;
import distribuidora.scrapping.repositories.DatosDistribuidoraRepository;
import distribuidora.scrapping.services.UpdaterService;
import distribuidora.scrapping.services.internal.InventorySystem;

/**
 * Tiene la finalidad de realizar la actualizacion de datos por distribuidora
 * como tambien el estado de cada una.
 * 
 * @see ActualizacionPorDocumentoServicio
 */
@RestController
@RequestMapping(value = "/actualizar")
public class ActualizacionController {
	@Autowired
	DatosDistribuidoraRepository datosDistribuidoraRepository;

	@Autowired
	InventorySystem inventorySystem;

	@Autowired
	UpdaterService updaterService;

	/**
	 * Permite actualizar los productos de las distribuidoras que dependan de
	 * excel. Actualiza una Distribuidora a la vez
	 * 
	 * @param documento
	 *            Debe contener todas sus partes.
	 * @throws Exception
	 * @see UpdateRequestExcel
	 */
	@PostMapping("/update")

	public DatosDistribuidoraDto update(
			@RequestPart(required = false) MultipartFile[] excels,
			@RequestParam String code) throws Exception {
		return updaterService.update(new UpdateRequest(code, excels));
	}

	/**
	 * Devuelve un map con el tipo de busqueda por distribuidora
	 * 
	 * @return map.excel dependen del excel, map.webScrapping dependen de Web
	 *         Scrapping map.webScrapping es un map de fecha Realizado y
	 *         Distribuidora
	 */
	@GetMapping
	public List<DatosDistribuidoraDto> obtenerTipoyEstadoDeDistribuidora() {
		return inventorySystem.getDistribuidoraStatus();
	}

	@GetMapping("/eliminarIndices")
	public void eliminarIndices() {
		inventorySystem.eliminarIndices();
	}

}
