package ru.cdfe.gdr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableEntityLinks
@EnableSpringDataWebSupport
public class WebConfigurer implements WebMvcConfigurer {
    private final MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    private final GdrProperties conf;
    
    @Autowired
    public WebConfigurer(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter, GdrProperties conf) {
        this.jackson2HttpMessageConverter = jackson2HttpMessageConverter;
        this.conf = conf;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter);
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/frontend/**")
                .addResourceLocations("classpath:/static/frontend/");
        
        registry.addResourceHandler("/hal-browser/**")
                .addResourceLocations("classpath:/static/hal-browser/");
        
        registry.addResourceHandler("**/favicon.ico")
                .addResourceLocations("classpath:/favicon.ico");
    }
    
    @Bean
    public HateoasPageableHandlerMethodArgumentResolver
    pageableResolver(HateoasSortHandlerMethodArgumentResolver sortResolver) {
        final HateoasPageableHandlerMethodArgumentResolver resolver =
                new HateoasPageableHandlerMethodArgumentResolver(sortResolver);
        
        resolver.setMaxPageSize(conf.getMaxPageSize());
        resolver.setFallbackPageable(PageRequest.of(0, conf.getDefaultPageSize()));
        
        return resolver;
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
