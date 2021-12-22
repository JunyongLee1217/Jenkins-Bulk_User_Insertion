/**
 * 
 * Xuye Luo
 * 
 * Created : get POSTString from user list, set one admin user is true
 * 
 */

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

public class getPOSTjson {
	
	private static HttpURLConnection connection;
	
	public String getJSON() {
		
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
	
	public  ArrayList<String> JSONParser(String input) {
		
		ArrayList<String> ar = new ArrayList<String>();
		//JSONArray peoples = new JSONArray(input);
		JSONObject info = new JSONObject(input);
		JSONArray users = info.getJSONArray("users");
		
		for(int i = 0; i < users.length(); i++) {
			JSONObject unit = users.getJSONObject(i);
			JSONObject user = unit.getJSONObject("user");
			
			String absoluteUrl = user.getString("absoluteUrl");
			String[] splitArray = absoluteUrl.split("http://localhost:8080/user/"); 
			
			//System.out.println(splitArray[1]);
			ar.add(splitArray[1]);
		}
		
		return ar;
		
	}
	
	public  String getPOSTString(ArrayList<String> name) {
		Scanner sc = new Scanner(System.in); 
        //System.out.println("input admin name:"); 
        //String adminuname = sc.nextLine(); 
        String adminuname = JenkinsClientMultipleUser.authUser[0];
        //System.out.println(adminuname); 
        //json: {"globalRoles": {"data": {"luo": {"admin": true, "student": true}, "luo3": {"admin": true, "student": true},  "luo6": { "student": true,"teacher":true}, "xuye": {"admin": false, "student": true}, "anonymous": {"admin": false, "student": false},"luo10": {"admin": false, "student": true}, "username":{"student":true, "teacher": true,"admin":true },   } }, "projectRoles": {"": ""}, "slaveRoles": {"": ""}, "core:apply": ""}

        String SPstring,EPstring,Padmin,Pstring;
        Padmin = "\""+adminuname+"\": {\"admin\": true, \"student\": true},";
        SPstring = "json: {\"globalRoles\": {\"data\": {" + Padmin;
        EPstring = " } }, \"projectRoles\": {\"\": \"\"}, \"slaveRoles\": {\"\": \"\"}, \"core:apply\": \"\"}";
        for(String Pstudent : name ){
        	   if( Pstudent.equals(adminuname) ==false ){
        		   SPstring = SPstring + "\""+Pstudent+"\": {\"admin\": false, \"student\": true},";
        	      }else {
			   
		   }
         }
        Pstring = SPstring + EPstring;
		return Pstring;
	}

//	public static void main(String[] args) {
//		
//		String JSONData, POSTString;
//		JSONData = getJSON();
//		//System.out.println(JSONData);
//		ArrayList<String> uname;
//		uname = JSONParser(JSONData);
//		POSTString = getPOSTString(uname);
//		//System.out.println(uname[0]);
//		System.out.println("POST String is :\n");
//		System.out.println(POSTString);
//		
//	}
}


