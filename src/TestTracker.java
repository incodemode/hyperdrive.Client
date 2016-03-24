import java.io.File;
import java.io.FilenameFilter;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;


import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

public class TestTracker {
	
	public void track(){
		// First, instantiate a Tracker object with the port you want it to listen on.
		// The default tracker port recommended by the BitTorrent protocol is 6969.
		Tracker tracker;
		try {
			tracker = new Tracker(new InetSocketAddress(6969));
		

			// Then, for each torrent you wish to announce on this tracker, simply created
			// a TrackedTorrent object and pass it to the tracker.announce() method:
			
			System.out.println("iterating filtered files");
			String[] extensions = new String [] {"torrent"};	
			
			String patternString = "[/\\\\]users[/\\\\][0-9a-zA-Z_-]+[/\\\\]torrents";
			Pattern pattern = Pattern.compile(patternString);
			
					
			for (File f : FileUtils.listFiles(new File("d:/torrent/users/mercenario"), extensions, true)) {
				String filePath = f.getAbsolutePath();
				Matcher matcher = pattern.matcher(filePath);
				System.out.println(f.toString());
				if(matcher.find()){
					System.out.println("was added");
					tracker.announce(TrackedTorrent.load(f));
				}
			}
	
			// Once done, you just have to start the tracker's main operation loop:
			// tracker.start();
	
			// You can stop the tracker when you're done with:
			// tracker.stop();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
