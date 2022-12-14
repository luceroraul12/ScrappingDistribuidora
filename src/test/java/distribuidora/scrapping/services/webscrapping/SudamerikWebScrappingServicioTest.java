package distribuidora.scrapping.services.webscrapping;

import distribuidora.scrapping.entities.productos.especificos.SudamerikEntidad;
import distribuidora.scrapping.util.ProductoUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SudamerikWebScrappingServicioTest {


    @Autowired
    SudamerikWebScrappingServicio servicio;

    @Autowired
    ProductoUtil<SudamerikEntidad> util;

    /**
     * Use tres enlaces para probar<br>
     * dentro de rango: 30 <br>
     * al limite: 48 <br>
     * fuera de rango: 22938470
     * @throws IOException
     */
    @Test
    void esDocumentValido() throws IOException {
        Document docBueno = Jsoup
                .connect("https://www.sudamerikargentina.com.ar/productos/pagina/30").get();
        Document docLimite = Jsoup
                .connect("https://www.sudamerikargentina.com.ar/productos/pagina/48").get();
        Document docMalo = Jsoup
                .connect("https://www.sudamerikargentina.com.ar/productos/pagina/22938470").get();

        try {
            assertTrue(servicio.esDocumentValido(docBueno));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            assertTrue(servicio.esDocumentValido(docLimite));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            assertFalse(servicio.esDocumentValido(docMalo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

//    @Test
//    void obtenerProductosAPartirDeElements() throws IOException {
//        Document docBueno = Jsoup.parse(new File("src/main/resources/static/sudamerik.html"));
//        Elements cantidadDeProductosObtenidos = servicio.filtrarElementos(docBueno);
//        List<SudamerikEntidad> productosCreados = servicio.obtenerProductosAPartirDeElements(cantidadDeProductosObtenidos);
//
//        assertEquals(7, productosCreados.size());
//
//    }

    @Test
    void filtrarElementos() throws IOException {
        Document docBueno = Jsoup.parse(new File("src/main/resources/static/sudamerik.html"));
        Elements cantidadDeProductosObtenidos = servicio.filtrarElementos(docBueno);

        assertEquals(7,cantidadDeProductosObtenidos.size());
    }

//    @Test
//    void mapearEntidadaProducto() throws IOException {
//        Document docBueno = Jsoup.parse(new File("src/main/resources/static/sudamerik.html"));
//        Elements cantidadDeProductosObtenidos = servicio.filtrarElementos(docBueno);
//        List<SudamerikEntidad> productosCreados = servicio.obtenerProductosAPartirDeElements(cantidadDeProductosObtenidos);
//
//        List<Producto> productosGenerados = util.convertirProductoyDevolverlo(productosCreados.get(6));
//
//        assertEquals(3, productosGenerados.size());
//    }

}