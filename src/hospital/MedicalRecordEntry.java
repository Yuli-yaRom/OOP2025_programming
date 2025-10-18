package hospital;

public class MedicalRecordEntry {
    private final int date;
    private final String diagnosis;
    private String notes;

    public MedicalRecordEntry(int date, String diagnosis, String notes) {
        this.date = date;
        this.diagnosis = diagnosis;
        this.notes = notes;
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
