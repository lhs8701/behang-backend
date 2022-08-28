package bh.bhback.global.config;

import bh.bhback.global.redis.LogoutAccessTokenRedisRepository;
import bh.bhback.global.security.CustomAccessDeniedHandler;
import bh.bhback.global.security.CustomAuthenticationEntryPoint;
import bh.bhback.global.security.JwtAuthenticationFilter;
import bh.bhback.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //@PreAuthorize, @Secured 사용을 위함
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() //기본설정은 비 인증시 로그인 폼 화면으로 리다이렉트 되는데 RestApi이므로 disalbe함
            .csrf().disable() // rest api이므로 상태를 저장하지 않으니 csrf 보안을 설정하지 않아도된다.
            .logout().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)

            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, logoutAccessTokenRedisRepository), UsernamePasswordAuthenticationFilter.class);

        //jwt 인증 필터를 MembernamePasswordAuthenticationFilter.class 전에 넣는다.
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/usage","/v3/api-docs","/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui/**", "/webjars/**", "/swagger/**");
        //swagger 관련 url에 대해서는 예외처리
    }
}



