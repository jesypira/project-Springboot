package pt.pira.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.pira.project.domain.Anime;

import java.util.List;


public interface AnimeRepository extends JpaRepository<Anime, Long>{

    List<Anime> findByName(String name);

}
