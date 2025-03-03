package com.udacity.jwdnd.course1.cloudstorage.Security;


import com.udacity.jwdnd.course1.cloudstorage.Services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {

    private final AuthenticationService authenticationService;

    public SecurityConfiguration(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm ->{
                    httpForm.loginPage("/login").permitAll();
                    httpForm.defaultSuccessUrl("/home", true)
                            .failureUrl("/login?error");

                })
                .authorizeHttpRequests(registry ->{
                    registry.requestMatchers("/signup","/css/**","/js/**","/img/**", "/file-upload/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .authenticationProvider(authenticationService)
                .build();
    }
}
