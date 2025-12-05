package patterns;
import appointment.Appointment;
import appointment.OfflineAppointment;
import appointment.OnlineAppointment;
import people.*;
import java.time.LocalDateTime;

public class AppointmentBuilder {
    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private String type;
    private double cost;
    private String roomNumber;
    private String meetingLink;

    public AppointmentBuilder setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
        return this;
    }

    public AppointmentBuilder setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public AppointmentBuilder setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public AppointmentBuilder setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
        return this;
    }

    public AppointmentBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public AppointmentBuilder setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public AppointmentBuilder setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
        return this;
    }

    public AppointmentBuilder setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public Appointment build() {
        if ("online".equalsIgnoreCase(type)) {
            if (meetingLink == null) {
                throw new IllegalStateException("Meeting link must be set for online appointments");
            }
            return new OnlineAppointment(appointmentId, patient, doctor, appointmentDateTime, meetingLink, cost);
        } else if ("offline".equalsIgnoreCase(type)) {
            if (roomNumber == null) {
                throw new IllegalStateException("Room number must be set for offline appointments");
            }
            return new OfflineAppointment(appointmentId, patient, doctor, appointmentDateTime, roomNumber, cost);
        } else {
            throw new IllegalArgumentException("Invalid appointment type: " + type);
        }
    }
}

