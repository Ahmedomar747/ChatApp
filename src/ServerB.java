import java.io.*; 
import java.net.*;
import java.util.ArrayList;

	class ServerB {
		
		
		public static ArrayList<MultithreadB> trs = new ArrayList<MultithreadB>();
		public static ArrayList<String> cls = new ArrayList<String>();
		static int count =0;
  public static void main(String argv[]) throws Exception 
    { 
	  
	  System.out.println("Server B Connected");
	  ServerSocket welcomeSocket = new ServerSocket(6789);     
      while(true) {
    	  	String clname;
            Socket connectionSocket = welcomeSocket.accept(); 
           DataInputStream dis = new DataInputStream(connectionSocket.getInputStream()); 
           DataOutputStream  dos = new DataOutputStream(connectionSocket.getOutputStream()); 
           if(count > 0) {
        	   clname = dis.readUTF();
           System.out.println("Client "+ clname + " Ready!");
           }
           else {
        	   clname = "A";
        	   System.out.println("Servers A & B are connected!");
           }
           count++;
           cls.add(clname); 
           MultithreadB socketthread = new MultithreadB(connectionSocket,clname,dis,dos);
           Thread t = new Thread(socketthread);
        	   trs.add(socketthread);
           t.start();  
        }
    }
	  
}
	class MultithreadB implements Runnable{
	 	String clientname;
		Socket socket;
		DataInputStream dis;
	    DataOutputStream dos;

	
		public MultithreadB(Socket socket,String cl,DataInputStream dis,DataOutputStream dos) {
			this.clientname = cl;
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
			 if(dx.equals("requestm")) {
				 String x = "";
				 for(int i =0;i < ServerB.cls.size();i++) {
					 x += ServerB.trs.get(i) + " ";
				 }
				 ServerB.trs.get(0).dos.writeUTF(x);
			 }
			 
			 if(clientname.equals("A")) {
				 String[] ax = dx.split("#");
				 String peer = ax[0];
				 String msg = ax[1];
				 System.out.println("From Server B to A : " + msg);
				 for(MultithreadB mt : ServerB.trs) {
		    		  if(mt.clientname.equals(peer)) {
		    			  mt.dos.writeUTF(msg);
		    			  mt.dos.flush();
		    			  break;
		    		  }
				 
					 }
				 
			 }
			 else {
			 if(dx.equals("bye")) {
				 this.socket.close();
		      		break;
			 			}
			 if(dx.equals("getmembers")) {
				 String members = "";
				 for(int i =0;i<ServerA.cls.size();i++) {
					 members += ServerA.cls.get(i) + " ";
				 }
				 members += "\n";
				 ServerA.trs.get(0).dos.writeUTF("requestm");
				 String x = ServerA.trs.get(0).dis.readUTF();
				 dos.writeUTF(members);
				 //dos.flush();
			 }else {
	  String[] ax = dx.split("#"); 
    	  String peer = ax[0];
    	  String clientchat = ax[1];
    	  //int ttl = Integer.parseInt(ax[2]);
    	  String msg = "Client " + clientname + ": " + clientchat;
    	  System.out.println(msg);
    	  if(ServerB.cls.contains(peer)) {
    	  for(MultithreadB mt : ServerB.trs) {
    		  if(mt.clientname.equals(peer)) {
    			  mt.dos.writeUTF(msg);
    			  mt.dos.flush();
    			  break;
    		  }
    	  }
    	  }else {
    		  	String newmsg = peer + "#" + msg;
    		  	ServerB.trs.get(0).dos.writeUTF(newmsg);
    	  }
			 }
    	  }
		 } 
    	  
  
		 
	     catch(Exception e) {System.out.println(e);break;}
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
	