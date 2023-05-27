package distribuidora.scrapping.services.excel;

import distribuidora.scrapping.entities.Producto;
import distribuidora.scrapping.entities.productos.especificos.IndiasEntidad;
import distribuidora.scrapping.util.IndiasUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IndiasExcelServicioTest {
    @InjectMocks
    IndiasExcelServicio servicio;

    @Spy
    IndiasUtil indiasUtil;

    List<IndiasEntidad> productos;
    @BeforeEach
    void setings() throws IOException {
        servicio.initImplementacion();

        File file = new File("src/main/resources/static/INDIAS.xls");
        MultipartFile[] multipartFile = new MultipartFile[1];
        multipartFile[0] = new MockMultipartFile("INDIAS", new FileInputStream(file));
        productos = servicio.obtenerProductosApartirDeExcels(multipartFile);
    }

    @Test
    void pruebaRow() throws IOException {
        List<Producto> productosFinales = indiasUtil.arregloToProducto(productos);

        assertEquals(405, productosFinales.size());
    }

    @Test
    void buscoProductosExistentes() throws IOException {
        Map<String, String> mapProductosFinales = indiasUtil.arregloToProducto(productos).stream()
                .collect(Collectors.toMap(Producto::getId, Producto::getDescripcion));

        assertTrue("LECHE DE COCO CELEBES 24x400".equalsIgnoreCase(mapProductosFinales.get("2217")));
        assertTrue("CIRUELAS SIN CAROZO x 5 kg.".equalsIgnoreCase(mapProductosFinales.get("3193")));
        assertTrue("JUGO FRUTOS BOSQUE DIET TETRA x 1".equalsIgnoreCase(mapProductosFinales.get("2951")));
    }
}