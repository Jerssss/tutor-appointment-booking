package client.landingpage.login;

import shared.interfaces.AuthService;

public class LoginModel {
    private final AuthService authService;

    public LoginModel(AuthService authService) {
        this.authService = authService;
    }
}
