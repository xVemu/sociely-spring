package pl.vemu.sociely.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.vemu.sociely.repositories.UserRepository;
import pl.vemu.sociely.utils.Roles;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> userRepository.findByEmail(email).get()); //TODO
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/users").hasAuthority(Roles.ADMIN.getAuthority())
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/api/posts")
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable(); //TODO delete on production?
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest // (2)
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // (development mode) static resources // (3)
                "/frontend/**",

                // (development mode) webjars // (3)
                "/webjars/**",

                // (production mode) static resources // (4)
                "/frontend-es5/**", "/frontend-es6/**",

                "/h2-console/**"
        );
    }
}
