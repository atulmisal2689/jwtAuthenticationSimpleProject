package com.jbk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jbk.service.CustomUserDetailsService;



@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter 
{
	
	    @Autowired
	    private CustomUserDetailsService customUserDetailsService;

	    @Autowired
	    private JwtAuthenticationFilter jwtFilter;

	    @Autowired
	    private JwtAuthenticationEntryPoint entryPoint;
	
	     @Override
	    protected void configure(HttpSecurity http) throws Exception 
	    {
	      http
             .csrf()
             .disable()
             .cors()
             .disable()
             .authorizeRequests()
             .antMatchers("/token").permitAll()
             .antMatchers(HttpMethod.OPTIONS).permitAll()
             .anyRequest().authenticated()
             .and()
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
             .and()
             .exceptionHandling().authenticationEntryPoint(entryPoint);
	    
	    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	    }
	     
	   
	     @Override
	     protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	     {
	         auth.userDetailsService(customUserDetailsService);
	     }
	     
	    @SuppressWarnings("deprecation")
		@Bean
	     public PasswordEncoder passwordEncoder() 
	     {
	         return NoOpPasswordEncoder.getInstance();
	     }
	    
	    @Override
		@Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception
	    {
	        return super.authenticationManagerBean();
	    }

	 
}
