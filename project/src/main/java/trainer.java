import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class trainer {
    public static void menu(Connection conn, ResultSet user)
    {
        int input;
        Scanner myObj = new Scanner(System.in);

        try {
            System.out.println("Welcome " + user.getString("fName") + user.getString("lName"));
            int id = user.getInt("trainerID");

            while (true) {
                System.out.println("Trainer Menu:");
                System.out.println("(1) Add availability");
                System.out.println("(2) Remove availability");
                System.out.println("(3) View my availabiliies");
                System.out.println("(4) View member profile");
                System.out.println("(5) Logout");

                input = myObj.nextInt();

                if (input == 1) {
                    addAvailability(conn, id);
                } else if (input == 2) {
                    removeAvailability(conn, id);
                } else if (input == 3) {
                    printAvailability(conn, id);
                } else if (input == 4) {
                    viewMemberProfile(conn);
                } else if (input == 5) {
                    main.menu(conn);
                    break;
                } else {
                    System.out.println("Invalid Input!");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addAvailability(Connection conn, int id)
    {
        String date;
        String startTime;
        String endTime;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        System.out.print("What date are you available? (yyyy-mm-dd) ");
        date = myObj.nextLine();

        System.out.print("What is your start time? (hh:mm) ");
        startTime = myObj.nextLine();

        System.out.print("What is your end time? (hh:mm) ");
        endTime = myObj.nextLine();

        java.util.Date parsedUtilDate = null;
        java.util.Date parsedUtilStartTime = null;
        java.util.Date parsedUtilEndTime = null;
        try {
            parsedUtilDate = dateFormat.parse(date);
            parsedUtilStartTime = timeFormat.parse(startTime);
            parsedUtilEndTime = timeFormat.parse(endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        java.sql.Date sqlDate = new java.sql.Date(parsedUtilDate.getTime());
        java.sql.Time sqlStartTime = new java.sql.Time(parsedUtilStartTime.getTime());
        java.sql.Time sqlEndTime = new java.sql.Time(parsedUtilEndTime.getTime());


        String sql = "INSERT INTO Availability (trainerID, date, startTime, endTime) VALUES" +
                "(?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setDate(2, sqlDate);
            statement.setTime(3, sqlStartTime);
            statement.setTime(4, sqlEndTime);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Successfully add availability!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewMemberProfile(Connection conn)
    {
        String fName;
        String lName;

        Scanner myObj = new Scanner(System.in);

        System.out.print("Please enter the member's first name: ");
        fName = myObj.nextLine();

        System.out.print("Please enter the member's last name: ");
        lName = myObj.nextLine();

        try {
            String sql = "SELECT * FROM Member WHERE fName = ? AND lName = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, fName);
            statement.setString(2, lName);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next())
            {
                int memberID = resultSet.getInt("memberID");
                String firstName = resultSet.getString("fName");
                String lastName = resultSet.getString("lName");
                String sex = resultSet.getString("sex");
                String email = resultSet.getString("email");
                String birthday = resultSet.getString("birthday");
                String fitnessGoal = resultSet.getString("fitnessGoal");
                String weight = resultSet.getString("weight");
                String height = resultSet.getString("height");

                String format = "%-25s%s%n";

                System.out.printf(format, "First Name:", firstName);
                System.out.printf(format, "Last Name:", lastName);
                System.out.printf(format, "Sex:", sex);
                System.out.printf(format, "Email:", email);
                System.out.printf(format, "Birthday:", birthday);
                System.out.printf(format, "Weight:", weight);
                System.out.printf(format, "Height:", height);
                System.out.printf(format, "Fitness Goal:", fitnessGoal);
                System.out.println("Fitness Achievements:");

                sql = "SELECT * FROM fitnessAchievement WHERE memberID =?";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, memberID);

                resultSet = statement.executeQuery();

                while (resultSet.next())
                {
                    String fitnessAchievement = resultSet.getString("achievement");
                    System.out.println("- " + fitnessAchievement);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeTrainerAvailability(Connection conn, int id, Date date, Time startTime, Time endTime, Time newStartTime, Time newEndTime)
    {
        String sql = "UPDATE Availability SET endTime=? WHERE trainerID =? AND date=? AND startTime =?";
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setTime(1, newEndTime);
            statement.setInt(2, id);
            statement.setDate(3, date);
            statement.setTime(4, startTime);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();

                sql = "INSERT INTO Availability (trainerID, date, startTime, endTime) VALUES (?, ?, ?, ?)";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, id);
                statement.setDate(2, date);
                statement.setTime(3, newStartTime);
                statement.setTime(4, endTime);

                rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    statement.close();
                    System.out.println("Update was successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTrainerAvailability(Connection conn, Date date, Time startTime, Time endTime, int id)
    {
        String sql = "INSERT INTO Availability (trainerID, date, startTime, endTime) VALUES" +
                "(?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setDate(2, date);
            statement.setTime(3, startTime);
            statement.setTime(4, endTime);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyTrainerAvailability(Connection conn, Time startTime, Time endTime, Time newStartTime, Time newEndTime, int id)
    {
        try
        {
            String sql = "UPDATE Availability SET endTime =? WHERE trainerID=? AND endTime =?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setTime(1, newStartTime);
            statement.setInt(2, id);
            statement.setTime(3, startTime);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
            }

            sql = "UPDATE Availability SET startTime =? WHERE trainerID=? AND startTime =?";
            statement = conn.prepareStatement(sql);
            statement.setTime(1, newEndTime);
            statement.setInt(2, id);
            statement.setTime(3, endTime);
            rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeAvailability(Connection conn, int id)
    {
        try
        {
            String sql = "SELECT COUNT(*) AS rowcount FROM Availability WHERE trainerID =?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt("rowcount");
                if (count == 0)
                {
                    System.out.println("You currently have no availabilities");
                    return;
                }
            }

            int availabilityID;

            printAvailability(conn, id);

            Scanner myObj = new Scanner(System.in);

            System.out.print("Which availability (id) would you like to remove? ");
            availabilityID = Integer.parseInt(myObj.nextLine());

            sql = "DELETE FROM Availability WHERE availabilityID=?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, availabilityID);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Remove availability with id:" + id + " was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printAvailability(Connection conn, int id)
    {
        try {
            String sql = "SELECT * FROM Availability WHERE trainerID =?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-20s %-20s %-20s %n";
            String rowFormat = "%-20s %-20s %-20s %-20s %n";

            System.out.println("Trainer Availability: ");
            System.out.printf(headerFormat, "Availability ID", "Date", "Start Time", "End Time");

            while(resultSet.next())
            {
                String availabilityID = resultSet.getString("availabilityID");
                String date = resultSet.getString("date");
                String startTime = resultSet.getString("startTime");
                String endTime = resultSet.getString("endTime");

                availabilityID = availabilityID.length() > 20 ? availabilityID.substring(0, 10) + "..." : availabilityID;
                date = date.length() > 20 ? date.substring(0, 10) + "..." : date;
                startTime = startTime.length() > 20 ? startTime.substring(0, 10) + "..." : startTime;
                endTime = endTime.length() > 20 ? endTime.substring(0, 15) + "..." : endTime;

                System.out.printf(rowFormat, availabilityID, date, startTime, endTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
