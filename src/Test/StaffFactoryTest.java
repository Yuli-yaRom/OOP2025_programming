package Test;

import org.junit.jupiter.api.Test;
import Main.patterns.creational.factory.StaffFactory;
import Main.people.Doctor;
import Main.people.Person;

import static org.junit.jupiter.api.Assertions.*;

public class StaffFactoryTest {

    @Test
    void createStaff_CreatesDoctor_WhenTypeIsDoctor() {
        int id = 1;
        String name = "John";
        String surname = "Doe";
        String phone = "111-222";
        String specialization = "Cardiology";
        String email = "john@example.com";

        Person staff = StaffFactory.createStaff(id, "doctor", name, surname, phone, specialization, email);

        assertNotNull(staff, "Співробітник не повинен бути null");
        assertTrue(staff instanceof Doctor, "Створений об'єкт має бути класу Doctor");

        Doctor doctor = (Doctor) staff;
        assertEquals(name, doctor.getName());
        assertEquals(specialization, doctor.getSpecialization());
    }

    @Test
    void createStaff_ReturnsNull_WhenTypeIsUnknown() {
        Person staff = StaffFactory.createStaff(2, "nurse", "Jane", "Doe", "333-444", "General", "jane@example.com");

        assertNull(staff, "Для невідомого типу має повертатися null (поки Nurse не реалізовано)");
    }

    @Test
    void createStaff_IsCaseInsensitive() {
        Person staff = StaffFactory.createStaff(3, "DOCTOR", "Test", "User", "555-666", "TestSpec", "test@test.com");

        assertNotNull(staff);
        assertTrue(staff instanceof Doctor);
    }
}
