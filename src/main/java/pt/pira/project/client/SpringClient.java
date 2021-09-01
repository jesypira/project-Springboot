package pt.pira.project.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pt.pira.project.domain.Anime;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        getForEntity();
        getForObject();
        getForObjectArray();
        getExchangeList();

       // postForObject();
        ResponseEntity<Anime> animeSaved2 = postExchange();

        Anime animeToBeUpdated = putExchange(animeSaved2);

        deleteExchange(animeToBeUpdated);

    }

    private static void deleteExchange(Anime animeToBeUpdated) {
        ResponseEntity<Void> animeDeleted = new RestTemplate().exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class, animeToBeUpdated.getId());

        log.info(animeDeleted);
    }

    private static Anime putExchange(ResponseEntity<Anime> animeSaved2) {
        Anime animeToBeUpdated = animeSaved2.getBody();
        animeToBeUpdated.setName("renomeado =) !!!");

        ResponseEntity<Void> animeUpdated = new RestTemplate().exchange("http://localhost:8080/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(animeToBeUpdated, createJsonHeader()),
                Void.class);

        log.info(animeUpdated);
        return animeToBeUpdated;
    }

    private static ResponseEntity<Anime> postExchange() {
        Anime kingdom2 = Anime.builder().name("kingdom 2").build();
        ResponseEntity<Anime> animeSaved2 = new RestTemplate().exchange("http://localhost:8080/animes/",
               HttpMethod.POST,
               new HttpEntity<>(kingdom2, createJsonHeader()), Anime.class);

        log.info("saved anime {}", animeSaved2);
        return animeSaved2;
    }

    private static void postForObject() {
        Anime kingdom = Anime.builder().name("kingdom").build();
        Anime animeSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", kingdom, Anime.class);
    }

    private static void getExchangeList() {
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all",
                                                HttpMethod.GET,
                                                null,
                                                new ParameterizedTypeReference<>() {});

        log.info(exchange.getBody());
    }

    private static void getForObjectArray() {
        Anime[] objs = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);

        log.info(Arrays.toString(objs));
    }

    private static void getForObject() {
        Anime obj = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 5);
        log.info(obj);
    }

    private static void getForEntity() {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 4);
        log.info(entity);
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
