package coreNetServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class API {

	
	private static String DEFAULT_URL = "core.ac.uk";
	//port is 443 because its default port in https
	private static int DEFAULT_PORT = 443;
	static String str;
	public static String connectAndRequest(int k,String query) throws IOException {
		
		 SSLSocketFactory socket =
	                (SSLSocketFactory)SSLSocketFactory.getDefault();
	        SSLSocket s =
	                (SSLSocket)socket.createSocket(DEFAULT_URL, DEFAULT_PORT);

	        s.startHandshake();

	        PrintWriter os = new PrintWriter(s.getOutputStream());
		
	        BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
	
	        if(k==0) {
			os.println("GET https://core.ac.uk:443/api-v2/articles/search/"+ query +"?page=1&pageSize=10&metadata=true&fulltext=false&citations=false&similar=false&duplicate=false&urls=false&faithfulMetadata=false&apiKey=O6EXPFpetIkgMou1WydzVxisTKj5QNfJ\n"
					+ "");
	        }
	        if(k==1) {
				os.println("GET https://core.ac.uk:443/api-v2/journals/search/"+ query +"?page=1&pageSize=10&metadata=true&fulltext=false&citations=false&similar=false&duplicate=false&urls=false&faithfulMetadata=false&apiKey=O6EXPFpetIkgMou1WydzVxisTKj5QNfJ\n"
						+ "");

	        }
	        os.flush();
	        
	        
	        str= is.readLine();
	        	
	        os.close();
	        is.close();
	        s.close();
	        return str;       

	        }
		
	
	}
		
		
		
		

	

