import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IDGenerator {
    private static int lastPatientID = 0;
    private static int lastDoctorID = 0;

    static {
        loadLastIDs();
    }

    private static void loadLastIDs() {
        try {
            BufferedReader patientReader = new BufferedReader(new FileReader("patients.txt"));
            String line;
            while ((line = patientReader.readLine()) != null) {
                if (line.startsWith("P")) {
                    String idStr = line.split("\\|")[0].substring(1);
                    lastPatientID = Math.max(lastPatientID, Integer.parseInt(idStr));
                }
            }
            patientReader.close();

            BufferedReader doctorReader = new BufferedReader(new FileReader("Doctors.txt"));
            while ((line = doctorReader.readLine()) != null) {
                if (line.startsWith("D")) {
                    String idStr = line.split("\\|")[0].substring(1);
                    lastDoctorID = Math.max(lastDoctorID, Integer.parseInt(idStr));
                }
            }
            doctorReader.close();
        } catch (IOException e) {
            System.out.println("Menggunakan ID default karena gagal membaca file");
        }
    }

    public static String generatePatientID() {
        lastPatientID++;
        return "P" + String.format("%02d", lastPatientID);
    }

    public static String generateDoctorID() {
        lastDoctorID++;
        return "D" + String.format("%02d", lastDoctorID);
    }
}