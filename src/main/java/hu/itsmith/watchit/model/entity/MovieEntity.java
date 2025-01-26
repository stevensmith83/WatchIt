package hu.itsmith.watchit.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movies")
public class MovieEntity {

    @Id
    @Column(name = "watchmode_id")
    private Integer watchmodeId;

    @Column(name = "imdb_id", length = 50)
    private String imdbId;

    @Column(name = "tmdb_id")
    private Integer tmdbId;

    @Column(name = "tmdb_type", length = 50)
    private String tmdbType;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "year")
    private Integer year;
}