package distribuidora.scrapping.services.webscrapping;

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
class LaGranjaDelCentroWebScrappingServicioTest {

    @Autowired
    LaGranjaDelCentroWebScrappingServicio servicio;


    @Test
    void pruebaGeneral()  {

    }

    /**
     * Prueba realizada 10/20/2022. Podria cambiar en el futuro.
     * caso bueno < 123
     * caso limite 123
     * caso malo > 123
     * @throws IOException
     */
    @Test
    void esDocumentValido() throws IOException {
        Document docBueno = Jsoup.connect("https://lagranjadelcentro.com.ar/productos.php?pagina=2").get();
        Document docLimite = Jsoup.connect("https://lagranjadelcentro.com.ar/productos.php?pagina=122").get();
        Document docMalo = Jsoup.connect("https://lagranjadelcentro.com.ar/productos.php?pagina=124").get();

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
//        Document docBueno = Jsoup.parse(new File("src/main/resources/static/la-granja-del-centro.html"));
//        Elements elementosObtenidos = servicio.filtrarElementos(docBueno);
//
//        List<LaGranjaDelCentroEntidad> productos = servicio.(elementosObtenidos);
//
//        assertEquals("ZAPALLO EN ALMIBAR X 450 GRS", productos.get(0).getNombreProducto());
//        assertEquals("ZARZAPARRILLA X 1 KG", productos.get(1).getNombreProducto());
//        assertEquals("ZUCRA FORTE 9 X 200 CC", productos.get(2).getNombreProducto());
//        assertEquals("ZUCRA FORTE 9 X 400 CC", productos.get(3).getNombreProducto());
//
//    }


    /**
     * Le paso un documento que siempre tiene objetos y deberia filtrar solo las partes que pueden contener elementos posibles de extraer informacion
     */
    @Test
    void filtrarElementos() throws IOException {
        Document docBueno = Jsoup.parse(new File("src/main/resources/static/la-granja-del-centro.html"));
        Elements elementosObtenidos = servicio.filtrarElementos(docBueno);

        assertEquals(4,elementosObtenidos.size());
    }
}