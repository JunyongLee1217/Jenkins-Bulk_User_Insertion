import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class JenkinsClientSingleUser{
	
	public static void main(String args[]) {
		
		String payload= "username :test2\npassword1:test2123\npassword2:test2123\nfullname:test2\nemail:test2123@gmail.com";
		//String test = "username :test2\npassword1:test2123\npassword2:test2123\nfullname:test2\nemail:test2123@gmail.com";
		String requestUrl="http://localhost:8080/securityRealm/createAccountByAdmin";
		sendPostRequest(requestUrl, payload);
		//System.out.println(test);
	}
	
	public static String sendPostRequest(String requestUrl, String payload) {
	    try {
	        URL url = new URL(requestUrl);
	        
	        
	        
	        
	        String user = "mahlet"; // username
	        String pass = "@Op252525"; // password or API token
	        String authStr = user + ":" + pass;
	        String encoding = Base64.getEncoder().encodeToString(authStr.getBytes("utf-8"));
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        
	        
	      
	        connection.setRequestMethod("POST");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setInstanceFollowRedirects(false);
	        //connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/atom+xml");
	        connection.setRequestProperty("Jenkins-Crumb", "3078eeb5d33dd5a436b17000bf67e9fb5025a7d0f48c3e55813dd59c4d10887d");
	        //connection.setRequestProperty("User-Agent", "Mozilla/5.0");
	        connection.setRequestProperty("Authorization", "Basic " + encoding);
	        connection.setRequestProperty("Accept", "application/atom+xml");
	       
	        //connection.setRequestProperty("Content-Type", "application/atom+xml; charset=UTF-8");
	       //connection.setRequestProperty("Postman-Token", "563b8065-87b0-47a4-b9af-eaaeaaab4bb0");
	       
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	        writer.close();
	        
	       // URLConnection conn = url.openConnection();
	    
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuffer jsonString = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
	        return jsonString.toString();
	        
	    } catch (Exception e) {
	    	 e.printStackTrace();
	            throw new RuntimeException(e.getMessage());
	            
	            
	    }

	}

}
