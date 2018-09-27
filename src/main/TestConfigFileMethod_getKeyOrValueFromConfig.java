package main;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({CommonMethods.class,Properties.class})

public class TestConfigFileMethod_getKeyOrValueFromConfig {
	Properties properties = mock(Properties.class);
	Set<String> allProperties;
	ConfigFile configFile = new ConfigFile();
	
	public void createSetAllProperties() {
		allProperties = new TreeSet<String>();
		allProperties.add("key1");
		allProperties.add("key2");
		allProperties.add("key3");
		
	}
	
	@Test 
	public void checkWhenResultTypeIsKey() {
		createSetAllProperties();		
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "loadPropertyFromFile")).toReturn(properties);
		when(properties.stringPropertyNames()).thenReturn(allProperties);		
		assertArrayEquals(configFile.getKeyOrValueFromConfig("key2", "key").toArray(), new String[] {"key2"});
		
	}
	
	@Test 
	public void checkWhenResultTypeIsValue() {
		createSetAllProperties();		
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "loadPropertyFromFile")).toReturn(properties);
		when(properties.stringPropertyNames()).thenReturn(allProperties);
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("returnValue");
		assertArrayEquals(configFile.getKeyOrValueFromConfig("key2", "value").toArray(), new String[] {"returnValue"});
		
	}
	
	@Test 
	public void checkWhenThereIsNoSuchKeyInConfigFileresultTypeisKey() {
		createSetAllProperties();		
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "loadPropertyFromFile")).toReturn(properties);
		when(properties.stringPropertyNames()).thenReturn(allProperties);		
		assertArrayEquals(configFile.getKeyOrValueFromConfig("keyNo", "key").toArray(), new String[]{});
		
	}
	
	@Test 
	public void checkWhenThereIsNoSuchKeyInConfigFileresultTypeisValue() {
		createSetAllProperties();		
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "loadPropertyFromFile")).toReturn(properties);
		when(properties.stringPropertyNames()).thenReturn(allProperties);		
		assertArrayEquals(configFile.getKeyOrValueFromConfig("keyNo", "value").toArray(), new String[]{});
		
	}
	
		

}
