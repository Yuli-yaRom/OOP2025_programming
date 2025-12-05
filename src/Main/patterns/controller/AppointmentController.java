package Main.patterns.controller;

import Main.appointment.Appointment;
import Main.exceptions.AppointmentBookingException;
import Main.exceptions.DoctorNotFoundException;
import Main.exceptions.InvalidInputException;
import Main.exceptions.PatientNotFoundException;
import Main.hospital.Department;
import Main.patterns.HospitalFacade;
import Main.patterns.observer.Observer;
import Main.people.Patient;
import Main.service.PaymentStrategy;
import Main.people.Doctor;

import java.time.LocalDateTime;
import java.util.List;



public class AppointmentController {
    private final HospitalFacade hospitalFacade;

    public AppointmentController() throws InvalidInputException {
        this.hospitalFacade = new HospitalFacade();
    }


    public void addObserver(Observer observer) {
        hospitalFacade.addObserver(observer);
    }


    public Appointment bookAppointment(String type, int appointmentId, Patient patient, Doctor doctor, LocalDateTime dateTime, String details, double cost) throws AppointmentBookingException {
        return hospitalFacade.bookAppointment(type, appointmentId, patient, doctor, dateTime, details, cost);
    }


    public void cancelAppointment(Appointment appointment) {
        hospitalFacade.cancelAppointment(appointment);
    }


    public void rescheduleAppointment(Appointment appointment, LocalDateTime newDateTime, String newRoomNumber) throws AppointmentBookingException {
        hospitalFacade.rescheduleAppointment(appointment, newDateTime, newRoomNumber);
    }


    public String completeAppointment(Appointment appointment, PaymentStrategy paymentStrategy, boolean applyInsurance) {
        return hospitalFacade.completeAppointment(appointment, paymentStrategy, applyInsurance);
    }


    public void recordMedicalHistory(Doctor doctor, Patient patient, String record) {
        hospitalFacade.recordMedicalHistory(doctor, patient, record);
    }


    public Patient findPatientById(int patientId) throws PatientNotFoundException {
        return hospitalFacade.findPatientById(patientId);
    }

    public Doctor findDoctorById(int doctorId) throws DoctorNotFoundException {
        return hospitalFacade.findDoctorById(doctorId);
    }

    public List<Department> getDepartments() {
        return hospitalFacade.getDepartments();
    }

    public List<LocalDateTime> getAvailableTimeSlots() {
        return hospitalFacade.getAvailableTimeSlots();
    }

    public List<String> getAvailableRooms() {
        return hospitalFacade.getAvailableRooms();
    }

    public List<Appointment> getAppointments() {
        return hospitalFacade.getAppointments();
    }

    public void addPatient(Patient patient) {
        hospitalFacade.addPatient(patient);
    }
}

