package distribuidora.scrapping.configs;

import java.util.Arrays;
import java.util.List;

public class Constantes {
	public static final String LV_DISTRIBUIDORAS = "DISTRIBUIDORAS";
	public static final String LV_DISTRIBUIDORA_MELAR = "MELAR";
	public static final String LV_DISTRIBUIDORA_LA_GRANJA_DEL_CENTRO = "LA_GRANJA_DEL_CENTRO";
	public static final String LV_DISTRIBUIDORA_DON_GASPAR = "DON_GASPAR";
	public static final String LV_DISTRIBUIDORA_INDIAS = "INDIAS";
	public static final String LV_DISTRIBUIDORA_FACUNDO = "FACUNDO";
	public static final String LV_DISTRIBUIDORA_VILLARES = "VILLARES";
	public static final String LV_DISTRIBUIDORA_TODAS = "TODAS";

	/**
	 * TODO: cuando comiencen a aparecer y desaparecer varias veces habra que
	 * acentarlo con estado en las bases de datos para que no hay que har un
	 * compilado nuevo a cada rato
	 */
	public static final List<String> DISTRIBUIDORAS_SIN_USO = Arrays
			.asList(LV_DISTRIBUIDORA_MELAR);
	public static final String LV_CATEGORIAS_CEREALES = "CEREALES";
	public static final String LV_CATEGORIAS_CEREALES_DESCRIPTION = "CEREALES";
	public static final String LV_MEDIDAS_VENTAS_1U = "MEDIDAS_VENTAS_1U";
	public static final String LV_MEDIDAS_VENTAS_1U_DESCRIPTION = "UNIDAD";
	public static final String LT_MEDIDAS_VENTAS = "MEDIDAS_VENTAS";
	
	
	// Estados de Ordenes
	public static final String ORDER_STATUS_PENDING = "PENDING";
	public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
	public static final String ORDER_STATUS_FINALIZED = "FINALIZED";
	public static final String ORDER_STATUS_INACTIVE = "INACTIVE";
	
	// Tipos de Distribuidoras
	public static final String LV_DISTRIBUIDORA_TYPE_EXCEL = "EXCEL";
	public static final String LV_DISTRIBUIDORA_TYPE_SCRAPPING = "WEB_SCRAPPING";
	
}
