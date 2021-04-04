package by.nintendo.configuration;

import by.nintendo.service.DetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "by.nintendo" )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   private final DetailsService detailsServiceMy;

    public WebSecurityConfig(DetailsService detailsService) {
        this.detailsServiceMy = detailsService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/home", "/reg").permitAll()
                .antMatchers("/user","/user/allUsers/**","/deleate","/user/find/**").hasRole("User")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("login")
                .loginPage("/auth")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
               .logoutUrl("/log").logoutSuccessUrl("/home")
                .permitAll().and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsServiceMy).passwordEncoder(bCryptPasswordEncoder());
    }

}
