package test;

import java.util.Properties;

import main.Common;
import main.VastServlet;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;




public class VastServletGetRespTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	VastServlet myServlet = new VastServlet();
	

	@Test
	public void checkStringInQS()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid6=5");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","afrin.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkStringInUriQSIsNull()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("ctc.ru");
		assertArrayEquals(new String[]{"200","ctc.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkStringInUriQSIsNotNull()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid=noInFile");
		when(request.getRequestURI()).thenReturn("puid6=5");
		assertArrayEquals(new String[]{"200","afrin.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkDefaultResp()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","un.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkCode204()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("event");
		assertArrayEquals(new String[]{"204","0","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkCode302LocationIsNotNull()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("finish");
		when(request.getRequestURI()).thenReturn("even");
		assertArrayEquals(new String[]{"302","0","http://google.ru"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkCode302LocationIsNull()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("finish2");
		when(request.getRequestURI()).thenReturn("even");
		assertArrayEquals(new String[]{"302","0","http://vi.ru"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkIfNoRespInConfig()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("noResp");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","un.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkIfEmptyRespInConfig()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("emptyResp");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","un.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkIfCodeInConfigNot200204302()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("anotherCode");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"159","0","0"}, myServlet.getResp(request));
	}
	
	@Test (expected=NullPointerException.class)
	public void checkIfNoDefaultFileRespLocation()
	{
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("anotherCode");
		when(request.getRequestURI()).thenReturn("/");
		
				
		myServlet.getResp(request);
	}
}
