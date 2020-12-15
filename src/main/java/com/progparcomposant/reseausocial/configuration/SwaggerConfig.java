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

/**
 * We can configure access in just one area but in this case I choose separate in two.
 *For configure auhotization only on mendpoint acces:
 *use @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String FRIENDSHIP="Relationships Management";
    public static final String INVITATION="Invitations Management";
    public static final String POST="Posts Management";
    public static final String USER="Users Management";
    public static final String ACCESS="Access Management";

    @Bean
    public Docket authTokenSecuredApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoSecure())
                .groupName("2- Auth Area (Requiring token)")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.progparcomposant.reseausocial.controllers.auth"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(FRIENDSHIP,"Manage users relationships"))
                .tags(new Tag(INVITATION,"All about invitations"))
                .tags(new Tag(POST,"All actions on posts are here"))
                .tags(new Tag(USER,"User? you can do anything here."))
                .securitySchemes(Collections.singletonList(new ApiKey("JWT", "Authorization", "header")))
                .securityContexts(Collections.singletonList(xAuthTokenSecurityContext()));
    }

    @Bean
    public Docket basicAuthSecuredApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoOpen())
                .groupName("1- Open area")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.progparcomposant.reseausocial.controllers.open"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(ACCESS,"Register and login. GetToken only for swagger"));
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
                        "Master III - M1 - S1"+
                        "<h3>Authenticated Area.</h3>" +
                        "For all access: Take a token !"+
                        "<ul>"+
                        "<li> Get token on the Open Area with  the getToken method and copy this.</li>"+
                        "<li> Click on Authorize button and paste all</li></ul>"+
                        "Access is granted to you.</br>"+
                        "<a href=\"mailto:julien.dudek@lacatholille.fr\">Julien Dudek</a></br>" +
                        "<a href=\"mailto:pierre.darcas@lacatholille.fr\">Pierre Darcas</a></br>" +
                        "<a href=\"mailto:morgan.lombard@lacatholille.fr\">Morgan Lombard</a></br>")
                .version("0.0.1-SNAPSHOT").build();
    }
    private ApiInfo apiInfoOpen() {
        return new ApiInfoBuilder().title("Reseau Social - Open area")
                .description("<h2>TP final - Programmation par composant.</h2></br><h3>Open Area.</h3></br>" +
                        "Master III - M1 - S1\n"+
                        "On the top right of the window, you can select the secure area.\n" +
                        "First, call \"getToken\" and copy its entire response.\n" +
                        "You need do that for testing all API in the Auth zone.\n"+
                        "You need do that for testing all API in the Auth zone.\n"+
                        "<a href=\"mailto:julien.dudek@lacatholille.fr\">Julien Dudek</a></br>" +
                        "<a href=\"mailto:pierre.darcas@lacatholille.fr\">Pierre Darcas</a></br>" +
                        "<a href=\"mailto:morgan.lombard@lacatholille.fr\">Morgan Lombard</a></br>")
                .version("0.0.1-SNAPSHOT").build();
    }
}