package distribuidora.scrapping.services.excel;

import distribuidora.scrapping.comunicadores.Comunicador;
import distribuidora.scrapping.entities.PeticionFrontEndDocumento;
import distribuidora.scrapping.enums.Distribuidora;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class ActualizacionPorDocumentoServicio {
    @Autowired
    private IndiasExcelServicio indiasExcelServicio;
    @Autowired
    private FacundoExcelServicio facundoExcelServicio;
    @Autowired
    private Comunicador comunicador;

    public void recibirDocumento(PeticionFrontEndDocumento documento) throws IOException {
        this.comunicador.getDisparadorActualizacionExcelPorDistribuidora().onNext(documento);
    }
    private void identificarDistribuidorayEjecutar(PeticionFrontEndDocumento documento) throws IOException {
        switch (documento.getDistribuidora()){
            case FACUNDO: {
                facundoExcelServicio.generarProductosEntidadYActualizarCollecciones(documento);
                break;
            }
            case INDIAS: {
                indiasExcelServicio.generarProductosEntidadYActualizarCollecciones(documento);
                break;
            }
        }
    }
}
