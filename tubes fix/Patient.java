public class Patient {
    private String ID;
    private String name;
    private String pass;
    private int age;
    private String address;
    private String phone;

    public Patient(String ID, String name, String pass, int age, String address, String phone) {
        this.ID = ID;
        this.name = name;
        this.pass = pass;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void showProfile() {
        System.out.println("Username: " + name);
        System.out.println("ID: " + ID);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
    }
}