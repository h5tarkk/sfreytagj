package cn.iocoder.mall.product.config;

import cn.iocoder.common.framework.config.GlobalExceptionHandler;
import cn.iocoder.common.framework.config.GlobalResponseBodyAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@Import(value = {GlobalResponseBodyAdvice.class, GlobalExceptionHandler.class}) // 统一全局返回
public class MVCConfiguration implements WebMvcConfigurer {

//    @Autowired
//    private SecurityInterceptor securityInterceptor;

//    @Reference
//    private OAuth2Service oauth2Service;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(securityInterceptor);
    }

}