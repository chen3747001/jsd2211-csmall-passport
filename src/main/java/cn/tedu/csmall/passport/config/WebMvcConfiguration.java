package cn.tedu.csmall.passport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringMvc的配置类
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    public WebMvcConfiguration(){
        System.out.println("创建配置类： WebMvcConfiguration");
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
