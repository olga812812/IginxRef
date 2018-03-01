package main;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OttServlet extends HttpServlet {
	
	Common common = new Common();

   protected void close(HttpServletRequest req) {
      HttpSession session = req.getSession(false);
      if(session != null) {
         synchronized(session) {
            session.invalidate();
         }
      }

   }

   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      common.print(req.getRequestURI());
      common.print(req.getQueryString());
      common.print(req.getHeader("User-Agent"));
      resp.setStatus(200);
      resp.setContentType("text/html");
      resp.getWriter().println("<h1>Ott Servlet</h1>");
      this.close(req);
   }
}
