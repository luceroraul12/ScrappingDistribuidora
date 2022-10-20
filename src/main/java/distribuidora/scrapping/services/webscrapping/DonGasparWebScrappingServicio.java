package distribuidora.scrapping.services.webscrapping;

import distribuidora.scrapping.entities.productos.especificos.DonGasparEntidad;
import distribuidora.scrapping.entities.Producto;
import distribuidora.scrapping.entities.UnionEntidad;
import distribuidora.scrapping.entities.productos.especificos.MelarEntidad;
import distribuidora.scrapping.enums.Distribuidora;
import distribuidora.scrapping.util.DonGasparUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class DonGasparWebScrappingServicio extends BusquedorPorWebScrapping<DonGasparEntidad> {


    @Autowired
    DonGasparUtil donGasparUtil;

    @Autowired
    WebDriver driver;



    public DonGasparWebScrappingServicio() {
        urlBuscador = "https://pidorapido.com/dongasparsj";
        distribuidora = Distribuidora.DON_GASPAR;
        esNecesarioUsarWebDriver = true;

//        setClasesTabla("container");
    }

    @Override
    protected boolean esDocumentValido(Document document) {
        return false;
    }

    @Override
    protected List<DonGasparEntidad> obtenerProductosAPartirDeElements(Elements elements) {
        List<DonGasparEntidad> productosGenerados = new ArrayList<>();

        elements.forEach( p -> {
            double precio;
            try {
                precio = Double.parseDouble(
                        p.select(".precio-box").text()
                                .replace("$",""));
            } catch(Exception e) {
                precio = 0;
            }
            String descripcion = p.select(".dfloat-left").text();

            productosGenerados.add(
                    DonGasparEntidad.builder()
                            .nombreProducto(descripcion)
                            .precio(precio)
                            .build()
            );
        });

        return productosGenerados;
    }

    @Override
    protected List<Elements> filtrarElementos(Document documento) {
        return Collections.singletonList(documento.getElementsByClass("producto_fila"));
    }


    @Override
    protected List<Producto> mapearEntidadaProducto(DonGasparEntidad productoEntidad) {
        return donGasparUtil.convertirProductoyDevolverlo(productoEntidad);
    }


//    protected Document generarDocumentos() throws IOException {
//        driver.get(getUrlBuscador());
//        String template = driver.getPageSource();
//
//        return Jsoup.parse(template);
//    }
//
//    protected Elements generarElementosProductos(Document doc) {
//        return doc.getElementsByClass("producto_fila");
//    }
//
//    protected void trabajarConElementsyObtenerProductosEspecificos(Elements productos) {
//
//        productos.forEach( p -> {
//            double precio;
//            try {
//                precio = Double.parseDouble(
//                        p.select(".precio-box").text()
//                                .replace("$",""));
//            } catch(Exception e) {
//                precio = 0;
//            }
//            String descripcion = p.select(".dfloat-left").text();
//
//            agregarProducto(
//                    DonGasparEntidad.builder()
//                            .nombreProducto(descripcion)
//                            .precio(precio)
//                            .build()
//            );
//        });
//
//        setContadorPaginasVacias(100);
//
//    }
//
//    protected Collection<Producto> convertirProductos(UnionEntidad<DonGasparEntidad> dataDB) {
//        return donGasparUtil.arregloToProducto(
//                dataDB.getDatos()
//        );
//    }
}
