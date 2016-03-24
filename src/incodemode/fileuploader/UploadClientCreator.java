package incodemode.fileuploader;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.turn.ttorrent.client.Client.ClientState;
import com.turn.ttorrent.client.SharedTorrent;

import incodemode.ttorrent.Client;

public class UploadClientCreator {
	
	public static void createClient(Session sshSession, File torrentFile, File locationFile){
	  
		
		
		try {
			
	        
	        System.out.println("connection established");
			Client client;
			//System.out.println(getRouterAddress());
			//InetAddress ia = InetAddress.getLocalHost();
			//ia = getInetAddress();
			//System.out.println(ia.toString());
			//InetSocketAddress ia2 = new InetSocketAddress("localhost", 9999);
			
			InetSocketAddress ia3 = new InetSocketAddress("localhost",6868);
			File parentLocationFile = locationFile.getParentFile();
			
			SharedTorrent torrent = SharedTorrent.fromFile(torrentFile, parentLocationFile);
			
			client = new Client( ia3.getAddress() , torrent , sshSession );
			
			client.setMaxDownloadRate(0.0);
			client.setMaxUploadRate(0.0);
		
			
			client.download();
			client.share();
			
			System.out.println("UploadClient - Uploading - " + locationFile.getAbsolutePath());
			// Downloading and seeding is done in background threads.
			// To wait for this process to finish, call:
			incodemode.ttorrent.Client.ClientState state = client.getState();
			System.out.println(state);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
