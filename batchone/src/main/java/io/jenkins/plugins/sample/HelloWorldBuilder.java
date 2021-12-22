package io.jenkins.plugins.sample;

import hudson.Launcher;

import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import java.io.*;

import java.net.*;
import java.util.*;


public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private final String name,pass,dest;
    private boolean useFrench;

    @DataBoundConstructor
    public HelloWorldBuilder(String name) {
        this.dest = "";
		this.pass = "";
		this.name = name;
        //this.pass = pass;
        //this.dest = dest;
    }

    public String getName() {
        return name;
    }

    public boolean isUseFrench() {
        return useFrench;
    }

    @DataBoundSetter
    public void setUseFrench(boolean useFrench) {
        this.useFrench = useFrench;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        run.addAction(new HelloWorldAction(name));
    	if (useFrench) {
            listener.getLogger().println("Bonjour, " + name + "!");
        } else {
            listener.getLogger().println("Hello, " + name + "!");
        }
    	
    	String[] authUser = new String[2];
    	authUser = (String[]) AuthfromUser();
    	APIInit(authUser);
    }

	public static void APIInit(String[] authUser) throws IOException {
		
		String requestUrl="http://localhost:8080/securityRealm/addUser";	
		processApiRequest(requestUrl, authUser);
		
	}

	public static void processApiRequest(String requestUrl, String[] authUser) throws IOException {
		
		
	File users = new File("C:\\students.csv");
	
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
	
	
	//sendPostRequest(requestUrl, payload, authUser);

   // rest payload and counter after the 1st 4 	
	payload = "";
	count = 0;
	
	}
	
	in.close();
}

	public static ArrayList<String> getHeaders() throws IOException {
		
        File users = new File("C:\\\\students.csv");
		
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

	private Object AuthfromUser() {
		// TODO Auto-generated method stub
    	String[] result = new String[2];
    	
    	result[0] = name;
		result[1] = pass;
		
		return result;
	}

	@Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
            if (value.length() < 4)
                return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_tooShort());
            if (!useFrench && value.matches(".*[éáàç].*")) {
                return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_reallyFrench());
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }

    }

}
