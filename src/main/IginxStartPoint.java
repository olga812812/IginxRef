package main;


import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;


public class IginxStartPoint {

   private static  CommonMethods callCommonMethod  = new CommonMethods();
   

   public static void main(String[] args) 
   {	  
    	  startIginx(createJettyServer());  
   }
   
   private static Server createJettyServer()
   {
	     Server jettyServer = new Server();
	     jettyServer.setConnectors(new ServerConnector[]{createJettyServerConnector(jettyServer), createJettyServerSSLConnector(jettyServer)});
	     addServlets(createServletContextHandlerForJettyServer(jettyServer));
	     return jettyServer;
   }
   
   private static ServerConnector createJettyServerConnector(Server jettyServer)
   {
	   ServerConnector serverJettyConnector = new ServerConnector(jettyServer);
	   serverJettyConnector.setPort(Integer.parseInt(callCommonMethod.getProperty("Port")));
	   return serverJettyConnector;
   }
   
   private static ServerConnector createJettyServerSSLConnector(Server jettyServer)
   {
	   HttpConfiguration httpsConfiguration = new HttpConfiguration();
	   httpsConfiguration.addCustomizer(new SecureRequestCustomizer());	   
       SslContextFactory sslContextFactory = new SslContextFactory();
       sslContextFactory.setKeyStorePath("ssl/iginx.jks");
       sslContextFactory.setKeyStorePassword("123456");
       sslContextFactory.setKeyManagerPassword("123456");
       ServerConnector sslConnector = new ServerConnector(jettyServer, new ConnectionFactory[]{new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(httpsConfiguration)});
       sslConnector.setPort(Integer.parseInt(callCommonMethod.getProperty("SslPort")));
       return sslConnector;
   }
   
   private static ServletContextHandler createServletContextHandlerForJettyServer(Server jettyServer)
   {
	   ServletContextHandler handler = new ServletContextHandler(jettyServer, "/");
	   return handler;
   }
   
   private static void addServlets(ServletContextHandler handler)
   {
	   handler.addServlet(VastServlet.class, "/");
	   handler.addServlet(OttServlet.class, "/ott");
   }
   
   private static void startIginx(Server jettyServer)
   {	            
       try {
          jettyServer.start();
          jettyServer.join();
       } catch (Exception exeption) 
       {
    	   exeption.printStackTrace();
       }
   }

}
