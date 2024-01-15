package JDBC.MiniBankingProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("--------------------------------------");
            System.out.println("SELECT ANY OF THESE OPTIONS : ");
            System.out.print("""
                1. Add Account
                2. Fund Transfer
                3. Mini Statement
                4. Exit
                """);
            int userOption = sc.nextInt();

            switch (userOption){
                case 1 -> {
                    System.out.print("Enter ID : ");
                    int id = sc.nextInt();

                    System.out.print("Enter Name : ");
                    sc.nextLine();
                    String name = sc.nextLine();


                    System.out.print("Enter Email : ");
                    String email = sc.nextLine();

                    System.out.print("Enter Phone Number : ");
                    String phoneNumber = sc.nextLine();

                    System.out.print("Enter Account Number : ");
                    int accountNo = sc.nextInt();

                    try{
                        AddUserData.insertIntoDatabase(id, name, email, phoneNumber, accountNo);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                case 2 -> {
                    System.out.print("Enter From Account Number : ");
                    int fromAcc = sc.nextInt();

                    System.out.print("Enter To Account Number : ");
                    int toAcc = sc.nextInt();

                    System.out.print("Enter Number : ");
                    int amt = sc.nextInt();

                    int fromId = 0, toId = 0;

                    int fromBal = 0;
                    int toBal = 0;


                    Connection con = GetDBConnection.getCon();
                    try{
                        con.setAutoCommit(false);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    try{
                       PreparedStatement ps1 = con.prepareStatement
                               ("Select id, balance from totalAmount where accountNo = ?");

                       ps1.setInt(1,fromAcc);
                       ResultSet rs1 = ps1.executeQuery();

                       while(rs1.next()){
                           fromId = rs1.getInt(1);
                           fromBal = rs1.getInt(2);
                       }

                       //----------------------------------------------------

                       PreparedStatement ps2 = con.prepareStatement
                               ("Select id,balance from totalAmount where accountNo = ?");

                       ps2.setInt(1,toAcc);
                       ResultSet rs2 = ps2.executeQuery();

                       while(rs2.next()){
                           toId = rs2.getInt(1);
                           toBal = rs2.getInt(2);
                       }

                       int newFromBal = fromBal - amt;
                       int newToBal = toBal + amt;

                       PreparedStatement ps3 = con.prepareStatement("update totalAmount set balance=? where accountNo=?");
                       ps3.setInt(1, newFromBal);
                       ps3.setInt(2, fromAcc);

                       int result1 = ps3.executeUpdate();

                        PreparedStatement ps4 = con.prepareStatement("update totalAmount set balance=? where accountNo=?");
                        ps4.setInt(1, newToBal);
                        ps4.setInt(2, toAcc);

                        int result2 = ps4.executeUpdate();
                        Date d = new Date();

                        //for Date
                        //MM - months and mm - minutes
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String currDate = sdf1.format(d);

                        //for Time
                        //HH(capital) - for 24 hours format and hh(small) - for 12 hours format
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                        String currTime = sdf2.format(d);

                        PreparedStatement ps5 = con.prepareStatement("insert into miniStatement values(?,?,?,?,?,?)");
                        ps5.setInt(1, fromId);
                        ps5.setInt(2,fromAcc);
                        ps5.setInt(3,amt);
                        ps5.setString(4,"D");
                        ps5.setString(5,currDate);
                        ps5.setString(6,currTime);
                        int result3 = ps5.executeUpdate();

                        PreparedStatement ps6 = con.prepareStatement("insert into miniStatement values(?,?,?,?,?,?)");
                        ps6.setInt(1, toId);
                        ps6.setInt(2,toAcc);
                        ps6.setInt(3,amt);
                        ps6.setString(4,"C");
                        ps6.setString(5,currDate);
                        ps6.setString(6,currTime);
                        int result4 = ps6.executeUpdate();

                        if(result1 > 0 && result2 > 0 && result3 > 0 && result4 > 0){
                            System.out.println("Amount Deposit Successfully");
                            con.commit();
                        }
                        else{
                            System.out.println("Transaction Failed");
                            con.rollback();
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        try {
                            con.rollback();
                        }
                        catch(Exception exp){
                            e.printStackTrace();
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter Account No : ");
                    int accNo = sc.nextInt();
                    try{
                        MiniStatement.getMiniStatement(accNo);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                default -> {
                    System.out.println("Bank App Closed");
                    System.exit(0);
                }
            }
        }
    }
}
