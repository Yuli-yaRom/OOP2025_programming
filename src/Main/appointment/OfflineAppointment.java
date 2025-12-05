package Main.appointment;
// Оффлайн прийом наслідує прийом
import Main.people.Doctor;
import Main.people.Patient;
import java.time.LocalDateTime;

public class OfflineAppointment extends Appointment {
    private String roomNumber;

    public OfflineAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String roomNumber, double cost) {
        super(appointmentId, patient, doctor, appointmentDateTime, cost);
        this.roomNumber = roomNumber;
    }

    @Override
    public void reschedule(LocalDateTime newDateTime) {

        this.appointmentDateTime = newDateTime;
    }

    public String getRoomNumber() {

        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {

        this.roomNumber = roomNumber;
    }
}
