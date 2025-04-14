package client.landingpage.login;


import javafx.event.ActionEvent;

public class LoginController {
    private final LoginView loginView; // The associated view for the login page
    private final LoginModel loginModel; // The associated model for the login page


    public LoginController(LoginView loginView, LoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;


        this.loginView.setActionSignInButton(this::handleSignIn);
        this.loginView.setActionSignUpButton(this::redirectToSignUp);
    }


    private void handleSignIn(ActionEvent event) {


    }


    private void redirectToSignUp(ActionEvent event) {


    }
}
