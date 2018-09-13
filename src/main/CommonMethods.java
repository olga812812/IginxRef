package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class CommonMethods {	  
  
   synchronized public  Properties loadPropertyFromFile() 
   {
      Properties prop = new Properties();
      FileInputStream stream = null;
      InputStreamReader reader = null;

      try {
         stream = new FileInputStream(new File("IGINX.conf"));
         reader = new InputStreamReader(stream, "Windows-1251");
         prop.load(reader);
         stream.close();
         reader.close();
          } 
      catch (Exception ex) 
         {
         System.out.println(ex);        
         }      
      return prop;
   }
	  
   public String getProperty(String propertyName)
   {
      return loadPropertyFromFile().getProperty(propertyName);
   }

   public void print(String strintToPrint) 
   {
      System.out.println(strintToPrint);
   }
}
