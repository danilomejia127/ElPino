/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eafit.catastro.seguridad;

/**
 *
 * @author jhon_
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static String REALM = "TIENDA_VIRTUAL";

	// Inyeccion de dependencia para que se ejecute al realizar peticiones http.
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("usuario").password("usuario").roles("USER");
	}

	// Metodo de configuracion para peticiones http.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/api/**").hasRole("ADMIN")
				// .antMatchers("/api","/publico").permitAll() // permitir
				// acceso sin autorizacion
				.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
				// Jhon: Cuando implementemos SESSIONES se debe modificar la
				// funcionalidad.
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// We don't need sessions to be created.
		// Ver ejemplos de parametrizacion:
		// https://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security/
	}

	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
		return new CustomBasicAuthenticationEntryPoint();
	}

	// metodo de configuracion para filtrar resources o acceso a archivos por
	// web
	/* To allow Pre-flight [OPTIONS] request from browser */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}
}