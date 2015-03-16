package nl.hu.v2iac1.login;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.mysql.MySQLConnection;
import org.json.JSONObject;
public class ExternalServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MySQLConnection DBConnection = new MySQLConnection();
        HttpSession session = request.getSession();
        GoogleAuthHelper helper = new GoogleAuthHelper();
        if (request.getParameter("code") == null
                || request.getParameter("state") == null) {
            response.sendRedirect(helper.buildLoginUrl());
            session.setAttribute("state", helper.getStateToken());
        } else if (request.getParameter("code") != null && request.getParameter("state") != null && request.getParameter("state").equals(session.getAttribute("state"))) {
            session.removeAttribute("state");
            String JSONString = helper.getUserInfoJson(request.getParameter("code"));
            JSONObject rootOfPage =  new JSONObject(JSONString);
            String email = rootOfPage.get("email").toString();
            try {
                Connection connect = DBConnection.getConnection();
                PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM users where email = ? AND external = 1");

                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    preparedStatement.close();
                    connect.close();
                    response.sendRedirect("/sample/");
                }else{
                    session.setAttribute("username", email);
                    session.setAttribute("externaltoken", "1");
                    response.sendRedirect("/sample/verysecret");
                }
            } catch (Exception ex) {
                
            }
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
