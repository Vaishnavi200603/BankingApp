package JDBC.MiniBankingProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserData {
    public static void insertIntoDatabase(int id, String name, String email, String phoneNo, int accountNo){
        Connection con = GetDBConnection.getCon();
        try{
            con.setAutoCommit(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try {
            PreparedStatement ps1 = con.prepareStatement("insert into users values(?, ?, ?, ?, ?)");
            ps1.setString(1, String.valueOf(id));
            ps1.setString(2, name);
            ps1.setString(3, email);
            ps1.setString(4, phoneNo);
            ps1.setString(5, String.valueOf(accountNo));

            int result1 = ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("insert into totalAmount values(?,?,?)");

            ps2.setString(1, String.valueOf(id));
            ps2.setString(2, String.valueOf(accountNo));
            ps2.setInt(3, 50000);

            int result2 = ps2.executeUpdate();

            if (result1 > 0 && result2 > 0) {
                System.out.println("Account Created Under Name - " + name);
                con.commit();
            }
            else{
                con.rollback();
                System.out.println("SORRY! Inconvenience Happen, " +
                        "Account Cannot Created Under Name - " + name);
            }

        }
        catch (Exception e) {
            try{
                con.rollback();
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }
}
