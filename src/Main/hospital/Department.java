package Main.hospital;
// Клас відділів лікарні
import Main.patterns.creational.factory.StaffFactory;
import Main.people.Doctor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {
    private String name;
    private final List<Doctor> doctors;

    public Department(String name) {
        this.name = name;
        this.doctors = new ArrayList<>();
    }

    public void addDoctor(int id, String name, String surname, String phone, String specialization, String email) {
        Doctor newDoctor = (Doctor) StaffFactory.createStaff(id, "doctor", name, surname, phone, specialization, email);
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
