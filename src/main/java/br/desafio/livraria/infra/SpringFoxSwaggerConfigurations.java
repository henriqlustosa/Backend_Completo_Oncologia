package br.desafio.livraria.infra;

import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxSwaggerConfigurations {

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
			
    private ApiInfo apiInfo() {
    	  return new ApiInfo(
    		      "Livraria", 
    		      "Some custom description of API.", 
    		      "API TOS", 
    		      "Terms of service", 
    		      new Contact("John Doe", "www.example.com", "myeaddress@company.com"), 
    		      "License of API", "API license URL", Collections.emptyList());
    }
}
