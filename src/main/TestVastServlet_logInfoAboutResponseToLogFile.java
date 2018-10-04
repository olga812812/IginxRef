package main;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)

public class TestVastServlet_logInfoAboutResponseToLogFile {
	VastServlet vastServletObject = new VastServlet();
	Logger logger = mock(Logger.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Set<String> allResponseHeaderValues = new HashSet<String>();
	
	
 @Before  
    public void setUp() {
	 vastServletObject.log=logger;	
	 allResponseHeaderValues.add("firstHeaderValue");	
    }
	
	@Test
	public void checkWhenNoResponseHeaders() {					
		Set<String> allResponseHeaders = new HashSet<String>();		
		when(response.getHeaderNames()).thenReturn(allResponseHeaders);
		vastServletObject.logInfoAboutResponseToLogFile(response);
		verify(logger, never()).info(anyString());		
	}

	@Test
	public void checkWhenOneResponseHeaders() {						
		Set<String> allResponseHeaders = new HashSet<String>();	
		allResponseHeaders.add("firstHeader");			
		when(response.getHeaderNames()).thenReturn(allResponseHeaders);
		when(response.getHeaders("firstHeader")).thenReturn(allResponseHeaderValues);
		vastServletObject.logInfoAboutResponseToLogFile(response);
		verify(logger, times(1)).info("Response headers: firstHeader=[firstHeaderValue]");	
	}
	
	@Test
	public void checkWhenThreeResponseHeaders() {						
		Set<String> allResponseHeaders = new HashSet<String>();	
		allResponseHeaders.add("firstHeader");
		allResponseHeaders.add("secondHeader");
		allResponseHeaders.add("thirdHeader");		
		when(response.getHeaderNames()).thenReturn(allResponseHeaders);
		when(response.getHeaders(anyString())).thenReturn(allResponseHeaderValues);
		vastServletObject.logInfoAboutResponseToLogFile(response);
		verify(logger, times(3)).info(anyString());	
	}
}
