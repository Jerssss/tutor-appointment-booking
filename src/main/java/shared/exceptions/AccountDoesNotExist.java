package shared.exceptions;

public class AccountDoesNotExist extends RuntimeException {
    public AccountDoesNotExist(String message) {
        super(message);
    }
}
