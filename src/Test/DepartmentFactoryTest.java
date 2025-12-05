package Test;
import Main.hospital.Department;
import org.junit.jupiter.api.Test;
import Main.patterns.creational.abstractfabric.DepartmentFactory;

import static org.junit.jupiter.api.Assertions.*;

public class DepartmentFactoryTest {

    @Test
    void createDepartment_CreatesCardiology_WithDefaultDoctors() {
        Department cardiology = DepartmentFactory.createDepartment("cardiology");

        assertNotNull(cardiology);
        assertEquals("cardiology", cardiology.getName());
        
        // Перевіряємо, що фабрика додала лікарів за замовчуванням
        assertFalse(cardiology.getDoctors().isEmpty(), "Відділення має містити лікарів");
        assertEquals(2, cardiology.getDoctors().size(), "В кардіології має бути 2 лікаря за замовчуванням");
        assertEquals("Cardiology", cardiology.getDoctors().getFirst().getSpecialization());
    }

    @Test
    void createDepartment_CreatesPediatrics_WithDefaultDoctors() {
        Department pediatrics = DepartmentFactory.createDepartment("pediatrics");

        assertNotNull(pediatrics);
        assertEquals("pediatrics", pediatrics.getName());
        
        // Перевіряємо, що фабрика додала лікаря
        assertEquals(1, pediatrics.getDoctors().size(), "В педіатрії має бути 1 лікар за замовчуванням");
    }

    @Test
    void createDepartment_ThrowsException_WhenUnknownDepartment() {
        String unknownDept = "Neurology";
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DepartmentFactory.createDepartment(unknownDept);
        });

        assertEquals("Unknown department: " + unknownDept, exception.getMessage());
    }
}