package cse.cu.srpsystem.entities;

import java.io.IOException;

import cse.cu.srpsystem.data.RemoteDataHandler;
import cse.cu.srpsystem.logics.AuthLogic;
import cse.cu.srpsystem.logics.Exceptions;
import cse.cu.srpsystem.logics.StatusListener;

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
            User user = AuthLogic.getInstance().authenticate(email, role, password);
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
        AuthLogic.getInstance().logoutUser(this);
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
