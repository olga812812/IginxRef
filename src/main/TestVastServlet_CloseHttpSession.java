package main;

import javax.servlet.http.HttpServletRequest;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Test;
import static org.mockito.Mockito.*;
import javax.servlet.http.HttpSession;

@RunWith(PowerMockRunner.class)

public class TestVastServlet_CloseHttpSession {
	VastServlet vastServletObject = new VastServlet();
	
	@Test	
	public void checkWhenSessionIsNotNull() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session= mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);
		vastServletObject.closeHttpSession(request);
		verify(session, times(1)).invalidate();		
		
	}
	
	@Test	
	public void checkWhenSessionIsNull() {		
		HttpServletRequest request = mock(HttpServletRequest.class);		
		when(request.getSession(false)).thenReturn(null);
		vastServletObject.closeHttpSession(request);			
	}
	
	

}
