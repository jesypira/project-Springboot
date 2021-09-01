package pt.pira.project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.pira.project.service.PiraUserDetailsService;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@SuppressWarnings("javaS5344")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PiraUserDetailsService piraUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //all URls needs basic auth
        http.csrf().disable()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/**").hasRole("USER")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        log.info("Password encoded {}", delegatingPasswordEncoder.encode("test"));

        auth.inMemoryAuthentication()
                .withUser("pira")
                .password(delegatingPasswordEncoder.encode("piraPassword"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("pira2")
                .password(delegatingPasswordEncoder.encode("pira2Password"))
                .roles("USER");

        auth.userDetailsService(piraUserDetailsService)
                .passwordEncoder(delegatingPasswordEncoder);
    }
}
