package incodemode.ttorrent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.slf4j.Logger;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class PortChooser {
	ServerSocketChannel channel;
	InetSocketAddress address;
	Logger logger;
	Session session;
	
	public PortChooser(ServerSocketChannel channel, InetSocketAddress address, Logger logger, Session session){
		
		this.channel = channel;
		this.address = address;
		this.logger = logger;
		this.session = session;
	}
	
	public InetSocketAddress getPort(InetAddress address){
		InetSocketAddress portAddress = null;
		int port = 0;
		for (port = ConnectionHandler.PORT_RANGE_START; port <= ConnectionHandler.PORT_RANGE_END; port++) {
			InetSocketAddress tryAddress = new InetSocketAddress(address, port);
			System.out.println(address.toString());
			System.out.println("thisPort: " + port);
			try {
				System.out.println("before port forwarding");
				session.setPortForwardingR(port, "localhost", port); 
				System.out.println("setted port forwarding");
				this.channel = ServerSocketChannel.open();
				
				System.out.println("openned forwarding");
				this.channel.socket().bind(tryAddress);
				System.out.println("binded");
				this.channel.configureBlocking(false);
				System.out.println("configureBlocking done");
				this.address = tryAddress;
				break;
			} catch (IOException ioe) {
				// Ignore, try next port
				System.out.println("error en coneccion io "+port);
				try {
					this.channel.socket().close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logger.warn("Could not bind to {}, trying next port...", tryAddress);
				try {
					System.out.println("cerrando puerto de ssh "+port);
					session.delPortForwardingR(port);
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					System.out.println("error al intentar cerrar el puerto de ssh "+port);
					//e.printStackTrace();
				}
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("error al intentar abrir el puerto de ssh "+port);
				logger.warn("Could not forwardR to {}, trying next port...", tryAddress);
			}
		}
		System.out.println("thisPort: " + port);
		System.out.println("well connected");
		return portAddress;
		
	}
}
