package distribuidora.scrapping.entities;

import distribuidora.scrapping.enums.TipoDistribuidora;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@Document(collection = "DatosDistribuidora")
public class DatosDistribuidora {
    @Id
    private String id;
    private String distribuidoraCodigo;
    private TipoDistribuidora tipo;
    private Date fechaActualizacion;
    private Integer cantidadDeProductosAlmacenados;
}
