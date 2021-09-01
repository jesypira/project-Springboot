package pt.pira.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.pira.project.domain.PiraUser;

public interface PiraUserRepository extends JpaRepository<PiraUser, Long> {

    PiraUser findByUsername(String username);

}
