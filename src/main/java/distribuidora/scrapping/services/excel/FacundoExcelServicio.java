package distribuidora.scrapping.services.excel;

import distribuidora.scrapping.entities.productos.especificos.FacundoEntidad;
import distribuidora.scrapping.entities.Producto;
import distribuidora.scrapping.enums.Distribuidora;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacundoExcelServicio extends BusquedorPorExcel<FacundoEntidad> {



    @Override
    boolean esRowValido(Row row) {
        boolean resultado = false;
        try{
            if (row.getCell(0).getCellType().equals(CellType.STRING)){
                if (row.getCell(1).getCellType().equals(CellType.STRING)){
                    if (row.getCell(2).getCellType().equals(CellType.STRING)){
                        if (contieneDouble(row.getCell(3))){
                            if (contieneDouble(row.getCell(4))){
                                resultado = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored){
        }
        return resultado;
    }

    @Override
    FacundoEntidad mapearRowPorProducto(Row row) {
        return FacundoEntidad.builder()
                .categoria(row.getCell(0).getStringCellValue())
                .subcategoria(row.getCell(1).getStringCellValue())
                .cantidad(row.getCell(2).getStringCellValue())
                .precioMayor(Double.valueOf(row.getCell(3).toString()))
                .precioMenor(Double.valueOf(row.getCell(4).toString()))
                .build();
    }

    private boolean contieneDouble(Cell celda) {
        boolean resultado;
        try {
            Double.parseDouble(celda.toString());
            resultado = true;
        } catch (Exception e) {
            resultado = false;
        }

        return resultado;
    }

    @Override
    protected void initEspecifico() {
        setDistribuidora(Distribuidora.FACUNDO);
    }
}
