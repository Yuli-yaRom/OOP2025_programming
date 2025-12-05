package appointment;
//Абстрактний клас, відповідальний за прийоми
import people.Doctor;
import people.Patient;
import java.time.LocalDateTime;


public abstract class Appointment {
    protected int appointmentId;
    protected Patient patient;
    protected Doctor doctor;
    protected LocalDateTime appointmentDateTime;
    protected double cost;
    protected AppointmentStatus status;

    public Appointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, double cost) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.cost = cost;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public double getCost() {

        return cost;
    }

    public Patient getPatient() {

        return patient;
    }

    public Doctor getDoctor() {

        return doctor;
    }

    public AppointmentStatus getStatus() {

        return status;
    }

    public void schedule() {

        this.status = AppointmentStatus.SCHEDULED;
    }

    public void cancel() {

        this.status = AppointmentStatus.CANCELED;
    }

    public void setStatus(AppointmentStatus status) {

        this.status = status;
    }

    public LocalDateTime getAppointmentDateTime() {

        return appointmentDateTime;
    }

    public abstract void reschedule(LocalDateTime newDateTime);
}

