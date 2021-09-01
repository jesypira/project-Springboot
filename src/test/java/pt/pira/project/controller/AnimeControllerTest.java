package pt.pira.project.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.pira.project.domain.Anime;
import pt.pira.project.requests.AnimePostRequestBody;
import pt.pira.project.requests.AnimePutRequestBody;
import pt.pira.project.service.AnimeService;
import pt.pira.project.util.AnimeCreator;
import pt.pira.project.util.AnimePostRequestBodyCreator;
import pt.pira.project.util.AnimePutRequestBodyCreator;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Execute with JUnit
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    //when you want to test the class
    @InjectMocks
    private AnimeController animeControllerMock;

    //for the classes that are inside @AnimeController
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup(){
       PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito
                .when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito
                .when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing()
                .when(animeServiceMock)
                .delete(ArgumentMatchers.anyLong());

        BDDMockito.doNothing()
                .when(animeServiceMock)
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class));

    }

    @Test
    @DisplayName("List returns list of animes inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeControllerMock.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of animes when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeControllerMock.listAll().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Long animeId = AnimeCreator.createValidAnime().getId();

        Anime animeFound = animeControllerMock.findById(animeId).getBody();

        Assertions.assertThat(animeFound).isNotNull();

        Assertions.assertThat(animeFound.getId()).isEqualTo(animeId);
    }

    @Test
    @DisplayName("findByName returns a list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animesFound = animeControllerMock.findByName(expectedName).getBody();

        Assertions.assertThat(animesFound).isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animesFound.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns a empty list when anime is not found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        BDDMockito
                .when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animesFound = animeControllerMock.findByName("empty").getBody();

        Assertions.assertThat(animesFound).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        Anime animeSaved = animeControllerMock
                            .save(AnimePostRequestBodyCreator.createAnimePostRequestsBody())
                            .getBody();

        Assertions.assertThat(animeSaved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("delete returns anime when successful")
    void delete_ReturnsVoid_WhenSuccessful(){
        ResponseEntity<Void> responseEntity = animeControllerMock.delete(1);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("replace returns anime when successful")
    void replace_ReturnsAnime_WhenSuccessful(){
        Assertions.assertThatCode(()-> animeControllerMock
                .replace(AnimePutRequestBodyCreator.createAnimePutRequestBody())
                .getBody()).doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeControllerMock
                .replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}