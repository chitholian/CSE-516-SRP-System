package cse.cu.srpsystem.data;

public class CredentialModel {
    public String email;
    public String password;
    public String role;

    public CredentialModel(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
