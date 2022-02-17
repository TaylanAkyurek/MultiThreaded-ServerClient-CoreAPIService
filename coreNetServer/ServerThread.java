package coreNetServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import handlePayload.Payload;
import handlePayload.Phase1Payload;

class ServerThread extends Thread {
	protected BufferedReader is;
	protected PrintWriter os;
	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;

	protected Socket s;
	private String line = new String();
	private String lines = new String();
	private ArrayList<String> userInfoList = new ArrayList<String>();
	
	private Payload payload = new Payload((byte)0,(byte)0,"temp");

	
	public ServerThread(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream());
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

		} catch (IOException e) {
			System.err.println("Server Thread. Run. IO error in server thread");
		}

		try {

			while (true) {
				getUsers();
								
				payload = (Payload) ois.readObject();
				line = payload.toString();
				
				if (line.compareTo("0") == 0) {
					
					auth();
						
				
				} 
				else {
					
					String username;
					
					payload = new Payload((byte)0,(byte)3,"enter your username");
			    	oos.writeObject(payload);
			    	oos.flush();
			    	oos.reset();

					
					payload = (Payload) ois.readObject();
					line = payload.toString();
					
					noResponse(line);

					if(userInfoList.contains(line)) {
					
					username = line;

					
					payload = (Payload) ois.readObject();
					line = payload.toString();

					noResponse(line);

					while (line.compareTo("quit") != 0) {
						lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
						if (line.substring(0,6).compareTo(Integer.toString((username+"29").hashCode()).substring(0, 6)) == 0) {
							if(payload.type == (byte)0) {
								String correctData = changeJsonData(getDataFromApi(0,line.substring(7, line.length())));
								payload = new Payload((byte)1,(byte)0,correctData);
						    	oos.writeObject(payload);
								
							}
							else if(payload.type == (byte)1) {
								String correctData = changeJsonData(getDataFromApi(1,line.substring(7, line.length())));
								payload = new Payload((byte)1,(byte)1,correctData);
						    	oos.writeObject(payload);

								}
							else{
								payload = new Payload((byte)1,(byte)3,"Append 0 or 1 to head of your string");
						    	oos.writeObject(payload);


							}
					    	oos.flush();
					    	oos.reset();
						} else {
							payload = new Payload((byte)1,(byte)3,"No Response - no valid token - type quit for trying again");
					    	oos.writeObject(payload);
					    	oos.flush();
					    	oos.reset();
							
						}
						System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);
						
						payload = (Payload) ois.readObject();
						line = payload.toString();
						
						noResponse(line);
					}
					
				}
					else {
						
						payload = (Payload) ois.readObject();
						line = payload.toString();
						
						noResponse(line);

						while (line.compareTo("quit") != 0) {
							lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
							payload = new Payload((byte)0,(byte)3,"No Response - no valid username - type quit for trying again ");
					    	oos.writeObject(payload);
					    	oos.flush();
					    	oos.reset();

							
							
							System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);
							
							payload = (Payload) ois.readObject();
							line = payload.toString();
						}	
						
						
					}

				}
			}
		} catch (IOException e) {
			line = this.getName(); 
			System.err.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
		} catch (NullPointerException e) {
			line = this.getName(); 
			System.err.println("Server Thread. Run.Client " + line + " Closed");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Closing the connection");
				if (is != null) {
					is.close();
					System.err.println(" Socket Input Stream Closed");
				}

				if (os != null) {
					os.close();
					System.err.println("Socket Out Closed");
				}
				if (s != null) {
					s.close();
					System.err.println("Socket Closed");
				}

			} catch (IOException ie) {
				System.err.println("Socket Close Error");
			}
		} 
	}

	public Boolean auth() throws IOException, ClassNotFoundException {

		String user;
		File users = new File(
				"/Users/alitaylanakyurek/eclipse-workspace/PA1-2 yeni bastan/src/coreNetServer/userinfo.txt");

		ArrayList<String> userList = new ArrayList<String>();
		BufferedReader userInfo = new BufferedReader(new FileReader(users));

		user = userInfo.readLine();
		int i=0;
		while (user != null) {
			
		
				userList.add(user);

						
			
			user = userInfo.readLine();
		}

		 i = 0;
		String username = null;

		while (i == 0) {

			
			payload = (Payload) ois.readObject();
			
			line = payload.toString();
			
			noResponse(line);
			
			
			if (userInfoList.contains(line)) {
				lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
				username = line;
				
				payload = new Payload((byte)0,(byte)3,"Correct ID, please enter your password");
		    	oos.writeObject(payload);
		    	oos.flush();
		    	oos.reset();
				System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);
				i = 1;
			} else {
				
				lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
				
				payload = new Payload((byte)0,(byte)2,"User does not exist");
				oos.writeObject(payload);
		    	oos.flush();
		    	oos.reset();
		    	
				System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);

			}

		}

		String password = userList.get(userList.indexOf(line) + 1);
		System.out.println(password);

		i = 1;

		while (i != 4) {

			payload = (Payload) ois.readObject();
			line = payload.toString();
			
			noResponse(line);

			System.out.println(line);
			if (line.compareTo(password) == 0) {

				lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
				
				payload = new Payload((byte)0,(byte)3,"Correct password");
				oos.writeObject(payload);
		    	oos.flush();
		    	oos.reset();
		    	
		    	System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);


				username += "29";

				String token = Integer.toString(username.hashCode()).substring(0, 6);

				payload = new Payload((byte)0,(byte)3,token);
				oos.writeObject(payload);
		    	oos.flush();
		    	oos.reset();
				return true;
			} else {
				lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
				
				payload = new Payload((byte)0,(byte)1,"Wrong password " + (3 - i) + " tries left");
				oos.writeObject(payload);
		    	oos.flush();
		    	oos.reset();
				
		    	System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);
				i++;
			}
			
		}
		
		payload = (Payload) ois.readObject();
		
		line = payload.toString();
		
		noResponse(line);

		lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();

		payload = new Payload((byte)0,(byte)2,"No more tries left");
		oos.writeObject(payload);
    	oos.flush();
    	oos.reset();
    	
    	//s.close();
    	
		System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);

		return false;

	}
	public String getDataFromApi(int k,String query) throws IOException {
			
			return API.connectAndRequest(k, query);
		}
	
	public String handleApiData() {
		return line;
		
		
	}
	public void noResponse(String message) throws IOException {
		if(message.compareTo("no request status received") == 0) {
			is.close();
			os.close();
			s.close();
		}
	}
	
	public String changeJsonData(String data) {
		
		
		char[] temp = data.toCharArray();
		int j=0;
		String sum = "Fields [ ";
		for(int i = 0; i < data.length(); i++) {
			
			
			if(temp[i] == '"') {
				if(temp[i+1] == 't') {
					if(temp[i+2] == 'i') {
						if(temp[i+3] == 't') {
							if(temp[i+4] == 'l') {
								if(temp[i+5] == 'e') {
									if(temp[i+6] == '"') {
										sum+=" title = ";
										while(temp[i+9+j]!='"') {
											sum+= temp[i+9+j];
											j++;
										}
										i+=j+9;
										j=0;
									}
									
								}
							}
						}
					}	
				}
			}
			if(temp[i] == '"') {
				if(temp[i+1] == 't') {
					if(temp[i+2] == 'o') {
						if(temp[i+3] == 'p') {
							if(temp[i+4] == 'i') {
								if(temp[i+5] == 'c') {
									if(temp[i+6] == 's') {
										if(temp[i+7] == '"') {
											sum+=" topics = [";
											while(temp[i+10+j]!='"') {
												sum+= temp[i+10+j];
												j++;
											}
											sum+=']';
											i+=j+10;
											j=0;
										}
									}
									
								}
							}
						}
					}	
				}
			}

	
			if(temp[i] == '"') {
				if(temp[i+1] == 'y') {
					if(temp[i+2] == 'e') {
						if(temp[i+3] == 'a') {
							if(temp[i+4] == 'r') {
								if(temp[i+5] == '"') {
									sum+=" year = ";
									while(temp[i+6+j]!=',') {
										sum+= temp[i+6+j];
										j++;
									}
									i+=j+6;
									j=0;
									
								}
							}
						}
					}	
				}
			}
			
		}
		
		sum+=" ]";
		
		return sum;
		
	}
	
	public void getUsers() throws IOException {
		String user;
		File users = new File(
				"/Users/alitaylanakyurek/eclipse-workspace/PA1-2 yeni bastan/src/coreNetServer/userinfo.txt");

		BufferedReader userInfo = new BufferedReader(new FileReader(users));

		user = userInfo.readLine();
		int i=0;
		while (user != null) {
			
			i++;
			if(i%2 == 1) {
				
				if(!userInfoList.contains(user)) {
				userInfoList.add(user);
				}

			}
			
			user = userInfo.readLine();
		}
	}

}
