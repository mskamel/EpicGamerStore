package com.store.videogames.config;

import com.store.videogames.common.PasswordEncoder;
import com.store.videogames.repository.interfaces.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    DataSource dataSource;
    @Autowired
    CustomerRepository customerRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerDetails();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(PasswordEncoder.getBcryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository()
    {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().
                antMatchers("/login").permitAll().
                antMatchers("/customer/register").permitAll().
                antMatchers("/forgot_password").permitAll().
                antMatchers("/reset_password").permitAll().
//                antMatchers("/verify").permitAll().
                anyRequest().authenticated()
                .and()
                .formLogin().permitAll().loginPage("/login").
                usernameParameter("username").passwordParameter("password")
                .permitAll().failureUrl("/login?error").defaultSuccessUrl("/").
                and().logout().permitAll().
                and().rememberMe().tokenRepository(persistentTokenRepository());
    }
}
