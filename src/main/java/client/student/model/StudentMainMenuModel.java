package client.student.model;

import shared.interfaces.StudentService;

public class StudentMainMenuModel {
        private StudentService studentService;

        public StudentMainMenuModel(StudentService studentService) {
            this.studentService = studentService;
        }
}

