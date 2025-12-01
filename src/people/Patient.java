package people;

import appointment.Appointment;
import hospital.MedicalRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patient extends Person {
    private int patientId;
    private final List<MedicalRecord> medicalRecords;

    public Patient(String name, String surname, int phone, List<MedicalRecord> medicalRecords, String email) {
        super(name, surname, phone, email);
        this.patientId = patientId;
        this.medicalRecords = new ArrayList<>();
    }
    public List<MedicalRecord> getMedicalRecords() {

        return Collections.unmodifiableList(medicalRecords);
    }
    public int getPatientId() {

        return patientId;
    }

    public void setPatientId(int patientId) {

        this.patientId = patientId;
    }

    public void scheduleAppointment(Appointment appointment) {
       //if slot is available add appointment
    }
}
