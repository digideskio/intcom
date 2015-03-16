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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.mysql.MySQLConnection;
import org.json.JSONObject;

/**
 *
 * @author Simon Whiteley <simonwhiteley@hotmail.com>
 */
public class ExternalServlet extends HttpServlet {
    
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
        MySQLConnection DBConnection = new MySQLConnection();
        HttpSession session = request.getSession();
        GoogleAuthHelper helper = new GoogleAuthHelper();
        if (request.getParameter("code") == null
                || request.getParameter("state") == null) {
            /*
             * initial visit to the page
             */
            response.sendRedirect(helper.buildLoginUrl());

            /*
             * set the secure state token in session to be able to track what we sent to google
             */
            session.setAttribute("state", helper.getStateToken());


        } else if (request.getParameter("code") != null && request.getParameter("state") != null && request.getParameter("state").equals(session.getAttribute("state"))) {

            session.removeAttribute("state");

            /*
             * Executes after google redirects to the callback url.
             * Please note that the state request parameter is for convenience to differentiate
             * between authentication methods (ex. facebook oauth, google oauth, twitter, in-house).
             * 
             * GoogleAuthHelper()#getUserInfoJson(String) method returns a String containing
             * the json representation of the authenticated user's information. 
             * At this point you should parse and persist the info.
             */
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
        processRequest(request, response);
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
    
  /*@Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // do stuff
  }

  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/oauth2callback");
    return url.build();
  }

  @Override
  protected AuthorizationCodeFlow initializeFlow() throws IOException {
    return new GoogleAuthorizationCodeFlow.Builder(
        new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
        "[[ENTER YOUR CLIENT ID]]", "[[ENTER YOUR CLIENT SECRET]]",
        Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(
        DATA_STORE_FACTORY).setAccessType("offline").build();
  }

  @Override
  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
    // return user ID
  }*/
}
