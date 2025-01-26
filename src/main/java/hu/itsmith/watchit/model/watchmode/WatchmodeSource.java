package hu.itsmith.watchit.model.watchmode;

public record WatchmodeSource(int id,
                              String name,
                              String type,
                              String logo_100px,
                              String iosAppstoreUrl,
                              String androidPlaystoreUrl,
                              String androidScheme,
                              String iosScheme,
                              String[] regions) {
}
