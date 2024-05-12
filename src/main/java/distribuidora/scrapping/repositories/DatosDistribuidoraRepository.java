package distribuidora.scrapping.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import distribuidora.scrapping.entities.DatosDistribuidora;

public interface DatosDistribuidoraRepository
		extends
			JpaRepository<DatosDistribuidora, Integer> {

	@Query("""
			select dd
			from DatosDistribuidora dd
				inner join d.distribuidora d
			where d.active = true
			order by d.code
			""")
	List<DatosDistribuidora> findActives();

	boolean existsByDistribuidoraCodigo(String distribuidoraCodigo);
	DatosDistribuidora findByDistribuidoraCodigo(String distribuidoraCodigo);
	void deleteByDistribuidoraCodigo(String distribuidoraCodigo);

	@Query("""
			select dd
			from DatosDistribuidora dd
				inner join d.distribuidora d
			where d.active = true
				and d.code = :code
			""")
	DatosDistribuidora getByCode(String code);
}
