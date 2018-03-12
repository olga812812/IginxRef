package test;


import static org.mockito.Mockito.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;


import main.VastServlet;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VastServlet.class)


public class VastServletDoGetTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	VastServlet myServlet = new VastServlet();

	
	
	@Test
	
	public void checkResp200()
	{
		Enumeration<String> headers = Collections.emptyEnumeration();
		PrintWriter pWriter = new PrintWriter(System.out);
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid6=5");
		when(request.getRequestURI()).thenReturn("/");
		when(request.getScheme()).thenReturn("http");
		when(request.getHeaderNames()).thenReturn(headers);
		when(request.getHeader(anyString())).thenReturn(null);
		when(request.getRemoteAddr()).thenReturn("192.156.156.156");
		PowerMockito.stub(PowerMockito.method(VastServlet.class, "getResp")).toReturn(new String[] {"200","un.xml","0"});
		
		
		try
		{
			when(resp.getWriter()).thenReturn(pWriter);
			myServlet.doGet(request, resp);}
		catch (Exception ex) 
		{ex.printStackTrace();}
		verify(resp).setStatus(200);
		verify(resp).setContentType("text/xml");
		
		
		
	
	  
	}
	
	

}
