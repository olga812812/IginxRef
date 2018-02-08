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
	public void checkgetResp()
	{
		
		request = mock(HttpServletRequest.class);
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid30=3");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","indesit.xml","0"}, myServlet.getResp(request));
	}

}
