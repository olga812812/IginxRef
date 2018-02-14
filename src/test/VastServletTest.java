package test;

import main.VastServlet;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;




public class VastServletTest {
	private HttpServletRequest request;
	VastServlet myServlet = new VastServlet();
	
	@Test
	public void checkgetRespStringInQS()
	{
		
		request = mock(HttpServletRequest.class);
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid6=5");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","afrin.xml","0"}, myServlet.getResp(request));
	}
	
	@Test
	public void checkgetRespStringInURI()
	{
		
		request = mock(HttpServletRequest.class);
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid56=secret");
		when(request.getRequestURI()).thenReturn("puid6=5");
		assertArrayEquals(new String[]{"200","afrin.xml","0"}, myServlet.getResp(request));
	}

}
