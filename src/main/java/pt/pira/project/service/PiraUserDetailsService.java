package pt.pira.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.pira.project.repository.PiraUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PiraUserDetailsService implements UserDetailsService {

    private final PiraUserRepository piraUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(piraUserRepository.findByUsername(username))
                .orElseThrow(()-> new UsernameNotFoundException("Pira user not found."));
    }
}
