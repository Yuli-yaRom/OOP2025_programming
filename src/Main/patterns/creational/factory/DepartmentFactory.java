package Main.patterns.creational.factory;
import Main.exceptions.InvalidInputException;
import Main.hospital.Department;

//Фабрика відділень

public class DepartmentFactory {
    public static Department createDepartment(String name) throws InvalidInputException {
        Department department = new Department(name);
        switch (name.toLowerCase()) {
            case "cardiology":
                department.addDoctor(1, "Dr. Smith", "987-654-3210", "dr.smith@example.com", "Cardiology", "Smith@gmail.com");
                department.addDoctor(2, "Dr. Jones", "123-456-7890", "dr.jones@example.com", "Cardiology", "Jones@gmail.com");
                break;
            case "pediatrics":
                department.addDoctor(3, "Dr. Miller", "111-222-3333", "dr.miller@example.com", "Pediatrics", "Miller@gmail.com");
                break;
            default:
                throw new IllegalArgumentException("Unknown department: " + name);
        }
        return department;
    }
}
