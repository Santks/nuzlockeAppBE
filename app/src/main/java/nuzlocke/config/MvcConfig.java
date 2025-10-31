package nuzlocke.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final IdempotencyFilter idempotencyFilter;

    @Autowired
    public MvcConfig(IdempotencyFilter idempotencyFilter) {
        this.idempotencyFilter = idempotencyFilter;
    }

    public void addInterceptorFilter(InterceptorRegistry registry) {
        registry.addInterceptor(idempotencyFilter);
    }

}
