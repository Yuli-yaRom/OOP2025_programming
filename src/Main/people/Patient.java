package Main.people;

import Main.appointment.Appointment;
import Main.hospital.MedicalRecord;
import java.util.ArrayList;
import java.util.List;

public class Patient extends Person {
    private final List<MedicalRecord> medicalHistory;

    public Patient(int Id, String name, String surname, String phone, String email) {
        super(Id, name, surname, phone, email);
        this.medicalHistory = new ArrayList<>();
    }
    public List<MedicalRecord> getMedicalHistory() {

        return medicalHistory;
    }

    public void addMedicalRecord(MedicalRecord record) {

        this.medicalHistory.add(record);
    }


}
