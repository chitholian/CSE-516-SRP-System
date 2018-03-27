package cse.cu.srpsystem.logics;

import java.net.ConnectException;

import cse.cu.srpsystem.data.CredentialModel;
import cse.cu.srpsystem.data.RemoteDataHandler;
import cse.cu.srpsystem.entities.User;

public class AuthLogic {
    private User user = new User(0, null);

    public User authenticate(String email, String role, String pass) throws Exception {
        return user = RemoteDataHandler.getInstance().loginUser(new CredentialModel(email, pass, role));
    }

    public User getUser() {
        return user;
    }

    public void logoutUser(User user) {
        this.user = new User(0, null);
    }

    private static class Helper {
        private static final AuthLogic logic = new AuthLogic();
    }

    public static AuthLogic getInstance() {
        return Helper.logic;
    }

    private AuthLogic() {
    }
}
