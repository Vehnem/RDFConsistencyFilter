package v122;

import java.io.File;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//
//    //@Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        log.info("The time is now {}", dateFormat.format(new Date()));
//    }
    
    /**
     * Removes data-sets older then 30 minutes from the file-system
     */
    @Scheduled(fixedRate = 15 * 60000)
    public void deleteOldFiles() {
    	File rootdir = new File("./RDF_DATA/");
				
		String[] subdirs = rootdir.list();
		
		for(Object subdir : subdirs) {
			
			File m = new File("./RDF_DATA/"+subdir.toString());
			
			long time_diff = Long.valueOf(new SimpleDateFormat("mm").format(System.currentTimeMillis() - m.lastModified()));
			
			if(time_diff > 30) {
				if( false == subdir.toString().equals( "README.md") ) deleteFolder(m);
				log.info("Deleted : "+subdir.toString());
			} else {
				log.info((30 -time_diff)+" min remain for "+subdir.toString());
			}
		}
    }
    
    /**
     * recursive folder deletion
     * 
     * @param folder
     */
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
        
        // has to delete file or empty folder
        folder.delete();
    }
}
