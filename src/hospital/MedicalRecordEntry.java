package hospital;

import people.Doctor;
import people.Patient;

public class MedicalRecordEntry {
    private final Patient patient;
    private final Doctor doctor;
    private final int date;
    private final String diagnosis;
    private String notes;

    public MedicalRecordEntry(Patient patient, Doctor doctor, int date, String diagnosis, String notes) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.diagnosis = diagnosis;
        this.notes = notes;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public int getDate() {
        return date;
    }
    public String getDiagnosis() {
        return diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
