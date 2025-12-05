package Main.people;

public class Doctor extends Person {
    private String specialization;


    public Doctor(int id, String name, String surname, String phone, String specialization, String email) {
        super(id, name, surname, phone, email);
        this.specialization = specialization;
    }


    public String getSpecialization() {

        return specialization;
    }

    public void setSpecialization(String specialization) {

        this.specialization = specialization;
    }
}
