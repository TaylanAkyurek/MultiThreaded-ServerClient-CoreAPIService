package CoreNetClient;

import java.io.IOException;
import java.util.Scanner;

import handlePayload.Payload;

public class Main {
	public static void main(String args[]) throws IOException, ClassNotFoundException {
		ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS,
				ConnectionToServer.DEFAULT_SERVER_PORT);
		connectionToServer.Connect();

		Scanner scanner = new Scanner(System.in);

		String serverResponse = " ";
		String message = "";
		long timeout;
		
		Payload payload = new Payload((byte)0,(byte)0,"temp");
	
		int i=0;

		String phase;
		

		while (true) {	
			
			
			System.out.println("which phase - 0 for auth, 1 for query, don't enter any input else");

			
			phase= scanner.nextLine();
			
			payload = new Payload((byte)Integer.parseInt(phase),(byte)0,phase);
			
			connectionToServer.sendPayload(payload);
			
			
			if (payload.phase == 0) {


				while (payload.type !=(byte)3) {
					
					
					System.out.println("Enter your id");

					timeout= System.currentTimeMillis();
					
					message = scanner.nextLine();
					
					if(((System.currentTimeMillis()-timeout)/1000)>20) {
						System.out.println("Timeout");

						connectionToServer.Disconnect();
					}					
					
					
					payload = new Payload((byte)0,(byte)0,message);
					
					connectionToServer.sendPayload(payload);

					payload = (Payload) connectionToServer.ois.readObject();
					
					System.out.println(serverResponse = payload.toString());
					


				}
				
				String temp = message;
				
				System.out.println("Enter your password");
				
				timeout= System.currentTimeMillis();
				
				message = scanner.nextLine();
				
				if(((System.currentTimeMillis()-timeout)/1000)>20) {
					System.out.println("Timeout");

					connectionToServer.Disconnect();
				}					

				i=1;
				while (!message.equals("quit")||i!=3) {
					
					payload = new Payload((byte)0,(byte)0,message);
					connectionToServer.sendPayload(payload);
					
					payload = (Payload) connectionToServer.ois.readObject();
					System.out.println(serverResponse = payload.toString());
					
					if (payload.type == (byte)3) {
						
						String tempToken;
						
						tempToken = ((Payload) connectionToServer.ois.readObject()).toString();
						connectionToServer.users.put(temp,tempToken);
						
						//following println is to show result of registered users and unique tokens
						System.out.println(connectionToServer.users);
						

						break;
					}
					
					if(payload.type == (byte)2) {
						break;
					}
					
					timeout= System.currentTimeMillis();
					
					message = scanner.nextLine();
					
					if(((System.currentTimeMillis()-timeout)/1000)>20) {
						System.out.println("Timeout");

						connectionToServer.Disconnect();
					}	
					
					i++;

				}
				
			} else {
				
				
				payload = (Payload) connectionToServer.ois.readObject();
				System.out.println(serverResponse = payload.toString());


				timeout= System.currentTimeMillis();
				
				message = scanner.nextLine();
				
				if(((System.currentTimeMillis()-timeout)/1000)>20) {
				
					System.out.println("Timeout");
					connectionToServer.Disconnect();
				}	
				String tokenTemp;
				if(connectionToServer.users.containsKey(message)) {
					tokenTemp =connectionToServer.users.get(message);

				}
				else {
					tokenTemp = "000000";

				}
				String headerPlusMessage; 

				payload = new Payload((byte)0,(byte)0,message);
				connectionToServer.sendPayload(payload);


				if(message.compareTo("no request status received")!= 0) {
					
					System.out.println("Enter your search request, to head of your string, add 0  for articles, add 1 for journals");
				}
				
				timeout= System.currentTimeMillis();
				
				message = scanner.nextLine();
				
				
				if(((System.currentTimeMillis()-timeout)/1000)>20) {
					System.out.println("Timeout");
					connectionToServer.Disconnect();
				}					
				
				
				headerPlusMessage = tokenTemp + message;

				while (true) {
					if(message.equals("quit")) {
						payload = new Payload((byte)0,(byte)0,"quit");
						connectionToServer.sendPayload(payload);
						break;
					}
					
					byte tempByte;
					if(message.substring(0, 1).compareTo("1") == 0) {
						tempByte=(byte)1;
					}
					else if (message.substring(0, 1).compareTo("0") == 0){
						tempByte=(byte)0;

					}
					else {
						tempByte= (byte)3;
					}
					payload = new Payload((byte)1,tempByte,headerPlusMessage);
					connectionToServer.sendPayload(payload);
					
					payload = (Payload) connectionToServer.ois.readObject();
					System.out.println(serverResponse = payload.toString());
					
					timeout= System.currentTimeMillis();
					
					message = scanner.nextLine();
					
					if(((System.currentTimeMillis()-timeout)/1000)>20) {
						
						System.out.println("Timeout");

						connectionToServer.Disconnect();
					}
					
					headerPlusMessage = tokenTemp + message;

					
				}
			}
		}
	}
}

