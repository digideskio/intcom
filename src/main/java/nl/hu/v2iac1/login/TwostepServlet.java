package nl.hu.v2iac1.login;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.Configuration;
import nl.hu.v2iac1.mysql.MySQLConnection;
public class TwostepServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        RequestDispatcher rd = null;
        String returnPage = request.getParameter("returnpage");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Two-step</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Two-step</h1>");
            out.println("<form method=\"POST\" action=\"\">");
            out.println("<strong>E-Mail:</strong><input name=\"email\" type=\"text\" /><br />");
            out.println("<input name=\"returnpage\" type=\"hidden\" value=\"" + returnPage + "\" />");
            out.println("<input type=\"submit\" value=\"Log in!\" /><br />");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    protected void processCodeRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        RequestDispatcher rd = null;
        String returnPage = request.getParameter("returnpage");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Two-step</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Two-step</h1>");
            out.println("<form method=\"POST\" action=\"\">");
            out.println("<strong>Code:</strong><input name=\"code\" type=\"text\" /><br />");
            out.println("<input name=\"returnpage\" type=\"hidden\" value=\"" + returnPage + "\" />");
            out.println("<input type=\"submit\" value=\"Log in!\" /><br />");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String returnPage = request.getParameter("returnpage");
        Configuration configuration = new Configuration();
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null && session.getAttribute("logintoken") != null) {
            String username = session.getAttribute("username").toString();
            String logintoken = session.getAttribute("logintoken").toString();
            if(logintoken.equals("1") && logintoken != null) {
                processRequest(request, response);
            }else{
                response.sendRedirect("/sample/login?returnpage="+returnPage);
            }
        }else{
            response.sendRedirect("/sample/login?returnpage="+returnPage);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MySQLConnection DBConnection = new MySQLConnection();
        HttpSession session = request.getSession(true);
        RequestDispatcher rd = null;
        Configuration configuration = new Configuration();
        if(session.getAttribute("username") != null && session.getAttribute("logintoken") != null) {
            String returnPage = request.getParameter("returnpage");
            String username = session.getAttribute("username").toString();
            String logintoken = session.getAttribute("logintoken").toString();
            if(logintoken.equals("1")) {
                if(returnPage.equals("topsecret")) {
                    try {
                        Connection connect = DBConnection.getConnection();
                        if(session.getAttribute("sendCode") == null) {
                            String email = request.getParameter("email").toString();
                            PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM users where username = ? AND email = ? AND twostep = 1");
                            preparedStatement.setString(1, username);
                            preparedStatement.setString(2, email);
                            ResultSet resultSet = preparedStatement.executeQuery();
                            if (!resultSet.next()) {
                                preparedStatement.close();
                                connect.close();
                                processRequest(request, response);
                            }else{
                                String userid = resultSet.getString("id");
                                preparedStatement.close();
                                String randomcode = UUID.randomUUID().toString();
                                preparedStatement = connect.prepareStatement("INSERT INTO login_tokens (userid, token, usedkey, created) VALUES (?, ?, 0, NOW())");
                                preparedStatement.setString(1, userid);
                                preparedStatement.setString(2, randomcode);
                                preparedStatement.executeUpdate();
                                Properties props = new Properties();
                                Session mailsession = Session.getDefaultInstance(props, null);
                                Message msg = new MimeMessage(mailsession);
                                msg.setFrom(new InternetAddress("no-reply@yuno.jelleluteijn.nl", "jelleluteijn.com tomcat"));
                                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, username));
                                msg.setSubject("Jouw inlog sleutel");
                                msg.setText("Hier is jouw inlog code: "+randomcode);
                                Transport.send(msg);
                                session.setAttribute("sendCode", "1");
                                processCodeRequest(request, response);
                                connect.close();
                            }
                        }else{
                            String code = request.getParameter("code").toString();
                            PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM users where username = ? AND twostep = 1");
                            preparedStatement.setString(1, username);
                            ResultSet resultSet = preparedStatement.executeQuery();
                            if (!resultSet.next()) {
                                preparedStatement.close();
                                connect.close();
                                processRequest(request, response);
                            }else{
                                String userid = resultSet.getString("id");
                                preparedStatement.close();
                                String randomcode = UUID.randomUUID().toString();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.MINUTE, -5);
                                preparedStatement = connect.prepareStatement("SELECT * FROM login_tokens WHERE userid = ? AND token = ? AND usedkey = 0 AND created > ?");
                                preparedStatement.setString(1, userid);
                                preparedStatement.setString(2, code);
                                preparedStatement.setString(3, dateFormat.format(cal.getTime()));
                                resultSet = preparedStatement.executeQuery();
                                if (!resultSet.next()) {
                                    preparedStatement.close();
                                    connect.close();
                                    processCodeRequest(request, response);
                                }else{
                                    preparedStatement.close();
                                    preparedStatement = connect.prepareStatement("UPDATE login_tokens SET usedkey = 1  WHERE userid = ? AND token = ?");
                                    preparedStatement.setString(1, userid);
                                    preparedStatement.setString(2, code);
                                    preparedStatement.executeUpdate();
                                    session.setAttribute("twosteptoken", "1");
                                    connect.close();
                                    response.sendRedirect("/sample/topsecret");
                                }
                                connect.close();
                            }
                        }
                    } catch (Exception ex) {
                        response.setContentType("text/html;charset=UTF-8");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("EXCEPTION!<br />");
                            ex.printStackTrace(out);
                        }
                    }
                }
            }else{
                response.sendRedirect("/sample/login?returnpage=secret");
            }
        }else{
            response.sendRedirect("/sample/login?returnpage=secret");
        }
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
