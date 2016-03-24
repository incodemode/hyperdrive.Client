import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jcraft.jsch.Session;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.Tracker;

import incodemode.fileuploader.Config;
import incodemode.fileuploader.TorrentCreator;
import incodemode.fileuploader.TorrentFileUploader;
import incodemode.fileuploader.UploadClientCreator;
import incodemode.fileuploader.ssh.SSHProxy;
import incodemode.fileuploader.ssh.SSHSessionCreator;
import incodemode.hyperdrive.tracker.restserver.units.ExecutionResult;
import incodemode.tracker.TorrentWatcher;

@SpringBootApplication
public class Main {
	File file;
	public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
		
		//File usersFile = new File("d:/torrent/users");
		
		//new TestTracker().track();
		//System.out.println("entra al torrent watcher");
		//new TorrentWatcher(usersFile).start();
		//System.out.println("pasa del torrent watcher");
		
		
		String amazonPath = "/recasts/";
		
		
		
		String uploadPath = "d:/recasts/2016-02-05 Dancer (P) vs LllukeJ (P)";
		//String uploadPath = "d:/torrent/testFolder";
		File uploadFile = new File(uploadPath);
		
		
		TorrentCreator torrentCreator = new TorrentCreator();
		String md5 = torrentCreator.getMd5(uploadFile);
		System.out.println(md5);
	
	
		File torrentFile = torrentCreator.create(uploadFile);
		
		SSHSessionCreator sessionCreator = new SSHSessionCreator();
		System.out.println("pasa de sshSessionCreator");
		Session sshSession = sessionCreator.create();
		System.out.println("pasa de create de sessionCreator");
		
		String serverIp = Config.getServerIp();
		
		SSHProxy proxy = new SSHProxy(sshSession, serverIp);
		proxy.start();
		
		TorrentFileUploader.uploadFile(sshSession, torrentFile);
		
		UploadClientCreator.createClient(sshSession, torrentFile, uploadFile);
		
		
		String startUploadUrl = "http://incodemode.com:8080/torrent/upload?md5="+md5+"&name="+ uploadFile.getName() + "&amazonPath=" + amazonPath;
		
		RestTemplate restTemplate = new RestTemplate();
		try{
			ExecutionResult executionResult = restTemplate.getForObject(startUploadUrl, ExecutionResult.class);
			System.out.println(executionResult);
		}catch(RestClientException e){
			System.out.println(e.toString());
		}
        
        //log.info(quote.toString());
        
		System.out.println(startUploadUrl);
        //
        
    }

}
