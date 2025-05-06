package client.admin.controller;

import client.admin.model.AdminTutorModel;
import shared.classes.Tutor;

import java.util.List;

public class AdminCreateTutorController {
    private final AdminTutorModel model;
    public AdminCreateTutorController(){
        this.model = new AdminTutorModel();
    }

    public boolean addNewTutor(String firstName, String lastName, long phoneNumber, String email, String expertise) {
        Tutor newTutor = new Tutor(firstName, lastName, phoneNumber, email, "Tutor", expertise);
        return model.addNewTutor(newTutor);
    }

    public List<String> viewExpertise(){
        return model.expertiseList();
    }
}
