package patterns.creational.factory;


import people.Doctor;
import people.Person;

public class StaffFactory {
    public static Person createStaff(String type, int doctorId, String name, String surname, String phone, String email) {
        if ("doctor".equalsIgnoreCase(type)) {
            return new Doctor(doctorId, name, surname, phone, email);
        }
        // We can add other staff types like Nurse here later
        return null;
    }
}
