package pt.pira.project.util;

import pt.pira.project.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody createAnimePostRequestsBody(){
        return AnimePostRequestBody.builder()
                .name("animePut test")
                .build();
    }

}
