package preproject.backend.handlers;

import preproject.backend.Connector;

import java.net.PasswordAuthentication;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RegisterHandler{
    public RegisterHandler() {}

    public boolean checkIfUserExists(String email) {
        Optional<ResultSet> resultSet;
        try (PreparedStatement statement = Connector.connect
                .prepareStatement("SELECT * FROM user_acc WHERE email=? LIMIT 1")){

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
                            "(email, pwd_hash, salt, user_fname, user_lname, verified, is_admin)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")){

                statement.setString(1, email);
                statement.setString(2, hashPassword.get());
                statement.setString(3, salt);
                statement.setString(4, fName);
                statement.setString(5, lName);
                statement.setBoolean(6, false);
                statement.setBoolean(7, false);

                statement.executeUpdate();

                return true; // all succeeds
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
