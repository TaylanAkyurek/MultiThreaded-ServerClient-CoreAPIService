package CoreNetClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import handlePayload.Payload;

/**
 * Created by Yahya Hassanzadeh on 20/09/2017.
 */

public class ConnectionToServer
{
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket s;

    protected BufferedReader is;
    protected PrintWriter os;
	
    protected ObjectOutputStream oos;
	protected ObjectInputStream ois;

    protected String serverAddress;
    protected int serverPort;
	protected HashMap<String, String> users = new HashMap<String, String>();
 
    
    public ConnectionToServer(String address, int port)
    {
        serverAddress = address;
        serverPort    = port;
    }

    
    public void Connect()
    {
        try
        {
            s=new Socket(serverAddress, serverPort);
            
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {

        	System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    
    public String SendForAnswer(String message)
    {
        String response = new String();
        try
        {
            
        	
            os.println(message);
            os.flush();
            
            response = is.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
    }

  
    public void sendPayload(Payload payload) throws IOException {
    	
    	
    	oos.writeObject((Payload) payload);
    	oos.flush();
    	oos.reset();
	
}
    
 
    public void Disconnect()
    {
        try
        {
            is.close();
            os.close();
            s.close();
            System.out.println("ConnectionToServer. SendForAnswer. Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
