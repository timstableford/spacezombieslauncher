package cx.it.hyperbadger.spacezombies.launcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class Auth {
	private String username, password, key = null;
	private Status status;
	public Auth(String username, String password, Status status){
		this.username = username;
		this.password = password;
		this.status = status;
	}
	public static void main(String[] args){
		Auth a = new Auth("tim","alkesh56;", null);
		System.out.println(a.login());
	}
	public boolean login(){
		try {
		    String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    MessageDigest md5 = MessageDigest.getInstance("MD5");
		    md5.update(password.getBytes(),0,password.length());
		    password = new BigInteger(1,md5.digest()).toString(16);
		    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    URL url = new URL(Main.loginURL);
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	if(line.contains("-")){
		    		String[] split = line.split("-");
		    		if(split.length>1){
		    			if(split[0].equals("auth_success")){
		    				this.key = split[1];
		    				System.out.println("Login success - Key: "+this.key);
		    				if(status!=null){ status.setStatus("Login success"); }
		    				return true;
		    			}else if(split[0].equals("auth_fail")){
		    				if(status!=null){ status.setStatus(split[1]); }
		    				System.err.println("Auth fail - Error code: "+split[1]);
		    			}
		    		}else{
		    			if(status!=null){ status.setStatus("Unknown server response"); }
		    			System.err.println("Unknown server response");
		    		}
		    	}else{
		    		if(status!=null){ status.setStatus("Unknown server response"); }
		    		System.err.println("Unknown server response");
		    	}
		    }
		    wr.close();
		    rd.close();
		    
		} catch (Exception e) {
			if(status!=null){ status.setStatus("Could not connect to login server"); }
			System.err.println("Could not connect to login server");
			return false;
		}
		if(status!=null){ status.setStatus("Login failed"); }
		return false;
	}
	public String getKey(){
		return key;
	}
}
