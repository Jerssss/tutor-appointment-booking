package client.student.model;

import shared.classes.*;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingModel {
    private final StudentService studentService;
    private int currentStudentId;

    public CreateBookingModel(StudentService studentService) {
        this.studentService = studentService;
    }

}