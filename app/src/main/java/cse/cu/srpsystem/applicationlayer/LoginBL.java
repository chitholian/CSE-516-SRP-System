package cse.cu.srpsystem.applicationlayer;

import cse.cu.srpsystem.dataaccesslayer.Credentials;
import cse.cu.srpsystem.dataaccesslayer.RemoteDAL;
import cse.cu.srpsystem.entities.User;

public class LoginBL {
    private User user = new User(0, null);

    public User authenticate(String email, String role, String pass) throws Exception {
        return user = RemoteDAL.getInstance().loginUser(new Credentials(email, pass, role));
    }

    public User getUser() {
        return user;
    }

    public void logoutUser(User user) {
        this.user = new User(0, null);
    }

    private static class Helper {
        private static final LoginBL logic = new LoginBL();
    }

    public static LoginBL getInstance() {
        return Helper.logic;
    }

    private LoginBL() {
    }
}
