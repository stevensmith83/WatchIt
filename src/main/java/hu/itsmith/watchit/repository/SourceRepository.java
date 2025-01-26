package hu.itsmith.watchit.repository;

import hu.itsmith.watchit.model.entity.SourceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends CrudRepository<SourceEntity, Integer> {
}
