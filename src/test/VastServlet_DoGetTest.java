package test;


import static org.mockito.Mockito.*;


import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;



import main.VastServlet;
import main.ConfigFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigFile.class)


public class VastServlet_DoGetTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	VastServlet myServlet = new VastServlet();
	Enumeration<String> headers = Collections.emptyEnumeration();
	PrintWriter pWriter = new PrintWriter(System.out);

	
	
	@Test
	
	public void checkResp200()
	{
		
		when(request.getHeaderNames()).thenReturn(headers);
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"200","un.xml","0"});
		
		try
		{
			when(resp.getWriter()).thenReturn(pWriter);
			myServlet.doGet(request, resp);}
		catch (Exception ex) 
		{
			ex.printStackTrace();}
		verify(resp).setStatus(200);
		verify(resp).setContentType("text/xml");
			
	  
	}
	
	@Test
	public void checkCORSHeaders()
	{
		String origin = "privet.ru";
		when(request.getHeader("Origin")).thenReturn(origin);
		when(request.getHeaderNames()).thenReturn(headers);
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"200","un.xml","0"});
		
		try
		{
			when(resp.getWriter()).thenReturn(pWriter);
			myServlet.doGet(request, resp);}
		catch (Exception ex) 
		{ex.printStackTrace();}
		
		verify(resp).setHeader("Access-Control-Allow-Origin", origin);
		verify(resp).setHeader("Access-Control-Allow-Credentials", "true");
		
	}
	
	@Test
	public void checkResp204()
	{
		when(request.getHeaderNames()).thenReturn(headers);
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"204","0","0"});
		try
		{
			when(resp.getWriter()).thenReturn(pWriter);
			myServlet.doGet(request, resp);}
		catch (Exception ex) 
		{ex.printStackTrace();}
		
		verify(resp).setStatus(204);
	}
	
	@Test
	public void checkResp302()
	{
		String location = "http://location.ru";
		when(request.getHeaderNames()).thenReturn(headers);
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"302","0",location});
		try
		{
			when(resp.getWriter()).thenReturn(pWriter);
			myServlet.doGet(request, resp);}
		catch (Exception ex) 
		{ex.printStackTrace();}
		
		verify(resp).setStatus(302);
		verify(resp).setHeader("Location", location);
	}
	
	@Test
	public void checkAnotherRespCode()
	{
		
		when(request.getHeaderNames()).thenReturn(headers);
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getResponseFromConfigFile")).toReturn(new String[] {"404","0", "0"});
		try
		{
			when(resp.getWriter()).thenReturn(pWriter);
			myServlet.doGet(request, resp);}
		catch (Exception ex) 
		{ex.printStackTrace();}
		
		verify(resp).setStatus(200);
		verify(resp).setContentType("text/html");
	}

}
