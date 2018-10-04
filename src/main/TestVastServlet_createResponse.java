package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigFile.class})

public class TestVastServlet_createResponse {
	HttpServletRequest request  = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	VastServlet vastServletObject = new VastServlet();	
	File fileMocked = mock(File.class);
	Scanner scannerMocked = new Scanner("someString");
	Logger logger = mock(Logger.class);
	
	
    @Test
 	public void checkResponse200() throws Exception{
    
    	FileInputStream fileInputStreamMocked = mock(FileInputStream.class);
    	when(response.getWriter()).thenReturn(new PrintWriter(System.out)); 
    	vastServletObject.log=logger;
    	when(fileMocked.exists()).thenReturn(true);    	
    	PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"200","vastFileName","0"});    	
    	PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStreamMocked);
    	PowerMockito.whenNew(Scanner.class).withAnyArguments().thenReturn(scannerMocked);
    	vastServletObject.createResponse(request, response);
    	verify(logger, times(2)).info(anyString());
    	verify(response).setStatus(200);
    	verify(response).setContentType("text/xml");
    	 
	
    }

}
