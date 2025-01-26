package hu.itsmith.watchit.model.watchmode;

public record WatchmodeMovieSource(int sourceId,
                                   String name,
                                   String type,
                                   String region,
                                   String iosUrl,
                                   String androidUrl,
                                   String webUrl,
                                   String format,
                                   double price,
                                   int seasons,
                                   int episodes) {
}
