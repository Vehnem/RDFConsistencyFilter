package springAPI;

import static springfox.documentation.builders.PathSelectors.regex;

//http://localhost:8080/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import rdfcf.SingletonStore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Start the SpringBootApplication / RestService
 */
@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@ComponentScan("springAPI")
public class SpringBoot {

	public static void main(String[] args) {
		
		/*
		 * Store prepare
		 */
		
		SingletonStore store = SingletonStore.getInstance();
		
		//useMemory ?
		store.init(true);
		
		/*
		 * Spring start
		 */
		SpringApplication.run(SpringBoot.class, args);
	}

	/**
	 * Swagger doc for Spring REsT Controller
	 * 
	 * @return
	 */
	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("default")
				.apiInfo(apiInfo())
				.select()
				.paths(regex("/rdfcf.*|/kgutil.*")).build();
	}

	/**
	 * ApiInfo configuration
	 * 
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Knowledge Graph API").
				description("description")
				.termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
				.contact("Marvin Hofer")
				.license("Apache License Version 2.0")
				.licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
				.version("1.0")
				.build();
	}
}

//Info



//TODO
//+SWAGGER API load Controller example query fix
//Escape Uri <http> with sign
//(-JSON or POJO return) not needed yet
//Filter issue