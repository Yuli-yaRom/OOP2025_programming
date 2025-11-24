package appointment;
// Онлайн прийом наслідує прийом
import people.Doctor;
import people.Patient;

import java.time.LocalDateTime;

public class OnlineAppointment extends Appointment {
    private String meetingLink;

    public OnlineAppointment(Doctor doctor, Patient patient, LocalDateTime appointmentDateTime, String meetingLink) {
        super(doctor, patient, appointmentDateTime);
        this.meetingLink = meetingLink;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }
}
