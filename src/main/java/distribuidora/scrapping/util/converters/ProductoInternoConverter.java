package distribuidora.scrapping.util.converters;

import distribuidora.scrapping.configs.Constantes;
import distribuidora.scrapping.entities.LookupValor;
import distribuidora.scrapping.entities.Producto;
import distribuidora.scrapping.entities.ProductoInterno;
import distribuidora.scrapping.dto.ProductoInternoDto;
import distribuidora.scrapping.repositories.ProductoRepository;
import distribuidora.scrapping.services.general.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductoInternoConverter extends Converter<ProductoInterno, ProductoInternoDto>{
	@Autowired
	LookupService lookupService;

	@Autowired
	ProductoRepository productoRepository;

	@Override
	public ProductoInternoDto toDto(ProductoInterno entidad) {
		ProductoInternoDto dto = ProductoInternoDto.builder()
				.id(entidad.getId())
				.nombre(entidad.getNombre())
				.descripcion(entidad.getDescripcion())
				.codigoReferencia(entidad.getCodigoReferencia())
				.fechaCreacion(entidad.getFechaCreacion())
				.fechaActualizacion(entidad.getFechaActualizacion())
				.precio(entidad.getPrecio())
				.build();

		if (entidad.getLvCategoria() != null){
			dto.setLvCategoria(entidad.getLvCategoria());
		} else {
			LookupValor lv = new LookupValor();
			lv.setCodigo("CEREALES");
			lv.setDescripcion("CEREALES");
			dto.setLvCategoria(lv);
		}

		if (entidad.getDistribuidoraReferencia() != null){
			LookupValor distribuidora = entidad.getDistribuidoraReferencia();
			dto.setDistribuidoraReferenciaCodigo(distribuidora.getCodigo());
			dto.setDistribuidoraReferenciaNombre(distribuidora.getDescripcion());
		}
		dto.setPrecioTransporte(entidad.getPrecioTransporte());
		dto.setPrecioEmpaquetado(entidad.getPrecioEmpaquetado());
		dto.setPorcentajeGanancia(entidad.getPorcentajeGanancia());
		dto.setIsUnit(entidad.getIsUnit());
		return dto;
	}

	@Override
	public ProductoInterno toEntidad(ProductoInternoDto dto) {
		Map<String, LookupValor> mapDistribuidoras = lookupService.getLookupValoresPorLookupTipoCodigo(
				Constantes.LV_DISTRIBUIDORAS).stream()
				.collect(Collectors.toMap(LookupValor::getCodigo, Function.identity()));
		ProductoInterno entidad = ProductoInterno.builder()
				.id(dto.getId())
				.nombre(dto.getNombre())
				.descripcion(dto.getDescripcion())
				.precio(dto.getPrecio() != null
						? dto.getPrecio()
						: 0.0)
				.isUnit(dto.getIsUnit() != null
						? dto.getIsUnit()
						: true)
				.build();

		// si se le pasa el codigo de la distribuidora
		// deberias tener el codigo del producto al que queres hacer referencia
		if (dto.getDistribuidoraReferenciaCodigo() != null){
			String distribuidoraCodigo = dto.getDistribuidoraReferenciaCodigo();
			entidad.setDistribuidoraReferencia(mapDistribuidoras.get(distribuidoraCodigo));
			entidad.setCodigoReferencia(dto.getCodigoReferencia());
		}
		if(dto.getLvCategoria() != null){
			LookupValor lvCategoria = lookupService.getlookupValorPorCodigo(dto.getLvCategoria().getCodigo());
			entidad.setLvCategoria(lvCategoria);
		}
		entidad.setPrecioTransporte(dto.getPrecioTransporte());
		entidad.setPrecioEmpaquetado(dto.getPrecioEmpaquetado());
		entidad.setPorcentajeGanancia(dto.getPorcentajeGanancia());
		return entidad;
	}

	@Override
	public List<ProductoInternoDto> toDtoList(List<ProductoInterno> productoInternos) {
		List<ProductoInternoDto> list = super.toDtoList(productoInternos);
		Map<String, Map<String, Producto>> mapProductFixed = productoRepository.findAll().stream()
				.collect(Collectors.groupingBy(Producto::getDistribuidoraCodigo,
						Collectors.toMap(p -> p.getId(), Function.identity())));
		list = list.stream()
				.map(p -> {
					// TODO: reveer esto por que algunas veces viene null
					if (p.getDistribuidoraReferenciaCodigo() != null){
						if (mapProductFixed.containsKey(p.getDistribuidoraReferenciaCodigo())){
							Map<String, Producto> mapDistro = mapProductFixed.get(p.getDistribuidoraReferenciaCodigo());
							if (mapDistro.containsKey(p.getCodigoReferencia())){
								Producto productDistro = mapDistro.get(p.getCodigoReferencia());
								if (productDistro != null){
									p.setReferenciaNombre(productDistro.getDescripcion());
								}
							}
						}
					}
					return p;
				})
				.collect(Collectors.toList());
		return list;
	}
}
