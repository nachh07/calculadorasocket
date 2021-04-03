package distribuidos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;

public class SocketClient {

    public static void main(String[] args) throws IOException{
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 5896);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //Transforma y mete en el output los objetos-true autoflush
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //cambia bytes a caracter
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String ecuacion="";
        String respuesta = "";
        
        while(true){
			System.out.print("Ingresá la expresión :\n(q para salir)\n");
		 	ecuacion= br.readLine();
	        out.println(ecuacion);
	        respuesta= (String) in.readLine();
	        System.out.println("Respuesta: " + respuesta);
	        if(ecuacion.equalsIgnoreCase("q")) {
		        break;
	        }
        }
    }
}