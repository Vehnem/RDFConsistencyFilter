package wetd;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class TryOut {

	public void tryhard() {
		File f = new File("./RDF_DATA/");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		
		String[] directories = f.list();
		
		for(Object d : directories) {
			
			File m = new File("./RDF_DATA/"+d.toString());
			
			String[] list = m.list();
			
			System.out.println(Arrays.toString(list));
		}
		
	}
}
