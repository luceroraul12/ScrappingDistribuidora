package distribuidora.scrapping.services;

import distribuidora.scrapping.entities.DonGasparEntidad;
import distribuidora.scrapping.entities.Producto;
import distribuidora.scrapping.entities.UnionEntidad;
import distribuidora.scrapping.enums.Distribuidora;
import distribuidora.scrapping.util.DonGasparUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class DonGasparService extends ScrapperTablaAbstract<DonGasparEntidad>{


    @Autowired
    DonGasparUtil donGasparUtil;

    @Autowired
    WebDriver driver;

    public DonGasparService() {
        setUrlBuscador("https://pidorapido.com/dongasparsj");
        setClasesTabla("container");
        setDistribuidora(Distribuidora.DON_GASPAR);
    }

    @Override
    protected Document generarDocument() throws IOException {
        driver.get(getUrlBuscador());
        String template = driver.getPageSource();

        return Jsoup.parse(template);
    }

    @Override
    protected Elements generarElementosProductos(Document doc) {
        return doc.getElementsByClass("producto_fila");
    }

    @Override
    protected void trabajarProductos(Elements productos) {

        productos.forEach( p -> {
            double precio;
            try {
                precio = Double.parseDouble(
                        p.select(".precio-box").text()
                                .replace("$",""));
            } catch(Exception e) {
                precio = 0;
            }
            String descripcion = p.select(".dfloat-left").text();

            agregarProducto(
                    DonGasparEntidad.builder()
                            .nombreProducto(descripcion)
                            .precio(precio)
                            .build()
            );
        });

        setContadorPaginasVacias(100);

    }

    @Override
    protected Collection<Producto> convertirProductos(UnionEntidad<DonGasparEntidad> dataDB) {
        return donGasparUtil.arregloToProducto(
                dataDB.getDatos()
        );
    }


}