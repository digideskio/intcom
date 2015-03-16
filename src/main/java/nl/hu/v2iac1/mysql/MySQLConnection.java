package nl.hu.v2iac1.mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.inject.Inject;
import nl.hu.v2iac1.Configuration;
public class MySQLConnection {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public Connection getConnection() throws Exception {
        Configuration configuration = new Configuration();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connect = DriverManager.getConnection("jdbc:mysql://" + configuration.getValue(Configuration.Key.DBHOST) + ":3306/" + configuration.getValue(Configuration.Key.DBNAME), configuration.getValue(Configuration.Key.DBUSER), configuration.getValue(Configuration.Key.DBPASS));
            return connect;
        } catch (Exception e) {
            throw e;
        }
    }
}
