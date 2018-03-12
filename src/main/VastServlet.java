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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class VastServlet extends HttpServlet {

   private final Logger log = Logger.getLogger(IGINXMain.class);
   Common common  = new Common();
  
   private String temp;


   protected void close(HttpServletRequest req) {
      HttpSession session = req.getSession(false);
      if(session != null) {
         synchronized(session) {
            session.invalidate();
         }
      }

   }
   
  protected ArrayList<String> getDataFromConfig(String key, String resultType)
   {
	 
	  Set<String> allNames = common.propLoad().stringPropertyNames();
	   Iterator<String> iter = allNames.iterator();
	   ArrayList<String> responseArray = new ArrayList<String>();
           
       while(iter.hasNext()) 
       {
          temp = (String)iter.next();
          if (temp.length()>=key.length()&&temp.substring(0, key.length()).equals(key))
             {if(resultType.equals("key")) responseArray.add(temp);
              if (resultType.equals("value")) responseArray.add(common.getProperty(temp));
             }
        }
       return responseArray;
   }

   public String[] getResp(HttpServletRequest req) {
      
	  
	  String filename = null;
      String defaultFile=common.getProperty("DefaultRespFile");
      String defaultRespCode = common.getProperty("DefaultRespCode");
      String defaultLocation = common.getProperty("DefaultLocation");
      String[] resp = new String[3];
      String[] defaultResp = new String[]{defaultRespCode, defaultFile, "0"};
      common.print("Default data are: "+defaultFile +", "+ defaultRespCode+", "+defaultLocation);
      
      ArrayList<String> urls = getDataFromConfig("url", "key");
     
      
     
      if(defaultFile != null && defaultRespCode != null && defaultLocation != null) {
    	 Iterator<String>  iter = urls.iterator();
         boolean cond=false;
         String[] urlValue;
         
         do {
            if(!iter.hasNext()) {
               return defaultResp;
            }

            temp = (String)iter.next();
            urlValue = common.getProperty(temp).split(",");
            if(req.getQueryString() != null) {
            	for(int i=0; i<urlValue.length; i++)
            	{
               cond = req.getRequestURI().contains(urlValue[i]) || req.getQueryString().contains(urlValue[i]);
               if (cond) break;
            	}
            } else {
            	for(int i=0; i<urlValue.length; i++)
            	{
               cond = req.getRequestURI().contains(urlValue[i]);
               if (cond) break;
            	}
                   }
         } while(!cond);

         String respNumber = temp.substring(3);
         String respCode = common.getProperty("code" + respNumber);
         
         
         if(respCode == null) {
            respCode = "200";
         }
         this.log.info("There is resp code for this URL in config file: " + respCode + " respNumber is " + respNumber);
         
         switch(respCode) {
         case "200":
            if(respCode.equals("200")) {
               String[] allResps =  common.propLoad().getProperty("resp" + respNumber, defaultFile).split(",");
               filename = allResps[(int)(Math.random()*allResps.length)];
               if(filename.equals("")) {
                   return defaultResp;
                }
               filename = allResps[(int)(Math.random()*allResps.length)];
               
               resp[0] = "200";
               resp[1] = filename;
               resp[2] = "0";
               return resp;
            }
            break;
         case "204":
            if(respCode.equals("204")) {
               resp[0] = "204";
               resp[1] = "0";
               resp[2] = "0";
               return resp;
            }
            break;
         case "302":
            if(respCode.equals("302")) {
               String respLocation = common.getProperty("location" + respNumber);
               resp[0] = "302";
               resp[1] = "0";
               if(respLocation == null) {
                  resp[2] = defaultLocation;
               } else {
                  resp[2] = respLocation;
               }

               return resp;
            }
         }
         
         resp[0] = respCode;
         resp[1] = "0";
         resp[2] = "0";
         return resp;
      } else { 
    	 	  throw new NullPointerException("You should add DefaultRespFile, DefaultRespCode and DefaultLocation to config file");
             }
   }

      public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	   Date date = new Date(new Date().getTime()+2592000000L);
	   SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd-MMM-yyyy", Locale.ENGLISH);
	   ArrayList<String> cookies = getDataFromConfig("Cookie", "value");
	   Iterator<String>  iter = cookies.iterator();
	   String requestId = String.valueOf(Math.round(Math.random() * 1.0E9D));
	   
	  
	   
       MDC.put("requestId", requestId);
      this.log.info("URL is:  " + req.getScheme() + ":/" + req.getRequestURI() + "?" + req.getQueryString()+" RequestId is: "+requestId);
      
      //log request headers
      Enumeration<String> headers = req.getHeaderNames();

      while(headers.hasMoreElements()) {
         String out = (String)headers.nextElement();
         this.log.debug("RequestHeader: "+ out + ": " + req.getHeader(out));
      }
      this.log.debug("Request's client IP: " + req.getRemoteAddr());

      PrintWriter out1 = resp.getWriter();
      String[] respArray = this.getResp(req);
      this.log.info("Response code: " + respArray[0]);
      if(req.getHeader("Cookie")==null)
      {
    	  while(iter.hasNext())
    	  {
    	  temp = (String)iter.next();
    	  common.print(temp);
    	  resp.addHeader("Set-Cookie", temp+requestId+"; expires="+dateFormat.format(date)+" 10:00:00 GMT; path=/; domain=" + common.getProperty("cookie_domain"));
    	  }
      }
      if(req.getHeader("Origin") != null) {
         resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
         resp.setHeader("Access-Control-Allow-Credentials", "true");
      }

    
         String responce=respArray[0];
         switch(responce) 
         {
         case "200":
            if(responce.equals("200")) {
               resp.setStatus(200);
               resp.setContentType("text/xml");
               String file = respArray[1];
               this.log.info("Response file is:  " +file); 
               FileInputStream fis = new FileInputStream(new File("html//" + file));
               Scanner in = new Scanner(fis);

               String line;
               while(in.hasNextLine()) {
                  line = in.nextLine();
                  if(line.contains("%session_id%")) {
                     line = line.replace("%session_id%", String.valueOf(requestId));
                  }
                  out1.println(line);
               }
               

               in.close();
               fis.close();
               break;
            }
            break;
         case "204":
            if(responce.equals("204")) {
               resp.setStatus(204);
               break;
            }
            break;
         case "302":
            if(responce.equals("302")) {
               resp.setStatus(302);
               resp.setHeader("Location", respArray[2]);
               break;
            }
         default:
        	 resp.setStatus(200);
             resp.setContentType("text/html");
             out1.println("<h1>OOOOPsOOOPsss...</h1>");
        	 
         }

       //log response headers
       Collection<String> respHeaders = resp.getHeaderNames();
       Iterator<String> iterator = respHeaders.iterator();
       while (iterator.hasNext())
       {
    	   temp =iterator.next();
    	   this.log.info("Response headers: " +temp+"="+ resp.getHeaders(temp));  
       }

      this.close(req);
   }
}
