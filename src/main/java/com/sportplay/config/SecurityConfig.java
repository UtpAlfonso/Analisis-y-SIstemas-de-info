package com.sportplay.config;

import com.sportplay.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Bean para codificar las contraseñas. Usamos BCrypt, el estándar de la industria.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el proveedor de autenticación.
    // Le dice a Spring Security cómo obtener los detalles del usuario y cómo verificar las contraseñas.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Nuestro servicio personalizado
        authProvider.setPasswordEncoder(passwordEncoder()); // Nuestro codificador de contraseñas
        return authProvider;
    }

    // El Bean principal que configura la cadena de filtros de seguridad.
    // Aquí definimos todas las reglas de acceso.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // --- Reglas de Acceso Público ---
                // Permitir acceso sin autenticación a recursos estáticos.
                .requestMatchers(
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/img/**"),
                    new AntPathRequestMatcher("/webjars/**") // Para librerías como Bootstrap si se gestionan con webjars
                ).permitAll()

                // Permitir acceso sin autenticación a páginas públicas.
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/catalogo/**"),
                    new AntPathRequestMatcher("/producto/**"),
                    new AntPathRequestMatcher("/registro"),
                    new AntPathRequestMatcher("/login")
                ).permitAll()

                // --- Reglas de Acceso Restringido ---
                // El dashboard y sus sub-rutas requieren rol de EMPLEADO o ADMIN.
                .requestMatchers(new AntPathRequestMatcher("/dashboard/**")).hasAnyRole("EMPLEADO", "ADMIN")
                
                // Operaciones CRUD de productos requieren rol de ADMIN.
                .requestMatchers(
                    new AntPathRequestMatcher("/dashboard/productos/nuevo"),
                    new AntPathRequestMatcher("/dashboard/productos/editar/**"),
                    new AntPathRequestMatcher("/dashboard/productos/eliminar/**")
                ).hasRole("ADMIN")

                // Todas las demás URLs requieren que el usuario esté autenticado.
                .anyRequest().authenticated()
            )
            // --- Configuración del Formulario de Login ---
            .formLogin(form -> form
                .loginPage("/login") // URL de nuestra página de login personalizada.
                .loginProcessingUrl("/login") // URL que procesa la autenticación (Spring se encarga de esto).
                .defaultSuccessUrl("/dashboard", true) // Redirige a /dashboard siempre tras un login exitoso.
                .failureUrl("/login?error=true") // Redirige aquí si el login falla.
                .permitAll() // Permite a todos acceder a la página de login.
            )
            // --- Configuración del Logout ---
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL para hacer logout.
                .logoutSuccessUrl("/login?logout=true") // Redirige aquí tras un logout exitoso.
                .invalidateHttpSession(true) // Invalida la sesión HTTP.
                .deleteCookies("JSESSIONID") // Borra la cookie de sesión.
                .permitAll()
            );

        // Registramos nuestro proveedor de autenticación.
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}