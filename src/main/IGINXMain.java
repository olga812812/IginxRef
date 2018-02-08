package main;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;


public class IGINXMain {

   private static final Logger log = Logger.getLogger(IGINXMain.class);


   public static void main(String[] args) {
      try {
         Server ex = new Server();
         ServerConnector connector = new ServerConnector(ex);
         connector.setPort(Integer.parseInt(Common.propLoad().getProperty("Port")));
         HttpConfiguration https = new HttpConfiguration();
         https.addCustomizer(new SecureRequestCustomizer());
         SslContextFactory sslContextFactory = new SslContextFactory();
         sslContextFactory.setKeyStorePath("ssl/iginx.jks");
         sslContextFactory.setKeyStorePassword("123456");
         sslContextFactory.setKeyManagerPassword("123456");
         ServerConnector sslConnector = new ServerConnector(ex, new ConnectionFactory[]{new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https)});
         sslConnector.setPort(Integer.parseInt(Common.propLoad().getProperty("SslPort")));
         ex.setConnectors(new ServerConnector[]{connector, sslConnector});
         ServletContextHandler handler = new ServletContextHandler(ex, "/");
         handler.addServlet(VastServlet.class, "/");
         handler.addServlet(OttServlet.class, "/ott");
         ex.start();
         ex.join();
      } catch (Exception ex) {
         ex.printStackTrace();
      }

   }
}
