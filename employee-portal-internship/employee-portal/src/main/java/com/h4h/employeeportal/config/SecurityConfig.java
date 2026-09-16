package com.h4h.employeeportal.config;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
@Configuration
public class SecurityConfig {
 @Bean public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
 @Bean public UserDetailsService users(PasswordEncoder encoder,@Value("${portal.admin.username}") String username,@Value("${portal.admin.password}") String password){
  return new InMemoryUserDetailsManager(User.withUsername(username).password(encoder.encode(password)).roles("HR").build());
 }
 @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  return http.csrf(c->c.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
   .authorizeHttpRequests(a->a.requestMatchers("/api/auth/csrf","/api/auth/login","/actuator/health","/","/index.html","/favicon.svg","/assets/**","/fonts/**","/employees","/employees/*/detail","/departments","/contracts","/leaves","/plans","/calendar","/templates","/settings","/error").permitAll().anyRequest().hasRole("HR"))
   .formLogin(f->f.loginProcessingUrl("/api/auth/login").successHandler((q,s,a)->{s.setContentType("application/json");s.getWriter().write("{\"authenticated\":true}");})
    .failureHandler((q,s,e)->{s.setStatus(401);s.setContentType("application/json");s.getWriter().write("{\"message\":\"Incorrect username or password.\"}");}))
   .logout(l->l.logoutUrl("/api/auth/logout").logoutSuccessHandler((q,s,a)->s.setStatus(204)))
   .exceptionHandling(e->e.authenticationEntryPoint((q,s,x)->s.sendError(401)).accessDeniedHandler((q,s,x)->s.sendError(403)))
   .build();
 }
}
