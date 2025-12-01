package service;

import appointment.*;
import patterns.singleton.RoomRegistry;
import people.*;
import exceptions.*;
import factory.AppointmentFactory;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import appointment.AppointmentType;

/**
 * Controller Pattern (GRASP)
 * Координує та делегує роботу між різними компонентами системи
 * Не виконує бізнес-логіку безпосередньо, а керує нею
 */
public class AppointmentService {
    private final List<Appointment> appointments;
    private final RoomRegistry roomRegistry;
    private final AppointmentFactory appointmentFactory;
    private final List<patterns.controler.AppointmentObserver> observers;

    public AppointmentService(RoomRegistry roomRegistry) {
        this.appointments = new ArrayList<>();
        this.roomRegistry = roomRegistry;
        this.appointmentFactory = new AppointmentFactory(roomRegistry);
        this.observers = new ArrayList<>();
    }

    /**
     * Координує процес створення online appointment
     */
    public Appointment bookOnlineAppointment(Doctor doctor, Patient patient,
                                             LocalDateTime dateTime)
            throws AppointmentBookingException {
        validateAppointmentRequest(doctor, patient, dateTime);

        try {
            Appointment appointment = appointmentFactory.createOnlineAppointment(
                    doctor, patient, dateTime
            );

            registerAppointment(appointment);
            notifyObservers(appointment, "CREATED");

            return appointment;
        } catch (Exception e) {
            throw new AppointmentBookingException("Failed to create online appointment: " +
                    e.getMessage());
        }
    }

    /**
     * Координує процес створення offline appointment
     */
    public Appointment bookOfflineAppointment(Doctor doctor, Patient patient,
                                              LocalDateTime dateTime)
            throws AppointmentBookingException {
        validateAppointmentRequest(doctor, patient, dateTime);

        try {
            Appointment appointment = appointmentFactory.createOfflineAppointment(
                    doctor, patient, dateTime
            );

            registerAppointment(appointment);
            notifyObservers(appointment, "CREATED");

            return appointment;
        } catch (Exception e) {
            throw new AppointmentBookingException("Failed to create offline appointment: " +
                    e.getMessage());
        }
    }

    /**
     * Координує процес створення urgent appointment
     */
    public Appointment bookUrgentAppointment(Doctor doctor, Patient patient,
                                             LocalDateTime dateTime)
            throws AppointmentBookingException {
        validateAppointmentRequest(doctor, patient, dateTime);

        try {
            Appointment appointment = appointmentFactory.createUrgentAppointment(
                    doctor, patient, dateTime
            );

            registerAppointment(appointment);
            notifyObservers(appointment, "CREATED");

            return appointment;
        } catch (Exception e) {
            throw new AppointmentBookingException("Failed to create urgent appointment: " +
                    e.getMessage());
        }
    }

    /**
     * Координує процес скасування appointment
     */
    public void cancelAppointment(Appointment appointment)
            throws AppointmentBookingException {
        if (appointment == null) {
            throw new AppointmentBookingException("Appointment cannot be null");
        }

        if (!appointments.contains(appointment)) {
            throw new AppointmentBookingException("Appointment not found");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentBookingException(
                    "Cannot cancel completed appointment");
        }

        appointment.cancel();

        // Звільнити кімнату якщо offline
        if (appointment.getType() == AppointmentType.OFFLINE &&
                appointment.getRoomNumber() != null) {
            roomRegistry.releaseRoom(appointment.getRoomNumber());
        }

        notifyObservers(appointment, "CANCELLED");
    }

    /**
     * Координує процес перенесення appointment
     */
    public void rescheduleAppointment(Appointment appointment,
                                      LocalDateTime newDateTime)
            throws AppointmentBookingException {
        if (appointment == null) {
            throw new AppointmentBookingException("Appointment cannot be null");
        }

        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new AppointmentBookingException.InvalidTimeException(
                    "Cannot reschedule to past time");
        }

        if (isDoctorBusy(appointment.getDoctor(), newDateTime, appointment)) {
            throw new AppointmentBookingException.TimeSlotOccupiedException(
                    "Doctor is not available at this time");
        }

        appointment.reschedule(newDateTime);
        notifyObservers(appointment, "RESCHEDULED");
    }

    /**
     * Координує процес завершення appointment
     */
    public void completeAppointment(Appointment appointment, String notes)
            throws AppointmentBookingException {
        if (appointment == null) {
            throw new AppointmentBookingException("Appointment cannot be null");
        }

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED &&
                appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new AppointmentBookingException(
                    "Can only complete scheduled or in-progress appointments");
        }

        appointment.setNotes(notes);
        appointment.complete();

        // Звільнити кімнату
        if (appointment.getType() == AppointmentType.OFFLINE &&
                appointment.getRoomNumber() != null) {
            roomRegistry.releaseRoom(appointment.getRoomNumber());
        }

        notifyObservers(appointment, "COMPLETED");
    }

    /**
     * Валідація запиту на створення appointment
     */
    private void validateAppointmentRequest(Doctor doctor, Patient patient,
                                            LocalDateTime dateTime)
            throws AppointmentBookingException {
        if (doctor == null) {
            throw new AppointmentBookingException("Doctor cannot be null");
        }
        if (patient == null) {
            throw new AppointmentBookingException("Patient cannot be null");
        }
        if (dateTime == null) {
            throw new AppointmentBookingException("DateTime cannot be null");
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new AppointmentBookingException.InvalidTimeException(
                    "Cannot book appointment in the past");
        }
        if (isDoctorBusy(doctor, dateTime, null)) {
            throw new AppointmentBookingException.TimeSlotOccupiedException(
                    "Doctor is not available at this time");
        }
    }

    /**
     * Перевірка доступності лікаря
     */
    private boolean isDoctorBusy(Doctor doctor, LocalDateTime dateTime,
                                 Appointment excludeAppointment) {
        return appointments.stream()
                .filter(app -> !app.equals(excludeAppointment))
                .filter(app -> app.getDoctor().equals(doctor))
                .filter(app -> app.getStatus() == AppointmentStatus.SCHEDULED ||
                        app.getStatus() == AppointmentStatus.IN_PROGRESS)
                .anyMatch(app -> {
                    long minutesDiff = Math.abs(
                            Duration.between(app.getAppointmentDateTime(), dateTime)
                                    .toMinutes()
                    );
                    return minutesDiff < 30; // 30 хвилин на прийом
                });
    }

    /**
     * Реєстрація appointment у системі
     */
    private void registerAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.getPatient().addAppointment(appointment);
    }

    // Observer pattern methods
    public void addObserver(AppointmentObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(AppointmentObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Appointment appointment, String eventType) {
        for (AppointmentObserver observer : observers) {
            observer.update(appointment, eventType);
        }
    }

    // Query methods
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appointments.stream()
                .filter(app -> app.getPatient().equals(patient))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointments.stream()
                .filter(app -> app.getDoctor().equals(doctor))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointments.stream()
                .filter(app -> app.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<LocalDateTime> getAvailableTimeSlots(Doctor doctor,
                                                     LocalDateTime startDate,
                                                     int days) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime current = startDate;
        LocalDateTime end = startDate.plusDays(days);

        while (current.isBefore(end)) {
            // Робочий час: 9:00 - 18:00
            if (current.getHour() >= 9 && current.getHour() < 18) {
                if (!isDoctorBusy(doctor, current, null)) {
                    availableSlots.add(current);
                }
            }
            current = current.plusMinutes(30);
        }

        return availableSlots;
    }
}