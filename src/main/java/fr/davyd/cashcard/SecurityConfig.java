package fr.davyd.cashcard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/cashcards/**")
                        .hasRole("CARD-OWNER"))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails davy = users
                .username("davy")
                .password(passwordEncoder().encode("password"))
                .roles("CARD-OWNER")
                .build();
        UserDetails math = users
                .username("math")
                .password(passwordEncoder().encode("password"))
                .roles("CARD-OWNER")
                .build();
        UserDetails hankOwnsNoCards = users
                .username("hank-owns-no-cards")
                .password(passwordEncoder().encode("password"))
                .roles("NON-OWNER")
                .build();
        return new InMemoryUserDetailsManager(davy, math, hankOwnsNoCards);
    }
}
