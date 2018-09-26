package main;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Method; 


import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonMethods.class)


public class TestConfigFileMethod_IsThereDefaultDataInConfigFile {	
	ConfigFile configFile = new ConfigFile();
	
	public String getDefaultValues(String parametrGetProperty, String defaultRespCode, String defaultRespFile, String defaultRespLocation) {
		switch (parametrGetProperty) {
		case "DefaultRespCode": return defaultRespCode;
		case "DefaultRespFile": return defaultRespFile;
		case "DefaultLocation": return defaultRespLocation;
		}
		return "bla-bla-bla";
	}
	
	@Test
	public void checkWhenAllDefaultValuesAreNotNull() {	
		
        PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("some text");
		assertTrue(configFile.isThereDefaultDataInConfigFile());
	}
	
	@Test 
	public void checkWhenDefaultRespCodeIsNull() {		
		PowerMockito.replace(PowerMockito.method(CommonMethods.class, "getProperty")).with(new InvocationHandler() {
			@Override 
			public Object invoke(Object proxy, Method getProperty, Object[] args)
					throws Throwable {
				return getDefaultValues((String)args[0], null, "file", "location");
				
			}
		});
		assertFalse(configFile.isThereDefaultDataInConfigFile());		
	}
	
	@Test 
	public void checkWhenDefaultRespFileIsNull() {		
		PowerMockito.replace(PowerMockito.method(CommonMethods.class, "getProperty")).with(new InvocationHandler() {
			@Override 
			public Object invoke(Object proxy, Method getProperty, Object[] args)
					throws Throwable {
				return getDefaultValues((String)args[0], "respCode200", null, "location");
				
			}
		});
		assertFalse(configFile.isThereDefaultDataInConfigFile());		
	}
	
	@Test 
	public void checkWhenDefaultRespLocationIsNull() {		
		PowerMockito.replace(PowerMockito.method(CommonMethods.class, "getProperty")).with(new InvocationHandler() {
			@Override 
			public Object invoke(Object proxy, Method getProperty, Object[] args)
					throws Throwable {
				return getDefaultValues((String)args[0], "respCode200", "file", null);
				
			}
		});
		assertFalse(configFile.isThereDefaultDataInConfigFile());		
	}

	@Test 
	public void checkWhenAllDefaultValuesAreNull() {		
		PowerMockito.replace(PowerMockito.method(CommonMethods.class, "getProperty")).with(new InvocationHandler() {
			@Override 
			public Object invoke(Object proxy, Method getProperty, Object[] args)
					throws Throwable {
				return getDefaultValues((String)args[0], null, null, null);
				
			}
		});
		assertFalse(configFile.isThereDefaultDataInConfigFile());		
	}



}
