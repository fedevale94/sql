package s07;

import java.sql.*;

import static jd.Config.*;

public class SimpleSelector {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT coder_id, first_name, last_name FROM coders")) {
            System.out.println("Looping on the result set");
            System.out.printf("%4s %20s %20s%n", "id", "first", "last");
            while (rs.next()) {
                int id = rs.getInt(1);
                String first = rs.getString(2);
                String last = rs.getString(3);

                System.out.printf("%4s %20s %20s%n", id, first, last);
            }
            System.out.println("Done");
        } catch (SQLException se) {
            throw new IllegalStateException(se);
        }
    }
}
