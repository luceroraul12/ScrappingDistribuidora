package distribuidora.scrapping.util;

import org.springframework.stereotype.Component;

import distribuidora.scrapping.entities.ProductoInterno;

@Component
public class CalculatorUtil {
	
	public Integer calculatePriceList(ProductoInterno p) {
		int result;
		double precio = p.getPrecio() != null ? p.getPrecio() : 0;
		double transporte = p.getPrecioTransporte() != null
				? p.getPrecioTransporte()
				: 0.0;
		double empaquetado = p.getPrecioEmpaquetado() != null
				? p.getPrecioEmpaquetado()
				: 0.0;
		double ganancia = (100 + (p.getPorcentajeGanancia() != null
				? p.getPorcentajeGanancia()
				: 0)) / 100;
		double impuesto = (100 + (p.getPorcentajeImpuesto() != null
				? p.getPorcentajeImpuesto()
				: 0)) / 100;
		double regulador = p.getRegulador() != null && p.getRegulador() != 0.0 ? p.getRegulador() : 1;
		double precioPorcentual = (precio * ganancia * impuesto) / regulador;
		result = (int) (precioPorcentual + transporte + empaquetado + ganancia);
		return result;
	}
	
}
