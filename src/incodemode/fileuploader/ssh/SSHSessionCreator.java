package incodemode.fileuploader.ssh;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.turn.ttorrent.client.SharedTorrent;

import incodemode.ttorrent.Client;

public class SSHSessionCreator extends Thread{
	
	public Session sshSession = null;
	
	public SSHSessionCreator(){
		super("SSHSessionCreator");
	}
	
	public Session create(){
		JSch jsch = new JSch();
        
		try {
			sshSession = jsch.getSession("root", "incodemode.com", 22);
		
	        sshSession.setPassword("holaMundo3$");
	        sshSession.setConfig("StrictHostKeyChecking", "no");
	        sshSession.connect();
	        start();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sshSession;
	}
	
	public void connect(){
		
		
		
	}
	public void run(){
		
			
			do{
				System.out.println("checking ssh connection");
				if(!sshSession.isConnected()){
					try {
						// disable socks proxy
						sshSession.connect();
						// reenable socks proxy
						System.out.println("trying to reconnect");
					}catch (JSchException e) {
						// TODO Auto-generated catch block
						
						//alerta al usuario
						e.printStackTrace();
					}
				}else{
					System.out.println("Session is working");
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(true);
	}
	
}
