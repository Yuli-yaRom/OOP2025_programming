package appointment;
// Оффлайн прийом наслідує прийом
import people.Doctor;
import people.Patient;

import java.time.LocalDateTime;

public class OfflineAppointment extends Appointment {

    private String roomNumber;

    public OfflineAppointment(Doctor doctor, Patient patient, LocalDateTime appointmentDateTime, String roomNumber) {
        super(doctor, patient, appointmentDateTime);
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}