package people;

import appointment.Appointment;
import hospital.MedicalRecordEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patient extends Person {
    private final List<MedicalRecordEntry> medicalRecords;
    private final List<Appointment> appointments;

    public Patient(String name, String surname, int phone, List<MedicalRecordEntry> medicalRecords) {
        super(name, surname, phone);
        this.medicalRecords = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }
    public List<MedicalRecordEntry> getMedicalRecords() {
        return Collections.unmodifiableList(medicalRecords);
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public void scheduleAppointment(Appointment appointment) {
       //if slot is available add appointment
    }
}
