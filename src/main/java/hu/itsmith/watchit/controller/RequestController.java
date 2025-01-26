package hu.itsmith.watchit.controller;

import hu.itsmith.watchit.model.MovieSource;
import hu.itsmith.watchit.model.watchmode.AutocompleteSearchResult;
import hu.itsmith.watchit.service.WatchmodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/movies")
public class RequestController {

    private final WatchmodeService watchmodeService;

    @GetMapping("/search")
    public ResponseEntity<AutocompleteSearchResult> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(watchmodeService.searchMovies(title));
    }

    @GetMapping("/sources")
    public ResponseEntity<List<MovieSource>> getMovieSources(@RequestParam Integer id, @RequestParam(required = false) String region) {
        region = region == null ? "" : region;
        return ResponseEntity.ok(watchmodeService.getMovieSources(id, region));
    }
}
