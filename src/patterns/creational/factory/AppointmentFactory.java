package factory;

import appointment.*;
import people.Doctor;
import people.Patient;
import singleton.;
import singleton.RoomType;
import hospital.RoomRegistry;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory Pattern
 * Інкапсулює логіку створення різних типів appointments
 * Приховує складність створення об'єктів від клієнта
 */
public class AppointmentFactory {
    private final RoomRegistry roomRegistry;

    public AppointmentFactory(RoomRegistry roomRegistry) {
        this.roomRegistry = roomRegistry;
    }

    /**
     * Factory Method для створення offline appointments
     */
    public Appointment createOfflineAppointment(Doctor doctor, Patient patient,
                                                LocalDateTime dateTime)
            throws Exception {
        Room room = roomRegistry.findAvailableRoom(RoomType.CONSULTATION);
        if (room == null) {
            throw new Exception("No available rooms for offline appointment");
        }

        roomRegistry.reserveRoom(room.getRoomNumber());

        return new Appointment.Builder()
                .withDoctor(doctor)
                .withPatient(patient)
                .withDateTime(dateTime)
                .withType(AppointmentType.OFFLINE)
                .withRoomNumber(room.getRoomNumber())
                .build();
    }

    /**
     * Factory Method для створення online appointments
     */
    public Appointment createOnlineAppointment(Doctor doctor, Patient patient,
                                               LocalDateTime dateTime) {
        String meetingLink = generateMeetingLink(doctor, patient);

        return new Appointment.Builder()
                .withDoctor(doctor)
                .withPatient(patient)
                .withDateTime(dateTime)
                .withType(AppointmentType.ONLINE)
                .withMeetingLink(meetingLink)
                .build();
    }

    /**
     * Factory Method для створення urgent appointments
     */
    public Appointment createUrgentAppointment(Doctor doctor, Patient patient,
                                               LocalDateTime dateTime)
            throws Exception {
        Room room = roomRegistry.findAvailableRoom(RoomType.EMERGENCY);
        if (room == null) {
            throw new Exception("No emergency rooms available");
        }

        roomRegistry.reserveRoom(room.getRoomNumber());

        return new Appointment.Builder()
                .withDoctor(doctor)
                .withPatient(patient)
                .withDateTime(dateTime)
                .withType(AppointmentType.URGENT)
                .withRoomNumber(room.getRoomNumber())
                .withPriority(true)
                .build();
    }

    private String generateMeetingLink(Doctor doctor, Patient patient) {
        String sessionId = UUID.randomUUID().toString().substring(0, 8);
        return String.format("https://telemedicine.hospital.com/session/%s-%s-%s",
                doctor.getSurname().toLowerCase(),
                patient.getSurname().toLowerCase(),
                sessionId);
    }
}
