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

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String FRIENDSHIP="Relationships Management.";
    public static final String INVITATION="Invitations Management.";
    public static final String POST="Posts Management.";
    public static final String USER="Users Management";
    public static final String ACCESS="Access Management";

    @Bean
    public Docket authTokenSecuredApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoSecure())
                .groupName("2- Requiring authentication") // 2 Dockets -> need to differ using groupName
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.progparcomposant.reseausocial.controllers.auth"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(FRIENDSHIP,""))
                .tags(new Tag(INVITATION,""))
                .tags(new Tag(POST,""))
                .tags(new Tag(USER,""))
                .securitySchemes(Collections.singletonList(new ApiKey("JSON Web Token (JWT", "Bearer", "header")))
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
                .build()
                .tags(new Tag(ACCESS,""));
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
                .description("<h2>TP final - Programmation par composant.</h2>"+
                        "<h3>Authenticated Area.</h3>" +
                        "For all access: Take a token !"+
                        "<ul>"+
                        "<li> Get token on the Open Area with  the getToken method and copy this.</li>"+
                        "<li> Click on Authorize button and paste all</li></ul>"+
                        "Access is granted to you.</br>"+
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