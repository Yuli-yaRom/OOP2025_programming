package hospital;

import java.time.LocalDateTime;

public class Appointment implements GetAppointment {
    private final Doctor doctor;
    private final Patient patient;
    private final LocalDateTime appointmentDateTime;
    private AppointmentStatus status;

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentDateTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }


    @Override
    public boolean set_appointment(int date, int time) {
        System.out.println(" ");
        return false;
    }

    @Override
    public boolean cancel() {
        if (this.status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.CANCELED;
            return true;
        }
        return false;
    }

    @Override
    public boolean complete() {
        if (this.status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.COMPLETED;
            return true;
        }
        return false;
    }
}
