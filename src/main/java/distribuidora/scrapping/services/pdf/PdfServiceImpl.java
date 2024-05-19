package distribuidora.scrapping.services.pdf;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import distribuidora.scrapping.configs.Constantes;
import distribuidora.scrapping.entities.CategoryHasUnit;
import distribuidora.scrapping.entities.Client;
import distribuidora.scrapping.entities.LookupValor;
import distribuidora.scrapping.entities.ProductoInterno;
import distribuidora.scrapping.entities.ProductoInternoStatus;
import distribuidora.scrapping.repositories.postgres.CategoryHasUnitRepository;
import distribuidora.scrapping.repositories.postgres.ProductoInternoStatusRepository;
import distribuidora.scrapping.services.ClientDataService;

@Service
public class PdfServiceImpl implements PdfService {

	@Autowired
	private CategoryHasUnitRepository categoryHasUnitRepository;
	@Autowired
	private ProductoInternoStatusRepository productoInternoStatusRepository;

	@Autowired
	private ClientDataService clientDataService;

	private void generatePdf(HttpServletResponse response, Integer clientId)
			throws DocumentException, IOException {
		// generacion del pdf
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				response.getOutputStream());

		// Variables sobre la fecha del PDF
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
				new Locale("es", "ES"));
		String dateConverted = sdf.format(new Date());
		// p = new Paragraph(String.format("Fecha de emisión: %s",
		// dateConverted),
		// smallBold);

		document.open();
		addMetaData(document);
		addTitlePage(document);
		addContent(document, writer, dateConverted, clientId);
		document.close();
	}

	private void addMetaData(Document document) {
		document.addTitle("Catalogo Pasionaria");
		document.addSubject("Mis productos");
		document.addKeywords("Pasionaria, Dietetica, Negocio, Productos");
		document.addAuthor("Juan");
		document.addCreator("Lucero Raul");
	}

	@Override
	public void addTitlePage(Document document)
			throws DocumentException, IOException {
		Client data = clientDataService.getById(1);

		Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Pruebas de logo del cliente
		Paragraph p = null;
		try {
			Image imageLogo = Image.getInstance(
					String.format("/resources/%s", data.getFilenameLogo()));
			imageLogo.scaleAbsolute(new Rectangle(300, 300));
			imageLogo.setAlignment(Element.ALIGN_CENTER);
			imageLogo.setSpacingAfter(0);
			imageLogo.setSpacingBefore(0);
			document.add(imageLogo);
		} catch (Exception e) {
			// En caso de que falle le agrego el datos string del mismo
			p = new Paragraph(data.getName().toUpperCase(), catFont);
			p.setAlignment(Element.ALIGN_CENTER);
			preface.add(p);
		}
		preface.add(p);
		// addEmptyLine(preface, 1);

		// Chunk link = null;

		preface.add(new Paragraph("Datos de contacto".toUpperCase(), subFont));

		preface.add(generateParagraphLink("Direccion", data.getAddress(),
				data.getAddressLink()));
		preface.add(generateParagraphLink("Telefono", data.getPhone(),
				data.getPhoneLink()));
		preface.add(generateParagraphLink("Instagram", data.getInstagram(),
				data.getInstagramLink()));
		preface.add(generateParagraphLink("Facebook", data.getFacebook(),
				data.getFacebookLink()));

		document.add(preface);
	}

	private Paragraph generateParagraphLink(String label, String name,
			String link) {
		Chunk result = new Chunk(String.format("%s - %s", label, name),
				smallFont);
		result.setAnchor(link);
		return new Paragraph(result);
	}

	@Override
	public void addContent(Document document, PdfWriter writer,
			String dateConverted, Integer clientId) throws DocumentException {
		List<CategoryHasUnit> categoryHasUnits = categoryHasUnitRepository
				.findAll();

		// TODO: Voy a comentarlo para ver que dice el cliente, en caso que lo
		// quiera de nuevo lo vuelvo a colocar
		// Paragraph title = new Paragraph("Categorias".toUpperCase(), catFont);
		// title.setAlignment(Element.ALIGN_CENTER);
		// document.add(title);

		// agrupo por categoria
		Map<LookupValor, List<CategoryHasUnit>> mapRelationsByLvCategory = new TreeMap<>(
				(a, b) -> a.getDescripcion().compareTo(b.getDescripcion()));
		mapRelationsByLvCategory.putAll(categoryHasUnits.stream()
				.collect(Collectors.groupingBy(r -> r.getCategory())));

		// busco los prodcutos
		List<ProductoInternoStatus> productoInternosStatus = productoInternoStatusRepository
				.findByClientId(clientId);

		for (Map.Entry<LookupValor, List<CategoryHasUnit>> entry : mapRelationsByLvCategory
				.entrySet()) {
			LookupValor lvCategory = entry.getKey();
			LookupValor lvUnit = entry.getValue().stream()
					.map(CategoryHasUnit::getUnit).findFirst().orElse(null);
			// me fijo si la categoria tiene productos asociados
			List<ProductoInternoStatus> productsByActualCategory = productoInternosStatus
					.stream()
					.filter(p -> p.getProductoInterno().getLvCategoria()
							.equals(lvCategory))
					// ordeno productos por nombre y si hay unidades los dejo al
					// final
					.sorted(orderProductoInternoStatusList()).toList();

			if (CollectionUtils.isNotEmpty(productsByActualCategory)) {

				// agrego datos de la categoria
				String categoryDescription = String.format("%s - %s",
						lvCategory.getDescripcion(), lvUnit.getDescripcion());
				Paragraph category = new Paragraph(categoryDescription,
						subFont);
				document.add(category);

				// agrego datos de los productos de la categoria
				Chunk leader = new Chunk(new DottedLineSeparator());
				com.itextpdf.text.List list = new com.itextpdf.text.List();
				for (ProductoInternoStatus productoInternoStatus : productsByActualCategory) {
					ListItem listItem = new ListItem();
					listItem.add(
							generateProductName(productoInternoStatus, lvUnit));
					listItem.add(leader);
					listItem.add(generatePriceWithUnitLogic(
							productoInternoStatus, lvUnit));
					list.add(listItem);
				}
				document.add(list);
				checkAndSetPageNumber(document, writer, dateConverted);
			}
		}
	}

	/**
	 * Valida en que hoja se encuentra en funcion del maximo de hojas y en caso
	 * de que aun no tenga paginado, lo agrega
	 * 
	 * @param document
	 */
	private void checkAndSetPageNumber(Document document, PdfWriter writer,
			String dateConverted) {
		float yNumberPage = 15f;
		float xNumberPage = document.getPageSize().getWidth() / 2;
		float yDateReference = yNumberPage;
		float xDateReference = 25;
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER,
				new Phrase(String.format("Pagina %d", writer.getPageNumber())),
				xNumberPage, yNumberPage, 0);
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_LEFT,
				new Phrase(
						String.format("Fecha de emisión: %s", dateConverted)),
				xDateReference, yDateReference, 0);
	}

	/**
	 * Encargado de generar el comparator final. El orden propuesto fue hay
	 * stock, no es unidad, nombre ascendente
	 * 
	 * @return
	 */
	private Comparator<ProductoInternoStatus> orderProductoInternoStatusList() {
		// Comparator auxiliar por check stock
		Comparator<ProductoInternoStatus> compartorByHasStock = (a, b) -> b
				.getHasStock().compareTo(a.getHasStock());
		// Comparator auxiliar por nombre de productos
		Comparator<ProductoInternoStatus> comparatorByProductName = (a, b) -> a
				.getProductoInterno().getNombre()
				.compareToIgnoreCase(b.getProductoInterno().getNombre());
		// Comparator auxiliar por flag de unidad
		Comparator<ProductoInternoStatus> comparatorByIsUnit = (a, b) -> a
				.getIsUnit().compareTo(b.getIsUnit());

		// Hago que primero haga por nombre, luego por flag y por ultimo de
		// nuevo nombre
		Comparator<ProductoInternoStatus> resultComparator = compartorByHasStock
				.thenComparing(comparatorByIsUnit)
				.thenComparing(comparatorByProductName);

		return resultComparator;
	}

	private String generateProductName(ProductoInternoStatus productStatus,
			LookupValor lvUnit) {
		String result;
		String productName = org.springframework.util.StringUtils
				.capitalize(productStatus.getProductoInterno().getNombre());
		String description = productStatus.getProductoInterno()
				.getDescripcion();
		boolean isCategoryUnit = lvUnit.getCodigo()
				.equals(Constantes.LV_MEDIDAS_VENTAS_1U);
		boolean isProductUnit = productStatus.getIsUnit();

		// seteo los datos del producto
		if (StringUtils.isNotEmpty(description)) {
			result = String.format("%s (%s)", productName, description);
		} else {
			result = productName;
		}

		// seteo los datos de la categoria en caso de que sea diferente de la
		// unidad
		if (!isCategoryUnit && isProductUnit)
			result = String.format("%s - %s", result,
					Constantes.LV_MEDIDAS_VENTAS_1U_DESCRIPTION);
		return result;
	}

	/**
	 * Genera el precio del producto basado en - precio base - precio de flete -
	 * precio de empaquetado - porcentaje de ganancia
	 * 
	 * @param p
	 * @return
	 */
	@Override
	public Integer generateBasePrice(ProductoInterno p) {
		int result;
		double precio = p.getPrecio() != null ? p.getPrecio() : 0;
		double transporte = p.getPrecioTransporte() != null
				? p.getPrecioTransporte()
				: 0.0;
		double empaquetado = p.getPrecioEmpaquetado() != null
				? p.getPrecioEmpaquetado()
				: 0.0;
		double ganancia = (100 + (p.getPorcentajeGanancia() != null
				? p.getPorcentajeGanancia()
				: 0)) / 100;
		double impuesto = (100 + (p.getPorcentajeImpuesto() != null
				? p.getPorcentajeImpuesto()
				: 0)) / 100;
		double regulador = p.getRegulador() != null && p.getRegulador() != 0.0
				? p.getRegulador()
				: 1;
		double precioPorcentual = (precio * ganancia * impuesto) / regulador;
		result = (int) (precioPorcentual + transporte + empaquetado + ganancia);
		return result;
	}

	@Override
	public int round(int result, int multiple) {
		// Recupero la cantidad multiplo
		int eachMultiple = Math.floorDiv(result, multiple);
		// Recupero el resto
		int mod = Math.floorMod(result, multiple);
		if (mod != 0) {
			return (eachMultiple + 1) * multiple;
		} else {
			return result;
		}
	}
	/**
	 * Toma el resultado de {@link #generateBasePrice(ProductoInterno)} y le
	 * aplica la logica de las unidades. Existen los siguientes casos -
	 * Categoria unidad (no importa unidad del producto): devuelve el precio
	 * base - Categoria fraccionada y producto unidad: devuelve precio base -
	 * Categoria fraccionada y producto fraccionado: devuelve precio fraccionado
	 * 
	 * @param productoInternoStatus
	 * @param lvUnit
	 * @return
	 */
	private String generatePriceWithUnitLogic(
			ProductoInternoStatus productoInternoStatus, LookupValor lvUnit) {
		double basePrice = generateBasePrice(
				productoInternoStatus.getProductoInterno());
		double result;
		boolean isCategoryUnit = lvUnit.getCodigo()
				.equals(Constantes.LV_MEDIDAS_VENTAS_1U);
		boolean isProductUnit = productoInternoStatus.getIsUnit();
		boolean hasStock = productoInternoStatus.getHasStock();

		// En caso de que el producto se encuentre marcado sin stock,
		// directamente retorno eso
		if (!hasStock) {
			return "SIN STOCK";
		}

		// si la categoria esta marcada como unidad solo retorno el precio
		if (isCategoryUnit || isProductUnit) {
			result = basePrice;
			// en caso contrario tengo que reducir el precio a la fraccion
			// especificada por
			// la unidad
		} else {
			result = basePrice * Double.parseDouble(lvUnit.getValor());
		}

		return String.valueOf(round((int) result, 5));
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	@Override
	public void getPdfByClientId(HttpServletResponse response, Integer clientId)
			throws IOException, DocumentException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pasionaria-catalogo"
				+ currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		generatePdf(response, clientId);
	}
}
