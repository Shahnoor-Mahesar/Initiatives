package my_package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:database.db";

    // Establish a connection to the database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Unable to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    // Initialize database tables if they don't exist
    public static void initializeDatabase() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();

            // Create users table if it doesn't exist
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, " +
                    "date_of_birth DATE, email TEXT, phone_number TEXT, address TEXT, password TEXT)";
            statement.execute(createUserTableSQL);

            // Create initiatives table if it doesn't exist
            String createInitiativesTableSQL = "CREATE TABLE IF NOT EXISTS initiatives ("
                    + "id INTEGER PRIMARY KEY,"
                    + "name TEXT,"
                    + "date DATE,"
                    + "time TIME,"
                    + "status TEXT,"
                    + "user_id INTEGER,"
                    + "FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")";
            statement.execute(createInitiativesTableSQL);

            // Create joined_initiatives table if it doesn't exist
            String createJoinedInitiativesTableSQL = "CREATE TABLE IF NOT EXISTS joined_initiatives ("
                    + "user_id INTEGER,"
                    + "initiative_id INTEGER,"
                    + "FOREIGN KEY (user_id) REFERENCES users(id),"
                    + "FOREIGN KEY (initiative_id) REFERENCES initiatives(id)"
                    + ")";
            statement.execute(createJoinedInitiativesTableSQL);

            // Add more CREATE TABLE statements for other tables

            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.out.println("Error initializing the database");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DB_URL);
            String sql = "INSERT INTO users (name, date_of_birth, email, phone_number, address, password) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.name);
            preparedStatement.setString(2, user.dateOfBirth);
            preparedStatement.setString(3, user.email);
            preparedStatement.setString(4, user.phoneNumber);
            preparedStatement.setString(5, user.address);
            preparedStatement.setString(6, user.password);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int userLogin(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL);
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addNewInitiative(Initiative newInitiative) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            // SQL statement to insert a new initiative
            String insertInitiativeSQL = "INSERT INTO initiatives (name, date, time, status, user_id) VALUES (?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertInitiativeSQL);

            // Set the parameters for the PreparedStatement
            preparedStatement.setString(1, newInitiative.name);
            preparedStatement.setString(2, newInitiative.date);
            preparedStatement.setString(3, newInitiative.time);
            preparedStatement.setString(4, newInitiative.status);
            preparedStatement.setInt(5, newInitiative.userId);

            // Execute the SQL statement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[][] getAllUserInitiatives(int userId) {
        List<String[]> initiativesData = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // SQL statement to retrieve all initiatives for a specific user
            String selectInitiativesSQL = "SELECT * FROM initiatives WHERE user_id = ?";

            preparedStatement = connection.prepareStatement(selectInitiativesSQL);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");

                String[] initiativeData = { String.valueOf(id), name, date, time, status };
                initiativesData.add(initiativeData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Convert the List to a two-dimensional array
        String[][] initiativesArray = new String[initiativesData.size()][5];
        for (int i = 0; i < initiativesData.size(); i++) {
            initiativesArray[i] = initiativesData.get(i);
        }

        return initiativesArray;
    }

    public static String[][] getAvailableInitiativesForUser(int userId) {
        List<String[]> availableInitiatives = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // SQL statement to retrieve available initiatives for a specific user
            String selectInitiativesSQL = "SELECT i.id, i.name, i.date, i.time, i.status " +
                    "FROM initiatives i " +
                    "LEFT JOIN joined_initiatives ji ON i.id = ji.initiative_id AND ji.user_id = ? " +
                    "WHERE i.status = 'Active' AND i.user_id <> ? AND ji.user_id IS NULL";

            preparedStatement = connection.prepareStatement(selectInitiativesSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");

                String[] initiativeData = { String.valueOf(id), name, date, time, status };
                availableInitiatives.add(initiativeData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Convert List<String[]> to a two-dimensional array
        String[][] resultArray = new String[availableInitiatives.size()][];
        resultArray = availableInitiatives.toArray(resultArray);

        return resultArray;
    }

    public static void joinInitiative(int userId, int initiativeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            // SQL statement to insert a new entry in the joined_initiatives table
            String joinInitiativeSQL = "INSERT INTO joined_initiatives (user_id, initiative_id) VALUES (?, ?)";

            preparedStatement = connection.prepareStatement(joinInitiativeSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, initiativeId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[][] getAllUsers() {
        List<String[]> userList = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();

            String selectUsersSQL = "SELECT * FROM users";
            resultSet = statement.executeQuery(selectUsersSQL);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateOfBirth = resultSet.getString("date_of_birth");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phone_number");
                String address = resultSet.getString("address");

                String[] userData = { String.valueOf(id), name, dateOfBirth, email, phoneNumber, address };
                userList.add(userData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userList.toArray(new String[0][0]);
    }

    public static String[][] getAllInitiatives() {
        List<String[]> initiativesData = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // SQL statement to retrieve all initiatives with user names
            String selectInitiativesSQL = "SELECT initiatives.id, initiatives.name, initiatives.date, " +
                    "initiatives.time, initiatives.status, users.name as user_name " +
                    "FROM initiatives " +
                    "JOIN users ON initiatives.user_id = users.id";

            preparedStatement = connection.prepareStatement(selectInitiativesSQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");
                String userName = resultSet.getString("user_name");

                String[] initiativeData = { String.valueOf(id), userName, name, date, time, status };
                initiativesData.add(initiativeData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Convert the List to a two-dimensional array
        String[][] initiativesArray = new String[initiativesData.size()][6];
        for (int i = 0; i < initiativesData.size(); i++) {
            initiativesArray[i] = initiativesData.get(i);
        }

        return initiativesArray;
    }

    public static void approveInitiative(int initiativeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            // SQL statement to update the status of the initiative to "Active"
            String updateInitiativeStatusSQL = "UPDATE initiatives SET status = 'Active' WHERE id = ?";

            preparedStatement = connection.prepareStatement(updateInitiativeStatusSQL);
            preparedStatement.setInt(1, initiativeId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
