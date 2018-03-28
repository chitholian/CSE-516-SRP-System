package cse.cu.srpsystem.dataaccesslayer;

public class Credentials {
    public String email;
    public String password;
    public String role;

    public Credentials(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
