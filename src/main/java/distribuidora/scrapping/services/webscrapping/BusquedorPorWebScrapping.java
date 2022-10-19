package distribuidora.scrapping.services.webscrapping;

import distribuidora.scrapping.entities.ProductoEspecifico;
import distribuidora.scrapping.services.BuscadorDeProductosEntidad;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para los servicios basados en Web Scrapping.
 * En caso de que alguna pagina no carge el template desde el programa pero si en el navegador, se debera utilizar un WebDriver que simule el uso del navegador.
 * @param <Entidad>
 */
public abstract class BusquedorPorWebScrapping<Entidad extends ProductoEspecifico> extends BuscadorDeProductosEntidad<Entidad, Boolean> {
    /**
     * Obligatorio para poder comenzar a utilizar el servicio
     */
    protected String urlBuscador;
    /**
     * Variable booleana utilizada para indicar si los productos estan distribuidos por paginadores.
     * En caso de true: se utilizara un metodo especifico para la generacion de las nuevas URL.
     * Valor por defecto: false.
     * @see BusquedorPorWebScrapping#generarNuevaURL(int)
     */
    protected Boolean esBuscadorConPaginador = false;

    /**
     * Variable booleana utilizada para indicar si es necesario contar con un Webdriver para generar los templates de la pagina Web.
     */
    protected Boolean esNecesarioUsarWebDriver = false;

    @Autowired
    WebDriver driver;

    @Override
    protected List<Entidad> trabajarDocumentoyObtenerSusProductosEspecificos(Boolean elementoAuxiliar) {
        List<Entidad> productostotales = new ArrayList<>();
        try {
            generarDocumentos().forEach(
                    document -> {
                        productostotales.addAll(
                                obtenerProductosPorDocument(document)
                        );
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productostotales;
    }

    /**
     * Encargado de crear todas los documentos asociados a la URL de la pagina.
     * Tener en cuenta si usa paginacion.
     * @return lista de documentos asociados a la pagina.
     * @throws IOException
     * @see BusquedorPorWebScrapping#generarNuevaURL(int)
     */
    private List<Document> generarDocumentos() throws IOException {
        List<Document> documentos = new ArrayList<>();
        int contador = 1;

        if (esBuscadorConPaginador){
            Document doc = Jsoup.connect(generarNuevaURL(contador)).get();
            while(esDocumentValido(doc)){
                documentos.add(
                        doc
                );
                contador++;
                doc = Jsoup.connect(generarNuevaURL(contador)).get();
            }
        } else {
            documentos.add(
                    Jsoup.connect(urlBuscador).get()
            );
        }
        return documentos;
    }

    /**
     * Encargado de generar un documento.
     * Tener en cuenta la variable esNecesarioWebDriver para esta generacion, debido a que el ordenador debera tener instalado los driver scomo el navegador seleccionado.
     * @return documento
     */
    private Document generarDocumento(String url) {
        Document documentoGenerado;
        if (esNecesarioUsarWebDriver){
            driver.get(url);
            String template = driver.getPageSource();
            documentoGenerado = Jsoup.parse(template);
        } else {
            try {
                documentoGenerado = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return documentoGenerado;
    }

    /**
     * Genera una nueva URL.
     * Toma la URL original y una variable contador para poder generar la nueva URL.
     * @param contador externo, va en incremento 1
     * @return nueva URL
     */
    protected String generarNuevaURL(int contador) {
        return urlBuscador+'/'+contador ;
    }

    /**
     * Encargado de validar las nuevas paginas creadas.
     * Es un metodo que deben aplicar las clases Especificas, debido a que cada pagina puede ser diferente del resto.
     * Solo es utilizado cuando se trabaja con paginador
     * @param document template de la pagina Web
     * @return true en caso de que sea valido
     * @see BusquedorPorWebScrapping#esBuscadorConPaginador
     * @see BusquedorPorWebScrapping#generarNuevaURL(int contador)
     */
    protected abstract boolean esDocumentValido(Document document);

    /**
     * Encargado de extraer productos Especificos de cada Document.
     * Extrae todos los datos posibles del mismo para poder crear productos especificos.
     * @param documento uno de los tanttos document que puede traer una pagina Web.
     * @return listado de productos especificos.
     */
    protected List<Entidad> obtenerProductosPorDocument(Document documento){
        List<Entidad> productosPorDocumento = new ArrayList<>();


        return productosPorDocumento;
    }

}