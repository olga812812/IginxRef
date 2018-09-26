package test;


import main.CommonMethods;
import main.VastServlet;
import main.ConfigFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonMethods.class)


public class ConfigFile_GetResponseFromConfigFileTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	ConfigFile configFile = new ConfigFile();
	

	@Test
	public void checkStringInQS()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid6=5");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","afrin.xml","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkStringInUriQSIsNull()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("ctc.ru");
		assertArrayEquals(new String[]{"200","ctc.xml","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkStringInUriQSIsNotNull()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("puid=noInFile");
		when(request.getRequestURI()).thenReturn("puid6=5");
		assertArrayEquals(new String[]{"200","afrin.xml","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkDefaultResp()
	{
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","un.xml","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkCode204()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("event");
		assertArrayEquals(new String[]{"204","0","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkCode302LocationIsNotNull()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("finish");
		when(request.getRequestURI()).thenReturn("even");
		assertArrayEquals(new String[]{"302","0","http://google.ru"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkCode302LocationIsNull()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("finish2");
		when(request.getRequestURI()).thenReturn("even");
		assertArrayEquals(new String[]{"302","0","http://vi.ru"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkIfNoRespInConfig()
	{
		
		
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("noResp");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","un.xml","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkIfEmptyRespInConfig()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("emptyResp");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"200","un.xml","0"}, configFile.getResponseFromConfigFile(request));
	}
	
	@Test
	public void checkIfCodeInConfigNot200204302()
	{
		
	
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn("anotherCode");
		when(request.getRequestURI()).thenReturn("/");
		assertArrayEquals(new String[]{"159","0","0"}, configFile.getResponseFromConfigFile(request));
	}

	
	@Test (expected=NullPointerException.class)
	
	public void checkIfNoDefaultFileRespLocation() 	
	{
		
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn(null);
				
		when(request.getProtocol()).thenReturn("HTTP/1.1");
		when(request.getQueryString()).thenReturn(null);
		when(request.getRequestURI()).thenReturn("/");
			
		configFile.getResponseFromConfigFile(request);
	}
}
