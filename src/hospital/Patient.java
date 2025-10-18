package hospital;

import java.util.List;

public class Patient extends Person {
    private final List<MedicalRecordEntry> medicalRecords;

    public Patient(String name, String surname, int phone, List<MedicalRecordEntry> medicalRecords) {
        super(name, surname, phone);
        this.medicalRecords = medicalRecords;
    }
    public List<MedicalRecordEntry> getMedicalRecords() {
        return medicalRecords;
    }
}
