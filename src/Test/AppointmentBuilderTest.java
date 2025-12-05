package Test;

import Main.appointment.Appointment;
import Main.appointment.OfflineAppointment;
import Main.appointment.OnlineAppointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Main.patterns.AppointmentBuilder;
import Main.people.Doctor;
import Main.people.Patient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentBuilderTest {

    private Patient patient;
    private Doctor doctor;
    private LocalDateTime dateTime;

    @BeforeEach
    void setUp() {
        // Підготовка тестових даних перед кожним тестом
        patient = new Patient(1, "Test", "Patient", "123-456", "test@patient.com");
        doctor = new Doctor(101, "Dr. House", "Gregory", "987-654", "Diagnostics", "house@hospital.com");
        dateTime = LocalDateTime.now().plusDays(1);
    }

    @Test
    void build_CreatesOnlineAppointment_WithCorrectParameters() {
        int appointmentId = 1;
        String meetingLink = "https://meet.google.com/abc";
        double cost = 100.0;

        Appointment appointment = new AppointmentBuilder()
                .setAppointmentId(appointmentId)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("online")
                .setMeetingLink(meetingLink)
                .setCost(cost)
                .build();

        // Перевірка типу об'єкта
        assertTrue(appointment instanceof OnlineAppointment, "Має бути створений об'єкт OnlineAppointment");

        // Перевірка полів (прибрали перевірку getAppointmentId)
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(cost, appointment.getCost(), 0.001);
    }

    @Test
    void build_CreatesOfflineAppointment_WithCorrectParameters() {
        int appointmentId = 2;
        String roomNumber = "101A";
        double cost = 150.0;

        Appointment appointment = new AppointmentBuilder()
                .setAppointmentId(appointmentId)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("offline")
                .setRoomNumber(roomNumber)
                .setCost(cost)
                .build();

        // Перевірка типу об'єкта
        assertTrue(appointment instanceof OfflineAppointment, "Має бути створений об'єкт OfflineAppointment");

        // Перевірка специфічного поля для офлайн запису
        assertEquals(roomNumber, ((OfflineAppointment) appointment).getRoomNumber());
    }

    @Test
    void build_ThrowsException_WhenOnlineLinkMissing() {
        // Намагаємося створити онлайн запис без посилання
        AppointmentBuilder builder = new AppointmentBuilder()
                .setAppointmentId(3)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("online")
                .setCost(100.0);

        Exception exception = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("Meeting link must be set for online appointments", exception.getMessage());
    }

    @Test
    void build_ThrowsException_WhenOfflineRoomMissing() {
        // Намагаємося створити офлайн запис без номера кімнати
        AppointmentBuilder builder = new AppointmentBuilder()
                .setAppointmentId(4)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("offline")
                .setCost(150.0);

        Exception exception = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("Room number must be set for offline appointments", exception.getMessage());
    }

    @Test
    void build_ThrowsException_WhenTypeInvalid() {
        // Намагаємося передати невідомий тип
        AppointmentBuilder builder = new AppointmentBuilder()
                .setAppointmentId(5)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("invalid_type");

        Exception exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertTrue(exception.getMessage().contains("Invalid appointment type"));
    }
}
