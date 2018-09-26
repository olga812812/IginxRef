package main;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonMethods.class)


public class TestConfigFileMethod_InitDefaultValues {
	
	private ConfigFile configFile = new ConfigFile();
	
	@Test
	public void checkThatAllDefaultVariablesAreInitialized() {
		PowerMockito.stub(PowerMockito.method(CommonMethods.class, "getProperty")).toReturn("ValueIsHere");
		configFile.initDefaultValues();
		assertEquals(configFile.defaultRespCode, "ValueIsHere");
		assertEquals(configFile.defaultFile, "ValueIsHere");
		assertEquals(configFile.defaultLocation, "ValueIsHere");	
		
	}
	
	
	

}
