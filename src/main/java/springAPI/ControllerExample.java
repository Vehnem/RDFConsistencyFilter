package springAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/test")
public class ControllerExample {

	static final String VERSION = "v1";
	
	/**
	 * testversion 1
	 */
	@RequestMapping(value = "/checkversion", method = RequestMethod.GET, produces = { "application/v1+json" })
	public String method_version1() {

		return "Version 1.0";
	}

	/**
	 * testversion 2
	 */
	@RequestMapping(value = "/checkversion", method = RequestMethod.GET, produces = { "application/v2+json" })
	public String method_version2() {

		return "Version 2.0";
	}
	
	@RequestMapping(value="unitTestExample",method = RequestMethod.GET, produces = "application/"+VERSION+"+json") 
	public List<String> methodtest(@RequestParam(value="param", required=false) String param) {
	
		List<String> list = new ArrayList<String>(Arrays.asList("Berlin", "Germany"));
			
		if(param != null) {
			list.add(param);
		}
		return list;
	}
	
	
}
