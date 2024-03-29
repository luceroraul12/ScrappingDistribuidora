package distribuidora.scrapping.entities;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;

/**
 * Es el tipo de dato que se obtiene del FrontEnd cuando se quiere hacer una actualizacion por medio de Documento
 * @see distribuidora.scrapping.controller.ActualizacionController
 */
@Getter
public class PeticionExcel extends Peticion {
    private final MultipartFile[] excels;

    @Builder
    public PeticionExcel(String distribuidoraCodigo, MultipartFile[] excels) {
        super(distribuidoraCodigo);
        this.excels = excels;
    }
}
