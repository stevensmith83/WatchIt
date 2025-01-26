package hu.itsmith.watchit.model.watchmode;

public record WatchmodeMovie(String name,
                             double relevance,
                             String type,
                             long id,
                             int year,
                             String resultType,
                             int tmdbId,
                             String tmdbType,
                             String imageUrl,
                             String imdbId) {
}
