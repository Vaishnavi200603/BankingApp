package JDBC.MiniBankingProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MiniStatement {
    public static void getMiniStatement(int accNo){
        try{
            Connection con = GetDBConnection.getCon();
            PreparedStatement ps1 = con.prepareStatement("select * from miniStatement where accountNo = ?");
            ps1.setInt(1, accNo);

            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()){
                System.out.print(rs1.getInt(3) + " | ");
                System.out.print(rs1.getString(4) + " | ");
                System.out.print(rs1.getString(5) + " | ");
                System.out.print(rs1.getString(6) + " | ");
                System.out.println();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
