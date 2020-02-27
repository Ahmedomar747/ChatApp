import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.io.*; 
import java.net.*;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument.Content; 
class Client { 
	
	 
    public static void write(DataOutputStream  dos,String x) {
		try {
			dos.writeUTF(x);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
public static void read(String x) {
		System.out.println(x);
}

    public static void main(String argv[]) throws Exception 
    { 
      
        int port = 0;
        BufferedReader in = 
                new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Select Server A or B.");
        while(true) {
        	String intext = in.readLine();
        if(intext.equals("A")) {
        	port = 4444;
        	System.out.println("Connected to Server A");
        	break;
        }
        else{
        	if(intext.equals("B")){
        		port = 6789;
        		System.out.println("Connected to Server B");
        		break;
        }
        else {
        	System.out.println("Error invalid Server! try again");}
        }
        }
        Socket clientSocket = new Socket("127.0.0.1", port);
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream()); 
        DataOutputStream  dos = new DataOutputStream(clientSocket.getOutputStream()); 
           
        System.out.println("Enter your name.");
        dos.writeUTF(in.readLine());
        Thread trread = new Thread(new Runnable() {
        	@Override
    		public void run() {
    			while(true) {
    				try {
    					if(dis.readUTF() != null) {
    						String dx = dis.readUTF();
    					read(dx);
    					}
    				} catch (IOException e) {
    					e.printStackTrace();break;
    				}
    			}
    			try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
        	
        });
        Thread trwrite = new Thread(new Runnable() {
        	@Override
    		public void run() {
    			while(true) {			
    				try {
    					String da = in.readLine();
    						write(dos,da);
    					
    					
    					}
    					
    				 catch (IOException e) {
    					e.printStackTrace();break;
    				}
    			}
    			try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		
    		}
        });
        trwrite.start();
        trread.start();
        
    } 
    
}
	