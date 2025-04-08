package server.services;

import shared.classes.User;
import shared.interfaces.AuthService;

import java.rmi.Remote;

public class AuthServiceImpl implements Remote, AuthService {
    @Override
    public User login() {
        return null;
    }

    @Override
    public User logout() {
        return null;
    }
}
