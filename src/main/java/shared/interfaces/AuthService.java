package shared.interfaces;

import shared.classes.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthService extends Remote {

    User login(String email, String password) throws RemoteException;

    User logout() throws RemoteException;
}
