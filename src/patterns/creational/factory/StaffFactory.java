package patterns.creational.factory;


import exceptions.InvalidInputException;
import people.Doctor;
import people.Person;

public class StaffFactory {
    public static Person createStaff(int id, String type, String name, String surname, String phone, String specialization, String email) {
        if ("doctor".equalsIgnoreCase(type)) {
            return new Doctor(id, name, surname, phone, specialization, email);
        }
        // We can add other staff types like Nurse here later
        return null;
    }

    public static Doctor createDoctor(int id, String name, String surname, String phone, String email, String specialization) throws InvalidInputException {
        return new Doctor(id, name, surname, phone, specialization, email);
    }
}
