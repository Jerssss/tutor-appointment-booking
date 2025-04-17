package client.admin.controller;

import client.admin.model.AdminStudentModel;
import shared.classes.Student;

public class AdminCreateStudentController {
    private final AdminStudentModel model;
    public AdminCreateStudentController(){
        this.model = new AdminStudentModel();
    }

    public boolean addNewStudent(String firstName, String lastName, long phoneNumber, String email, String academicLevel) {
        Student newStudent = new Student(firstName, lastName, phoneNumber, email, "Student", 0.0, academicLevel);
        return model.addNewStudent(newStudent);
    }
}
