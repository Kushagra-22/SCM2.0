package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {
    // private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("admin123")
    // .password("admin123")
    // .roles("ADMIN","USER")
    // .build();
    // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);
    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailService;
    @Autowired
    private AuthFailtureHandler authFailtureHandler;
    @Autowired
    private OAuthAuthenicationSuccessHandler handler;



    // COnfiguration of authentication provider
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // User detail ka object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        // Password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/about", "/signup").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/dashboard");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureHandler(authFailtureHandler);

            // formLogin.successHandler(new AuthenticationSuccessHandler() {

            // @Override
            // public void onAuthenticationSuccess(HttpServletRequest request,
            // HttpServletResponse response,
            // Authentication authentication) throws IOException, ServletException {

            // }

            // });

        });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // httpSecurity.oauth2Client(Customizer.withDefaults());
        httpSecurity.oauth2Login(oauth -> {
        oauth.loginPage("/login");
        oauth.successHandler(handler);
        });

        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
