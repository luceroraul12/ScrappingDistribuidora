package distribuidora.scrapping.services.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.configs.Constantes;
import distribuidora.scrapping.entities.DatosDistribuidora;
import distribuidora.scrapping.entities.ExternalProduct;

@Service
public class VillaresExcelService extends ProductSearcherExcel {
	@Override
	boolean esRowValido(Row row) {
		return row != null && row.getCell(1) != null
				&& row.getCell(1).getCellType().equals(CellType.NUMERIC);
	}

	@Override
	public void setCodes() {
		setDistribuidoraCodigo(Constantes.LV_DISTRIBUIDORA_VILLARES);
	}

	@Override
	protected ExternalProduct convertirRowEnProductoEspecifico(Row row,
			DatosDistribuidora data) {
		Integer cantidadCeldas = row.getPhysicalNumberOfCells();
		Integer celdasGranel = 12;
		Integer celdasGourmetFraccionado = 11;
		Integer celdasDistribuido = 10;
		Integer indiceInicioCeldaPrecio = 0;
		// inicializo
		String code = String
				.valueOf(Math.round(row.getCell(1).getNumericCellValue()) + "R-"
						+ row.getRowNum());
		String cantidad = "";
		String title = "";
		String brand = "";
		String unit = "";
		Double price = 0.0;

		// producto granel
		if (cantidadCeldas.equals(celdasGranel)) {
			indiceInicioCeldaPrecio = celdasGranel - 4;
			title = row.getCell(2).toString();
			cantidad = row.getCell(3).toString();
			cantidadMinima = row.getCell(4).toString();
			brand = row.getCell(6).toString();
			unit = row.getCell(7).toString();
		}
		// producto gourmet o fraccionado
		if (cantidadCeldas.equals(celdasGourmetFraccionado)) {
			indiceInicioCeldaPrecio = celdasGourmetFraccionado - 4;
			title = row.getCell(2).toString();
			cantidad = row.getCell(3).toString();
			brand = row.getCell(5).toString();
			unit = row.getCell(6).toString();
		}
		// producto distribuidos
		if (cantidadCeldas.equals(celdasDistribuido)) {
			indiceInicioCeldaPrecio = celdasDistribuido - 4;
			title = row.getCell(2).toString();
			cantidad = row.getCell(3).toString();
			unit = row.getCell(4).toString();
			brand = row.getCell(5).toString();
		}

		Boolean estaDisponible = row.getCell(indiceInicioCeldaPrecio)
				.getCellType().equals(CellType.NUMERIC);
		if (estaDisponible) {
			price = row.getCell(indiceInicioCeldaPrecio).getNumericCellValue();
		}

		title = String.format("%s %s %s", brand, unit, title);
		return new ExternalProduct(null, title, price, null,
				getTipoDistribuidora(), code);
	}
}
