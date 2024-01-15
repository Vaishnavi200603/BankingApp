package JDBC.MiniBankingProject;

import java.sql.Connection;
import java.sql.DriverManager;

public class GetDBConnection {
    static Connection con;
    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?useSSL=false", "root", "student");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    static Connection getCon(){
        return con;
    }

}

