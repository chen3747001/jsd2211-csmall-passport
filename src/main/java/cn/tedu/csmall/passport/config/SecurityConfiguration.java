package cn.tedu.csmall.passport.config;

import cn.tedu.csmall.passport.filter.JwtAuthorizationFilter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.Authenticator;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.debug("创建@Bean方法定义的对象： PasswordEncoder");
        return new BCryptPasswordEncoder();
        //返回不操作的密码加密器
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        log.debug("创建@Bean方法定义的对象：AuthenticationManager");
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // 【配置白名单】
        //在配置路径时，星号是通配符
        //1个星号只能匹配任意文件夹或者文件的名称，但不能跨多个层级
        // 例如：/*/test.js，可以匹配到 /a/test.js 和 /b/test.js，但不可以匹配到 /a/b/test.js
        // 2个连续的星号可以匹配若干个文件夹的层级
        // 例如：/**/test.js，可以匹配 /a/test.js 和 /b/test.js 和 /a/b/test.js
        String[] urls={
            "/doc.html",
            "/**/*.js",
            "/**/*.css",
            "/swagger-resources",
            "/v2/api-docs",
            "/admins/login",
        };

        //禁用 CSRF（防止伪造的跨域攻击）
        http.csrf().disable();

        /*用来处理客户端发送的OPTIONS请求，通过启用Spring Security自带的过滤器*/
        // 启用Spring Security框架的处理跨域的过滤器，此过滤器将放行跨域请求，包括预检的OPTIONS请求
//        http.cors();

        http.authorizeRequests()// 对请求执行认证与授权
                // .antMatchers(urls) // 匹配某些请求路径，必须严格匹配路径
                .mvcMatchers(urls) // 匹配某些请求路径，匹配路径时不关心扩展名，例如配置的路径是 /admin，则可以匹配上 /admin、/admin.html、/admin.jpg
                .permitAll()//（对此前匹配的路径）允许访问
                // 以下代码是用于对预检的OPTIONS请求直接放行的
                .mvcMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .anyRequest()//除以上配置过的请求路径以外的所有请求路径
                .authenticated();//要求是已经通过认证的

        //开启表单认证，即视为未通过认证时，将重定向到登录表单，如果无此配置，则直接响应403
//        http.formLogin();
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
