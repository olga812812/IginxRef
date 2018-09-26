package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ConfigFile {
	protected CommonMethods callCommonMethod  = new CommonMethods();
	private final Logger log = Logger.getLogger(IginxStartPoint.class);
	protected String defaultRespCode,defaultFile,defaultLocation;   	
	
	public String[] getResponseFromConfigFile(HttpServletRequest request) {
		  String responseVastFileName;
		  String responseCode;
		  String responseLocation;
		  String responseNumberFromConfigFile;
		  String responseUrlFromConfigFile;
		  String[] defaultResponse = new String[]{defaultRespCode, defaultFile, "0"};  
		
	      if(!isThereDefaultDataInConfigFile()) throw new NullPointerException("You should add DefaultRespFile, DefaultRespCode and DefaultLocation to config file");	 	      
	      responseUrlFromConfigFile=getResponseUrlFromConfigFile(request); 	 	      
	      if(responseUrlFromConfigFile==null) return defaultResponse;
	    		
	      responseNumberFromConfigFile = responseUrlFromConfigFile.substring(3);
	      responseCode = callCommonMethod.getProperty("code" + responseNumberFromConfigFile);        
	      if(responseCode == null) responseCode = "200";       
	      log.info("There is resp code for this URL in config file: " + responseCode + " respNumber is " + responseNumberFromConfigFile);
	         	         	 
	      switch(responseCode) {
	         case "200":
	            if(responseCode.equals("200")) {	               
	               String[] allVastFilesInResponse =  callCommonMethod.loadPropertyFromFile().getProperty("resp" + responseNumberFromConfigFile, defaultFile).split(",");               
	               responseVastFileName = allVastFilesInResponse[(int)(Math.random()*allVastFilesInResponse.length)];
	               if(responseVastFileName.equals("")) return defaultResponse;	            
	               responseLocation = "0";
	               return new String[]{responseCode, responseVastFileName, responseLocation};
	            }
	            break;
	         case "204":
	            if(responseCode.equals("204")) {
	               responseVastFileName = "0";
	               responseLocation = "0";
	               return new String[]{responseCode, responseVastFileName, responseLocation};
	            }
	            break;
	         case "302":
	            if(responseCode.equals("302")) {
	               responseLocation = callCommonMethod.getProperty("location" + responseNumberFromConfigFile);	             
	               responseVastFileName = "0";
	               if (responseLocation == null)  responseLocation = defaultLocation;	                        
	               return new String[]{responseCode, responseVastFileName, responseLocation};
	            }	                  	 
	        	  	        	  
	        }   
	        responseVastFileName = "0";
    	    responseLocation = "0";	         
	        return new String[]{responseCode, responseVastFileName, responseLocation};
	        
	   }
	
	protected boolean isThereDefaultDataInConfigFile() {
		initDefaultValues();	    		
	    if(defaultFile == null || defaultRespCode == null || defaultLocation == null) return false; 
	    else return true;
	}
	
	protected void initDefaultValues() {
		 defaultFile=callCommonMethod.getProperty("DefaultRespFile");
	     defaultRespCode = callCommonMethod.getProperty("DefaultRespCode");
	     defaultLocation = callCommonMethod.getProperty("DefaultLocation");	 		
	}
	
	protected String getResponseUrlFromConfigFile(HttpServletRequest request) {
		ArrayList<String> allUrlsFromConfigFile = getKeyOrValueFromConfig("url", "key");
	    Iterator<String>  allUrlsIterator = allUrlsFromConfigFile.iterator();
	    String url;
	    String[] urlValue;
	    boolean isThereResponseInConfigFile=false;
	    String requestUriAndQueryString;
	    
	    if (request.getQueryString()!=null) requestUriAndQueryString=request.getRequestURI()+request.getQueryString(); else requestUriAndQueryString=request.getRequestURI();	  
	         do {
	            if(!allUrlsIterator.hasNext()) return null;
	            url = (String)allUrlsIterator.next();
	            urlValue = callCommonMethod.getProperty(url).split(",");
	            	for(int i=0; i<urlValue.length; i++) {
	            	  isThereResponseInConfigFile = requestUriAndQueryString.contains(urlValue[i]);
	                  if (isThereResponseInConfigFile)  break;	                	
	            	}
	  
	         } while(!isThereResponseInConfigFile);
	    return url;
	}
	
	protected ArrayList<String> getKeyOrValueFromConfig(String key, String resultType) {
		   Set<String> allProperties = callCommonMethod.loadPropertyFromFile().stringPropertyNames();
		   Iterator<String> propertiesIterator = allProperties.iterator();
		   ArrayList<String> dataFromConfigFile = new ArrayList<String>();
		   String property;
	           
	       while(propertiesIterator.hasNext()) {
	    	   property = (String)propertiesIterator.next();
	           if (property.length()>=key.length()&&property.substring(0, key.length()).equals(key)) {
	        	  if(resultType.equals("key")) dataFromConfigFile.add(property);
	              if (resultType.equals("value")) dataFromConfigFile.add(callCommonMethod.getProperty(property));
	          }
	       }
	       return dataFromConfigFile;
	   }

	
}
