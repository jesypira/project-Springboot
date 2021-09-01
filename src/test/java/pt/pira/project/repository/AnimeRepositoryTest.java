package pt.pira.project.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.pira.project.domain.Anime;
import pt.pira.project.util.AnimeCreator;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persist anime when successful")
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    void save_UpdateAnime_WhenSuccessful(){
        Anime animeToBeUdated = AnimeCreator.createValidUpdatedAnime();
        Anime animeUpdated = this.animeRepository.save(animeToBeUdated);
        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isNotNull();
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeToBeUdated.getName());
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);
        this.animeRepository.delete(savedAnime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by Name returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);
        String name = savedAnime.getName();

        List<Anime> animes = this.animeRepository.findByName(name);
        Assertions.assertThat(animes)
                .isNotEmpty()
                .contains(savedAnime);
    }

    @Test
    @DisplayName("Find by Name returns an empty list when anime is not found")
    void findByName_ReturnsEmptyList_WhenAnimeNotFound(){
        List<Anime> animes = this.animeRepository.findByName("empty");
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throws ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Anime animeToBeSaved = new Anime();
        // Assertions.assertThatThrownBy(() -> this.animeRepository.save(animeToBeSaved))
        // .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                    .isThrownBy(() -> this.animeRepository.save(animeToBeSaved))
                .withMessageContaining("The anime's name cannot be empty");
    }
}