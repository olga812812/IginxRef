package main;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigFile.class})


public class TestVastServlet_AddHeadersToResponse {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	VastServlet vastServletObject = new VastServlet();
	ArrayList<String> allCookiesFromConfgiFile = new ArrayList<String>();

	
	@Test
	public void checkWhenRequestCookieHeaderIsNotNull() {
		
		PowerMockito.stub(PowerMockito.method(ConfigFile.class,"getKeyOrValueFromConfig")).toReturn(allCookiesFromConfgiFile);
		when(request.getHeader("Cookie")).thenReturn("requestHasCookie");
		vastServletObject.addHeadersToResponse(request, response);
		verify(response, never()).addHeader(anyString(),anyString());
		verify(response, never()).setHeader(anyString(),anyString());		
	}
	
	@Test
	public void checkWhenRequestCookieHeaderIsNull() {
		
		allCookiesFromConfgiFile.add("addCookie1");
		allCookiesFromConfgiFile.add("addCookie2");
		PowerMockito.stub(PowerMockito.method(ConfigFile.class,"getKeyOrValueFromConfig")).toReturn(allCookiesFromConfgiFile);
		when(request.getHeader("Cookie")).thenReturn(null);
		vastServletObject.addHeadersToResponse(request, response);
		verify(response).addHeader(eq("Set-Cookie"), contains(allCookiesFromConfgiFile.get(0)+vastServletObject.requestId));
		verify(response).addHeader(eq("Set-Cookie"), contains(allCookiesFromConfgiFile.get(1)+vastServletObject.requestId));
			
	}
	
	@Test
	public void checkWhenRequestOriginHeaderisNull() {
		
		PowerMockito.stub(PowerMockito.method(ConfigFile.class,"getKeyOrValueFromConfig")).toReturn(allCookiesFromConfgiFile);
		when(request.getHeader("Origin")).thenReturn(null);
		vastServletObject.addHeadersToResponse(request, response);
		verify(response, never()).addHeader(anyString(),anyString());
		verify(response, never()).setHeader(anyString(),anyString());	
		
	}
	
	@Test 
	public void checkWhenRequestOriginHeaderIsNotNull() {
		PowerMockito.stub(PowerMockito.method(ConfigFile.class,"getKeyOrValueFromConfig")).toReturn(allCookiesFromConfgiFile);
		when(request.getHeader("Origin")).thenReturn("someOriginHeaderFromRequest");
		vastServletObject.addHeadersToResponse(request, response);
		verify(response).setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
  	    verify(response).setHeader("Access-Control-Allow-Credentials", "true");		
	}

}
