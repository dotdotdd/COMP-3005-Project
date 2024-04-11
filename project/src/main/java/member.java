import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class member {
    public static void menu(Connection conn, ResultSet user)
    {
        int input;
        Scanner myObj = new Scanner(System.in);

        try {
            System.out.println("Welcome " + user.getString("fName") + " " + user.getString("lName"));
            int id = user.getInt("memberID");
            while (true) {
                System.out.println("Member Menu:");
                System.out.println("(1) Update personal information");
                System.out.println("(2) Update fitness goal");
                System.out.println("(3) Update health metrics");
                System.out.println("(4) Display exercise routines");
                System.out.println("(5) Display exercise achievement");
                System.out.println("(6) Display health statistics");
                System.out.println("(7) Schedule training sessions");
                System.out.println("(8) Join group fitness classes");
                System.out.println("(9) Logout");

                input = myObj.nextInt();

                if (input == 1) {
                    updatePersonalInfo(conn, id);
                } else if (input == 2) {
                    updateFitnessGoal(conn, id);
                } else if (input == 3) {
                    updateHealthMetrics(conn, id);
                } else if (input == 4) {
                    displayExerciseRoutine(conn, id);
                } else if (input == 5) {
                    displayAchievements(conn, id);
                } else if (input == 6) {
                    displayHealthStats(conn, id);
                } else if (input == 7) {
                    scheduleTrainingSessions(conn, id);
                } else if (input == 8) {
                    joinGroupClass(conn, id);
                } else if (input == 9) {
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

    private static void updatePersonalInfo(Connection conn, int id)
    {
        String input;
        String update;
        int phoneNum;
        Scanner myObj = new Scanner(System.in);

        System.out.println("What information would you like to update? (fName, lName, email, username, password, sex)");
        input = myObj.nextLine();

        System.out.println("What would you like to update it to?");

        update = myObj.nextLine();

        String sql = "UPDATE Member SET " + input + "=? WHERE memberid =?";
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, update);
            statement.setInt(2, id);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Update was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void updateFitnessGoal(Connection conn, int id)
    {
        String update;
        Scanner myObj = new Scanner(System.in);

        System.out.println("What would you like to change your fitness goal to?");
        update = myObj.nextLine();

        String sql = "UPDATE Member SET fitnessgoal=? WHERE memberid=?";
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, update);
            statement.setInt(2, id);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Update was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateHealthMetrics(Connection conn, int id)
    {
        String input;
        int update;
        Scanner myObj = new Scanner(System.in);

        System.out.println("What information would you like to update? (weight, height)");
        input = myObj.nextLine();

        System.out.println("What would you like to update it to?");
        update = myObj.nextInt();

        String sql = "UPDATE Member SET " + input + "=? WHERE memberid =?";
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, update);
            statement.setInt(2, id);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Update was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayExerciseRoutine(Connection conn, int id)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT \n" +
                            "    HasRoutine.memberID,\n" +
                            "    Routine.routineName,\n" +
                            "    Routine.description\n" +
                            "FROM \n" +
                            "    HasRoutine\n" +
                            "JOIN \n" +
                            "    Routine ON HasRoutine.routineID = Routine.routineID\n" +
                            "WHERE\n" +
                            "\tmemberID =?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-40s %n";
            String rowFormat = "%-20s %-40s %n";

            System.out.printf(headerFormat, "Routine Name", "Routine Description");

            while(resultSet.next())
            {
                String routineName = resultSet.getString("routineName");
                String description = resultSet.getString("description");

                routineName = routineName.length() > 20 ? routineName.substring(0, 17) + "..." : routineName;
                description = description.length() > 40 ? description.substring(0, 37) + "..." : description;

                System.out.printf(rowFormat, routineName, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAchievements(Connection conn, int id)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM fitnessAchievement WHERE memberid =?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Fitness Achievements");

            while(resultSet.next())
            {
                System.out.println("- " + resultSet.getString("achievement"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayHealthStats(Connection conn, int id)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Member WHERE memberid =?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-15s %-30s %n";
            String rowFormat = "%-15s %-30s %n";

            System.out.printf(headerFormat, "Weight (lbs)", "Height (cm)");

            while(resultSet.next())
            {
                String weight = resultSet.getString("weight");
                String height = resultSet.getString("height");

                weight = weight.length() > 20 ? weight.substring(0, 10) + "..." : weight;
                height = height.length() > 30 ? height.substring(0, 25) + "..." : height;

                System.out.printf(rowFormat, weight, height);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void scheduleTrainingSessions(Connection conn, int id)
    {
        int trainerID;
        String sessionName = "Private session for user: " + id;
        String type = "Private Sessions";
        String date;
        String startTime;
        String endTime;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        System.out.print("Enter session date (yyyy-mm-dd): ");
        date = myObj.nextLine();

        System.out.print("Enter start time (hh:mm): ");
        startTime = myObj.nextLine();

        System.out.print("Enter end time (hh:mm): ");
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

        String sql = "SELECT COUNT(*) AS rowcount FROM Availability WHERE date =? AND startTime <=?  AND endTime >=?";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            statement.setDate(1, sqlDate);
            statement.setTime(2, sqlStartTime);
            statement.setTime(3, sqlEndTime);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt("rowcount");
                if (count == 0)
                {
                    System.out.println("There are currently no trainers available");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print("Enter available trainer ID: ");
        admin.printAvailableTrainers(conn, sqlDate, sqlStartTime, sqlEndTime);
        trainerID = Integer.parseInt(myObj.nextLine());

        sql = "INSERT INTO TrainingSessions (trainerID, sessionName, type, date, startTime, endTime) VALUES" +
                "(?, ?, ?, ?, ?, ?)";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, trainerID);
            statement.setString(2, sessionName);
            statement.setString(3, type);
            statement.setDate(4, sqlDate);
            statement.setTime(5, sqlStartTime);
            statement.setTime(6, sqlEndTime);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Successfully Create Class");

                sql = "SELECT * FROM Availability WHERE date =? AND startTime <=?  AND endTime >=?";
                statement = conn.prepareStatement(sql);
                statement.setDate(1, sqlDate);
                statement.setTime(2, sqlStartTime);
                statement.setTime(3, sqlEndTime);

                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    trainer.removeTrainerAvailability(conn, trainerID, sqlDate, resultSet.getTime("startTime"),
                            resultSet.getTime("endTime"), sqlEndTime, sqlStartTime);
                }

                sql = "SELECT * FROM TrainingSessions WHERE date =? AND startTime =?  AND endTime =? AND trainerID =?";
                statement = conn.prepareStatement(sql);
                statement.setDate(1, sqlDate);
                statement.setTime(2, sqlStartTime);
                statement.setTime(3, sqlEndTime);
                statement.setInt(4, trainerID);

                resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    sql = "INSERT INTO SessionParticipants (sessionID, memberID) VALUES (?, ?)";
                    statement = conn.prepareStatement(sql);
                    statement.setInt(1, resultSet.getInt("sessionID"));
                    statement.setInt(2, id);

                    rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        statement.close();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void joinGroupClass(Connection conn, int id)
    {
        int input;

        Scanner myObj = new Scanner(System.in);

        try {
            String sql = "SELECT COUNT(*) AS rowcount FROM TrainingSessions";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt("rowcount");
                if (count == 0)
                {
                    System.out.println("There are currently no training sessions");
                    return;
                }
            }

            printSessions(conn);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Which session (session ID) would you like to join? ");
        input = Integer.parseInt(myObj.nextLine());

        String sql = "INSERT INTO SessionParticipants (sessionID, memberID) VALUES (?, ?)";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, input);
            statement.setInt(2, id);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void printSessions(Connection conn)
    {
        try {
            String sql = "SELECT * FROM Trainer " +
                    "INNER JOIN TrainingSessions ON Trainer.trainerID = TrainingSessions.trainerID " +
                    "WHERE NOT type ='Private Sessions' ";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-20s %-20s %-20s %-20s %-20s %-20s %n";
            String rowFormat = "%-20s %-20s %-20s %-20s %-20s %-20s %-20s %n";

            System.out.println("Active Group Sessions: ");
            System.out.printf(headerFormat, "Session ID", "Trainer Name", "Session Name", "Session Type", "Date", "Start Time", "End Time");

            while(resultSet.next())
            {
                String sessionID = resultSet.getString("sessionID");
                String trainerName = resultSet.getString("fName") + " " + resultSet.getString("lName");
                String sessionName = resultSet.getString("sessionName");
                String type = resultSet.getString("type");
                String date = resultSet.getString("date");
                String startTime = resultSet.getString("startTime");
                String endTime = resultSet.getString("endTime");

                sessionID = sessionID.length() > 20 ? sessionID.substring(0, 10) + "..." : sessionID;
                trainerName = trainerName.length() > 20 ? trainerName.substring(0, 10) + "..." : trainerName;
                sessionName = sessionName.length() > 20 ? sessionName.substring(0, 15) + "..." : sessionName;
                type = type.length() > 20 ? type.substring(0, 15) + "..." : type;
                date = date.length() > 20 ? date.substring(0, 15) + "..." : date;
                startTime = startTime.length() > 20 ? startTime.substring(0, 15) + "..." : startTime;
                endTime = endTime.length() > 20 ? endTime.substring(0, 15) + "..." : endTime;

                System.out.printf(rowFormat, sessionID, trainerName, sessionName, type, date, startTime, endTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
