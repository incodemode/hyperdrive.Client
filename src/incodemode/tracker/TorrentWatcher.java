package incodemode.tracker;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class TorrentWatcher  extends Thread{
	WatchService watcher;
	public TorrentWatcher(File usersFile){
		Path usersPath = usersFile.toPath();
		       
	
	    try {
	       watcher = usersPath.getFileSystem().newWatchService();
	       usersPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
	       StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
	       System.out.println("llega a watcher take");
	       
	       System.out.println("pasa watcher take");
	       
	       
	    } catch (Exception e) {
	        System.out.println("Error: " + e.toString());
	    }
	    
	}

	@Override
	public void run() {
		WatchKey watckKey;
		while(true){
		try {
			watckKey = watcher.take();
		
		System.out.println("llega al run");
		
			try {
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			List<WatchEvent<?>> events = watckKey.pollEvents();
		       for (WatchEvent event : events) {
		            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
		                System.out.println("Created: " + event.context().toString());
		            }
		            if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
		                System.out.println("Delete: " + event.context().toString());
		            }
		            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
		                System.out.println("Modify: " + event.context().toString());
		            }
		        }
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		
	}
}
