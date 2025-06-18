import java.util.Scanner;
import java.io.*;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;
// import java.util.InputMismatchException;

public class Main {
    static int whoLogin;
    static String IDPatientLogin; 
    static String IDDoctorLogin;
    static AppointmentQueue appointmentQueue = new AppointmentQueue();
    static BST patientBST = new BST();
    static Scanner scanner = new Scanner(System.in);

   public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearActiveDoctorsFile();
        }));
        
        loadPatientsToBST();
        
        int choice;
        do {
            ClearTerminal();
            System.out.println("========================================");
            System.out.println("||  SELAMAT DATANG DI DAISUKE CLINIC  ||");
            System.out.println("========================================");
            System.out.println("1. REGISTER");
            System.out.println("2. LOGIN");
            System.out.println("3. QUIT");
            choice = InputValidator.getValidInteger("Pilihanmu : ",1,Integer.MAX_VALUE);
            if (choice == 1) {
                register();
            } else if (choice == 2) {
                whoLogin = login();
                
                if (whoLogin == 1) { 
                    menuPatient();
                } else if (whoLogin == 2) { 
                    while (whoLogin == 2) {
                        menuDoctor();
                    }
                } else if (whoLogin == 3) { 
                    menuAdmin();
                }
            }
        } while (choice != 3);
        
        
    }

    private static void clearActiveDoctorsFile() {
        try {
            new FileWriter("active_doctors.txt", false).close();
        } catch (IOException e) {
            System.err.println("Gagal membersihkan file active_doctors.txt");
        }
    }

    public static void loadPatientsToBST() {
        try (BufferedReader reader = new BufferedReader(new FileReader("patients.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    Patient patient = new Patient(parts[0], parts[1], parts[2], 
                                                Integer.parseInt(parts[3]), parts[4], parts[5]);
                    patientBST.insert(patient);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading patients to BST: " + e.getMessage());
        }
    }

    public static void register() {
        ClearTerminal();
        System.out.println("=======================");
        System.out.println("|| Register sebagai: ||");
        System.out.println("=======================");
        System.out.println("1. Pasien");
        System.out.println("2. Dokter");
        int choice = InputValidator.getValidInteger("Pilihanmu: ",1,Integer.MAX_VALUE);

        if (choice == 1) {
            String name = InputValidator.getValidName("Masukan nama anda : ");
            String pass = InputValidator.getValidPassword("Masukan password anda : ");
            int age = InputValidator.getValidAge("Masukan umur anda : ");
            String address = InputValidator.getValidAddress("Masukan alamat anda : ");
            String hp = InputValidator.getValidPhoneNumber("Masukan nomor handphone anda : ");

            String ID = IDGenerator.generatePatientID(); 
            String temp = ID + "|" + name + "|" + pass + "|" + age + "|" + address + "|" + hp;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("patients.txt", true))) {
                writer.write(temp);
                writer.newLine();
                patientBST.insert(new Patient(ID, name, pass, age, address, hp));
                System.out.println("\nRegistrasi pasien berhasil!");
                System.out.println("ID Pasien Anda: " + ID);
                System.out.println("Data diri:");
                System.out.println("Nama: " + name);
                System.out.println("Umur: " + age);
                System.out.println("Alamat: " + address);
                System.out.println("Nomor HP: " + hp);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan data!");
                e.printStackTrace();
            }
        } else if (choice == 2) {
            System.out.println("\n-- Registrasi Dokter --");
            
            
            String name = InputValidator.getValidName("Masukan nama lengkap : ");
            if (name.isEmpty()) {
                System.out.println("Nama tidak boleh kosong!");
                return;
            }
            
            String specialty = InputValidator.getValidName("Masukan spesialisasi : ");
            if (specialty.isEmpty()) {
                System.out.println("Spesialisasi tidak boleh kosong!");
                return;
            }
            
            String password = InputValidator.getValidPassword("Masukan password : ");
            if (password.isEmpty()) {
                System.out.println("Password tidak boleh kosong!");
                return;
            }
            
            String workingHours;
            boolean isValid;
            do {
                System.out.print("\"Masukkan jam kerja (format HH.MM-HH.MM, contoh: 08.00-16.30) : ");
                workingHours = scanner.nextLine();
                isValid = workingHours.matches("^\\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}$");
                if (!isValid) {
                    System.out.println("Format jam kerja tidak valid! Contoh format yang benar: 08.00-16.30");
                }
            } while (!isValid);
            
            String doctorId = IDGenerator.generateDoctorID(); 
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Doctors.txt", true))) {
                String doctorData = String.join("|", 
                    doctorId, name, password, specialty, workingHours);
                writer.write(doctorData);
                writer.newLine();
                System.out.println("\nRegistrasi dokter berhasil!");
                System.out.println("ID Dokter Anda: " + doctorId);
                System.out.println("Nama: " + name);
                System.out.println("Specialty: " + specialty);
                System.out.println("Jam Kerja: " + workingHours);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan data dokter!");
                e.printStackTrace();
            }
        }
        pressEnterToContinue();
    }

    public static int login() {
        ClearTerminal();
        System.out.print("Masukkan Id : ");
        String inputID = scanner.nextLine();
        System.out.print("Masukkan Password : ");
        String inputPass = scanner.nextLine();

        int role = detectIDType(inputID);

        String fileName;
        if(role == 1) {
            fileName = "patients.txt"; 
        } else if (role == 2) {
            fileName = "Doctors.txt";
        } else if(role == 3){
            fileName = "admin.txt";
        } else {
            fileName = "eror";
        }
        boolean isLoginSuccess = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                String[] parts = baris.split("\\|"); 
                String id = parts[0];
                String password = parts[2];

                if (id.equals(inputID) && password.equals(inputPass)) {
                    if (role == 2) {
                        recordDoctorsLogin(inputID, inputPass);
                        IDDoctorLogin = inputID;
                        isLoginSuccess = true;
                        System.out.println("\nLogin berhasil sebagai dokter " + parts[1] + "!");
                        return role;
                    } else if (role == 1) {
                        isLoginSuccess = true;
                        IDPatientLogin = inputID;
                        System.out.println("\nLogin berhasil sebagai pasien " + parts[1] + "!");
                        return role;
                    } else if (role == 3) {
                        System.out.println("\nLogin berhasil sebagai admin!");
                        return role;
                    }
                }
            }

            if (!isLoginSuccess) {
                System.out.println("\nLogin gagal! ID atau password salah.");
                pressEnterToContinue();
                return -1;
            }

        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file.");
            pressEnterToContinue();
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public static void recordDoctorsLogin(String ID, String pass) {
        try {
            // Cek apakah dokter sudah login sebelumnya
            boolean alreadyLoggedIn = false;
            File activeDoctorsFile = new File("active_doctors.txt");
            if (activeDoctorsFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(activeDoctorsFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith(ID + "|")) {
                            alreadyLoggedIn = true;
                            break;
                        }
                    }
                }
            }

            if (!alreadyLoggedIn) {
                try (BufferedReader reader = new BufferedReader(new FileReader("Doctors.txt"));
                    BufferedWriter writer = new BufferedWriter(new FileWriter("active_doctors.txt", true))) {
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 4 && parts[0].equals(ID) && parts[2].equals(pass)) {
                            String time = java.time.LocalTime.now().toString();
                            writer.write(parts[0] + "|" + parts[1] + "|" + parts[3] + "|" + time);
                            writer.newLine();
                            System.out.println("Dokter " + parts[1] + " berhasil login.");
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Dokter sudah login sebelumnya.");
            }
        } catch (IOException e) {
            System.out.println("Gagal mencatat login dokter.");
            e.printStackTrace();
        }
    }

    public static void pressEnterToContinue() {
        System.out.println("\nTekan Enter untuk kembali ke menu...");
        scanner.nextLine();
    }

    public static int detectIDType(String ID) {
        if (ID == null || ID.length() == 0) {
            return -1;
        }

        char firstChar = Character.toUpperCase(ID.charAt(0));
        if (firstChar == 'P') {
            return 1; // Pasien
        } else if (firstChar == 'D') {
            return 2; // Dokter
        } else if (firstChar == 'A') {
            return 3; // Admin
        } else {
            return -1; 
        }
    }

    public static void menuPatient() {
        int choice;
        do {
            ClearTerminal();
            System.out.println("================================");
            System.out.println("||        PATIENT MENU        ||");
            System.out.println("================================");
            System.out.println("1. Jadwal Dokter Aktif");
            System.out.println("2. Membuat Appointment");
            System.out.println("3. Riwayat Appointment Aktif");
            System.out.println("4. Riwayat Periksa");
            System.out.println("5. Logout");
            choice = InputValidator.getValidInteger("Pilihanmu : ",1,Integer.MAX_VALUE);

            switch (choice) {
                case 1:
                    ClearTerminal();
                    showActiveDoctors();
                    pressEnterToContinue();
                    break;
                case 2:
                    ClearTerminal();
                    makeAppointment();
                    pressEnterToContinue();
                    break;
                case 3:
                    ClearTerminal();
                    HistoryAppointment();
                    pressEnterToContinue();
                    break;
                case 4:
                    ClearTerminal();
                    riwayatPeriksa();
                    pressEnterToContinue();
                    break;
                case 5:
                    whoLogin = -1;
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);
    }

    public static void showActiveDoctors() {
        System.out.println("===========================================================================");
        System.out.println("||                    Daftar Dokter yang Sedang Aktif                    ||");
        System.out.println("===========================================================================");
        System.out.println("|| ID Dokter | Nama Dokter     | Spesialisasi | Jam Kerja  | Waktu Login ||");
        System.out.println("===========================================================================");
        
        try (BufferedReader activeReader = new BufferedReader(new FileReader("active_doctors.txt"));
            BufferedReader doctorReader = new BufferedReader(new FileReader("Doctors.txt"))) {
            
            Map doctorMap = new Map(50);
            String doctorLine;
            while ((doctorLine = doctorReader.readLine()) != null) {
                String[] doctorData = doctorLine.split("\\|");
                if (doctorData.length >= 5) {
                    doctorMap.put(doctorData[0], doctorData);
                }
            }

            String activeLine;
            boolean found = false;
            while ((activeLine = activeReader.readLine()) != null) {
                String[] activeData = activeLine.split("\\|");
                if (activeData.length >= 4) {
                    String[] doctorInfo = doctorMap.get(activeData[0]);
                    if (doctorInfo != null) {
                        String loginTime = activeData[3];
                        String formattedTime = formatLoginTime(loginTime);
                        
                        System.out.printf("|| %-9s | %-15s | %-12s | %-10s | %-10s ||\n",
                                activeData[0], 
                                activeData[1].length() > 15 ? activeData[1].substring(0, 12) + "..." : activeData[1],
                                activeData[2],
                                doctorInfo[4], 
                                formattedTime);
                        found = true;
                    }
                }
            }

            if (!found) { 
                System.out.println("||                   Tidak ada dokter yang aktif                     ||");
            }
            System.out.println("===========================================================================");
        } catch (IOException e) {
            System.out.println("Gagal membaca data dokter.");
            e.printStackTrace();
        }
    }

    private static String formatLoginTime(String fullTime) {
        try {
            int start = fullTime.indexOf('T') + 1; 
            if (start < 0) start = 0;
            
            if (fullTime.length() >= start + 8) {
                return fullTime.substring(start, start + 8);
            }
            return fullTime; 
        } catch (Exception e) {
            return fullTime; 
        }
    }

    public static void makeAppointment() {
        showActiveDoctors();

        System.out.print("Masukkan ID dokter yng ingin dipilih :");
        String doctorID = scanner.nextLine();
        try {
            BufferedReader doctorReader = new BufferedReader(new FileReader("Doctors.txt"));
            String doctorLine;
            String[] doctorData = null;
            
            while ((doctorLine = doctorReader.readLine()) != null) {
                String[] parts = doctorLine.split("\\|");
                if (parts.length >= 5 && parts[0].equals(doctorID)) {
                    doctorData = parts;
                    break;
                }
            }
            doctorReader.close();

            if (doctorData == null) {
                System.out.println("Dokter tidak ditemukan!");
                return;
            }

            String workingHours = doctorData[4]; 
            String[] hoursParts = workingHours.split("-");
            String startTimeStr = hoursParts[0];
            String endTimeStr = hoursParts[1];

            // Konversi ke menit
            int startTime = convertToMinutes(startTimeStr);
            int endTime = convertToMinutes(endTimeStr);

            // Input jam appointment dengan validasi
            String appointmentTime;
            boolean isValidTime = false;
            do {
                System.out.print("Masukkan jam appointment (format HH.MM, contoh 09.30): ");
                appointmentTime = scanner.nextLine();
                
                // Validasi format
                if (!appointmentTime.matches("^([0-1]?[0-9]|2[0-3])\\.[0-5][0-9]$")) {
                    System.out.println("Format waktu tidak valid! Gunakan format HH.MM");
                    continue;
                }

                int appointmentMinutes = convertToMinutes(appointmentTime);
                
                // Validasi dalam jam kerja
                if (appointmentMinutes < startTime || appointmentMinutes > endTime) {
                    System.out.println("Jam appointment di luar jam kerja dokter (" + workingHours + ")");
                    isValidTime = false;
                } else {
                    isValidTime = true;
                }
            } while (!isValidTime);

            // Cek apakah dokter aktif
            boolean isDoctorActive = false;
            BufferedReader activeReader = new BufferedReader(new FileReader("active_doctors.txt"));
            String activeLine;
            while ((activeLine = activeReader.readLine()) != null) {
                if (activeLine.startsWith(doctorID + "|")) {
                    isDoctorActive = true;
                    break;
                }
            }
            activeReader.close();

            if (!isDoctorActive) {
                System.out.println("Dokter tidak aktif saat ini!");
                return;
            }

            // Buat appointment
            Appointment appt = new Appointment(IDPatientLogin, doctorID, doctorData[1], appointmentTime);
            appointmentQueue.addAppointment(appt);
            System.out.println("Appointment berhasil dibuat untuk jam " + appointmentTime);

        } catch (IOException e) {
            System.out.println("Gagal membaca data dokter.");
            e.printStackTrace();
        }
    }

    private static int convertToMinutes(String timeStr) {
        String[] parts = timeStr.split("\\.");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    public static void HistoryAppointment() {
        System.out.println("\nRiwayat Appointment Aktif:");
        appointmentQueue.displayAppointments();
    }

    public static void riwayatPeriksa() {
        System.out.println("\nRiwayat Periksa:");
        
        if (IDPatientLogin == null || IDPatientLogin.isEmpty()) {
            System.out.println("Anda belum login sebagai pasien.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("medical_records.txt"))) {
            String line;
            boolean found = false;
            int counter = 1;

            System.out.println("======================================================================================");
            System.out.println("No  | Jam    | Dokter      | Diagnosa           | Resep Obat           | Tindakan     ");
            System.out.println("======================================================================================");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].equals(IDPatientLogin)) {
                    String doctorName = getDoctorName(parts[1]);
                    String waktu = parts[2].length() > 5 ? parts[2].substring(11, 16) : parts[2]; 
                    
                    System.out.printf("%-2d | %-9s | %-11s | %-17s | %-19s | %-20s\n", 
                                    counter++, 
                                    waktu,
                                    doctorName.length() > 11 ? doctorName.substring(0, 8) + "..." : doctorName,
                                    parts[3].length() > 17 ? parts[3].substring(0, 14) + "..." : parts[3],
                                    parts[4].length() > 19 ? parts[4].substring(0, 16) + "..." : parts[4],
                                    parts[5].length() > 20 ? parts[5].substring(0, 17) + "..." : parts[5]);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("Anda belum memiliki riwayat pemeriksaan.");
            }
            System.out.println("======================================================================================");
        } catch (FileNotFoundException e) {
            System.out.println("Belum ada riwayat pemeriksaan tersimpan.");
        } catch (IOException e) {
            System.out.println("Gagal membaca riwayat pemeriksaan.");
            e.printStackTrace();
        }
    }

    private static String getDoctorName(String doctorID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Doctors.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equals(doctorID)) {
                    return parts[1]; 
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca data dokter.");
        }
        return "Unknown Doctor";
    }

    public static void menuDoctor() {
        int choice;
        do {
            ClearTerminal();
            System.out.println("=================================");
            System.out.println("||         DOKTER MENU         ||");
            System.out.println("=================================");
            System.out.println("1. Lihat antrian appointment");
            System.out.println("2. Proses Appointment");
            System.out.println("3. Riwayat pasien");
            System.out.println("4. Kembali ke Menu Awal");
            System.out.println("5. Logout");
            choice = InputValidator.getValidInteger("Pilihanmu : ",1,Integer.MAX_VALUE);

            switch (choice) {
                case 1:
                    ClearTerminal();
                    viewAppointments();
                    pressEnterToContinue();
                    break;
                case 2:
                    ClearTerminal();
                    processAppointment();
                    pressEnterToContinue();
                    break;
                case 3:
                    ClearTerminal();
                    patientHistory();
                    pressEnterToContinue();
                    break;
                case 4:
                    whoLogin = -1;
                    return; 
                case 5:
                    doctorLogout(IDDoctorLogin);
                    whoLogin = -1;
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (true);
    }

    public static void viewAppointments() {
        System.out.println("\nAntrian Appointment:");
        appointmentQueue.displayAppointments();
    }

    public static void processAppointment() {
        if (appointmentQueue.isEmpty()) {
            System.out.println("Tidak ada appointment yang perlu diproses.");
            return;
        }

        Appointment appt = appointmentQueue.processAppointment();
        System.out.println("\nMemproses appointment untuk:");
        System.out.println("Pasien: " + appt.patientID);
        System.out.println("Dokter: " + appt.doctorName);
        System.out.println("Jam: " + appt.time);

        String diagnosis = InputValidator.getValidDiagnosis("Masukkan diagnosa : ");
        String prescription = InputValidator.getValidName("Masukkan resep Obat : ");
        String action = InputValidator.getValidTreatment("Masukkan Tindakan : ");

        // Menyimpan ke medical record
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("medical_records.txt", true))) {
            writer.write(appt.patientID + "|" + appt.doctorID + "|" + appt.time + "|" + 
                         diagnosis + "|" + prescription + "|" + action);
            writer.newLine();
            System.out.println("Appointment berhasil diproses dan data disimpan.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan rekam medis.");
            e.printStackTrace();
        }
    }

    public static void patientHistory() {
        System.out.print("Masukkan ID Pasien: ");
        String patientID = scanner.nextLine();

        System.out.println("\nRiwayat Pasien " + patientID + ":");
        try (BufferedReader reader = new BufferedReader(new FileReader("medical_records.txt"))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].equals(patientID)) {
                    System.out.println("Jam: " + parts[2]);
                    System.out.println("Diagnosa: " + parts[3]);
                    System.out.println("Resep: " + parts[4]);
                    System.out.println("Tindakan: " + parts[5]);
                    System.out.println("----------------------");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("Tidak ditemukan riwayat untuk pasien ini.");
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca rekam medis.");
            e.printStackTrace();
        }
    }

    public static void doctorLogout(String doctorID) {
        File inputFile = new File("active_doctors.txt");
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(doctorID + "|")) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    found = true;
                }
            }

            if (found) {
                System.out.println("Logout berhasil untuk dokter " + doctorID);
            } else {
                System.out.println("Dokter tidak ditemukan dalam daftar aktif.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Gagal update file logout.");
        }
    }

    public static void menuAdmin() {
        int choice;
        do {
            ClearTerminal();
            System.out.println("=============================================");
            System.out.println("||               ADMIN MENU                ||");
            System.out.println("=============================================");
            System.out.println("1. Add New Patient");
            System.out.println("2. Remove Patient by ID");
            System.out.println("3. Search Patient by Name");
            System.out.println("4. Display All Patients");
            System.out.println("5. Doctor Login");
            System.out.println("6. Doctor Logout");
            System.out.println("7. View Last Logged-in Doctor");
            System.out.println("8. Schedule Appointment");
            System.out.println("9. Process Appointment");
            System.out.println("10. Display Upcoming Appointments");
            System.out.println("11. Search Patient by ID (BST)");
            System.out.println("12. Display All Patients (BST Inorder)");
            System.out.println("13. Logout");
            choice = InputValidator.getValidInteger("Pilihanmu : ",1,Integer.MAX_VALUE);

            switch (choice) {
                case 1:
                    ClearTerminal();
                    addNewPatient();
                    pressEnterToContinue();
                    break;
                case 2:
                    ClearTerminal();
                    removePatientByID();
                    pressEnterToContinue();
                    break;
                case 3:
                    ClearTerminal();
                    searchPatientByName();
                    pressEnterToContinue();
                    break;
                case 4:
                    ClearTerminal();
                    displayAllPatients();
                    pressEnterToContinue();
                    break;
                case 5:
                    ClearTerminal();
                    doctorLoginAdmin();
                    pressEnterToContinue();
                    break;
                case 6:
                    ClearTerminal();
                    doctorLogoutAdmin();
                    pressEnterToContinue();
                    break;
                case 7:
                    ClearTerminal();
                    viewLastLoggedInDoctor();
                    pressEnterToContinue();
                    break;
                case 8:
                    ClearTerminal();
                    scheduleAppointmentAdmin();
                    pressEnterToContinue();
                    break;
                case 9:
                    ClearTerminal();
                    System.out.println("Akses ditolak. Fitur ini khusus untuk dokter!");
                    pressEnterToContinue();
                    break;
                case 10:
                    ClearTerminal();
                    displayUpcomingAppointments();
                    pressEnterToContinue();
                    break;
                case 11:
                    ClearTerminal();
                    searchPatientByIDBST();
                    pressEnterToContinue();
                    break;
                case 12:
                    ClearTerminal();
                    displayAllPatientsBST();
                    pressEnterToContinue();
                    break;
                case 13:
                    whoLogin = -1;
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    pressEnterToContinue();
            }
        } while (choice != 13);
    }

    public static void addNewPatient() {
        String name = InputValidator.getValidName("Masukan nama Pasien: ");
        String pass = InputValidator.getValidPassword("Masukkan Password : ");
        int age = InputValidator.getValidAge("Masukkan umur : ");
        String address = InputValidator.getValidAddress("Masukkan alamat : ");
        String hp = InputValidator.getValidPhoneNumber("Masukkan nomor handphone : ");

        String ID = IDGenerator.generatePatientID(); 
        String temp = ID + "|" + name + "|" + pass + "|" + age + "|" + address + "|" + hp;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("patients.txt", true))) {
            writer.write(temp);
            writer.newLine();
            patientBST.insert(new Patient(ID, name, pass, age, address, hp));
            System.out.println("Pasien berhasil ditambahkan!");
        } catch (IOException e) {
            System.out.println("Gagal menambahkan pasien!");
            e.printStackTrace();
        }
    }

    public static void removePatientByID() {
        System.out.print("Masukkan Id pasien yang akan dihapus : ");
        String id = scanner.nextLine();

        // Menghapus dari FIle
        File inputFile = new File("patients.txt");
        File tempFile = new File("temp.txt");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && parts[0].equals(id)) {
                    found = true;
                    continue; 
                }
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Gagal menghapus pasien dari file.");
            e.printStackTrace();
            return;
        }

        if (found) {
            patientBST.delete(id);
            
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                System.out.println("Gagal update file pasien.");
            } else {
                System.out.println("Pasien dengan ID " + id + " berhasil dihapus.");
            }
        } else {
            System.out.println("Pasien dengan ID " + id + " tidak ditemukan.");
            tempFile.delete();
        }
    }

    public static void searchPatientByName() {
        String name = InputValidator.getValidName("Masukkan nama pasien : ");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("patients.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[1].toLowerCase().contains(name.toLowerCase())) {
                    System.out.println("ID: " + parts[0]);
                    System.out.println("Nama: " + parts[1]);
                    System.out.println("Umur: " + parts[3]);
                    System.out.println("Alamat: " + parts[4]);
                    System.out.println("No HP: " + parts[5]);
                    System.out.println("----------------------");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("Pasien dengan nama '" + name + "' tidak ditemukan.");
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pasien.");
            e.printStackTrace();
        }
    }

    public static void displayAllPatients() {
        System.out.println("\nDaftar Semua Pasien:");
        try (BufferedReader reader = new BufferedReader(new FileReader("patients.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    System.out.println("ID: " + parts[0]);
                    System.out.println("Nama: " + parts[1]);
                    System.out.println("Umur: " + parts[3]);
                    System.out.println("Alamat: " + parts[4]);
                    System.out.println("No HP: " + parts[5]);
                    System.out.println("----------------------");
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pasien.");
            e.printStackTrace();
        }
    }

    public static void doctorLoginAdmin() {
        System.out.print("Masukkan Id dokter : ");
        String id = scanner.nextLine();
        String pass = InputValidator.getValidPassword("Masukkan Password : ");

        recordDoctorsLogin(id, pass);
    }

    public static void doctorLogoutAdmin() {
        System.out.print("Masukkan Id dokter yang akan dihapus : ");
        String id = scanner.nextLine();
        doctorLogout(id);
    }

    public static void viewLastLoggedInDoctor() {
        System.out.println("\nDokter Terakhir Login:");
        try (BufferedReader reader = new BufferedReader(new FileReader("active_doctors.txt"))) {
            String lastLine = null, line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }

            if (lastLine != null) {
                String[] parts = lastLine.split("\\|");
                System.out.println("ID: " + parts[0]);
                System.out.println("Nama: " + parts[1]);
                System.out.println("Spesialisasi: " + parts[2]);
                System.out.println("Waktu Login: " + parts[3]);
            } else {
                System.out.println("Belum ada dokter yang login.");
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file dokter aktif.");
            e.printStackTrace();
        }
    }

    public static void scheduleAppointmentAdmin() {
        System.out.print("Masukkan Id Pasien : ");
        String patientID = scanner.nextLine();
        System.out.print("Masukkan Id dokter : ");
        String doctorID = scanner.nextLine();

        String doctorName = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("Doctors.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equals(doctorID)) {
                    doctorName = parts[1];
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca data dokter.");
            e.printStackTrace();
            return;
        }

        if (doctorName.isEmpty()) {
            System.out.println("Dokter dengan ID " + doctorID + " tidak ditemukan.");
            return;
        }

        System.out.print("Masukkan jam Appointment (hh:mm) : ");
        String time = scanner.nextLine();

        Appointment appt = new Appointment(patientID, doctorID, doctorName, time);
        appointmentQueue.addAppointment(appt);
        System.out.println("Appointment berhasil dijadwalkan!");
    }

    public static void processAppointmentAdmin() {
        if (appointmentQueue.isEmpty()) {
            System.out.println("Tidak ada appointment yang perlu diproses.");
            return;
        }

        Appointment appt = appointmentQueue.processAppointment();
        System.out.println("\nMemproses appointment untuk:");
        System.out.println("Pasien: " + appt.patientID);
        System.out.println("Dokter: " + appt.doctorName);
        System.out.println("Jam: " + appt.time);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("processed_appointments.txt", true))) {
            writer.write(appt.patientID + "|" + appt.doctorID + "|" + appt.time);
            writer.newLine();
            System.out.println("Appointment berhasil diproses.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan appointment yang diproses.");
            e.printStackTrace();
        }
    }

    public static void displayUpcomingAppointments() {
        System.out.println("\nAppointment yang Akan Datang:");
        appointmentQueue.displayAppointments();
    }

    public static void searchPatientByIDBST() {
        System.out.println("Masukkan Id pasien : ");
        String id = scanner.nextLine();
        Patient patient = patientBST.search(id);
        if (patient != null) {
            System.out.println("\nData Pasien Ditemukan:");
            System.out.println("ID: " + patient.getID());
            System.out.println("Nama: " + patient.getName());
            System.out.println("Umur: " + patient.getAge());
            System.out.println("Alamat: " + patient.getAddress());
            System.out.println("No HP: " + patient.getPhone());
        } else {
            System.out.println("Pasien dengan ID " + id + " tidak ditemukan.");
        }
    }

    public static void displayAllPatientsBST() {
        System.out.println("\nDaftar Semua Pasien (BST Inorder):");
        patientBST.inorder();
    }

    public static String getNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input tidak boleh kosong!");
            }
        } while (input.isEmpty());
        return input;
    }

    public static boolean validateWorkingHours(String hours) {
        return hours.matches("^\\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}$");
    }

    public static void ClearTerminal() {
    try {
        if (System.getProperty("os.name").contains("Windows")) {
            // Windows
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            // Unix/Linux/Mac
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    } catch (Exception e) {
        System.out.println("\n".repeat(50));
    }
}
}
