package distribuidora.scrapping.services.webscrapping;

import distribuidora.scrapping.entities.productos.especificos.FacundoEntidad;
import distribuidora.scrapping.enums.Distribuidora;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FacundoRenovadoWebScrappingServicio extends BusquedorPorWebScrapping<FacundoEntidad>{
    @Override
    protected void initImplementacion() {
        setDistribuidora(Distribuidora.FACUNDO);
        setUrlBuscador("http://gglobal.net.ar/bernal/?cliente");
        setEsNecesarioUsarWebDriver(false);
    }

    @Override
    protected boolean esDocumentValido(Document document) throws Exception {
        return false;
    }

    @Override
    protected FacundoEntidad obtenerProductosAPartirDeElements(Element elementProducto) {
        String nombre = elementProducto
                .getElementsByTag("h5")
                .textNodes().get(0).text()
                .replaceAll("_","")
                .subSequence(0, elementProducto.getElementsByTag("h5").textNodes().get(0).text().length()-3)
                .toString();

//        [\d.,]+ solo tomo numeros . y ,
        Double precio = Double.valueOf(elementProducto
                .getElementsByTag("strong")
                .text()
                .replaceAll(" ","")
                .subSequence(1, elementProducto.getElementsByTag("strong").text().length()-1)
                .toString());
//        Matcher m = Pattern.compile("[\\d.,]+").matcher(elementProducto.getElementsByTag("strong").text());
//        String precio = m.group();

        return FacundoEntidad.builder()
                .categoria(nombre)
                .precioMayor(Double.valueOf(precio))
                .build();
    }

    @Override
    protected Elements filtrarElementos(Document documento) {
        return documento.getElementsByClass("item row pointer");
    }
}
