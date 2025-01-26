package hu.itsmith.watchit.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "movie_source_map")
public class MovieSourceMapEntity {

    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_source_map_id_gen")
    @SequenceGenerator(name = "movie_source_map_id_gen", sequenceName = "movie_source_map_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", referencedColumnName = "watchmode_id")
    private MovieEntity movieEntity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private SourceEntity sourceEntity;

    @Column(name = "region", length = 3)
    private String region;

    @Column(name = "add_date", nullable = false)
    private LocalDate addDate;
}