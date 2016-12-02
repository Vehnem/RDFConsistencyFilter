package springAPI;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configure the web-page routes
 * 
 * @author Marvin Hofer
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * Error page
	 */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/suggest").setViewName("suggest.html");
    }

}