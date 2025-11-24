package patterns.creational.fabric;

import people.Doctor;

public class StaffFactory {
    public StaffFactory createStaff(String type) {
        if ("Doctor".equalsIgnoreCase(type)) {
            return null;
        }
        else {
            throw new IllegalArgumentException("Invalid Staff type");
        }
    }

}
