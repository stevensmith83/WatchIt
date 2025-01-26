package hu.itsmith.watchit.service;

import hu.itsmith.watchit.model.MovieSource;
import hu.itsmith.watchit.model.entity.MovieEntity;
import hu.itsmith.watchit.model.entity.MovieSourceMapEntity;
import hu.itsmith.watchit.model.entity.SourceEntity;
import hu.itsmith.watchit.model.watchmode.AutocompleteSearchResult;
import hu.itsmith.watchit.model.watchmode.WatchmodeMovieSource;
import hu.itsmith.watchit.model.watchmode.WatchmodeSource;
import hu.itsmith.watchit.repository.MovieRepository;
import hu.itsmith.watchit.repository.MovieSourceMapRepository;
import hu.itsmith.watchit.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchmodeService {

    private final MovieSourceMapRepository movieSourceMapRepository;
    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final SourceRepository sourceRepository;

    @Value("${watchmode.api.key}")
    private String apiKey;

    public AutocompleteSearchResult searchMovies(String title) {
        String url = "https://api.watchmode.com/v1/autocomplete-search/?apiKey=" + apiKey + "&search_value=" + title + "&search_type=1";
        ResponseEntity<AutocompleteSearchResult> response = restTemplate.getForEntity(url, AutocompleteSearchResult.class);
        var result = response.getBody();
        log.info("Autocomplete search result: {}", result);

        return result;
    }

    public List<MovieSource> getMovieSources(Integer id, String region) {
        List<MovieSourceMapEntity> sourceList = movieSourceMapRepository.findAllByMovieEntity_WatchmodeIdAndRegion(id, region);

        if (sourceList.isEmpty()) {
            log.info("No local source-mapping found for id {}, retrieving Watchmode list", id);
            String url = "https://api.watchmode.com/v1/title/" + id + "/sources/?apiKey=" + apiKey;
            ResponseEntity<WatchmodeMovieSource[]> response = restTemplate.getForEntity(url, WatchmodeMovieSource[].class);
            List<WatchmodeMovieSource> resultList = response.getBody() == null ? new ArrayList<>() : Arrays.asList(response.getBody());
            resultList = resultList.stream()
                    .filter(r -> region.isEmpty() || r.region().equals(region))
                    .toList();

            Set<MovieSourceMapEntity> entities = new HashSet<>();

            for (WatchmodeMovieSource watchmodeMovieSource : resultList) {
                MovieEntity movie = movieRepository.findById(id).orElse(null);
                SourceEntity source = sourceRepository.findById(watchmodeMovieSource.sourceId()).orElse(null);
                MovieSourceMapEntity movieSourceMapEntity = new MovieSourceMapEntity();
                movieSourceMapEntity.setMovieEntity(movie);
                movieSourceMapEntity.setSourceEntity(source);
                movieSourceMapEntity.setRegion(watchmodeMovieSource.region());
                movieSourceMapEntity.setAddDate(LocalDate.now());
                entities.add(movieSourceMapEntity);
            }

            sourceList.addAll((Collection<? extends MovieSourceMapEntity>) movieSourceMapRepository.saveAll(entities));
        }

        List<MovieSource> movieSources = new ArrayList<>();

        for (var source : sourceList) {
            movieSources.add(new MovieSource(source.getSourceEntity().getLogoUrl(), source.getRegion()));
        }

        log.info("Source entities: {}", movieSources);

        return movieSources;
    }

    public void refreshSources() {
        String url = "https://api.watchmode.com/v1/sources/?apiKey=" + apiKey;
        ResponseEntity<WatchmodeSource[]> response = restTemplate.getForEntity(url, WatchmodeSource[].class);
        var resultList = response.getBody() == null ? new ArrayList<WatchmodeSource>() : Arrays.asList(response.getBody());
        List<SourceEntity> sources = new ArrayList<>();

        for (WatchmodeSource source : resultList) {
            SourceEntity sourceEntity = new SourceEntity();
            sourceEntity.setId(source.id());
            sourceEntity.setName(source.name());
            sourceEntity.setLogoUrl(source.logo_100px());
            sources.add(sourceEntity);
        }

        sourceRepository.saveAll(sources);
    }
}
