package main;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommonMethods.class,ConfigFile.class})



public class TestConfigFileMethod_getResponseUrlFromConfigFile {
	
	HttpServletRequest request = mock(HttpServletRequest.class);
	ConfigFile configFile = new ConfigFile();

	@Test 
	public void checkWhenNoOverlapInConfigFileAndUriAndQueryString() {
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("noOverlap");
		assertNull(configFile.getResponseUrlFromConfigFile(request));
		
		
	}
	
	@Test 
	public void checkWhenThereISOverlapInConfigFileAndUriAndQueryString() {
		ArrayList<String> allUrlsFromConfigFile = new ArrayList<String>();
		allUrlsFromConfigFile.add("urlX");
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("a=b");
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "urlX");
		
		
	}
	
	@Test
	public void checkWhenThereAreSeveralValuesInUrlInConfigFile() {
		ArrayList<String> allUrlsFromConfigFile = new ArrayList<String>();	
		allUrlsFromConfigFile.add("afirst");
		allUrlsFromConfigFile.add("urlX");
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("pc=a,second=cv,a=b");
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "urlX");
		
		
	}
	
	
}
