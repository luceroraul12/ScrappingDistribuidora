package distribuidora.scrapping.services.webscrapping;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import distribuidora.scrapping.configs.Constantes;
import distribuidora.scrapping.entities.ExternalProduct;

@Service
public class DonGasparWebScrappingServicio extends ProductSearcherWeb {

	@Override
	protected boolean esDocumentValido(Document document) {
		return false;
	}

	@Override
	protected ExternalProduct obtenerProductosAPartirDeElements(
			Element elementProducto) {
		new ArrayList<>();
		String code = elementProducto.attr("id").toString();
		double price;
		try {
			price = Double.parseDouble(elementProducto.select(".precio-box")
					.text().replace("$", ""));
		} catch (Exception e) {
			price = 0;
		}
		String description = elementProducto.select(".dfloat-left").text();

		return new ExternalProduct(null, description, price, null,
				getTipoDistribuidora(), code);
	}

	@Override
	protected Elements filtrarElementos(Document documento) {
		return documento.getElementsByClass("producto");
	}

	@Override
	public void setCodes() {
		setDistribuidoraCodigo(Constantes.LV_DISTRIBUIDORA_DON_GASPAR);
	}

	@Override
	protected int generarUltimoIndicePaginador() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}
