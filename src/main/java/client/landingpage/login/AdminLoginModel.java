package client.landingpage.login;

import shared.interfaces.AuthService;

public class AdminLoginModel {
    private final AuthService authService;


    public AdminLoginModel(AuthService authService) {
        this.authService = authService;
    }
}
