import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class admin {
    public static void menu(Connection conn, ResultSet user)
    {
        int input;
        Scanner myObj = new Scanner(System.in);

        try {
            System.out.println("Welcome " + user.getString("fName") + user.getString("lName"));
            int id = user.getInt("adminID");

            while (true) {
                System.out.println("Admin Menu:");
                System.out.println("(1) Book Room");
                System.out.println("(2) Remove Room Booking");
                System.out.println("(3) View Room Booking");
                System.out.println("(4) Equipment Maintenance Monitoring");
                System.out.println("(5) Class Schedule Updating");
                System.out.println("(6) Process Payment");
                System.out.println("(7) View All Payments");
                System.out.println("(8) Logout");

                input = myObj.nextInt();

                if (input == 1) {
                    roomBooking(conn, id);
                } else if (input == 2) {
                    removeRoomBooking(conn);
                } else if (input == 3) {
                    printRoomBooking(conn);
                } else if (input == 4) {
                    maintanenceViewing(conn);
                } else if (input == 5) {
                    classSchedulingMenu(conn);
                } else if (input == 6) {
                    billing(conn, id);
                } else if (input == 7) {
                    viewPayments(conn);
                }else if (input == 8) {
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

    private static void roomBooking(Connection conn, int id)
    {
        int roomID;
        String date;
        String startTime;
        String endTime;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        System.out.println("Currently booked rooms");
        printRoomBooking(conn);

        System.out.println("Which room would you like to book? Please enter the room ID");
        printRooms(conn);
        roomID = Integer.parseInt(myObj.nextLine());

        System.out.print("What date would you like to book it? (yyyy-mm-dd) ");
        date = myObj.nextLine();

        System.out.print("What is the start time? (hh:mm) ");
        startTime = myObj.nextLine();

        System.out.print("What is the end time? (hh:mm) ");
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

        String sql = "INSERT INTO RoomBooking (roomID, adminID, date, startTime, endTime) VALUES" +
                "(?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, roomID);
            statement.setInt(2, id);
            statement.setDate(3, sqlDate);
            statement.setTime(4, sqlStartTime);
            statement.setTime(5, sqlEndTime);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Successfully booked room!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printRooms(Connection conn)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Rooms");
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-40s %n";
            String rowFormat = "%-20s %-40s %n";

            System.out.println("Rooms: ");
            System.out.printf(headerFormat, "Room ID", "Room Names");

            while(resultSet.next())
            {
                String roomID = resultSet.getString("roomID");
                String roomName = resultSet.getString("roomName");

                roomID = roomID.length() > 20 ? roomID.substring(0, 10) + "..." : roomID;
                roomName = roomName.length() > 40 ? roomName.substring(0, 37) + "..." : roomName;

                System.out.printf(rowFormat, roomID, roomName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void removeRoomBooking(Connection conn)
    {
        int bookingID;

        Scanner myObj = new Scanner(System.in);

        try
        {
            String sql = "SELECT COUNT(*) AS rowcount FROM RoomBooking";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt("rowcount");
                if (count == 0)
                {
                    System.out.println("There are currently no rooms booked");
                    return;
                }
            }

            printRoomBooking(conn);

            System.out.print("Which booking would you like to remove? Select booking ID ");
            bookingID = Integer.parseInt(myObj.nextLine());

            sql = "DELETE FROM RoomBooking WHERE bookingID=?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, bookingID);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Remove booking with bookingID:" + bookingID + " was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printRoomBooking(Connection conn)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM RoomBooking");
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-20s %-20s %-20s %-20s %n";
            String rowFormat = "%-20s %-20s %-20s %-20s %-20s %n";

            System.out.println("Room Booking: ");
            System.out.printf(headerFormat, "Booking ID", "Room ID", "Date", "Start Time", "End Time");

            while(resultSet.next())
            {
                String bookingID = resultSet.getString("bookingID");
                String roomID = resultSet.getString("roomID");
                String date = resultSet.getString("date");
                String startTime = resultSet.getString("startTime");
                String endTime = resultSet.getString("endTime");

                bookingID = bookingID.length() > 20 ? bookingID.substring(0, 10) + "..." : bookingID;
                roomID = roomID.length() > 20 ? roomID.substring(0, 10) + "..." : roomID;
                date = date.length() > 20 ? date.substring(0, 15) + "..." : date;
                startTime = startTime.length() > 20 ? startTime.substring(0, 15) + "..." : startTime;
                endTime = endTime.length() > 20 ? endTime.substring(0, 15) + "..." : endTime;

                System.out.printf(rowFormat, bookingID, roomID, date, startTime, endTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void maintanenceViewing(Connection conn)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Equipments");
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-20s %-20s %n";
            String rowFormat = "%-20s %-20s %-20s %n";

            System.out.println("Equipments: ");
            System.out.printf(headerFormat, "Equipment ID", "Equipment Name", "Last Maintenence");

            while(resultSet.next())
            {
                String equipmentID = resultSet.getString("equipmentID");
                String equipmentName = resultSet.getString("equipmentName");
                String lastMaintenence = resultSet.getString("lastMaintenence");

                equipmentID = equipmentID.length() > 20 ? equipmentID.substring(0, 10) + "..." : equipmentID;
                equipmentName = equipmentName.length() > 20 ? equipmentName.substring(0, 10) + "..." : equipmentName;
                lastMaintenence = lastMaintenence.length() > 20 ? lastMaintenence.substring(0, 15) + "..." : lastMaintenence;

                System.out.printf(rowFormat, equipmentID, equipmentName, lastMaintenence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void billing(Connection conn, int id)
    {
        int memberID;
        int amount;
        String date;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.print("Please enter the member's ID: ");
        memberID = Integer.parseInt(myObj.nextLine());

        System.out.print("Please enter today's date: (yyyy-mm-dd) ");
        date = myObj.nextLine();

        System.out.print("Please enter the total amount ($): ");
        amount = Integer.parseInt(myObj.nextLine());

        java.util.Date parsedUtilDate = null;
        try {
            parsedUtilDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        java.sql.Date sqlDate = new java.sql.Date(parsedUtilDate.getTime());

        String sql = "INSERT INTO Billing (memberID, adminID, amount, date) VALUES" +
                "(?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, memberID);
            statement.setInt(2, id);
            statement.setInt(3, amount);
            statement.setDate(4, sqlDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Payment Approve!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewPayments(Connection conn)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Billing");
            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-20s %-20s %-20s %-20s %n";
            String rowFormat = "%-20s %-20s %-20s %-20s %-20s %n";

            System.out.println("Billing History: ");
            System.out.printf(headerFormat, "Billing ID", "Member ID", "Admin ID", "Amount", "Date");

            while(resultSet.next())
            {
                String billingID = resultSet.getString("billingID");
                String memberID = resultSet.getString("memberID");
                String adminID = resultSet.getString("adminID");
                String amount = resultSet.getString("amount");
                String date = resultSet.getString("date");

                billingID = billingID.length() > 20 ? billingID.substring(0, 10) + "..." : billingID;
                memberID = memberID.length() > 20 ? memberID.substring(0, 10) + "..." : memberID;
                adminID = adminID.length() > 20 ? adminID.substring(0, 10) + "..." : adminID;
                amount = amount.length() > 20 ? amount.substring(0, 15) + "..." : amount;
                date = amount.length() > 20 ? date.substring(0, 15) + "..." : date;

                System.out.printf(rowFormat, billingID, memberID, adminID, amount, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void classSchedulingMenu(Connection conn)
    {
        int input;
        Scanner myObj = new Scanner(System.in);

        while (true) {
            System.out.println("Class Schedule Update Menu:");
            System.out.println("(1) Add group class");
            System.out.println("(2) Remove group class");
            System.out.println("(3) Update class time");
            System.out.println("(4) Update class trainer");
            System.out.println("(5) View classes");
            System.out.println("(6) Exit");

            input = myObj.nextInt();

            if (input == 1) {
                addGroupClass(conn);
            } else if (input == 2) {
                removeGroupClass(conn);
            } else if (input == 3) {
                updateClassTime(conn);
            } else if (input == 4) {
                updateClassTrainer(conn);
            } else if (input == 5) {
                printSessions(conn);
            } else if (input == 6) {
                break;
            } else {
                System.out.println("Invalid Input!");
            }
        }
    }

    private static void addGroupClass(Connection conn)
    {
        int trainerID;
        String sessionName;
        String type;
        String date;
        String startTime;
        String endTime;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        System.out.print("Enter session name: ");
        sessionName = myObj.nextLine();

        System.out.print("Enter session type: ");
        type = myObj.nextLine();

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

        printAvailableTrainers(conn, sqlDate, sqlStartTime, sqlEndTime);
        System.out.print("Enter available trainer ID: ");
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
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printAvailableTrainers(Connection conn, java.sql.Date date, java.sql.Time startTime, java.sql.Time endTime)
    {
        try {
            String sql = "SELECT * FROM Availability WHERE date =? AND startTime <=?  AND endTime >=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDate(1, date);
            statement.setTime(2, startTime);
            statement.setTime(3, endTime);

            ResultSet resultSet = statement.executeQuery();

            String headerFormat = "%-20s %-20s %-20s %-20s %n";
            String rowFormat = "%-20s %-20s %-20s %-20s %n";

            System.out.println("Trainer(s) Available: ");
            System.out.printf(headerFormat, "Trainer ID", "Trainer First Name", "Trainer Last Name", "Trainer Sex");

            while(resultSet.next())
            {
                int trainerID = resultSet.getInt("trainerID");

                sql = "SELECT * FROM Trainer WHERE trainerID =?";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, trainerID);

                resultSet = statement.executeQuery();

                while(resultSet.next())
                {
                    String printTrainerID = resultSet.getString("trainerID");
                    String fName = resultSet.getString("fName");
                    String lName = resultSet.getString("lName");
                    String sex = resultSet.getString("sex");

                    printTrainerID = printTrainerID.length() > 20 ? printTrainerID.substring(0, 10) + "..." : printTrainerID;
                    fName = fName.length() > 20 ? fName.substring(0, 10) + "..." : fName;
                    lName = lName.length() > 20 ? lName.substring(0, 15) + "..." : lName;
                    sex = sex.length() > 20 ? sex.substring(0, 15) + "..." : sex;

                    System.out.printf(rowFormat, printTrainerID, fName, lName, sex);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeGroupClass(Connection conn)
    {
        int sessionID;
        int trainerID;
        Date date;
        Time startTime;
        Time endTime;

        Scanner myObj = new Scanner(System.in);

        printSessions(conn);
        System.out.print("Which session (session ID) would you like to remove? ");

        sessionID = Integer.parseInt(myObj.nextLine());

        try
        {
            String sql = "SELECT * FROM TrainingSessions WHERE sessionID =?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, sessionID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                date = resultSet.getDate("date");
                startTime = resultSet.getTime("startTime");
                endTime = resultSet.getTime("endTime");
                trainerID = resultSet.getInt("trainerID");

                trainer.addTrainerAvailability(conn, date, startTime, endTime, trainerID);
            }

            sql = "DELETE FROM SessionParticipants WHERE sessionID = ?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, sessionID);
            int rowsRemoved = statement.executeUpdate();
            if (rowsRemoved > 0) {
                statement.close();
            }

            sql = "DELETE FROM TrainingSessions WHERE sessionID =?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, sessionID);
            rowsRemoved = statement.executeUpdate();
            if (rowsRemoved > 0) {
                statement.close();
                System.out.println("Remove session with id:" + sessionID + " was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void printSessions(Connection conn)
    {
        try {
            String sql = "SELECT * FROM Trainer " +
                    "INNER JOIN TrainingSessions ON Trainer.trainerID = TrainingSessions.trainerID ";
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

    private static void updateClassTime(Connection conn)
    {
        int sessionID;
        String startTime;
        String endTime;
        Time oldStartTime;
        Time oldEndTime;
        int trainerID;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        printSessions(conn);
        System.out.print("Which session (session ID) would you like to modify? ");
        sessionID = Integer.parseInt(myObj.nextLine());

        System.out.print("What would you like to change the start time to? (hh:mm) ");
        startTime = myObj.nextLine();

        System.out.print("What would you like to change the end time to? (hh:mm) ");
        endTime = myObj.nextLine();

        java.util.Date parsedUtilStartTime = null;
        java.util.Date parsedUtilEndTime = null;
        try {
            parsedUtilStartTime = timeFormat.parse(startTime);
            parsedUtilEndTime = timeFormat.parse(endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        java.sql.Time sqlStartTime = new java.sql.Time(parsedUtilStartTime.getTime());
        java.sql.Time sqlEndTime = new java.sql.Time(parsedUtilEndTime.getTime());

        try
        {
            String sql = "SELECT * FROM TrainingSessions WHERE sessionID =?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, sessionID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                oldStartTime = resultSet.getTime("startTime");
                oldEndTime = resultSet.getTime("endTime");
                trainerID = resultSet.getInt("trainerID");

                trainer.modifyTrainerAvailability(conn, oldStartTime, oldEndTime, sqlStartTime, sqlEndTime, trainerID);
            }

            sql = "UPDATE TrainingSessions SET startTime=?, endTime =? WHERE sessionID=?";
            statement = conn.prepareStatement(sql);
            statement.setTime(1, sqlStartTime);
            statement.setTime(2, sqlEndTime);
            statement.setInt(3, sessionID);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statement.close();
                System.out.println("Update was successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateClassTrainer(Connection conn)
    {
        int sessionID;
        Time startTime;
        Time endTime;
        Date date;
        int trainerID;
        int oldTrainerID;

        Scanner myObj = new Scanner(System.in);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        printSessions(conn);
        System.out.print("Which session (session ID) would you like to modify? ");
        sessionID = Integer.parseInt(myObj.nextLine());

        try
        {
            String sql = "SELECT * FROM TrainingSessions WHERE sessionID =?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, sessionID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                startTime = resultSet.getTime("startTime");
                endTime = resultSet.getTime("endTime");
                date = resultSet.getDate("date");
                oldTrainerID = resultSet.getInt("trainerID");

                sql = "SELECT COUNT(*) AS rowcount FROM Availability WHERE date =? AND startTime <=?  AND endTime >=?";
                statement = null;
                try {
                    statement = conn.prepareStatement(sql);
                    statement.setDate(1, date);
                    statement.setTime(2, startTime);
                    statement.setTime(3, endTime);
                    resultSet = statement.executeQuery();

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

                printAvailableTrainers(conn, date, startTime, endTime);

                System.out.print("Please select a new trainer: ");
                trainerID = Integer.parseInt(myObj.nextLine());

                sql = "UPDATE TrainingSessions SET trainerID =? WHERE sessionID=?";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, trainerID);
                statement.setInt(2, sessionID);
                int rowsInserted = statement.executeUpdate();

                if(rowsInserted > 0)
                {
                    sql = "SELECT * FROM Availability WHERE date =? AND startTime <=?  AND endTime >=?";
                    statement = conn.prepareStatement(sql);
                    statement.setDate(1, date);
                    statement.setTime(2, startTime);
                    statement.setTime(3, endTime);

                    resultSet = statement.executeQuery();

                    if(resultSet.next()) {
                        trainer.removeTrainerAvailability(conn, trainerID, date, resultSet.getTime("startTime"),
                                resultSet.getTime("endTime"), endTime, startTime);
                    }

                    trainer.addTrainerAvailability(conn, date, startTime, endTime, oldTrainerID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
