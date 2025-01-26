package hu.itsmith.watchit.repository;

import hu.itsmith.watchit.model.entity.MovieEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<MovieEntity, Integer> {
}
