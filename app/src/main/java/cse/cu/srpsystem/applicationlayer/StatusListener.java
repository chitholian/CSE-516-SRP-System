package cse.cu.srpsystem.applicationlayer;

public interface StatusListener {
    void listen(Status status);

    enum Status {
        SUCCESSFUL, ERR_CONNECTION_FAILED, ERR_CONNECTION_REFUSED, ERR_INCORRECT_CREDENTIAL, ERR_PERMISSION_DENIED, UNKNOWN_ERROR, ERR_INVALID_OPTION
    }
}
