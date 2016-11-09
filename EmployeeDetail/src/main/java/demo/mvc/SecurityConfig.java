package demo.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*@Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService;
	
	@Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }*/

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user1").password("user1").roles("USER")
			.and()
				.withUser("super").password("super").roles("ADMIN");
		/*auth.inMemoryAuthentication()
				.withUser("user1").password("user1").authorities("USER")
			.and()
				.withUser("super").password("super").authorities("ADMIN");*/
	}
	
	/*@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/employees").access("hasRole('ADMIN')")
				.antMatchers(HttpMethod.PUT, "/employees").access("hasRole('ADMIN')")
				.antMatchers(HttpMethod.DELETE, "/employees").access("hasRole('ADMIN')")
				.antMatchers(HttpMethod.GET, "/employees").denyAll()
			.and()
				.formLogin();
//			.and()
//				.csrf();
	}*/
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/employees/**").access("hasRole('ADMIN')")
				.antMatchers(HttpMethod.PUT, "/employees/**").access("hasRole('ADMIN')")
				.antMatchers(HttpMethod.DELETE, "/employees/**").access("hasRole('ADMIN')")
			/*.antMatchers(HttpMethod.POST, "/employees_post/**").hasAuthority("ADMIN")
			.antMatchers(HttpMethod.PUT, "/employees_put/**").hasAuthority("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/employees_delete/**").hasAuthority("ADMIN")*/
				.antMatchers("/employees/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin();
	}
}
