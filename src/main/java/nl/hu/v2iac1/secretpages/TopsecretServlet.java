package nl.hu.v2iac1.secretpages;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.Configuration;
public class TopsecretServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Configuration configuration = new Configuration();
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null && session.getAttribute("logintoken") != null) {
            String username = session.getAttribute("username").toString();
            String logintoken = session.getAttribute("logintoken").toString();
            if(logintoken.equals("1")) {
                if(session.getAttribute("twosteptoken") != null) {
                    String twosteptoken = session.getAttribute("twosteptoken").toString();
                    if(twosteptoken.equals("1")) {
                        response.setContentType("text/html;charset=UTF-8");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("<!DOCTYPE html>");
                            out.println("<html>");
                            out.println("<head>");
                            out.println("<title>TOPSecret</title>");            
                            out.println("</head>");
                            out.println("<body>");
                            out.println("<h1>TOPSecret</h1>");
                            out.println("Hello "+username+", this is TOP secret: " + configuration.getValue(Configuration.Key.TOPSECRET));
                            out.println("</body>");
                            out.println("</html>");
                        }
                    }else{
                        response.sendRedirect("/sample/twostep?returnpage=topsecret");
                    }
                }else{
                    response.sendRedirect("/sample/twostep?returnpage=topsecret");
                }
            }else{
                response.sendRedirect("/sample/login?returnpage=topsecret");
            }
        }else{
            response.sendRedirect("/sample/login?returnpage=topsecret");
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