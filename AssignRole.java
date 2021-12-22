/**
 * 
 * Junyong Lee
 * 
 * Created : JSON data parsing is done with gson library, returning current usernames in people tab in Jenkins
 * 
 */


import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class AssignRole {
	
	private static HttpURLConnection connection;
	
	public static String getJSON() {
		
		String returnValue = "" ;
		
		try {
			
			BufferedReader reader;
			String line;
			StringBuffer responseContent = new StringBuffer();
	        //Auth
	        String user = "junyonglee"; // username
	        String pass = "Sodapop1324!"; // password or API token
	        String authStr = user + ":" + pass;
	        String encoding = Base64.getEncoder().encodeToString(authStr.getBytes("utf-8"));
			
	        
	        

	        
			URL url = new URL("http://localhost:8080/asynchPeople/api/json?pretty=true");
			connection = (HttpURLConnection) url.openConnection();
			
			// Property Setup
			connection.setRequestMethod("GET");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");			
	        connection.setRequestProperty("Authorization", "Basic " + encoding);
	        connection.setRequestProperty("Accept", "application/atom+xml");			
			connection.connect();
			
			
			// Get status code
			int status = connection.getResponseCode();
			//System.out.println(status);
			
			
			
			if(status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while( (line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while( (line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			//System.out.println(responseContent.toString());
			returnValue = responseContent.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch( IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		
		return returnValue;
		
	}
	
	public static ArrayList JSONParser(String input) {
		
		ArrayList<String> ar = new ArrayList<String>();
		//JSONArray peoples = new JSONArray(input);
		JSONObject info = new JSONObject(input);
		JSONArray users = info.getJSONArray("users");
		
		
		
		for(int i = 0; i < users.length(); i++) {
			JSONObject unit = users.getJSONObject(i);
			JSONObject user = unit.getJSONObject("user");
			
			String absoluteUrl = user.getString("absoluteUrl");
			String[] splitArray = absoluteUrl.split("http://localhost:8080/user/"); 
			
			System.out.println(splitArray[1]);
			ar.add(splitArray[1]);
		}
		
		return ar;
		
	}
	
	
	public static void main(String[] args) {
		
		String JSONData;
		JSONData = getJSON();
		//System.out.println(JSONData);
		JSONParser(JSONData);
		
	}
	

}


