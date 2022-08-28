package bh.bhback.global.config;

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


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() //기본설정은 비 인증시 로그인 폼 화면으로 리다이렉트 되는데 RestApi이므로 disalbe함
            .csrf().disable() // rest api이므로 상태를 저장하지 않으니 csrf 보안을 설정하지 않아도된다.

            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()  ---------------------------> @PreAuthorize, @Secured로 대체함
//            .authorizeRequests() //URL 별 권한 관리를 설정하는 옵션의 시작점, antMathcers를 작성하기 위해서는 먼저 선언되어야 한다.
//            //권한 관리 대상을 지정하는 옵션
//            .antMatchers("/login", "/signup").permitAll() //로그인 및 가입에 대한 접근은 누구나 가능하도록 함 (*/login , */signup)
//            .antMatchers(HttpMethod.GET, "/exception/**").permitAll() // /execption으로 오는 url에 대한 접근은 누구나 가능하도록 함 (에러 시)
//            .anyRequest().hasRole("USER")// 그 외 나머지 요청은 인증된 회원만 가능 : anyRequest().hasRole("USER")

            //CustomAuthenticationEntryPoint 클래스를 호출 ->  /exception/entryPoint로 리다이렉트 -> ExceptionController에서 해당 URL 처리, CAuthenticationEntryException()가 호출
            .and()
            .authorizeRequests()
            .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.POST, "/signup", "/login",
                    "/reissue", "/social/**").permitAll()
            .antMatchers(HttpMethod.GET, "/oauth/kakao/**").permitAll()
            .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
            //.anyRequest().hasRole("USER")

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)

            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        //jwt 인증 필터를 MembernamePasswordAuthenticationFilter.class 전에 넣는다.
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/usage","/v3/api-docs","/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui/**", "/webjars/**", "/swagger/**");
        //swagger 관련 url에 대해서는 예외처리
    }
}



