package distribuidora.scrapping.repositories.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import distribuidora.scrapping.entities.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {

	@Query("""
			SELECT l
			FROM Label l
			WHERE l.client.id = :clientId
			""")
	List<Label> findLabelsByClientId(Integer clientId);

}
