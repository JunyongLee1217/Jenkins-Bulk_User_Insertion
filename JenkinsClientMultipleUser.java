/**
 *Mahlet Zemedie
 *Created: to take input from CSV file and add the users in Jenkins 
 *
 *
 * Junyong Lee
 * 
 * Created : Automize authorization parameters
 * Created : Basic user interface to give task options
 * 
 */
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class JenkinsClientMultipleUser {
	
	public static String[] authUser = new String[2];
	public static void main(String args[]) throws IOException {
        
		
		//String[] authUser = new String[2];
        authUser = AuthfromUser();
		int answer = prompt();
		
		
        if(answer == 1) {
			APIInit(authUser);
		}
        
        else if(answer == 2) {
        	
        	// The process for role assignment goes here-------------
    		String JSONData, POSTString;
    		
    		getPOSTjson Jsondata = new getPOSTjson();
    		
    		JSONData = Jsondata.getJSON();
    		//System.out.println(JSONData);
    		ArrayList<String> uname;
    		uname = Jsondata.JSONParser(JSONData);
    		POSTString = Jsondata.getPOSTString(uname);
    		//System.out.println(uname[0]);
    		System.out.println("POST String is :\n");
    		System.out.println(POSTString);
//        	System.out.println("Still making role assingment part...\nSystem shut down");
//        	System.exit(0);
        }
        
        else {
        	System.out.println("Please enter 1 or 2...");
        	System.exit(0);
        }
		
	}
	
	public static void mainPrompt() {
		
	}

	public static int prompt() {
		Scanner scn = new Scanner(System.in);
		System.out.println("Which task do you want to do?");
		System.out.println("1. Add multiple users");
		System.out.println("2. Assign roles");
		int answer = scn.nextInt();
		return answer;
	}
	
	
	public static void APIInit(String[] authUser) throws IOException {
		
		String requestUrl="http://localhost:8080/securityRealm/createAccountByAdmin";	
		processApiRequest(requestUrl, authUser);
		
	}
	
	
	/**
	 *Takes url and process a REST api request to create multiple users 
	 *from csv file
	 * @param requestUrl
	 * @throws IOException
	 *
	 */
	
	public static void processApiRequest(String requestUrl, String[] authUser) throws IOException {
		
		
	File users = new File("students.csv");
	
    BufferedReader in = new BufferedReader(new FileReader(users));
	
	in.readLine();
	
	
	int count = 0 ;
	String payload = "";
	String S;
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
	
	
	sendPostRequest(requestUrl, payload, authUser);

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
	
        File users = new File("students.csv");
		
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
	
	
	public static String[] AuthfromUser() {
		String[] result = new String[2];
		Scanner scn1 = new Scanner(System.in);		
		Scanner scn2 = new Scanner(System.in);
		System.out.println("Authorization");
		System.out.print("Jenkins ID: ");
		String ID = scn1.nextLine();
		System.out.print("\nJenkins Password: ");
		String PASS = scn2.nextLine();
		
		result[0] = ID;
		result[1] = PASS;
		
		return result;
		
	}
/**
 * 
 * @param requestUrl
 * @param payload
 * @return 
 */
	public static String sendPostRequest(String requestUrl, String payload, String[] authUser) {
	    try {

	        URL url = new URL(requestUrl);
	        


	        
//	        String authStr = user + ":" + pass;
	        String authStr = authUser[0] + ":" + authUser[1];
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
			
			if(responseCode == 403) {
				System.out.println("Please disable the security setting in Jenkins and try again.");
				System.out.println("Manage Jenkins - Script console - hudson.security.csrf.GlobalCrumbIssuerConfiguration.DISABLE_CSRF_PROTECTION = true");
			}

	
	    
			
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

