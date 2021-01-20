package org.pyatsotok.randomizer.domain;

public enum MovieGenre {

    ACTION("Action"),
    ADVENTURE( "Adventure"),
    ANIMATION( "Animation"),
    COMEDY( "Comedy"),
    CRIME( "Crime"),
    DOCUMENTARY( "Documentary"),
    DRAMA("Drama"),
    FAMILY( "Family"),
    FANTASY( "Fantasy"),
    HISTORY( "History"),
    HORROR( "Horror"),
    MUSIC( "Music"),
    MYSTERY( "Mystery"),
    ROMANCE( "Romance"),
    SCIENCE_FICTION( "Science fiction"),
    TV_MOVIE("TV Movie"),
    THRILLER( "Thriller"),
    WAR( "War"),
    WESTERN( "Western");

    private final String title;

    MovieGenre(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
