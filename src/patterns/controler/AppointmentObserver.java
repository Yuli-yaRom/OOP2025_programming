package controler;

import appointment.Appointment;

/**
 * Observer Pattern - interface
 * Дозволяє об'єктам підписуватися на події та отримувати сповіщення
 */
public interface AppointmentObserver {
    void update(Appointment appointment, String eventType);
}
