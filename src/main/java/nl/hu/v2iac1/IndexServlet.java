package nl.hu.v2iac1;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class IndexServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Secrets!</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Secrets!</h1>");
            out.println("<ul>");
            out.println("<li>Deze <a href=\"/sample/public\">link</a> mag publiek zijn.</li>");
            out.println("<li>Deze <a href=\"/sample/secret\">link</a> moet met een username/password beveiligd worden. (+3 pt)</li>");
            out.println("<li>Deze <a href=\"/sample/verysecret\">link</a> moet via een externe identity provider beveiligd worden. (+3 pt)</li>");
            out.println("<li>Deze <a href=\"/sample/topsecret\">link</a> moet met two-factor authenticatie beveiligd worden. (+3 pt)</li>");
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
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