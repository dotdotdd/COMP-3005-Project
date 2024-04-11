import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Scanner;

public class main
{
    public static void main(String[] args)
    {
        String url = "jdbc:postgresql://localHost:5432/project";
        String user = "postgres";
        String password = "password";

        try
        {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null)
            {
                menu(conn);
            }
            else
            {
                System.out.println("Failed to establish connection.");
            }
            assert conn != null;
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void menu(Connection conn)
    {
        int input;
        Scanner myObj = new Scanner(System.in);

        System.out.println("Welcome to Health and Fitness Club");
        System.out.println("(1) Login");
        System.out.println("(2) Signup");
        System.out.println("(3) Exit");

        input = myObj.nextInt();

        while (true) {
            if (input == 1) {
                login(conn);
            } else if (input == 2) {
                signup(conn);
            } else if (input == 3) {
                System.exit(0);
            } else {
                System.out.println("Invalid Input!");
            }
        }
    }

    private static void signup(Connection conn) {
        String fName;
        String lName;
        String email;
        String username;
        String password;
        String sex;
        String birthday;
        int weight;
        int height;
        String fitnessGoal;
        String fitnessAchievement;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        System.out.print("Enter first name: ");
        fName = myObj.nextLine();
        System.out.print("Enter last name: ");
        lName = myObj.nextLine();
        System.out.print("Enter email: ");
        email = myObj.nextLine();
        System.out.print("Enter username: ");
        username = myObj.nextLine();
        System.out.print("Enter password: ");
        password = myObj.nextLine();
        System.out.print("Enter sex: ");
        sex = myObj.nextLine();
        System.out.print("Enter birthday (yyyy-mm-dd): ");
        birthday = myObj.nextLine();
        System.out.print("Enter fitness goal: ");
        fitnessGoal = myObj.nextLine();
        System.out.print("Enter fitness achievement: ");
        fitnessAchievement = myObj.nextLine();
        System.out.print("Enter weight (lbs): ");
        weight = Integer.parseInt(myObj.nextLine());
        System.out.print("Enter height (cm): ");
        height = Integer.parseInt(myObj.nextLine());

        java.util.Date parsedUtilDate = null;
        try {
            parsedUtilDate = format.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        java.sql.Date sqlDate = new java.sql.Date(parsedUtilDate.getTime());

        try
        {
            String sql = "INSERT INTO Member (fName, lName, email, username, password, sex, birthday, weight, height, fitnessGoal) VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, email);
            statement.setString(4, username);
            statement.setString(5, password);
            statement.setString(6, sex);
            statement.setDate(7, sqlDate);
            statement.setInt(8, weight);
            statement.setInt(9, height);
            statement.setString(10, fitnessGoal);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                sql = "SELECT * FROM Member WHERE username = ? AND password = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet result = statement.executeQuery();

                if (result.next()) {

                    sql = "INSERT INTO fitnessAchievement (memberID, achievement) VALUES" +
                            "(?, ?)";
                    statement = conn.prepareStatement(sql);
                    statement.setInt(1, result.getInt("memberID"));
                    statement.setString(2, fitnessAchievement);

                    rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        member.menu(conn, result);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void login(Connection conn)
    {
        String username;
        String password;

        Scanner myObj = new Scanner(System.in);

        System.out.print("Enter username: ");
        username = myObj.nextLine();
        System.out.print("Enter password: ");
        password = myObj.nextLine();

        try
        {
            String sql = "SELECT * FROM Member WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                // login successful
                member.menu(conn, result);
            }

            sql = "SELECT * FROM Trainer WHERE username = ? AND password = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            result = statement.executeQuery();

            if (result.next())
            {
                // login successful
                trainer.menu(conn, result);
            }

            sql = "SELECT * FROM Admin WHERE username = ? AND password = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            result = statement.executeQuery();

            if (result.next())
            {
                // login successful
                admin.menu(conn, result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}