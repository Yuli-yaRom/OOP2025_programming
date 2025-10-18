package hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Doctor extends Person {
    private String specialization;
    private final List<Appointment> schedule;

    public Doctor(String name, String surname, int phone, String specialization) {
        super(name, surname, phone);
        this.specialization = specialization;
        this.schedule = new ArrayList<>();

    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public List<Appointment> getSchedule() {
        return schedule;
    }
    public void addAppointment(Appointment appointment) {
        this.schedule.add(appointment);
    }
}
