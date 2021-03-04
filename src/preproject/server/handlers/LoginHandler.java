package preproject.server.handlers;

import preproject.server.Connector;

import java.net.PasswordAuthentication;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginHandler {
    public LoginHandler() {}

    /**
     * Checks if the user exists in the database through sql query.
     *
     * @param emailInput email to find
     * @return row data of email found
     * @throws SQLException error with query
     */
    private Optional<ResultSet> findUserByEmail(String emailInput) throws SQLException {
        PreparedStatement statement = Connector.connect
                .prepareStatement("SELECT * FROM user_acc WHERE user_acc.email = ?");
        statement.setString(1, emailInput);

        return Optional.ofNullable(statement.executeQuery());
    }

    /**
     * Verifies if the given password results to the same hash if verified by the use of {@link PasswordHandler} class.
     *
     * @param pwdAuth  repo for password/username
     * @param userRepo repo for the row data
     * @return correct password authentication
     */
    private boolean verifyUserAuthentication(PasswordAuthentication pwdAuth, ResultSet userRepo) {
        try {
            if (userRepo.next()) {
                String hash = userRepo.getString(3);
                String salt = userRepo.getString(4);

                return new PasswordHandler().verifyPassword(String.valueOf(pwdAuth.getPassword()), hash, salt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the row data if the given {@code userRepo} passed the authentication process.
     *
     * When using this method, the handler will find the given email in the database first. If it doesn't exist
     * it will return an {@code Optional.empty()}. However, if it exists, it will then verify if the password given
     * by the user matches the same password found in the database.
     *
     * @param pwdAuth repo for password/username
     * @return row data
     */
    public Optional<ResultSet> loginUser(PasswordAuthentication pwdAuth) {
        Optional<ResultSet> userRepo;
        try {
            userRepo = findUserByEmail(pwdAuth.getUserName());

            if (userRepo.isPresent()) {
                if (verifyUserAuthentication(pwdAuth, userRepo.get())) {
                    return userRepo;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
