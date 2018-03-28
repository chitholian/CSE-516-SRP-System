package cse.cu.srpsystem.applicationlayer;

public class Exceptions {
    public static class InvalidCredentialException extends Exception {
        public InvalidCredentialException(String msg) {
            super(msg);
        }

    }
}
