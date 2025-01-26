package hu.itsmith.watchit.repository;

import hu.itsmith.watchit.model.entity.MovieSourceMapEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieSourceMapRepository extends CrudRepository<MovieSourceMapEntity, Integer> {

    List<MovieSourceMapEntity> findAllByMovieEntity_WatchmodeIdAndRegion(Integer id, String region);
}
