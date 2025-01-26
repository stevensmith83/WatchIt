package hu.itsmith.watchit.controller;

import hu.itsmith.watchit.service.WatchmodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final WatchmodeService watchmodeService;

    @GetMapping("/sources")
    public ResponseEntity<Void> refreshSources() {
        watchmodeService.refreshSources();
        return new ResponseEntity<>(NO_CONTENT);
    }
}
