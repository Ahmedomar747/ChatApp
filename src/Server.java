import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector; 

	class Server {
		
		
		static ArrayList<Multithread> trs = new ArrayList<Multithread>();
		static int count = 10;
		static int serverid;
		
  public static void main(String argv[]) throws Exception 
    { 

	  ServerSocket welcomeSocket = new ServerSocket(4444);
      while(true) {
            Socket connectionSocket = welcomeSocket.accept(); 
            count++;
           DataInputStream dis = new DataInputStream(connectionSocket.getInputStream()); 
           DataOutputStream  dos = new DataOutputStream(connectionSocket.getOutputStream()); 
           System.out.println("Client NO. "+ count + " Ready!");
           Multithread socketthread = new Multithread(connectionSocket,count+"",dis,dos);
           Thread t = new Thread(socketthread);
        	   trs.add(socketthread);
           t.start();
           
        } 
    }
	  
}
	class Multithread implements Runnable{
	 	String clientid;
		Socket socket;
		final DataInputStream dis;
	    final DataOutputStream dos;
	
		public Multithread(Socket socket,String id,DataInputStream dis,DataOutputStream dos) {
			this.clientid = id;
			this.socket = socket;
			this.dis = dis;
			this.dos = dos;
		}
	@Override	
	 public void run(){
		
		String dx;
	     while(true) {
		 try {
			 dx = dis.readUTF();
			 	  
		if(dx.equals("bye")) {
			this.socket.close();
		      		break;
			}
    	  String peerid = dx.substring(0, 2);
    	  String clientchat = dx.substring(3);
    	  String servertext = "Client N0. " + clientid + ":" + clientchat;
    	  System.out.println(servertext);
    	  for(Multithread mt : Server.trs) {
    		  if(mt.clientid.equals(peerid)) {
    			  mt.dos.writeUTF(servertext);
    			  mt.dos.flush();
    			  break;
    	  }
    	  }
    	  
  
		 }catch(Exception e) {System.out.println(e);;}
	     }
		 try { 
	 this.dis.close();
	 this.dos.close();
	 
	 
	 }
		 catch(IOException e) {
			 System.out.println(e);
		 }
	      
}
 }
	