package main;


import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigFile.class, VastServlet.class, PrintWriter.class})

public class TestVastServlet_CreateResponse {
	HttpServletRequest request  = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	VastServlet vastServletObject = new VastServlet();	
	Scanner scannerMocked;
	Logger logger = mock(Logger.class);
	PrintWriter printWriterObject = Mockito.spy(new PrintWriter(System.out));
		
	
	@Before
	public void setUp() throws Exception{
		vastServletObject.log=logger;   
		when(response.getWriter()).thenReturn(printWriterObject); 		    	    	        		
	}
	
	
	
    @Test
 	public void checkResponse200WithoutSessionIdInVastFile() throws Exception { 
    	FileInputStream fileInputStreamMocked = mock(FileInputStream.class);
    	String vastFileBody= "scannerString";
    	scannerMocked = new Scanner(vastFileBody);  
    	PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStreamMocked);
    	PowerMockito.whenNew(Scanner.class).withAnyArguments().thenReturn(scannerMocked);
    	PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"200","vastFileName","0"});    	    	       	
    	vastServletObject.createResponse(request, response);
    	verify(logger, times(2)).info(anyString());
    	verify(response).setStatus(200);
    	verify(response).setContentType("text/xml");
    	verify(printWriterObject).println(vastFileBody);
    	 
	
    }
    
    @Test
    public void checkResponse200WithSessionIdInVastFile() throws Exception {    	
    	FileInputStream fileInputStreamMocked = mock(FileInputStream.class);
    	scannerMocked = new Scanner("scanner%session_id%String");
    	PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStreamMocked);
    	PowerMockito.whenNew(Scanner.class).withAnyArguments().thenReturn(scannerMocked);
    	PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"200","vastFileName","0"});
    	vastServletObject.createResponse(request, response);
    	verify(logger, times(2)).info(anyString());
    	verify(response).setStatus(200);
    	verify(response).setContentType("text/xml");
    	verify(printWriterObject).println("scanner"+vastServletObject.requestId+"String");
    }

    @Test
    public void checkResponse204() throws Exception {    	
    	PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"204","0","0"});
    	vastServletObject.createResponse(request, response);
    	verify(logger, times(1)).info(anyString());
    	verify(response).setStatus(204);
    	
    }
    
    @Test
    public void checkResponse302() throws Exception {    	
    	String responseLocationFromMethodGetResponseFromConfigFile = "responseLocation";
    	PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"302","0",responseLocationFromMethodGetResponseFromConfigFile});
    	vastServletObject.createResponse(request, response);
    	verify(logger, times(1)).info(anyString());
    	verify(response).setStatus(302);
    	verify(response).setHeader("Location", responseLocationFromMethodGetResponseFromConfigFile);
    	
    }
    
    @Test
    public void checkDefaultResponse() throws Exception {
    	PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"unknownResponseCode","0","0"});
    	vastServletObject.createResponse(request, response);
    	verify(response).setStatus(200);
    	verify(response).setContentType("text/html");
    	verify(printWriterObject).println(vastServletObject.defaultResponsePage);
    	
    }
    
}
