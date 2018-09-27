package main;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
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
	ArrayList<String> allUrlsFromConfigFile = new ArrayList<String>();	
	
	@Before
	public void SetUp() {
		createArrayWithListOfUrlsFromConfigFile();
	}
	
	public void createArrayWithListOfUrlsFromConfigFile() {		
		allUrlsFromConfigFile.add("firstUrl");
		allUrlsFromConfigFile.add("urlX");
		
	}

	@Test 
	public void checkWhenNoOverlapInConfigFileAndUriAndQueryString() {
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("noOverlap");
		assertNull(configFile.getResponseUrlFromConfigFile(request));
		
		
	}
	
	@Test 
	public void checkWhenThereISOverlapInConfigFileAndUriAndQueryString() {		
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("a=b");
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "firstUrl");
		
		
	}
	
	@Test
	public void checkWhenThereAreSeveralValuesInUrlInConfigFile() {	
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("pc=a,a=b,second=cv");
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "firstUrl");		
		
	}
	
	@Test
	public void checkWhenUrlInConfFileisEmpty() {		
		when(request.getRequestURI()).thenReturn("hello.ru");
		when(request.getQueryString()).thenReturn("a=b");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("");		
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "firstUrl");	
		
	}
	
	@Test
	public void checkWhenUriAndQueryStringAreEmpty() {		
		when(request.getRequestURI()).thenReturn("");
		when(request.getQueryString()).thenReturn("");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("someValueInUrl");		
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertNull(configFile.getResponseUrlFromConfigFile(request));	
		
	}
	
	@Test
	public void checkWhenUriIsEmpty() {		
		when(request.getRequestURI()).thenReturn("");
		when(request.getQueryString()).thenReturn("someQueryString");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("Query");		
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "firstUrl");		
		
	}
	
	@Test
	public void checkWhenQueryStringIsEmpty() {		
		when(request.getRequestURI()).thenReturn("someUriString");
		when(request.getQueryString()).thenReturn("");
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("Uri");		
		PowerMockito.stub(PowerMockito.method(ConfigFile.class, "getKeyOrValueFromConfig")).toReturn(allUrlsFromConfigFile);
		assertEquals(configFile.getResponseUrlFromConfigFile(request), "firstUrl");		
		
	}
}
