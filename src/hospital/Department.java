package hospital;

import patterns.creational.factory.StaffFactory;
import people.Doctor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {
    private String name;
    private List<Doctor> doctors;

    public Department(String name, List<String> Doctors, Boolean active) {
        this.name = name;
        this.doctors = new ArrayList<>();
    }

    public void addDoctor(int id, String name, String phoneNumber, String email, String specialization) {
        Doctor newDoctor = (Doctor) StaffFactory.createStaff("doctor", id, name, phoneNumber, email, specialization);
        doctors.add(newDoctor);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Doctor> getDoctors() {
        return Collections.unmodifiableList((doctors));
    }
}
