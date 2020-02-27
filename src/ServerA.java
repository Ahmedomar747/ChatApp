import java.io.*; 
import java.net.*;
import java.util.ArrayList; 

	class ServerA {
		
		
		public static ArrayList<MultithreadA> trs = new ArrayList<MultithreadA>();
		public static ArrayList<String> cls = new ArrayList<String>();
		
  public static void main(String argv[]) throws Exception 
    { 
	  System.out.println("Server A Connected");
	  Socket servsocket = new Socket("127.0.0.1", 6789);
	  ServerSocket welcomeSocket = new ServerSocket(4444);
	  DataInputStream sdis = new DataInputStream(servsocket.getInputStream()); 
      DataOutputStream  sdos = new DataOutputStream(servsocket.getOutputStream());
      MultithreadA socketthread = new MultithreadA(servsocket,"B",sdis,sdos);
      Thread ts = new Thread(socketthread);
   	   trs.add(socketthread);
   	   System.out.println("Servers A & B are connected!");
   	   ts.start();
   	   
      
      while(true) {
            Socket connectionSocket = welcomeSocket.accept(); 
           DataInputStream dis = new DataInputStream(connectionSocket.getInputStream()); 
           DataOutputStream  dos = new DataOutputStream(connectionSocket.getOutputStream()); 
           String clname = dis.readUTF();
           cls.add(clname);
           System.out.println("Client "+ clname + " Ready!");
           MultithreadA socketthread1 = new MultithreadA(connectionSocket,clname,dis,dos);
           Thread t = new Thread(socketthread1);
        	   trs.add(socketthread1);
           t.start();
           
           
        }
    }
	  
}
	class MultithreadA implements Runnable{
	 	String clientname;
		Socket socket;
		DataInputStream dis;
	    DataOutputStream dos;

	
		public MultithreadA(Socket socket,String cl,DataInputStream dis,DataOutputStream dos) {
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
			 
			 if(clientname.equals("B")) {
				 if(dx.equals("requestm")) {
					 String x = "";
					 for(int i =0;i < ServerA.cls.size();i++) {
						 x += ServerA.trs.get(i) + " ";
					 }
					 ServerB.trs.get(0).dos.writeUTF(x);
				 }
				 String[] ax = dx.split("#");
				 String peer = ax[0];
				 if(!ax[1].equals(null)) {
				 String msg = ax[1];
				 System.out.println("From Server B to A : " + msg);
				 for(MultithreadA mt : ServerA.trs) {
		    		  if(mt.clientname.equals(peer)) {
		    			  mt.dos.writeUTF(msg);
		    			  mt.dos.flush();
		    			  break;
		    		  }
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
				 for(int i =1;i<ServerA.cls.size();i++) {
					 members += ServerA.cls.get(i) + " ";
				 }
				 members += "\n";
				 ServerA.trs.get(0).dos.writeUTF("requestm");
				 if(dis.readUTF() != null) {
				 String x = ServerA.trs.get(0).dis.readUTF();
				 dos.writeUTF(members );
				 dos.flush();
				 }
			 }else {
	  String[] ax = dx.split("#"); 
    	  String peer = ax[0];
    	  String clientchat = ax[1];
    	  //int ttl = Integer.parseInt(ax[2]);
    	  String msg = "Client " + clientname + ": " + clientchat;
    	  System.out.println(msg);
    	  if(ServerA.cls.contains(peer)) {
    	  for(MultithreadA mt : ServerA.trs) {
    		  if(mt.clientname.equals(peer)) {
    			  mt.dos.writeUTF(msg);
    			  mt.dos.flush();
    			  break;
    		  }
    	  }
    	  }else {
    		  	String newmsg = peer + "#" + msg;
    		  	ServerA.trs.get(0).dos.writeUTF(newmsg);
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

