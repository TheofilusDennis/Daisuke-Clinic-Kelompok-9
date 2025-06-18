class Appointment implements Comparable<Appointment> {
    String patientID;
    String doctorID;
    String doctorName;
    String time; 

    public Appointment(String patientID, String doctorID, String doctorName, String time) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.time = time;
    }

    @Override
    public int compareTo(Appointment other) {
        return this.time.compareTo(other.time);
    }
}