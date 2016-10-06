package wetd;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
    
    //@Scheduled(fixedRate = 15 * 60000)
    public void deleteOldFiles() {
    	File rootdir = new File("./RDF_DATA/");
				
		String[] subdirs = rootdir.list();
		
		for(Object subdir : subdirs) {
			
			File m = new File("./RDF_DATA/"+subdir.toString());
			
			long time_diff = Long.valueOf(new SimpleDateFormat("mm").format(System.currentTimeMillis() - m.lastModified()));
			
			if(time_diff > 30) {
				deleteFolder(m);
				log.info("Deleted : "+subdir.toString());
			} else {
				log.info(time_diff+" min remain for "+subdir.toString());
			}
		}
    }
    
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
       
        if (null != files) { //some JVMs return null for empty dirs
            
        	for (File f: files) {
                
        		if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        
        // has to delete file or folder
        folder.delete();
    }
}
