import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HospitalSystem system = new HospitalSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("Loading data...");
        FileManager.loadAllData(system);
        System.out.println("System ready.\n");

        while (true) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║   Hospital Management System         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Login as Admin                   ║");
            System.out.println("║  2. Login as Doctor                  ║");
            System.out.println("║  3. Login as Patient                 ║");
            System.out.println("║  4. Exit                             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Select: ");

            int mainChoice = readInt(sc);
            sc.nextLine();

            if (mainChoice == 4) {
                FileManager.saveAllData(system);
                System.out.println("Goodbye!");
                break;
            }

            if (mainChoice < 1 || mainChoice > 3) {
                System.out.println("Invalid option.\n");
                continue;
            }

            System.out.print("Username: ");
            String username = sc.nextLine().trim();
            System.out.print("Password: ");
            String password = sc.nextLine().trim();

            switch (mainChoice) {

                case 1:
                    if (username.equals("admin") && password.equals("admin123")) {
                        System.out.println("Welcome, Admin!");
                        adminMenu(system, sc);
                    } else {
                        System.out.println("Invalid Admin credentials.");
                    }
                    break;

                case 2:
                    Doctor loggedDoc = null;
                    for (int i = 0; i < system.getDoctorCount(); i++) {
                        Doctor d = system.getDoctors()[i];
                        if (d.getUsername().equals(username) && d.getPassword().equals(password)) {
                            loggedDoc = d;
                            break;
                        }
                    }
                    if (loggedDoc != null) {
                        System.out.println("Welcome, " + loggedDoc.getDisplayName() + "!");
                        doctorMenu(loggedDoc, system, sc);
                    } else {
                        System.out.println("Invalid Doctor credentials.");
                    }
                    break;

                case 3:
                    Patient loggedPat = null;
                    for (int i = 0; i < system.getPatientCount(); i++) {
                        Patient p = system.getPatients()[i];
                        if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                            loggedPat = p;
                            break;
                        }
                    }
                    if (loggedPat != null) {
                        System.out.println("Welcome, " + loggedPat.getName() + "!");
                        patientMenu(loggedPat, system, sc);
                    } else {
                        System.out.println("Invalid Patient credentials.");
                    }
                    break;
            }
        }
        sc.close();
    }

    static void adminMenu(HospitalSystem system, Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println(" 1. Add Doctor");
            System.out.println(" 2. Register Patient");
            System.out.println(" 3. Assign Patient to Doctor");
            System.out.println(" 4. Create Appointment");
            System.out.println(" 5. View All Doctors");
            System.out.println(" 6. View All Patients");
            System.out.println(" 7. View All Appointments");
            System.out.println(" 8. Search Patient by ID");
            System.out.println(" 9. Search Doctor by ID");
            System.out.println("10. Generate Reports");
            System.out.println("11. Save Data");
            System.out.println("12. Logout");
            System.out.print("Select: ");

            int choice = readInt(sc);
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Doctor ID       : "); String did   = sc.nextLine().trim();
                    System.out.print("Name            : "); String dname = sc.nextLine().trim();
                    System.out.print("Username        : "); String duser = sc.nextLine().trim();
                    System.out.print("Password        : "); String dpass = sc.nextLine().trim();
                    System.out.print("Specialization  : "); String dspec = sc.nextLine().trim();
                    System.out.print("Phone           : "); String dph   = sc.nextLine().trim();
                    System.out.print("Department      : "); String ddept = sc.nextLine().trim();
                    system.addDoctor(new Doctor(dname, did, duser, dpass, dspec, dph, ddept));
                    break;

                case 2:
                    System.out.print("Patient ID      : "); String pid   = sc.nextLine().trim();
                    System.out.print("Name            : "); String pname = sc.nextLine().trim();
                    System.out.print("Username        : "); String puser = sc.nextLine().trim();
                    System.out.print("Password        : "); String ppass = sc.nextLine().trim();
                    System.out.print("Age             : ");
                    int age = readInt(sc); sc.nextLine();
                    System.out.print("Gender (M/F)    : "); String pgend = sc.nextLine().trim();
                    System.out.print("Phone           : "); String pph   = sc.nextLine().trim();
                    system.registerPatient(new Patient(pname, pid, puser, ppass, age, pgend, pph, null));
                    break;

                case 3:
                    System.out.print("Doctor ID  : "); String aDocId = sc.nextLine().trim();
                    System.out.print("Patient ID : "); String aPatId = sc.nextLine().trim();
                    system.assignPatientToDoctor(aDocId, aPatId);
                    break;

                case 4:
                    System.out.print("Appointment ID : "); String appId  = sc.nextLine().trim();
                    System.out.print("Patient ID     : "); String patId  = sc.nextLine().trim();
                    System.out.print("Doctor ID      : "); String docId  = sc.nextLine().trim();
                    System.out.print("Date (YYYY-MM-DD): "); String date = sc.nextLine().trim();
                    System.out.print("Time (HH:mm)   : "); String time   = sc.nextLine().trim();
                    system.createAppointment(appId, patId, docId, date, time);
                    break;

                case 5:  system.viewAllDoctors();      break;
                case 6:  system.viewAllPatients();     break;
                case 7:  system.viewAllAppointments(); break;

                case 8:
                    System.out.print("Patient ID: "); String spid = sc.nextLine().trim();
                    system.searchPatientById(spid);
                    break;

                case 9:
                    System.out.print("Doctor ID: "); String sdid = sc.nextLine().trim();
                    system.searchDoctorById(sdid);
                    break;

                case 10: system.generateReports();          break;
                case 11: FileManager.saveAllData(system);   break;
                case 12: System.out.println("Logged out."); return;

                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void doctorMenu(Doctor doc, HospitalSystem system, Scanner sc) {
        while (true) {
            System.out.println("\n--- Doctor Menu: " + doc.getDisplayName() + " ---");
            System.out.println("1. View My Profile");
            System.out.println("2. View Assigned Patients");
            System.out.println("3. View My Appointments");
            System.out.println("4. Update Appointment Status");
            System.out.println("5. Logout");
            System.out.print("Select: ");

            int choice = readInt(sc);
            sc.nextLine();

            switch (choice) {
                case 1: doc.viewPersonalInformation(); break;
                case 2: doc.viewAssignedPatients();    break;
                case 3: doc.viewAppointments();        break;

                case 4:
                    System.out.print("Appointment ID: "); String aid = sc.nextLine().trim();
                    System.out.println("1. Confirmed  2. Completed  3. Cancelled");
                    System.out.print("Choose new status: ");
                    int statusChoice = readInt(sc); sc.nextLine();
                    doc.updateAppointmentStatus(aid, statusChoice);
                    FileManager.saveAllData(system);
                    break;

                case 5: System.out.println("Logged out."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void patientMenu(Patient pat, HospitalSystem system, Scanner sc) {
        while (true) {
            System.out.println("\n--- Patient Menu: " + pat.getName() + " ---");
            System.out.println("1. View My Profile");
            System.out.println("2. View Assigned Doctor");
            System.out.println("3. View My Appointments");
            System.out.println("4. Book Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Logout");
            System.out.print("Select: ");

            int choice = readInt(sc);
            sc.nextLine();

            switch (choice) {
                case 1: pat.viewPersonalInformation(); break;
                case 2: pat.viewAssignedDoctor();      break;
                case 3: pat.viewAppointments();        break;

                case 4:
                    System.out.print("Appointment ID   : "); String bAppId = sc.nextLine().trim();
                    System.out.print("Date (YYYY-MM-DD): "); String bDate  = sc.nextLine().trim();
                    System.out.print("Time (HH:mm)     : "); String bTime  = sc.nextLine().trim();
                    pat.bookAppointment(bAppId, bDate, bTime, system);
                    FileManager.saveAllData(system);
                    break;

                case 5:
                    System.out.print("Appointment ID to cancel: "); String cId = sc.nextLine().trim();
                    pat.cancelAppointment(cId);
                    FileManager.saveAllData(system);
                    break;

                case 6: System.out.println("Logged out."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a number: ");
            sc.next();
        }
        return sc.nextInt();
    }
}
