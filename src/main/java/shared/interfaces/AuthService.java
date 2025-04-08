package shared.interfaces;

import shared.classes.User;

public interface AuthService {
    User login();
    User logout();
}
