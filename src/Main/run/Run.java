package Main.run;

import Main.appointment.Appointment;
import Main.appointment.AppointmentStatus;
import Main.appointment.OfflineAppointment;
import Main.exceptions.*;
import Main.hospital.Department;
import Main.patterns.controller.AppointmentController;
import Main.patterns.observer.Observer;
import Main.people.Doctor;
import Main.people.Patient;
import Main.service.CreditCardPayment;
import Main.service.InsurancePayment;
import Main.service.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class Run {
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

    //метод для вибору об'єкта зі списку
    private static <T> T selectFromList(Scanner scanner, List<T> items, String title, Function<T, String> displayMapper) {
        System.out.println("--- " + title + " ---");
        if (items.isEmpty()) {
            System.out.println("List is empty.");
            return null;
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + displayMapper.apply(items.get(i)));
        }
        System.out.print("Enter number: ");
        int index = getIntegerInput(scanner);
        if (index < 1 || index > items.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return items.get(index - 1);
    }

    private static int getIntegerInput(Scanner scanner) {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }


     //пошук пацієнта
    private static Patient findPatientInteractive(Scanner scanner, AppointmentController controller) {
        System.out.print("Enter patient ID: ");
        int patientId = getIntegerInput(scanner);
        try {
            return controller.findPatientById(patientId);
        } catch (PatientNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //Основна логіка
    private static void bookAppointment(Scanner scanner, AppointmentController controller) {
        System.out.print("Enter appointment type (online/offline): ");
        String type = scanner.nextLine();

        Patient patient = findPatientInteractive(scanner, controller);
        if (patient == null) return;

        Doctor doctor = selectDoctor(scanner, controller);
        if (doctor == null) return;

        LocalDateTime timeSlot = selectFromList(scanner, controller.getAvailableTimeSlots(), "Select a Time Slot", LocalDateTime::toString);
        if (timeSlot == null) return;

        String details;
        double cost;

        if ("offline".equalsIgnoreCase(type)) {
            String room = selectRoomInteractive(scanner, controller);
            if (room == null) return;
            details = room;
            cost = 150.0;
        } else {
            System.out.print("Enter meeting link: ");
            details = scanner.nextLine();
            cost = 100.0;
        }

        try {
            Appointment newAppointment = controller.bookAppointment(type, appointmentIdCounter++, patient, doctor, timeSlot, details, cost);
            System.out.println("Appointment booked successfully!");
            printAppointmentDetails(newAppointment);
        } catch (AppointmentBookingException | DoctorUnavailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void rescheduleAppointment(Scanner scanner, AppointmentController controller) {
        performActionOnAppointment(scanner, controller, appointment -> {
            LocalDateTime newTimeSlot = selectFromList(scanner, controller.getAvailableTimeSlots(), "Select a New Time Slot", LocalDateTime::toString);
            if (newTimeSlot == null) return;

            String newRoomNumber = null;
            if (appointment instanceof OfflineAppointment) {
                newRoomNumber = selectRoomInteractive(scanner, controller);
            }

            try {
                controller.rescheduleAppointment(appointment, newTimeSlot, newRoomNumber);
                System.out.println("Appointment for " + appointment.getPatient().getName() + " has been rescheduled.");
            } catch (AppointmentBookingException e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    private static void cancelAppointment(Scanner scanner, AppointmentController controller) {
        performActionOnAppointment(scanner, controller, appointment -> {
            controller.cancelAppointment(appointment);
            System.out.println("Appointment for " + appointment.getPatient().getName() + " has been canceled.");
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

    private static void recordMedicalHistory(Scanner scanner, AppointmentController controller) {
        Patient patient = findPatientInteractive(scanner, controller);
        if (patient == null) return;

        Doctor doctor = selectDoctor(scanner, controller);
        if (doctor == null) return;

        System.out.print("Enter medical record: ");
        String record = scanner.nextLine();

        controller.recordMedicalHistory(doctor, patient, record);
        System.out.println("Medical record updated.");
    }

    private static void viewMedicalHistory(Scanner scanner, AppointmentController controller) {
        Patient patient = findPatientInteractive(scanner, controller);
        if (patient == null) return;

        System.out.println("--- Medical History for " + patient.getName() + " ---");
        if (patient.getMedicalHistory().isEmpty()) {
            System.out.println("No medical history found.");
        } else {
            patient.getMedicalHistory().forEach(System.out::println);
        }
    }

    //Допоміжні методи

    private static Doctor selectDoctor(Scanner scanner, AppointmentController controller) {
        Department department = selectFromList(scanner, controller.getDepartments(), "Select a Department", Department::getName);
        if (department == null) return null;

        Doctor selectedDoctor = selectFromList(scanner, department.getDoctors(), "Select a Doctor",
                d -> d.getName() + " (" + d.getSpecialization() + ")");

        if (selectedDoctor == null) return null;

        try {
            return controller.findDoctorById(selectedDoctor.getId());
        } catch (DoctorNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static Appointment selectAppointment(Scanner scanner, AppointmentController controller) {
        List<Appointment> scheduledAppointments = new ArrayList<>();
        for (Appointment appointment : controller.getAppointments()) {
            if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                scheduledAppointments.add(appointment);
            }
        }

        return selectFromList(scanner, scheduledAppointments, "Select a Scheduled Appointment",
                app -> app.getPatient().getName() + " with " + app.getDoctor().getName() + " at " + app.getAppointmentDateTime());
    }

    private static String selectRoomInteractive(Scanner scanner, AppointmentController controller) {
        System.out.println("--- Select an Available Room ---");
        System.out.println("Available rooms: " + controller.getAvailableRooms());
        System.out.print("Enter room number: ");
        return scanner.nextLine();
    }

    private static void performActionOnAppointment(Scanner scanner, AppointmentController controller, Consumer<Appointment> action) {
        Appointment appointment = selectAppointment(scanner, controller);
        if (appointment != null) {
            action.accept(appointment);
        }
    }

    private static void printAppointmentDetails(Appointment app) {
        System.out.println("  Patient: " + app.getPatient().getName());
        System.out.println("  Doctor: " + app.getDoctor().getName());
        System.out.println("  Time: " + app.getAppointmentDateTime());
    }

    private static void setupInitialData(AppointmentController controller) {
        try {
            controller.addPatient(new Patient(1, "John", "Doe", "123-456-7890", "John@gmail.com"));
            controller.addPatient(new Patient(2, "Jane", "Smith", "234-444-111", "Jane@gmail.com"));
        } catch (InvalidInputException e) {
            System.out.println("Error setting up initial data: " + e.getMessage());
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
}
