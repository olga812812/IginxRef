package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class VastServlet extends HttpServlet {
   protected Logger log = Logger.getLogger(IginxStartPoint.class);
   private CommonMethods callCommonMethod  = new CommonMethods();
   private ConfigFile configFile = new ConfigFile();
   private String requestId = String.valueOf(Math.round(Math.random() * 1.0E9D));
   
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
	 
	   logInfoAboutHttpRequestToLogFile(request);
	   addHeadersToResponse(request, response);
	   createResponse(request, response);
	   logInfoAboutResponseToLogFile(response);
       closeHttpSession(request);
   }
   
  
  protected void logInfoAboutHttpRequestToLogFile(HttpServletRequest request){
	  addRequestIdToLogFile();
      log.info("URL is:  " + request.getScheme() + ":/" + request.getRequestURI() + "?" + request.getQueryString()+" requestuestId is: "+requestId);
      
      Enumeration<String> requestHeaders = request.getHeaderNames();
      String header;
      while(requestHeaders.hasMoreElements()) {
    	 header = (String)requestHeaders.nextElement();
         log.debug("requestHeader: "+ header + ": " + request.getHeader(header));
      }
      
      log.debug("requestuest's client IP: " + request.getRemoteAddr()); 
  }
  
  
  protected void addRequestIdToLogFile() {
	   MDC.put("requestuestId", requestId);
  }
  
  protected void addHeadersToResponse(HttpServletRequest request, HttpServletResponse response){
	   Date dateOneMonthAhead = new Date(new Date().getTime()+2592000000L);
	   SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd-MMM-yyyy", Locale.ENGLISH);	
	   ArrayList<String> allCookies = configFile.getKeyOrValueFromConfig("Cookie", "value");
	   Iterator<String>  cookiesIterator = allCookies.iterator();
	   String cookie;
	   
      if(request.getHeader("Cookie")==null) {
   	    while(cookiesIterator.hasNext()) {
   	     cookie = (String)cookiesIterator.next();
   	     response.addHeader("Set-Cookie", cookie+requestId+"; expires="+dateFormat.format(dateOneMonthAhead)+" 10:00:00 GMT; path=/; domain=" + callCommonMethod.getProperty("cookie_domain"));
   	     }
        }
       
      if(request.getHeader("Origin") != null) {
   	     response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
   	     response.setHeader("Access-Control-Allow-Credentials", "true");
       }  
  }
  
  protected void createResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
	  PrintWriter responseOutStream = response.getWriter();    
	  String[] responseFromConfigFile = configFile.getResponseFromConfigFile(request);
      log.info("Response code: " + responseFromConfigFile[0]);
      String responseCode=responseFromConfigFile[0];
      
         switch(responseCode) {
         case "200":
            if(responseCode.equals("200")) {
               response.setStatus(200);
               response.setContentType("text/xml");
               String pathToVastFiles = "html//";
               String responseVastFile = responseFromConfigFile[1];
               log.info("Response file is:  " +responseVastFile); 
               
               FileInputStream fis = new FileInputStream(new File(pathToVastFiles + responseVastFile));
               Scanner in = new Scanner(fis);

               String responseVastFileLine;
               while(in.hasNextLine()) {
            	  responseVastFileLine = in.nextLine();
                  if(responseVastFileLine.contains("%session_id%")) {
                	  responseVastFileLine = responseVastFileLine.replace("%session_id%", String.valueOf(requestId));
                  }
                  responseOutStream.println(responseVastFileLine);
               }
               in.close();
               fis.close();
               break;
              }
            break;
         case "204":
            if(responseCode.equals("204")) {
            	response.setStatus(204);
               break;
            }
            break;
         case "302":
            if(responseCode.equals("302")) {
            	response.setStatus(302);
            	response.setHeader("Location", responseFromConfigFile[2]);
               break;
            }
         default:
        	 response.setStatus(200);
        	 response.setContentType("text/html");
        	 responseOutStream.println("<h1>OOOOPsOOOPsss...</h1>");
        }
  }    
  
   protected void logInfoAboutResponseToLogFile(HttpServletResponse response){
	   Collection<String> allResponseHeaders = response.getHeaderNames();
       String responseHeader;
       Iterator<String> iterator = allResponseHeaders.iterator();
       while (iterator.hasNext()) {
    	   responseHeader =iterator.next();
    	   log.info("Response headers: " +responseHeader+"="+ response.getHeaders(responseHeader));  
       }	   
   }
   
   protected void closeHttpSession(HttpServletRequest request) {
	      HttpSession session = request.getSession(false);
	      if(session != null) {
	         synchronized(session) {
	            session.invalidate();
	         }
	      }

	   }
      
}
