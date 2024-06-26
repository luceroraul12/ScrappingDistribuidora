package distribuidora.scrapping.services.webscrapping;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.configs.Constantes;
import distribuidora.scrapping.entities.ExternalProduct;

@Service
public class LaGranjaDelCentroWebScrappingService extends ProductSearcherWeb {

	@Override
	public void setCodes() {
		setDistribuidoraCodigo(
				Constantes.LV_DISTRIBUIDORA_LA_GRANJA_DEL_CENTRO);
	}

	/**
	 * Para este caso, contienen un apartado de paginador. En el template
	 * aparece la siguiente etiqueta 'span' con los siguientes atributos:<br>
	 * span class="p-activo">123 <br>
	 * Esta etiqueta solo esta presente cuando es una pagina valida. Por lo
	 * tanto voy a condicionar en funcion a eso.
	 * 
	 * @param document
	 *            template de la pagina Web siguiente
	 * @return
	 */
	@Override
	protected boolean esDocumentValido(Document document) {
		boolean esValido = false;
		for (Element element : document.getElementsByTag("span")) {
			if (element.hasClass("p-activo")) {
				esValido = true;
			}
		}

		return esValido;
	}

	@Override
	protected ExternalProduct obtenerProductosAPartirDeElements(
			Element elementProducto) {
		String code = elementProducto.attr("data-product-id");
		String title = elementProducto.getElementsByClass("h3-content-1")
				.text();
		Double price = Double.valueOf(
				elementProducto.getElementsByClass("p-precio-content-1").text()
						.replaceAll("[$.]", "").replaceAll(",", "."));

		return new ExternalProduct(null, title, price, null,
				getTipoDistribuidora(), code);
	}

	@Override
	protected Elements filtrarElementos(Document documento) {
		return documento.select("div.box-content-1");
	}

	@Override
	protected int generarUltimoIndicePaginador() throws IOException {
		Document document = Jsoup.connect(getUrlBuscador()).get();
		Elements element = document.select(".paginador > .p");
		String url = element.get(1).attr("href");
		return Integer.parseInt(url.split("=")[1]);
	}
}
