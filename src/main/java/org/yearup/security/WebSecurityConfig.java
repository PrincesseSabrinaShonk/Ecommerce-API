package org.yearup.security;

import org.yearup.security.jwt.JWTConfigurer;
import org.yearup.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Main Spring Security configuration class.
 * Enables JWT-based authentication and method-level security.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // Handles creation and validation of JWT tokens
    private final TokenProvider tokenProvider;
    // Handles authentication failures (401 Unauthorized)
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // Handles authorization failures (403 Forbidden)
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    // Custom UserDetailsService that loads users from the database
    private final UserModelDetailsService userModelDetailsService;

    // Constructor injection for all required security components.
    public WebSecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            UserModelDetailsService userModelDetailsService
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.userModelDetailsService = userModelDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure paths and requests that should be ignored by Spring Security
     * @param web
     */
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    /**
     * Configure security settings
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                // Disable CSRF protection because this API uses JWT tokens
                .csrf().disable()

                // Configure custom handlers for authentication and authorization errors
                .exceptionHandling()
                // Triggered when a user is not authenticated (401 Unauthorized)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // Triggered when a user is authenticated but lacks permission (403 Forbidden)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // Do not create or use HTTP sessions
                // Each request must include a valid JWT token
                // create no session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // Apply the custom JWT security configuration
                // This adds the JWT filter to the Spring Security filter chain
                .and()
                .apply(securityConfigurerAdapter());
    }
                 //Registers the JWTConfigurer, which injects the JWT filter
                 // responsible for validating tokens on incoming requests.
    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}

