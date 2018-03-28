package cse.cu.srpsystem.entities;

import java.io.IOException;

import cse.cu.srpsystem.applicationlayer.LoginBL;
import cse.cu.srpsystem.applicationlayer.Exceptions;
import cse.cu.srpsystem.applicationlayer.StatusListener;

public class User {
    public int id;
    private int user_id;
    private String role;
    public String name;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public int getUserId() {
        return user_id;
    }

    public StatusListener.Status login(String email, String role, String password) {
        try {
            User user = LoginBL.getInstance().authenticate(email, role, password);
            if (user != null && user.id != 0) {
                user.role = role;
                user.user_id = user.id;
                return StatusListener.Status.SUCCESSFUL;
            }
            return StatusListener.Status.ERR_INCORRECT_CREDENTIAL;
        } catch (Exceptions.InvalidCredentialException e) {
            return StatusListener.Status.ERR_INCORRECT_CREDENTIAL;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusListener.Status.ERR_CONNECTION_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StatusListener.Status.UNKNOWN_ERROR;
    }

    public void logout() {
        LoginBL.getInstance().logoutUser(this);
        role = null;
        user_id = id = 0;
    }

    public boolean isLoggedIn() {
        return user_id != 0 && role != null;
    }

    @Override
    public String toString() {
        return name;
    }
}
