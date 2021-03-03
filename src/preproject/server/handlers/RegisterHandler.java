package preproject.server.handlers;

import preproject.server.Connector;

import java.net.PasswordAuthentication;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

public class RegisterHandler {
    public RegisterHandler() {
    }

    public boolean checkIfUserExists(String email) {
        Optional<ResultSet> resultSet;
        try (PreparedStatement statement = Connector.connect
                .prepareStatement("SELECT * FROM user_acc WHERE email=? LIMIT 1")) {

            statement.setString(1, email);

            resultSet = Optional.ofNullable(statement.executeQuery());
            if (resultSet.isPresent())
                if (resultSet.get().next())
                    return resultSet.get().getString("email").equalsIgnoreCase(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // if query fails, simply return false
    }

    public boolean registerUser(PasswordAuthentication userRepo, String fName, String lName) {
        String email = userRepo.getUserName();
        String password = String.valueOf(userRepo.getPassword());

        if (checkIfUserExists(email)) { // if email already exists in the database, do not allow and return false
            return false;
        }

        PasswordHandler pwdHandler = new PasswordHandler();
        Optional<String> hashPassword = pwdHandler.generateHashPassword(password);

        if (hashPassword.isPresent()) {
            String salt = pwdHandler.getSalt();
            System.out.println(salt);

            try (PreparedStatement statement = Connector.connect.prepareStatement("INSERT INTO messenger.user_acc " +
                    "(email, pwd_hash, salt, user_fname, user_lname, verified, is_admin, user_color)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                statement.setString(1, email);
                statement.setString(2, hashPassword.get());
                statement.setString(3, salt);
                statement.setString(4, fName);
                statement.setString(5, lName);
                statement.setBoolean(6, false);
                statement.setBoolean(7, false);
                statement.setString(8, getRandomUserColor());

                statement.executeUpdate();

                return true; // all succeeds
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getRandomUserColor() {
        Random random = new Random();
        switch (random.nextInt(10) + 1) {
            case 1:
                return "#16A085";
            case 2:
                return "#3498DB";
            case 3:
                return "#9B59B6";
            case 4:
                return "#596275";
            case 5:
                return "#E74C3C";
            case 6:
                return "#E67E22";
            case 7:
                return "#F1C40F";
            case 8:
                return "#7F8C8D ";
            case 9:
                return "#2ECC71";
            case 10:
                return "#F17EA6";
        }
        return "#000000";
    }
}
