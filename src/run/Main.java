package run;

import appointment.Appointment;
import appointment.AppointmentStatus;
import appointment.OfflineAppointment;
import exceptions.AppointmentBookingException;
import exceptions.DoctorNotFoundException;
import exceptions.InvalidInputException;
import exceptions.PatientNotFoundException;
import hospital.Department;
import hospital.MedicalRecord;
import patterns.controller.AppointmentController;
import patterns.observer.Observer;
import people.Doctor;
import people.Patient;
import service.CreditCardPayment;
import service.InsurancePayment;
import service.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    private static int appointmentIdCounter = 1;

    public static void main(String[] args) {
        try {
            AppointmentController controller = new AppointmentController();
            setupInitialData(controller);


            Observer patientNotifier = message -> System.out.println("\n--- Patient Notification ---\n" + message);
            controller.addObserver(patientNotifier);


            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    printMenu();
                    int choice = getIntegerInput(scanner);

                    switch (choice) {
                        case 1:
                            bookAppointment(scanner, controller);
                            break;
                        case 2:
                            cancelAppointment(scanner, controller);
                            break;
                        case 3:
                            rescheduleAppointment(scanner, controller);
                            break;
                        case 4:
                            completeAppointment(scanner, controller);
                            break;
                        case 5:
                            recordMedicalHistory(scanner, controller);
                            break;
                        case 6:
                            viewMedicalHistory(scanner, controller);
                            break;
                        case 7:
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
        } catch (InvalidInputException e) {

            System.out.println("Fatal error during application setup: " + e.getMessage());
        }
    }


    private static int getIntegerInput(Scanner scanner) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }


    private static void printMenu() {
        System.out.println("\n--- Hospital Management System ---");
        System.out.println("1. Book an Appointment");
        System.out.println("2. Cancel an Appointment");
        System.out.println("3. Reschedule an Appointment");
        System.out.println("4. Complete an Appointment");
        System.out.println("5. Record Medical History");
        System.out.println("6. View Patient Medical History");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }


    private static void bookAppointment(Scanner scanner, AppointmentController controller) {
        System.out.print("Enter appointment type (online/offline): ");
        String type = scanner.nextLine();
        System.out.print("Enter patient ID: ");
        int patientId = getIntegerInput(scanner);

        try {

            Patient patient = controller.findPatientById(patientId);
            Doctor doctor = selectDoctor(scanner, controller);
            if (doctor == null) {
                return;
            }

            System.out.println("--- Select a Time Slot ---");
            listTimeSlots(controller);
            System.out.print("Enter time slot number: ");
            int timeSlotNumber = getIntegerInput(scanner);
            if (timeSlotNumber < 1 || timeSlotNumber > controller.getAvailableTimeSlots().size()) {
                System.out.println("Invalid time slot number.");
                return;
            }
            LocalDateTime timeSlot = controller.getAvailableTimeSlots().get(timeSlotNumber - 1);

            String details;
            double cost;

            if ("offline".equalsIgnoreCase(type)) {
                System.out.println("--- Select an Available Room ---");
                listAvailableRooms(controller);
                System.out.print("Enter room number: ");
                details = scanner.nextLine();
                cost = 150.0;
            } else {
                System.out.print("Enter meeting link: ");
                details = scanner.nextLine();
                cost = 100.0;
            }

            Appointment newAppointment = controller.bookAppointment(type, appointmentIdCounter++, patient, doctor, timeSlot, details, cost);
            System.out.println("Appointment booked successfully!");
            System.out.println("  Patient: " + newAppointment.getPatient().getName());
            System.out.println("  Doctor: " + newAppointment.getDoctor().getName());
            System.out.println("  Time: " + newAppointment.getAppointmentDateTime());
        } catch (PatientNotFoundException | AppointmentBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static Doctor selectDoctor(Scanner scanner, AppointmentController controller) {
        System.out.println("--- Select a Department ---");
        listDepartments(controller);
        System.out.print("Enter department number: ");
        int departmentNumber = getIntegerInput(scanner);
        if (departmentNumber < 1 || departmentNumber > controller.getDepartments().size()) {
            System.out.println("Invalid department number.");
            return null;
        }
        Department department = controller.getDepartments().get(departmentNumber - 1);

        System.out.println("--- Select a Doctor ---");
        listDoctors(department);
        System.out.print("Enter doctor number: ");
        int doctorNumber = getIntegerInput(scanner);
        if (doctorNumber < 1 || doctorNumber > department.getDoctors().size()) {
            System.out.println("Invalid doctor number.");
            return null;
        }
        Doctor selectedDoctor = department.getDoctors().get(doctorNumber - 1);
        try {
            return controller.findDoctorById(selectedDoctor.getId());
        } catch (DoctorNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    private static Appointment selectAppointment(Scanner scanner, AppointmentController controller) {
        System.out.println("--- Select a Scheduled Appointment ---");
        List<Appointment> scheduledAppointments = new ArrayList<>();
        for (Appointment appointment : controller.getAppointments()) {
            if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                scheduledAppointments.add(appointment);
            }
        }

        if (scheduledAppointments.isEmpty()) {
            System.out.println("No scheduled appointments available.");
            return null;
        }

        int i = 1;
        for (Appointment appointment : scheduledAppointments) {
            System.out.println(i++ + ". " + appointment.getPatient().getName() + " with " + appointment.getDoctor().getName() + " at " + appointment.getAppointmentDateTime() + " (" + appointment.getStatus() + ")");
        }

        System.out.print("Enter appointment number: ");
        int appointmentNumber = getIntegerInput(scanner);
        if (appointmentNumber < 1 || appointmentNumber > scheduledAppointments.size()) {
            System.out.println("Invalid appointment number.");
            return null;
        }
        return scheduledAppointments.get(appointmentNumber - 1);
    }


    private static void recordMedicalHistory(Scanner scanner, AppointmentController controller) {
        System.out.print("Enter patient ID: ");
        int patientId = getIntegerInput(scanner);

        try {
            Patient patient = controller.findPatientById(patientId);
            Doctor doctor = selectDoctor(scanner, controller);
            if (doctor == null) {
                return;
            }
            System.out.print("Enter medical record: ");
            String record = scanner.nextLine();
            controller.recordMedicalHistory(doctor, patient, record);
            System.out.println("Medical record updated.");
        } catch (PatientNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void setupInitialData(AppointmentController controller) {
        try {
            controller.addPatient(new Patient(1, "John", "Doe", "123-456-7890", "John@gmail.com"));
            controller.addPatient(new Patient(2, "Jane", "Smith", "234-444-111", "Jane@gmail.com"));
        } catch (InvalidInputException e) {
            System.out.println("Error setting up initial data: " + e.getMessage());
        }
    }

    private static void performActionOnAppointment(Scanner scanner, AppointmentController controller, Consumer<Appointment> action) {
        Appointment appointment = selectAppointment(scanner, controller);
        if (appointment != null) {
            action.accept(appointment);
        }
    }

    private static void cancelAppointment(Scanner scanner, AppointmentController controller) {
        performActionOnAppointment(scanner, controller, appointment -> {
            controller.cancelAppointment(appointment);
            System.out.println("Appointment for " + appointment.getPatient().getName() + " has been canceled.");
        });
    }


    private static void rescheduleAppointment(Scanner scanner, AppointmentController controller) {
        performActionOnAppointment(scanner, controller, appointment -> {
            System.out.println("--- Select a New Time Slot ---");
            listTimeSlots(controller);
            System.out.print("Enter time slot number: ");
            int timeSlotNumber = getIntegerInput(scanner);
            if (timeSlotNumber < 1 || timeSlotNumber > controller.getAvailableTimeSlots().size()) {
                System.out.println("Invalid time slot number.");
                return;
            }
            LocalDateTime newTimeSlot = controller.getAvailableTimeSlots().get(timeSlotNumber - 1);

            String newRoomNumber = null;
            if (appointment instanceof OfflineAppointment) {
                System.out.println("--- Select a New Room ---");
                listAvailableRooms(controller);
                System.out.print("Enter new room number: ");
                newRoomNumber = scanner.nextLine();
            }

            try {
                controller.rescheduleAppointment(appointment, newTimeSlot, newRoomNumber);
                System.out.println("Appointment for " + appointment.getPatient().getName() + " has been rescheduled.");
            } catch (AppointmentBookingException e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }


    private static void completeAppointment(Scanner scanner, AppointmentController controller) {
        performActionOnAppointment(scanner, controller, appointment -> {
            System.out.print("Enter payment method (credit/insurance): ");
            String paymentMethod = scanner.nextLine();
            PaymentStrategy paymentStrategy;
            boolean applyInsurance = false;
            if ("credit".equalsIgnoreCase(paymentMethod)) {
                paymentStrategy = new CreditCardPayment("1234-5678-9012-3456");
            } else if ("insurance".equalsIgnoreCase(paymentMethod)) {
                paymentStrategy = new InsurancePayment("POL-12345");
                applyInsurance = true;
            } else {
                System.out.println("Invalid payment method.");
                return;
            }

            String confirmation = controller.completeAppointment(appointment, paymentStrategy, applyInsurance);
            System.out.println(confirmation);
        });
    }


    private static void listDepartments(AppointmentController controller) {
        int i = 1;
        for (Department department : controller.getDepartments()) {
            System.out.println(i++ + ". " + department.getName());
        }
    }

    private static void listDoctors(Department department) {
        int i = 1;
        for (Doctor doctor : department.getDoctors()) {
            System.out.println(i++ + ". " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
        }
    }

    private static void listTimeSlots(AppointmentController controller) {
        int i = 1;
        for (LocalDateTime timeSlot : controller.getAvailableTimeSlots()) {
            System.out.println(i++ + ". " + timeSlot);
        }
    }

    private static void listAvailableRooms(AppointmentController controller) {
        System.out.println("Available rooms: " + controller.getAvailableRooms());
    }


    private static void viewMedicalHistory(Scanner scanner, AppointmentController controller) {
        System.out.print("Enter patient ID: ");
        int patientId = getIntegerInput(scanner);
        try {
            Patient patient = controller.findPatientById(patientId);
            System.out.println("--- Medical History for " + patient.getName() + " ---");
            if (patient.getMedicalHistory().isEmpty()) {
                System.out.println("No medical history found.");
            } else {
                for (MedicalRecord record : patient.getMedicalHistory()) {
                    System.out.println(record);
                }
            }
        } catch (PatientNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
