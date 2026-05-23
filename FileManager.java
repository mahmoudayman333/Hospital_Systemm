import java.io.*;
import java.util.Scanner;

public class FileManager {

    private static final String DOCTORS_FILE      = "doctors.txt";
    private static final String PATIENTS_FILE     = "patients.txt";
    private static final String APPOINTMENTS_FILE = "appointments.txt";
    private static final String USERS_FILE        = "users.txt";


    public static void saveAllData(HospitalSystem system) {
        saveDoctors(system);
        savePatients(system);
        saveAppointments(system);
        saveUsers(system);
        System.out.println("All data saved successfully.");
    }

    private static void saveDoctors(HospitalSystem system) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DOCTORS_FILE))) {
            for (int i = 0; i < system.getDoctorCount(); i++) {
                Doctor d = system.getDoctors()[i];
                writer.println(d.getId() + "," + d.getName() + "," + d.getUsername() + ","
                        + d.getPassword() + "," + d.getSpecialization() + ","
                        + d.getPhone() + "," + d.getDepartment());
            }
        } catch (IOException e) {
            System.out.println("Error saving doctors: " + e.getMessage());
        }
    }

    private static void savePatients(HospitalSystem system) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PATIENTS_FILE))) {
            for (int i = 0; i < system.getPatientCount(); i++) {
                Patient p = system.getPatients()[i];
                String docId = (p.getAssignedDoctor() != null)
                        ? p.getAssignedDoctor().getId() : "None";
                writer.println(p.getId() + "," + p.getName() + "," + p.getUsername() + ","
                        + p.getPassword() + "," + p.getAge() + "," + p.getGender() + ","
                        + p.getPhone() + "," + docId);
            }
        } catch (IOException e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }

    private static void saveAppointments(HospitalSystem system) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (int i = 0; i < system.getAppointmentCount(); i++) {
                Appointment a = system.getAppointments()[i];
                writer.println(a.getAppointmentId() + "," + a.getPatient().getId() + ","
                        + a.getDoctor().getId() + "," + a.getDate() + ","
                        + a.getTime() + "," + a.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    private static void saveUsers(HospitalSystem system) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("admin,admin123,Admin,A001");

            for (int i = 0; i < system.getDoctorCount(); i++) {
                Doctor d = system.getDoctors()[i];
                writer.println(d.getUsername() + "," + d.getPassword() + ",Doctor," + d.getId());
            }
            for (int i = 0; i < system.getPatientCount(); i++) {
                Patient p = system.getPatients()[i];
                writer.println(p.getUsername() + "," + p.getPassword() + ",Patient," + p.getId());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static void loadAllData(HospitalSystem system) {
        loadDoctors(system);
        loadPatients(system);
        loadAppointments(system);
    }

    private static void loadDoctors(HospitalSystem system) {
        File file = new File(DOCTORS_FILE);
        if (!file.exists() || file.length() == 0) return;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] d = line.split(",");
                if (d.length < 7) continue;

                Doctor doctor = new Doctor(d[1], d[0], d[2], d[3], d[4], d[5], d[6]);
                system.addDoctor(doctor);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not fully load doctors file.");
        }
    }

    private static void loadPatients(HospitalSystem system) {
        File file = new File(PATIENTS_FILE);
        if (!file.exists() || file.length() == 0) return;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] p = line.split(",");
                if (p.length < 8) continue;

                int age = 0;
                try { age = Integer.parseInt(p[4]); } catch (NumberFormatException ignored) {}

                Patient patient = new Patient(p[1], p[0], p[2], p[3], age, p[5], p[6], null);

                if (!p[7].equals("None")) {
                    Doctor d = system.findDoctorById(p[7]);
                    if (d != null) {
                        patient.setAssignedDoctor(d);
                        d.addPatient(patient);
                    }
                }
                system.registerPatient(patient);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not fully load patients file.");
        }
    }

    private static void loadAppointments(HospitalSystem system) {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists() || file.length() == 0) return;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] a = line.split(",");
                if (a.length < 6) continue;

                Patient p = system.findPatientById(a[1]);
                Doctor  d = system.findDoctorById(a[2]);

                if (p != null && d != null) {
                    Appointment app = new Appointment(a[0], p, a[3], d, a[4], a[5]);

                    system.getAppointments()[system.getAppointmentCount()] = app;
                    system.setAppointmentCount(system.getAppointmentCount() + 1);

                    p.addAppointment(app);
                    d.addAppointment(app);
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not fully load appointments file.");
        }
    }
}
