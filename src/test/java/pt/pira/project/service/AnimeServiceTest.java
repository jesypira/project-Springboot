package pt.pira.project.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.pira.project.domain.Anime;
import pt.pira.project.exception.BadRequestException;
import pt.pira.project.repository.AnimeRepository;
import pt.pira.project.util.AnimeCreator;
import pt.pira.project.util.AnimePostRequestBodyCreator;
import pt.pira.project.util.AnimePutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    //when you want to test the class
    @InjectMocks
    private AnimeService animeServiceMock;

    //for the classes that are inside @AnimeController
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup(){

        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito
                .when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito
                .when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing()
                .when(animeRepositoryMock)
                .delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("List returns list of animes inside page object when successful")
    void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeServiceMock.listAll(PageRequest.of(1,1));
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of animes when successful")
    void listAllNonPageable_ReturnsListOfAnimes_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = animeServiceMock.listAllNonPageable();
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
        Anime animeFound = animeServiceMock.findById(animeId);
        Assertions.assertThat(animeFound).isNotNull();
        Assertions.assertThat(animeFound.getId()).isEqualTo(animeId);
    }

    @Test
    @DisplayName("findById throws BadRequestException when anime is not found")
    void findById_ThrowsBadRequestException_WhenAnimeIsNotFound(){
        BDDMockito
                .when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(()-> animeServiceMock.findById(1));
    }

    @Test
    @DisplayName("findByName returns a list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animesFound = animeServiceMock.findByName(expectedName);
        Assertions.assertThat(animesFound).isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animesFound.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns a empty list when anime is not found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        BDDMockito
                .when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animesFound = animeServiceMock.findByName("empty");
        Assertions.assertThat(animesFound).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        Anime animeSaved = animeServiceMock
                .save(AnimePostRequestBodyCreator.createAnimePostRequestsBody());
        Assertions.assertThat(animeSaved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("delete returns anime when successful")
    void delete_ReturnsVoid_WhenSuccessful(){
        Assertions.assertThatCode(()->animeServiceMock.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace returns anime when successful")
    void replace_ReturnsAnime_WhenSuccessful(){
        Assertions.assertThatCode(()-> animeServiceMock
                .replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

}