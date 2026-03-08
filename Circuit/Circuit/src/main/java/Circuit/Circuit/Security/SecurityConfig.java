package Circuit.Circuit.Security;

import Circuit.Circuit.Model.Enum.Cargo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
                        .requestMatchers("/home").authenticated()

                        // ===== USUÁRIOS =====
                        .requestMatchers("/usuarios/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())

                        // ===== FINANCEIRO =====
                        .requestMatchers("/financeiro/**", "/contas-pagar/**", "/contas-receber/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())

                        // ===== RELATÓRIOS =====
                        .requestMatchers("/relatorios/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())

                        // ===== FORNECEDORES =====
                        .requestMatchers("/fornecedores/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())

                        // ===== VENDAS =====
                        .requestMatchers("/vendas/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.VENDEDOR.name(), Cargo.RECEPCIONISTA.name(), Cargo.AUXILIAR.name())

                        // ===== PEDIDOS =====
                        .requestMatchers("/pedidos/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.VENDEDOR.name(), Cargo.AUXILIAR.name())

                        // ===== CLIENTES =====
                        .requestMatchers(HttpMethod.POST, "/clientes/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.VENDEDOR.name())
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.VENDEDOR.name(), Cargo.RECEPCIONISTA.name(), Cargo.AUXILIAR.name())

                        // ===== ESTOQUE =====
                        .requestMatchers(HttpMethod.GET, "/estoque/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.VENDEDOR.name(), Cargo.TECNICO.name(), Cargo.RECEPCIONISTA.name(), Cargo.AUXILIAR.name())
                        .requestMatchers(HttpMethod.POST, "/estoque/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.AUXILIAR.name())
                        .requestMatchers(HttpMethod.PUT, "/estoque/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.AUXILIAR.name())
                        .requestMatchers(HttpMethod.DELETE, "/estoque/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())

                        // ===== APARELHOS =====
                        .requestMatchers("/aparelhos/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.TECNICO.name(), Cargo.AUXILIAR.name())

                        // ===== MODELOS =====
                        .requestMatchers("/modelos/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.TECNICO.name(), Cargo.AUXILIAR.name())

                        // ===== SERVIÇOS =====
                        .requestMatchers("/servicos/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.TECNICO.name(), Cargo.AUXILIAR.name())

                        // ===== PEÇAS =====
                        .requestMatchers("/pecas/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.TECNICO.name(), Cargo.AUXILIAR.name())

                        // ===== FUNCIONÁRIOS =====
                        .requestMatchers("/funcionarios/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())

                        // ===== ORDEM DE SERVIÇO (OS) =====
                        .requestMatchers(HttpMethod.POST, "/ordens-servico/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.RECEPCIONISTA.name(), Cargo.AUXILIAR.name())
                        .requestMatchers(HttpMethod.PUT, "/ordens-servico/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.TECNICO.name(), Cargo.AUXILIAR.name())
                        .requestMatchers(HttpMethod.DELETE, "/ordens-servico/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/ordens-servico/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.TECNICO.name(), Cargo.RECEPCIONISTA.name(), Cargo.AUXILIAR.name())

                        // ===== PRODUTOS =====
                        .requestMatchers("/produtos/**").hasAnyRole(Cargo.ADMIN.name(), Cargo.GERENTE.name(), Cargo.VENDEDOR.name(), Cargo.AUXILIAR.name())

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .rememberMe(rm -> rm
                        .key("circuit-producao-key")
                        .tokenValiditySeconds(604800)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutHandler)
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
