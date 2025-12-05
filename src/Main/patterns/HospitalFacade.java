package Main.patterns;

import Main.appointment.Appointment;
import Main.appointment.AppointmentStatus;
import Main.appointment.OfflineAppointment;
import Main.exceptions.*;
import Main.hospital.Department;
import Main.hospital.MedicalRecord;
import Main.patterns.creational.factory.DepartmentFactory;
import Main.patterns.decorators.AppointmentBilling;
import Main.patterns.decorators.InsuranceDecorator;
import Main.patterns.observer.AppointmentNotifier;
import Main.patterns.observer.Observer;
import Main.patterns.singleton.RoomRegistry;
import Main.people.Doctor;
import Main.people.Patient;
import Main.service.BillingService;
import Main.service.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HospitalFacade {
    private AppointmentNotifier appointmentNotifier;
    private List<Patient> patients;
    private List<Appointment> appointments;
    private Department cardiologyDepartment;
    private Department pediatricsDepartment;
    private List<LocalDateTime> availableTimeSlots;

    public HospitalFacade() throws InvalidInputException {
        this.appointmentNotifier = new AppointmentNotifier();
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.cardiologyDepartment = DepartmentFactory.createDepartment("cardiology");
        this.pediatricsDepartment = DepartmentFactory.createDepartment("pediatrics");
        setupTimeSlots();
    }

    private void setupTimeSlots() {
        availableTimeSlots = new ArrayList<>();
        availableTimeSlots.add(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
        availableTimeSlots.add(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        availableTimeSlots.add(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void addObserver(Observer observer) {
        appointmentNotifier.addObserver(observer);
    }

    public Appointment bookAppointment(String type, int appointmentId, Patient patient, Doctor doctor, LocalDateTime dateTime, String details, double cost) throws AppointmentBookingException {
        if (!isDoctorAvailable(doctor, dateTime)) {
            throw new DoctorUnavailableException("Doctor " + doctor.getName() + " is unavailable at " + dateTime);
        }

        if ("offline".equalsIgnoreCase(type)) {
            return bookOfflineAppointment(appointmentId, patient, doctor, dateTime, details, cost);
        } else if ("online".equalsIgnoreCase(type)) {
            return bookOnlineAppointment(appointmentId, patient, doctor, dateTime, details, cost);
        } else {
            throw new IllegalArgumentException("Invalid appointment type: " + type);
        }
    }

    private Appointment bookOfflineAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime dateTime, String roomNumber, double cost) throws AppointmentBookingException {
        if (!RoomRegistry.getInstance().getAvailableRooms().contains(roomNumber)) {
            throw new AppointmentBookingException("Room " + roomNumber + " is not available.");
        }
        AppointmentBuilder builder = new AppointmentBuilder();
        Appointment appointment = builder.setAppointmentId(appointmentId)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("offline")
                .setRoomNumber(roomNumber)
                .setCost(cost)
                .build();

        bookOfflineAppointmentRoom(roomNumber);

        appointment.schedule();
        appointments.add(appointment);
        return appointment;
    }

    private Appointment bookOnlineAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime dateTime, String meetingLink, double cost) {
        AppointmentBuilder builder = new AppointmentBuilder();
        Appointment appointment = builder.setAppointmentId(appointmentId)
                .setPatient(patient)
                .setDoctor(doctor)
                .setAppointmentDateTime(dateTime)
                .setType("online")
                .setMeetingLink(meetingLink)
                .setCost(cost)
                .build();

        appointment.schedule();
        appointments.add(appointment);
        return appointment;
    }

    private void bookOfflineAppointmentRoom(String roomNumber) {
        RoomRegistry.getInstance().bookRoom(roomNumber);
    }

    public void cancelAppointment(Appointment appointment) {
        appointment.cancel();
        if (appointment instanceof OfflineAppointment) {
            RoomRegistry.getInstance().releaseRoom(((OfflineAppointment) appointment).getRoomNumber());
        }
        availableTimeSlots.add(appointment.getAppointmentDateTime());
        appointmentNotifier.newSlotAvailable();
    }

    public void rescheduleAppointment(Appointment appointment, LocalDateTime newDateTime, String newRoomNumber) throws AppointmentBookingException {
        if (appointment instanceof OfflineAppointment) {
            OfflineAppointment offlineAppointment = (OfflineAppointment) appointment;
            RoomRegistry.getInstance().releaseRoom(offlineAppointment.getRoomNumber());
            if (newRoomNumber != null) {
                if (!RoomRegistry.getInstance().getAvailableRooms().contains(newRoomNumber)) {
                    throw new AppointmentBookingException("New room " + newRoomNumber + " is not available.");
                }
                offlineAppointment.setRoomNumber(newRoomNumber);
                RoomRegistry.getInstance().bookRoom(newRoomNumber);
            }
        }
        availableTimeSlots.add(appointment.getAppointmentDateTime());
        appointment.reschedule(newDateTime);
        availableTimeSlots.remove(newDateTime);
        appointmentNotifier.newSlotAvailable();
    }

    public String completeAppointment(Appointment appointment, PaymentStrategy paymentStrategy, boolean applyInsurance) {
        BillingService billingService = new AppointmentBilling(appointment.getCost());
        if (applyInsurance) {
            billingService = new InsuranceDecorator(billingService);
        }
        double finalCost = billingService.calculateCost();
        String paymentConfirmation = paymentStrategy.pay(finalCost);
        if (appointment instanceof OfflineAppointment) {
            RoomRegistry.getInstance().releaseRoom(((OfflineAppointment) appointment).getRoomNumber());
        }
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return paymentConfirmation;
    }

    public void recordMedicalHistory(Doctor doctor, Patient patient, String record) {
        MedicalRecord newRecord = new MedicalRecord(doctor, record);
        patient.addMedicalRecord(newRecord);
    }

    public Patient findPatientById(int patientId) throws PatientNotFoundException {
        for (Patient patient : patients) {
            if (patient.getId() == patientId) {
                return patient;
            }
        }
        throw new PatientNotFoundException("Patient with ID " + patientId + " not found.");
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public Department getCardiologyDepartment() {
        return cardiologyDepartment;
    }

    public Department getPediatricsDepartment() {
        return pediatricsDepartment;
    }

    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        departments.add(cardiologyDepartment);
        departments.add(pediatricsDepartment);
        return Collections.unmodifiableList(departments);
    }

    public List<LocalDateTime> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public List<String> getAvailableRooms() {
        return RoomRegistry.getInstance().getAvailableRooms();
    }

    public Doctor findDoctorById(int doctorId) throws DoctorNotFoundException {
        for (Doctor doctor : getAllDoctors()) {
            if (doctor.getId() == doctorId) {
                return doctor;
            }
        }
        throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found.");
    }

    private List<Doctor> getAllDoctors() {
        List<Doctor> allDoctors = new ArrayList<>();
        allDoctors.addAll(cardiologyDepartment.getDoctors());
        allDoctors.addAll(pediatricsDepartment.getDoctors());
        return allDoctors;
    }


    private boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getId() == doctor.getId() &&
                    appointment.getAppointmentDateTime().equals(dateTime) &&
                    appointment.getStatus() != AppointmentStatus.CANCELED) {
                return false;
            }
        }
        return true;
    }
}


