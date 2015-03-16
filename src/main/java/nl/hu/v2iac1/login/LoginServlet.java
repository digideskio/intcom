/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hu.v2iac1.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static javax.management.remote.JMXConnectorFactory.connect;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.mysql.MySQLConnection;

/**
 *
 * @author Jelle
 */
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        RequestDispatcher rd = null;
        String returnPage = request.getParameter("returnpage");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Login</h1>");
            out.println("<form method=\"POST\" action=\"\">");
            out.println("<strong>Username:</strong><input name=\"username\" type=\"text\" /><br />");
            out.println("<strong>Password:</strong><input name=\"password\" type=\"password\" /><br />");
            out.println("<input name=\"returnpage\" type=\"hidden\" value=\"" + returnPage + "\" />");
            out.println("<input type=\"submit\" value=\"Log in!\" /><br />");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MySQLConnection DBConnection = new MySQLConnection();
        HttpSession session = request.getSession(true);
        RequestDispatcher rd = null;
        String returnPage = request.getParameter("returnpage");
        String GivenUsername = request.getParameter("username");
        String GivenPassword = request.getParameter("password");

        if (GivenUsername == null || "".equals(GivenUsername) || GivenPassword == null || "".equals(GivenPassword) || returnPage == null || "".equals(returnPage)) {
            
        }else{
            if(returnPage.equals("secret") || returnPage.equals("topsecret")) {
                try {
                    Connection connect = DBConnection.getConnection();
                    PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM users where username = ? AND password = ? AND login = 1");

                    preparedStatement.setString(1, GivenUsername);
                    preparedStatement.setString(2, GivenPassword);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    if (!resultSet.next()) {
                        preparedStatement.close();
                        connect.close();
                    }else{
                        session.setAttribute("username", GivenUsername);
                        session.setAttribute("logintoken", "1");
                        if(returnPage.equals("secret")) {
                            response.sendRedirect("/sample/secret");
                        }else if(returnPage.equals("topsecret")) {
                            response.sendRedirect("/sample/twostep?returnpage=topsecret");
                        }
                    }
                } catch (Exception ex) {
                    
                }
            }
        }
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
