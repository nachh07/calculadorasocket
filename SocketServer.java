package distribuidos;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketServer extends Thread {
		protected Socket socket;
			public SocketServer(Socket socket) {
			this.socket = socket;
			System.out.println("Se ha conectado un nuevo cliente!");
			System.out.println("IP:" + socket.getInetAddress().getHostAddress()); 
			System.out.println("Puerto: "+socket.getLocalPort());
	        start();
		}
		
		public void run(){
			 while(!this.isInterrupted()){
				 try {
		            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            String request;
		            while ((request = in.readLine()) != null) {
		                System.out.println("Mensaje recibido:" + request);
		                
		                if(request.equalsIgnoreCase("q") || request.equalsIgnoreCase("Q")) { 
		                	out.println("Cerrando conexión");
		                	in.close();
		                    out.close();
		                    socket.close();
		                    this.interrupt();
		                	break;
		                };
		                try{
		                	out.println(eval(""+request));
		                }catch(Exception e) {
		                	out.println("Expresion incorrecta. Revisa la expresión [q para salir]");
		                }
		            }
				 }
				 catch(Exception err) {
					 System.out.println(err);
					 break;
				 }
		     }
		}
		
	    public double eval(final String str) {
	        return new Object() {
	            int pos = -1, ch;
	
	            void nextChar() {
	                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	            }
	
	            boolean eat(int charToEat) {
	                while (ch == ' ') nextChar();
	                if (ch == charToEat) {
	                    nextChar();
	                    return true;
	                }
	                return false;
	            }
	
	            double parse() {
	                nextChar();
	                double x = parseExpression();
	                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	                return x;
	            }
	
	            double parseExpression() {
	                double x = parseTerm();
	                for (;;) {
	                    if      (eat('+')) x += parseTerm(); // Suma 
	                    else if (eat('-')) x -= parseTerm(); // Resta
	                    else return x;
	                }
	            }
	
	            double parseTerm() {
	                double x = parseFactor();
	                for (;;) {
	                    if      (eat('*')) x *= parseFactor(); // multiplicación
	                    else if (eat('/')) x /= parseFactor(); // division
	                    else return x;
	                }
	            }
	
	            double parseFactor() {
	                if (eat('+')) return parseFactor(); 
	                if (eat('-')) return -parseFactor(); 
	
	                double x;
	                int startPos = this.pos;
	                if (eat('(')) { // parentesis
	                    x = parseExpression();
	                    eat(')');
	                } else if ((ch >= '0' && ch <= '9') || ch == '.') { //numeros
	                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                    x = Double.parseDouble(str.substring(startPos, this.pos));
	                } else if (ch >= 'a' && ch <= 'z') { // funciones
	                    while (ch >= 'a' && ch <= 'z') nextChar();
	                    String func = str.substring(startPos, this.pos);
	                    x = parseFactor();
	                    if (func.equals("sqrt")) x = Math.sqrt(x);
	                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                    else throw new RuntimeException("Unknown function: " + func);
	                } else {
	                    throw new RuntimeException("Unexpected: " + (char)ch);
	                }
	
	                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentes
	
	                return x;
	            }
	        }.parse();
	    }
}