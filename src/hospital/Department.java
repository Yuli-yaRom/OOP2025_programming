package hospital;

import java.util.List;

public class Department {
    private String name;
    private final List<String> Doctors;
    private Boolean active;

    public Department(String name, List<String> Doctors, Boolean active) {
        this.name = name;
        this.Doctors = Doctors;
        this.active = active;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getDoctors() {
        return Doctors;
    }

    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
