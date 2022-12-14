package distribuidora.scrapping.services.webscrapping;

import distribuidora.scrapping.entities.productos.especificos.MelarEntidad;
import distribuidora.scrapping.enums.Distribuidora;
import distribuidora.scrapping.util.MelarUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MelarSeleniumWebScrappingServicio extends BusquedorPorWebScrapping<MelarEntidad> {
    @Autowired
    MelarUtil melarUtil;


    @Override
    protected boolean esDocumentValido(Document document) throws Exception {
        return false;
    }

    @Override
    protected MelarEntidad obtenerProductosAPartirDeElements(Element elementProducto) {
        List<String> renglon = new ArrayList<>();

        Elements partes = elementProducto.getElementsByTag("td");
        renglon.clear();
        partes.forEach(td -> {
            renglon.add(td.text());
        });
        double precioFraccionado;
        double precioGranel;

        try{
            precioFraccionado = Double.parseDouble(renglon.get(6)
                    .replaceAll("\\.","")
                    .replaceAll(",","."));
        } catch (Exception e){
            precioFraccionado = 0.0;
        }

        try{
            precioGranel = Double.parseDouble(renglon.get(7)
                    .replaceAll("\\.","")
                    .replaceAll(",","."));
        } catch (Exception e){
            precioGranel = 0.0;
        }

        return MelarEntidad.builder()
                .distribuidora(getDistribuidora())
                .codigo(renglon.get(0))
                .producto(renglon.get(1))
                .fraccion(renglon.get(2))
                .granel(renglon.get(3))
                .origen(renglon.get(4))
                .medida(renglon.get(5))
                .precioFraccionado(precioFraccionado)
                .precioGranel(precioGranel)
                .build();
    }

    @Override
    protected Elements filtrarElementos(Document documento) {
        return documento.getElementsByTag("table")
                .select("table > tbody > tr:not(.group)");
    }

    @Override
    protected void initImplementacion() {
        setDistribuidora(Distribuidora.MELAR);
        setEsNecesarioUsarWebDriver(true);
        setUrlBuscador("https://listadepreciosmelar.com.ar");
    }
}
