package incodemode.fileuploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class TorrentFileUploader {
	public static boolean uploadFile(Session sshSession, File torrentFile){
		boolean uploaded = uploadFile(sshSession, torrentFile, 0);
		if(!uploaded){
			// make all the fuzz
		}
		return uploaded;
	}
	public static boolean uploadFile(Session sshSession, File torrentFile, int chance){
		
		String workingDirectory = getUploadTorrentDirectory();
		boolean error = false;
		Channel channel;
		try {
			channel = sshSession.openChannel("sftp");
		
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			System.out.println("working sftp working directory: " + workingDirectory);
			channelSftp.cd("/var/www/client-torrents");
	
			String torrentName = torrentFile.getName();
			System.out.println("torrent file to upload: " + torrentFile.toString());
			channelSftp.put(new FileInputStream(torrentFile), torrentName, ChannelSftp.OVERWRITE);
			channelSftp.cd("/var/www/torrents");
			channelSftp.put(new FileInputStream(torrentFile), torrentName, ChannelSftp.OVERWRITE);
			
			channelSftp.exit();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			error = true;
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			error = true;
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			error = true;
			e.printStackTrace();
		}
		
		if(error && chance < 10){
			uploadFile(sshSession, torrentFile, chance+1);
		}else if (error){
			return false;
		}
		return true;
	}
	
	public static String getUploadTorrentDirectory(){
		
		String usersDirectory = Config.getUsersDirectory();
		String userName = Config.getUserName();
		
		String uploadTorrentDirectory = "/var/www/torrents/";
		
		return uploadTorrentDirectory;
		
	}
}
