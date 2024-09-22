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
			SELECT pe.product.id
			FROM ProductEffect pe
			WHERE pe.label.id IN :labelIds
			""")
	List<Integer> findProductIdsByLabelIds(List<Integer> labelIds);

	@Query("""
			SELECT pe
			FROM ProductEffect pe
			WHERE pe.product.id IN :productIds
			""")
	List<ProductEffect> findProductEffectByProductIds(List<Integer> productIds);

}
