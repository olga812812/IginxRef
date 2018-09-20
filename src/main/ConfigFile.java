package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ConfigFile {
	private CommonMethods callCommonMethod  = new CommonMethods();
	private final Logger log = Logger.getLogger(IginxStartPoint.class);

	public String[] getResponseFromConfigFile(HttpServletRequest request) {
		  
	      String defaultFile=callCommonMethod.getProperty("DefaultRespFile");
	      String defaultRespCode = callCommonMethod.getProperty("DefaultRespCode");
	      String defaultLocation = callCommonMethod.getProperty("DefaultLocation");
	 
	      if(defaultFile == null || defaultRespCode == null || defaultLocation == null) throw new NullPointerException("You should add DefaultRespFile, DefaultRespCode and DefaultLocation to config file");
	      else { 	 
	    	 ArrayList<String> allUrlsFromConfigFile = getDataFromConfig("url", "key");
	    	 Iterator<String>  allUrlsIterator = allUrlsFromConfigFile.iterator();
	    	 String url;
	    	 String[] urlValue;
	    	 String[] defaultResponse = new String[]{defaultRespCode, defaultFile, "0"}; 
	         boolean isThereResponseInConfigFile=false;
	         String requestUriAndQueryString;
	         if (request.getQueryString()!=null) requestUriAndQueryString=request.getRequestURI()+request.getQueryString(); else requestUriAndQueryString=request.getRequestURI();
	         callCommonMethod.print(requestUriAndQueryString);
	         
	         do {
	            if(!allUrlsIterator.hasNext()) return defaultResponse;
	            url = (String)allUrlsIterator.next();
	            urlValue = callCommonMethod.getProperty(url).split(",");
	            	for(int i=0; i<urlValue.length; i++) {
	            	  isThereResponseInConfigFile = requestUriAndQueryString.contains(urlValue[i]);
	                  if (isThereResponseInConfigFile) break;
	            	}
	  
	         } while(!isThereResponseInConfigFile);

	         String responseNumberFromConfigFile = url.substring(3);
	         String responseCode = callCommonMethod.getProperty("code" + responseNumberFromConfigFile);        
	         if(responseCode == null) responseCode = "200";       
	         log.info("There is resp code for this URL in config file: " + responseCode + " respNumber is " + responseNumberFromConfigFile);
	         
	         
	         String[] responseFromConfigFile = new String[3];
	         switch(responseCode) {
	         case "200":
	            if(responseCode.equals("200")) {
	               String responseVastFileName = null;
	               String[] allVastFilesInResponse =  callCommonMethod.loadPropertyFromFile().getProperty("resp" + responseNumberFromConfigFile, defaultFile).split(",");               
	               responseVastFileName = allVastFilesInResponse[(int)(Math.random()*allVastFilesInResponse.length)];
	               if(responseVastFileName.equals("")) return defaultResponse;
	               responseFromConfigFile[0] = "200";
	               responseFromConfigFile[1] = responseVastFileName;
	               responseFromConfigFile[2] = "0";
	               return responseFromConfigFile;
	            }
	            break;
	         case "204":
	            if(responseCode.equals("204")) {
	               responseFromConfigFile[0] = "204";
	               responseFromConfigFile[1] = "0";
	               responseFromConfigFile[2] = "0";
	               return responseFromConfigFile;
	            }
	            break;
	         case "302":
	            if(responseCode.equals("302")) {
	               String respLocation = callCommonMethod.getProperty("location" + responseNumberFromConfigFile);
	               responseFromConfigFile[0] = "302";
	               responseFromConfigFile[1] = "0";
	               if(respLocation == null) {
	            	   responseFromConfigFile[2] = defaultLocation;
	               } else {
	            	   responseFromConfigFile[2] = respLocation;
	               }
	               return responseFromConfigFile;
	            }
	          default:
	        	  responseFromConfigFile[0] = responseCode;
	              responseFromConfigFile[1] = "0";
	              responseFromConfigFile[2] = "0";
	              
	          }         
	         return responseFromConfigFile;
	      }   
	   }
	
	protected ArrayList<String> getDataFromConfig(String key, String resultType)
	   {
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
