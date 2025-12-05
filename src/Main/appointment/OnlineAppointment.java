package Main.appointment;
// Онлайн прийом наслідує прийом
import Main.people.Doctor;
import Main.people.Patient;
import java.time.LocalDateTime;


public class OnlineAppointment extends Appointment {
    private String meetingLink;

    public OnlineAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String meetingLink, double cost) {
        super(appointmentId, patient, doctor, appointmentDateTime, cost);
        this.meetingLink = meetingLink;
    }

    @Override
    public void reschedule(LocalDateTime newDateTime) {

        this.appointmentDateTime = newDateTime;
    }
}


