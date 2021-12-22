/**
 * Mahlet Zemedie 
 * 
 * 
 * Created :  processApiRequest(), Purpose :  process REST api to create multiple users
 * Created : getHeaders(), purpose : Get column names from csv file and concatenate the format to prepare payload
 * Initial code: sendPostRequest() 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import org.json.*;


public class Test {
	
	public static void main(String args[]) throws IOException {

		String requestUrlAddUser="http://localhost:8080/securityRealm/createAccountByAdmin";
		String requestUrlAddrole ="http://localhost:8080/role-strategy/assignSubmit";
		String s = "{\"globalRoles\": {\"data\": {\"admin\": {\"admin\": true, \"student\": true, \"TA\" : True} } }}";
        //JSONObject result = new JSONObject(s);
		processApiRequest(requestUrlAddUser);
		sendPostRequestAddrole(requestUrlAddrole,s );

		
		
	}
	
	
	/**
	 *Takes url and process a REST api request to create multiple users 
	 *from csv file
	 * @param requestUrl
	 * @throws IOException
	 *
	 */
	
	public static void processApiRequest(String requestUrl) throws IOException {
		
		
	File users = new File("students.csv");
	
    BufferedReader in = new BufferedReader(new FileReader(users));
	
	in.readLine();
	
	
	int count = 0 ;
	String payload = "";
	String S;
	//String name;
	while ((S = in.readLine()) != null) {
		
	String [] line = S.split(",") ;
	
	Iterator<String> iter = getHeaders().iterator();
	
	while (iter.hasNext()) {
	for (int i= 0 ; i < line.length; i++) {
		
		String header = iter.next();
		payload+= header+ line[i] ;
		count++;
		
		if (count>4)
		break;
	}
	
	}
	
   sendPostRequestAddUser(requestUrl, payload);
// rest payload and counter after the 1st 4 	
	payload = "";
	count = 0;
	
	}
	
	in.close();
}



	/**
	 * Get column names from csv file and concatenate the format to prepare payload
	 * @return list of string in a format ready for payload
	 * @throws IOException
	 */
	
	
	public static ArrayList<String> getHeaders() throws IOException {
	
        File users = new File("Students.csv");
		
        BufferedReader in = new BufferedReader(new FileReader(users));
		
		String line = in.readLine();
		
		String[] strArray = line.split(",");
		
		ArrayList<String> S = new ArrayList<String>();
		
		for (int i = 0; i < strArray.length; i++ ) {
			
			//System.out.println(strArray[i]);
			
			if (strArray[i].equals("username"))
				S.add(strArray[i] + "=");
			if (strArray[i].equals("password1"))
				S.add("&"+strArray[i] + "=");
			if (strArray[i].equals("password2"))
				S.add("&"+strArray[i] + "=");
			if (strArray[i].equals("fullname"))
				S.add("&"+strArray[i] + "=");
			if (strArray[i].equals("email"))
				S.add("&"+strArray[i] + "=");
		}
		
		return S;
	}
/**
 * 
 * @param requestUrl
 * @param payload
 * @return 
 */
	public static String sendPostRequestAddUser(String requestUrl, String payload) {
	    try {

	        URL url = new URL(requestUrl);
	        
	        
	        
	        //Auth
	        String user = "Admin"; // username
	        String pass = "admin"; // password or API token
	        String authStr = user + ":" + pass;
	        String encoding = Base64.getEncoder().encodeToString(authStr.getBytes("utf-8"));
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();	      
			
			
			// Property Setup
			connection.setRequestMethod("POST");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");			
	        connection.setRequestProperty("Authorization", "Basic " + encoding);
	        connection.setRequestProperty("Accept", "application/atom+xml");			
			connection.connect();




			// Write Post
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	
			writer.write(payload);
			
			writer.flush();

			// Check response code
			int responseCode = connection.getResponseCode();
			System.out.println("The response code is : " + responseCode);

	
	    
			
	        //Input Stream
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuffer jsonString = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
			br.close();
			writer.close();
	        connection.disconnect();
	        return jsonString.toString();
	        
	    } catch (Exception e) {
	    	 e.printStackTrace();
	            throw new RuntimeException(e.getMessage());   
	    }

	}
	
	public static String sendPostRequestAddrole(String requestUrl, String payload) {
	    try {

	        URL url = new URL(requestUrl);
	        
	        
	        
	        //Auth
	        String user = "Admin"; // username
	        String pass = "admin"; // password or API token
	        String authStr = user + ":" + pass;
	        String encoding = Base64.getEncoder().encodeToString(authStr.getBytes("utf-8"));
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();	      
			
			
			// Property Setup
			connection.setRequestMethod("POST");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");			
	        connection.setRequestProperty("Authorization", "Basic " + encoding);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");			
			connection.connect();




			// Write Post
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	
			writer.write(payload);
			
			writer.flush();

			// Check response code
			int responseCode = connection.getResponseCode();
			System.out.println("The response code is : " + responseCode);

	
	    
			
	        //Input Stream
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuffer jsonString = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
			br.close();
			writer.close();
	        connection.disconnect();
	        return jsonString.toString();
	        
	    } catch (Exception e) {
	    	 e.printStackTrace();
	            throw new RuntimeException(e.getMessage());   
	    }

	}


}

