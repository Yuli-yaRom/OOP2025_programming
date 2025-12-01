package people;

public class Doctor extends Person {
    private int doctorId;
    private String specialization;


    public Doctor(String name, String surname, int phone, String specialization, int doctorId, String email) {
        super(name, surname, phone, email);
        this.doctorId = doctorId;
        this.specialization = specialization;
    }


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {

        this.doctorId = doctorId;
    }

    public String getSpecialization() {

        return specialization;
    }

    public void setSpecialization(String specialization) {

        this.specialization = specialization;
    }
}
