package incodemode.fileuploader.ssh;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import incodemode.hyperdrive.tracker.restserver.units.ExecutionResult;

public class SSHProxy extends Thread{
	
	public Session sshSession;
	public Integer localProxyPort = null;
	public String serverIp;
	public int firstServerPort = 9000;
	public int lastServerPort = 9100;
	
	public SSHProxy(Session sshSession, String serverIp){
		
		this.sshSession = sshSession;
		this.serverIp = serverIp;
		
		makeProxy();
		
	}
	
	public void run(){
		while(true){
			System.out.println("checking ssh Proxy");
			if(!checkProxy()){
				System.out.println("checkProxy did not pass, fixing");
				deletePort();
				makeProxy();
			}else{
				System.out.println("sshProxy is working");
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
        
        
        
        
        
		
	}
	
	public void makeProxy(){
		if(localProxyPort == null){
			localProxyPort = 9999;
		}
        for(int i = 0; i< 1000 ; i++ ){
        	try{
        		sshSession.setPortForwardingL(localProxyPort, serverIp, 9995);//for download from server (connect to proxy maybe not needed)
        		
        		break;
        	}catch (JSchException e) {
    			// TODO Auto-generated catch block
    			//e.printStackTrace();
    		}
        	localProxyPort++;
        }
        
        //sshSession.setPortForwardingL(9998, "localhost", 9995);
        String localProxyPortString = Integer.toString(localProxyPort);
        System.out.println("localProxyPort has been set: " + localProxyPort);
        
        System.getProperties().put( "proxySet", "true" );
        //System.getProperties().put( "socksnonProxyHosts", "localhost" );
        System.getProperties().put( "socksProxyHost", "localhost" ); //talvez cambiar esto a localhost
        System.getProperties().put( "socksProxyPort", localProxyPortString);
	}
	public void deletePort(){
		try {
			if(localProxyPort != null){
				sshSession.delPortForwardingL(localProxyPort);
			}
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkProxy(){
		String proxyCheckUrl = "http://"+serverIp+":8080/proxy/check";
		
		RestTemplate restTemplate = new RestTemplate();
		try{
			ExecutionResult<String> executionResult = (ExecutionResult<String>) restTemplate.getForObject(proxyCheckUrl, ExecutionResult.class);
			System.out.println("======================================="+executionResult.getResponse());
			System.out.println(serverIp);
			System.out.println(executionResult.getResponse());
			String testedIp =  executionResult.getResponse();
			if(serverIp.equals(testedIp)){
				return true;
			}
			
		}catch(RestClientException e){
			System.out.println(e.toString());
		}
		
		return false;
	}
	
}
