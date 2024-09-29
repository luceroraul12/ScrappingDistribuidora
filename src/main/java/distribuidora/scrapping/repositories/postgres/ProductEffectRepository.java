package distribuidora.scrapping.repositories.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import distribuidora.scrapping.entities.ProductEffect;

@Repository
public interface ProductEffectRepository
		extends
			JpaRepository<ProductEffect, Integer> {

	@Query("""
			SELECT pe
			FROM ProductEffect pe
				INNER JOIN pe.label l
				INNER JOIN l.client c
			WHERE (:labelIds IS NULL OR l.id IN :labelIds)
				AND c.id = :clientId
			""")
	List<ProductEffect> findProductEffectByLabelIds(Integer clientId, List<Integer> labelIds);

}
