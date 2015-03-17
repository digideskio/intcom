package nl.hu.v2iac1.secretpages;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.Configuration;
public class SecretServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Configuration configuration = new Configuration();
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null && session.getAttribute("logintoken") != null) {
            String username = session.getAttribute("username").toString();
            String logintoken = session.getAttribute("logintoken").toString();
            if(logintoken.equals("1") && logintoken != null) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Secret</title>");            
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Secret</h1>");
                    out.println("Hello "+username+", this is secret:<br />"+ configuration.getValue(Configuration.Key.SECRET));
                    out.println("</body>");
                    out.println("</html>");
                }
            }else{
                response.sendRedirect("/intcom/login?returnpage=secret");
            }
        }else{
            response.sendRedirect("/intcom/login?returnpage=secret");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}