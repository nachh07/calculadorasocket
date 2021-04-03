package distribuidos;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
	 	private static ServerSocket server;
	    private static int port = 5896;
	    public static void main(String args[]) throws IOException{
	    	server = new ServerSocket(port);
	    	while(true) {
	    		System.out.println("Esperando a que un cliente se conecte...");
		    	new SocketServer(server.accept());
	    	}
	    }
	    
}
