package people;

import appointment.Appointment;
import hospital.MedicalRecordEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Doctor extends Person {
    private String specialization;
    private Integer experience;
    private final List<Appointment> schedule;

    public Doctor(String name, String surname, int phone, String specialization, Integer experience) {
        super(name, surname, phone);
        this.specialization = specialization;
        this.schedule = new ArrayList<>();
        this.experience = experience;

    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public List<Appointment> getSchedule() {
        return Collections.unmodifiableList(schedule);
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getExperience() {
        return experience;
    }

    public void performMedicalCare(MedicalRecordEntry mr) {
    }


}
