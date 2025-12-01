package hospital;

import people.Doctor;


import java.time.LocalDateTime;

public class MedicalRecord {
    private final LocalDateTime date;
    private final Doctor doctor;
    private final String description;

    public MedicalRecord(Doctor doctor, String description) {
        this.date = LocalDateTime.now();
        this.doctor = doctor;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Record on " + date + " by " + doctor.getName() + ": " + description;
    }
}


