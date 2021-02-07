package preproject.backend.handlers;

import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginHandler {
    Connection connect;

    public LoginHandler(Connection connect) {
        this.connect = connect;
    }

    /**
     * Checks if the user exists in the database through sql query.
     *
     * @param emailInput email to find
     * @return row data of email found
     * @throws SQLException
     */
    private Optional<ResultSet> findUserByEmail(String emailInput) throws SQLException {
        PreparedStatement statement = connect
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
     * @throws SQLException
     */
    private boolean verifyUserAuthentication(PasswordAuthentication pwdAuth, ResultSet userRepo) throws SQLException {
        String hash = userRepo.getString(3);
        String salt = userRepo.getString(4);

        return new PasswordHandler().verifyPassword(String.valueOf(pwdAuth.getPassword()), hash, salt);
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
     * @throws SQLException
     */
    public Optional<ResultSet> exportUserRepo(PasswordAuthentication pwdAuth) throws SQLException {
        Optional<ResultSet> userRepo = findUserByEmail(pwdAuth.getUserName());

        if (userRepo.isPresent()) {
            if (verifyUserAuthentication(pwdAuth, userRepo.get())) {
                return userRepo;
            }
        }

        return Optional.empty();
    }
}
