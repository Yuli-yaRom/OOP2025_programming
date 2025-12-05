package people;


public abstract class Person {
    private int id;
    private String name;
    private String surname;
    private int phone;
    private String email;

    public Person(int id, String name, String surname, int phone, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
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

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public int getId() {

        return id;
    }
    public void setId(int id) {

        this.id = id;
    }
}