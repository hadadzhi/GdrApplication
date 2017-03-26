package ru.cdfe.gdr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableEntityLinks
@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    private final MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    
    @Autowired
    public WebMvcConfigurer(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        this.jackson2HttpMessageConverter = jackson2HttpMessageConverter;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter);
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
}
