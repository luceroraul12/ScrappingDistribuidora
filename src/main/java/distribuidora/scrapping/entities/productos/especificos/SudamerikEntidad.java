package distribuidora.scrapping.entities.productos.especificos;


import java.util.List;

import distribuidora.scrapping.entities.ProductoEspecifico;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SudamerikEntidad extends ProductoEspecifico {
    private String nombreProducto;
    private List<SudamerikConjuntoEspecifico> cantidadesEspecificas;

    @Builder
    public SudamerikEntidad(String id, String distribuidoraCodigo, String nombreProducto,
			List<SudamerikConjuntoEspecifico> cantidadesEspecificas) {
		super(id, distribuidoraCodigo);
		this.nombreProducto = nombreProducto;
		this.cantidadesEspecificas = cantidadesEspecificas;
	}

    @Data
    @Builder
    public static class SudamerikConjuntoEspecifico {
        private String cantidadEspecifica;
        private Double precio;
    }

	@Override
	public Double getPrecioExterno() {
		return cantidadesEspecificas.get(0).getPrecio();
	}
}
