package server.services;

import shared.classes.User;
import shared.interfaces.AuthService;
import java.io.Serializable;
import java.rmi.Remote;

public class AuthServiceImpl implements Remote, AuthService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID

    @Override
    public User login() {
        return null;
    }

    @Override
    public User logout() {
        return null;
    }
}