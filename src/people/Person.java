package people;

public abstract class Person {
    private String name;
    private Integer age;
    private String surname;
    private int phone;

    public Person(String name, String surname, int phone) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    protected Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}