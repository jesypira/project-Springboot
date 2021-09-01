package pt.pira.project.util;

import pt.pira.project.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("anime test")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("anime test")
                .id(1L)
                .build();
    }

    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .name("anime test updated")
                .id(1L)
                .build();
    }

}
