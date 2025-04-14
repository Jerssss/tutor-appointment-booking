package client.landingpage.login;

import javafx.event.ActionEvent;

public class AdminLoginController {
    private final AdminLoginView adminLoginView; // The associated view for the login page
    private final AdminLoginModel adminLoginModel; // The associated model for the login page


    public AdminLoginController(AdminLoginView adminLoginView, AdminLoginModel adminLoginModel) {
        this.adminLoginView = adminLoginView;
        this.adminLoginModel = adminLoginModel;


        this.adminLoginView.setActionSignInButton(this::handleSignIn);
    }


    private void handleSignIn(ActionEvent event) {


    }
}


