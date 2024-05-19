package distribuidora.scrapping.services.webscrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import distribuidora.scrapping.entities.DatosDistribuidora;
import distribuidora.scrapping.entities.ExternalProduct;
import distribuidora.scrapping.entities.UpdateRequest;
import distribuidora.scrapping.services.ProductSearcher;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase base para los servicios basados en Web Scrapping.
 * 
 */
@Getter
@Setter
public abstract class ProductSearcherWeb extends ProductSearcher {

	/**
	 * Obligatorio para poder comenzar a utilizar el servicio
	 */
	private String urlBuscador;
	/**
	 * Variable booleana utilizada para indicar si los productos estan
	 * distribuidos por paginadores. En caso de true: se utilizara un metodo
	 * especifico para la generacion de las nuevas URL. Valor por defecto:
	 * false.
	 * 
	 * @see ProductSearcherWeb#generarNuevaURL(int)
	 */
	private Boolean esBuscadorConPaginador = false;

	@Override
	public List<ExternalProduct> adquirirProductosEntidad(UpdateRequest request,
			DatosDistribuidora data) throws IOException {
		List<ExternalProduct> productostotales = new ArrayList<>();
		setUrlBuscador(data.getWebUrl());
		setEsBuscadorConPaginador(data.isPaginator());

		productostotales = generarDocumentos().stream().parallel()
				.map(this::obtenerProductosPorDocument)
				.flatMap(Collection::stream).collect(Collectors.toList());
		return productostotales;
	}

	/**
	 * Encargado de crear todas los documentos asociados a la URL de la pagina.
	 * Tener en cuenta si usa paginacion.
	 * 
	 * @return lista de documentos asociados a la pagina.
	 * @throws IOException
	 * @see ProductSearcherWeb#generarNuevaURL(int)
	 */
	protected List<Document> generarDocumentos() throws IOException {
		List<Document> documentos = new ArrayList<>();
		if (esBuscadorConPaginador) {
			int contador = 1;
			String str = Jsoup.connect(generarNuevaURL(contador))
					.timeout(2 * 60 * 1000).maxBodySize(0).execute().body();
			Document doc = Jsoup.parse(str);
			while (esDocumentValido(doc)) {
				documentos.add(doc);
				contador++;
				doc = generarDocumento(generarNuevaURL(contador));
			}
		} else {
			documentos.add(generarDocumento(urlBuscador));
		}
		return documentos;
	}

	/**
	 * Encargado de generar un documento.
	 * 
	 * @return documento
	 */
	private Document generarDocumento(String url) throws IOException {
		Document documentoGenerado;
		String str = Jsoup.connect(url).timeout(2 * 60 * 1000).maxBodySize(0)
				.execute().body();
		documentoGenerado = Jsoup.parse(str);
		return documentoGenerado;
	}

	/**
	 * Genera una nueva URL. Toma la URL original y una variable contador para
	 * poder generar la nueva URL.
	 * 
	 * @param contador
	 *            externo, va en incremento 1
	 * @return nueva URL
	 */
	private String generarNuevaURL(int contador) {
		return urlBuscador + contador;
	}

	/**
	 * Encargado de validar las nuevas paginas creadas. Es un metodo que deben
	 * aplicar las clases Especificas, debido a que cada pagina puede ser
	 * diferente del resto. Solo es utilizado cuando se trabaja con paginador
	 * 
	 * @param document
	 *            template de la pagina Web
	 * @return true en caso de que sea valido
	 * @see ProductSearcherWeb#esBuscadorConPaginador
	 * @see ProductSearcherWeb#generarNuevaURL(int contador)
	 */
	protected abstract boolean esDocumentValido(Document document);

	/**
	 * Encargado de extraer productos Especificos de cada Document. Extrae todos
	 * los datos posibles del mismo para poder crear productos especificos.
	 * 
	 * @param documento
	 *            uno de los tanttos document que puede traer una pagina Web.
	 * @return listado de productos especificos.
	 */
	protected List<ExternalProduct> obtenerProductosPorDocument(
			Document documento) {
		return filtrarElementos(documento).stream()
				.map(this::obtenerProductosAPartirDeElements)
				.collect(Collectors.toList());
	}

	/**
	 * Genera un producto especifico a partir de un element.
	 * 
	 * @param elementProducto
	 *            elemento que contiene datos
	 * @return un producto especifico
	 */
	protected abstract ExternalProduct obtenerProductosAPartirDeElements(
			Element elementProducto);

	/**
	 * Deja solo los elementos que contienen datos convertibles a productos.
	 * 
	 * @param documento
	 *            documento que contiene elementos
	 * @return elementos filtrados
	 */
	protected abstract Elements filtrarElementos(Document documento);

	/**
	 * Meotodo que obtiene el indice maximo del paginador.
	 * 
	 * @return indice maximo de paginador.
	 * @throws IOException
	 */
	protected abstract int generarUltimoIndicePaginador() throws IOException;

}
