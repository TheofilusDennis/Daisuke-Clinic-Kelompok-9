public class Doctor {
    String ID;
    String name;
    String specialty;
    String password;
    String loginTime;
    boolean isLoggedIn;

    public Doctor(String ID, String name, String specialty, String password, String loginTime, boolean isLoggedIn) {
        this.ID = ID;
        this.name = name;
        this.specialty = specialty;
        this.password = password;
        this.loginTime = loginTime;
        this.isLoggedIn = isLoggedIn;
    }

    public Doctor(String ID, String name, String specialty, String password) {
        this(ID, name, specialty, password, null, false); // chaining
    }

    public String getSpecialty(){
        return specialty;
    }

    void login() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.loginTime = now.format(formatter);
        this.isLoggedIn = true;
    }

    void logout() {
        this.loginTime = null;
        this.isLoggedIn = false;
    }
}

