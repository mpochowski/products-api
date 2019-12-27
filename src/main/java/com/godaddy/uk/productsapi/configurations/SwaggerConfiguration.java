package com.godaddy.uk.productsapi.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /**
     * Creates Docket bean for SpringFox integration.
     * Specifies path to controllers, where swagger documentation can be generated from.
     *
     * @return configured Docket object
     */
    @Bean
    public Docket buildDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Authentication.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.godaddy.uk.productsapi.controllers"))
                .build()
                .apiInfo(buildApiDocumentation());
    }

    /**
     * Builds API Static Info.
     *
     * @return ApiInfo instance
     */
    private ApiInfo buildApiDocumentation() {
        return new ApiInfoBuilder()
                .title("GoDaddy Products API")
                .description("GoDaddy Products Spring Boot REST API Server")
                .version("1.0.0")
                .build();
    }

}