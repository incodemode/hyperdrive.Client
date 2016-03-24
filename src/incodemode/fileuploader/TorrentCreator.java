package incodemode.fileuploader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import incodemode.ttorrent.Torrent;
import incodemode.hyperdrive.checker.Md5;

public class TorrentCreator {
	Md5 md5;
	public TorrentCreator(){
		md5 = new Md5();
	}
	
	//returns path to torrent
	public File create(File uploadFile){
		try {
			
			System.out.println("creating file");
			File torrentFile = getForNewFile(uploadFile);
			if(!torrentFile.exists()){
				String announceUrlString = Config.getAnnounceUrl();
				URL announceUrl = new URL(announceUrlString);
				URI announceUri = announceUrl.toURI();
				
				Torrent torrent = null;
				
				if(uploadFile.isDirectory()){
			        List<File> filesList = (List<File>) FileUtils.listFiles(uploadFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			        
			        torrent = Torrent.create(uploadFile, filesList, announceUri , "created by Incodemode");
				}else{
					System.out.println("llega a single file upload creator");
					torrent = Torrent.create(uploadFile, announceUri , "created by Incodemode");
				}
			
			
			
			//torrentFile = new File("d:/torrent/mercenariovsigor.torrent");
			
				
				FileOutputStream fileStream = new FileOutputStream(torrentFile);
				
				
				torrent.save(fileStream);
				fileStream.close();
			}
			System.out.println(torrentFile.toString() + " might have been created for " + uploadFile.getName());
			return torrentFile;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
        
	}
	public File getForNewFile(File file) {
		
		File f = null;
		
		Long timeMillis = System.currentTimeMillis();
		String torrentPrefix = Long.toString(timeMillis);
		
		String md5String = md5.md5(file);
		System.out.println("el md5 es: " + md5String);
		String userName = Config.getUserName();
		f = new File("d:/torrent/"+  userName + "-" + md5String + "-" + file.getName() + ".torrent");
		return f;
		
		
	}
	public String getMd5(File f){
		return md5.md5(f);
	}
	public static String getMd5_2(File f){
		if(!f.isFile()){
			return "folder";
		}
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			Path filePath = Paths.get(f.getPath());
		
			InputStream is;
			
				is = Files.newInputStream(filePath, (OpenOption) StandardOpenOption.READ);
			
		    DigestInputStream dis = new DigestInputStream(is, md);
		  /* Read decorated stream (dis) to EOF as normal... */
		
		    byte[] digest = md.digest();
		    String hex = DatatypeConverter.printHexBinary(digest);
		    return hex;
		    //return new String(digest, StandardCharsets.UTF_8);
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
