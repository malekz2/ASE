package productivityApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author malekzuhdi on 23/09/2025
 */
public class StudentEntryApp {
    private static final String DB_URL = "jdbc:sqlite:/Users/malekzuhdi/IdeaProjects/ASE/src/main/resources/databases/studentInfo";
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if(conn != null){
                System.out.println("Connected to database!");
                String insertQuery = "INSERT INTO student_dg_tmp VALUES (?, ?, ?, ?)";
                //binds all the input into a single query
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);


                System.out.print("Ender studnet ID ");
                int id = input.nextInt();
                input.nextLine();

                System.out.print("Enter Student name ");
                String name = input.nextLine();
                input.nextLine();

                System.out.print("enter student grade level: ");
                int grade = input.nextInt();
                input.nextLine();

                System.out.print("Enter student birthday (YYYY-MM-DD) ");
                String birthday = input.nextLine();

                //SQL follows a base 1 index structure
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setInt(3, grade);
                pstmt.setString(4, birthday);

                pstmt.executeUpdate();
                System.out.println("Student successfully added");

            }
        } catch (SQLException e) {

            System.out.println("SQLException or Error" + e.getMessage());
        }
    }
}
