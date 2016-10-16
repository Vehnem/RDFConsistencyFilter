package v122;

import static springfox.documentation.builders.PathSelectors.regex;

//http://localhost:8080/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@ComponentScan("v122")
public class SpringBoot {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot.class, args);
	}

	/**
	 * Swagger doc for /rdf-cf Controller
	 * 
	 * @return
	 */
	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("default")
				.apiInfo(apiInfo())
				.select()
				.paths(regex("/rdf-cf.*")).build();
	}

	//@Override
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Api Information").
				description("Api Information with SWAGGER")
				.termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
				.contact("Marvin Hofer")
				.license("Apache License Version 2.0")
				.licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
				.version("2.0")
				.build();
	}
}

//TODO
//filter several properties at once so you not need to save dataset_result in Filesystem
//SWAGGER API load Controller example query fix
//Default Controller + SWAGGER + RDF_EXAMPLES
//JSON or POJO return