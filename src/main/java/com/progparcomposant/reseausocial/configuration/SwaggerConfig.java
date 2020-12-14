package com.progparcomposant.reseausocial.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    

    @Bean
    public Docket authTokenSecuredApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoSecure())
                .groupName("2- Requiring authentication") // 2 Dockets -> need to differ using groupName
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.progparcomposant.reseausocial.controllers.auth"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(new ApiKey("JWT", "Authorization", "header")))
                .securityContexts(Collections.singletonList(xAuthTokenSecurityContext()));
    }

    @Bean
    public Docket basicAuthSecuredApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoOpen())
                .groupName("1- Open area") // 2 Dockets -> need to differ using groupName
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.progparcomposant.reseausocial.controllers.open"))
                .paths(PathSelectors.any())
                .build();
    }

    private SecurityContext xBasicSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(
                        new SecurityReference("xBasic",
                                new AuthorizationScope[0])))
                .build();
    }

    private SecurityContext xAuthTokenSecurityContext() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return SecurityContext.builder().securityReferences(Arrays.asList(new SecurityReference("JWT", authorizationScopes))).build();
    }
    private ApiInfo apiInfoSecure() {
        return new ApiInfoBuilder().title("Reseau Social - Auth area")
                .description("<h2>TP final - Programmation par composant.</h2></br><h3>Authenticated Area.</h3></br>" +
                        "For all access you need a token :"+
                        "<ul>"+
                        "<li> Get token on the Open Area with  the getToken method and copy this.</li>"+
                        "<li> Click on <span>Authorize</span> button and paste all</li>"+
                        "</ul>"+
                        "You grant the access"+
                        "<a href=\"mailto:julien.dudek@lacatholille.fr\">Julien Dudek</a></br>" +
                        "<a href=\"mailto:pierre.darcas@lacatholille.fr\">Pierre Darcas</a></br>" +
                        "<a href=\"mailto:morgan.lombard@mozilla.org\">Morgan Lombard</a></br>")
                .version("0.0.1-SNAPSHOT").build();
    }
    private ApiInfo apiInfoOpen() {
        return new ApiInfoBuilder().title("Reseau Social - Open area")
                .description("<h2>TP final - Programmation par composant.</h2></br><h3>Open Area.</h3></br>" +
                        "<a href=\"mailto:julien.dudek@lacatholille.fr\">Julien Dudek</a></br>" +
                        "<a href=\"mailto:pierre.darcas@lacatholille.fr\">Pierre Darcas</a></br>" +
                        "<a href=\"mailto:morgan.lombard@mozilla.org\">Morgan Lombard</a></br>")
                .version("0.0.1-SNAPSHOT").build();
    }
}