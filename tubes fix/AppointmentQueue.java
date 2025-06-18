public class AppointmentQueue {
    private LinkedList<Appointment> queue = new LinkedList<>();

    public void addAppointment(Appointment appt) {
        queue.addSorted(appt, new java.util.Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                return a1.time.compareTo(a2.time);
            }
        });
    }

    public Appointment processAppointment() {
        return queue.poll();
    }

    public void displayAppointments() {
        if (queue.isEmpty()) {
            System.out.println("Tidak ada appointment.");
            return;
        }

        LinkedList.Node<Appointment> current = queue.getHead();
        while (current != null) {
            Appointment appt = current.data;
            System.out.println("Pasien: " + appt.patientID + 
                             ", Dokter: " + appt.doctorName + 
                             ", Jam: " + appt.time);
            current = current.next;
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}