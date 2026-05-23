public class HospitalSystem {

    private Doctor[]      doctors      = new Doctor[100];
    private Patient[]     patients = new Patient[100];
    private Appointment[] appointments = new Appointment[500];

    private int doctorCount   = 0;
    private int patientCount     = 0;
    private int appointmentCount = 0;

    public Doctor[]   getDoctors()                          { return doctors; }
    public Patient[]    getPatients()                        { return patients; }
    public Appointment[] getAppointments()                    { return appointments; }

    public int getDoctorCount()                              { return doctorCount; }
    public int getPatientCount()                             { return patientCount; }
    public int getAppointmentCount()                         { return appointmentCount; }

    public void setAppointmentCount(int appointmentCount)     { this.appointmentCount = appointmentCount; }

    public void addDoctor(Doctor doctor) {
        if (doctorCount >= doctors.length) {
            System.out.println("erorr");
            return;
        }
        if (findDoctorById(doctor.getId()) != null) {
            System.out.println("Error: Doctor ID " + doctor.getId() + " already exists.");
            return;
        }
        doctors[doctorCount++] = doctor;
        System.out.println("Doctor added successfully: " + doctor.getDisplayName());
    }

    public void registerPatient(Patient patient) {
        if (patientCount >= patients.length) {
            System.out.println("errpr");
            return;
        }
        if (findPatientById(patient.getId()) != null) {
            System.out.println("Error: Patient ID " + patient.getId() + " already exists.");
            return;
        }
        patients[patientCount++] = patient;
        System.out.println("Patient registered successfully: " + patient.getName());
    }

    public void assignPatientToDoctor(String doctorId, String patientId) {
        Doctor  d = findDoctorById(doctorId);
        Patient p = findPatientById(patientId);

        if (d == null) { System.out.println("Error: Doctor ID not found."); return; }
        if (p == null) { System.out.println("Error: Patient ID not found."); return; }

        p.setAssignedDoctor(d);
        d.addPatient(p);
        System.out.println("Patient " + p.getName() + " assigned to " + d.getDisplayName());
    }

    public void createAppointment(String appId, String patientId, String doctorId, String date, String time) {
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Appointment date cannot be empty.");
            return;
        }
        if (time == null || time.trim().isEmpty()) {
            System.out.println("Error: Appointment time cannot be empty.");
            return;
        }
        if (!time.matches("\\d{2}:\\d{2}")) {
            System.out.println("Error: Time format must be HH:mm ");
            return;
        }

        // Check duplicate appointment ID
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].getAppointmentId().equals(appId)) {
                System.out.println("Error: Appointment ID " + appId + " already exists.");
                return;
            }
        }

        Patient p = findPatientById(patientId);
        Doctor  d = findDoctorById(doctorId);

        if (p == null) { System.out.println("Error: Patient not found."); return; }
        if (d == null) { System.out.println("Error: Doctor not found.");  return; }

        if (p.getAssignedDoctor() == null
                || !p.getAssignedDoctor().getId().equals(doctorId)) {
            System.out.println("Error: Patient is not assigned to this doctor.");
            return;
        }

        String finalTime = resolveTimeConflict(doctorId, date, time);

        Appointment newApp = new Appointment(appId, p, date, d, finalTime, "Confirmed");
        appointments[appointmentCount++] = newApp;
        d.addAppointment(newApp);
        p.addAppointment(newApp);

        System.out.println("Appointment created successfully!");
        System.out.println("  Patient: " + p.getName() + "  |  Doctor: " + d.getDisplayName()
                + "  |  Date: " + date + "  |  Time: " + finalTime);
    }

    public void viewAllDoctors() {
        if (doctorCount == 0) { System.out.println("No doctors registered."); return; }
        System.out.println("=== All Doctors (" + doctorCount + ") ===");
        for (int i = 0; i < doctorCount; i++) {
            System.out.println("  " + (i + 1) + ".");
            doctors[i].viewPersonalInformation();
        }
    }

    public void viewAllPatients() {
        if (patientCount == 0) { System.out.println("No patients registered."); return; }
        System.out.println("=== All Patients (" + patientCount + ") ===");
        for (int i = 0; i < patientCount; i++) {
            System.out.println("  " + (i + 1) + ".");
            patients[i].viewPersonalInformation();
        }
    }

    public void viewAllAppointments() {
        if (appointmentCount == 0) { System.out.println("No appointments found."); return; }
        System.out.println("=== All Appointments (" + appointmentCount + ") ===");
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println("  " + (i + 1) + ".");
            appointments[i].viewAppointment();
            System.out.println("  --------------------");
        }
    }

    public Doctor searchDoctorById(String id) {
        Doctor d = findDoctorById(id);
        if (d != null) {
            d.viewPersonalInformation();
        } else {
            System.out.println("Doctor with ID " + id + " not found.");
        }
        return d;
    }

    public Patient searchPatientById(String id) {
        Patient p = findPatientById(id);
        if (p != null) {
            p.viewPersonalInformation();
        } else {
            System.out.println("Patient with ID " + id + " not found.");
        }
        return p;
    }

    public Doctor findDoctorById(String id) {
        for (int i = 0; i < doctorCount; i++) {
            if (doctors[i].getId().equals(id)) return doctors[i];
        }
        return null;
    }

    public Patient findPatientById(String id) {
        for (int i = 0; i < patientCount; i++) {
            if (patients[i].getId().equals(id)) return patients[i];
        }
        return null;
    }


    public void generateReports() {
        System.out.println("\n========== Hospital System Report ==========");
        System.out.println("Total Doctors      : " + doctorCount);
        System.out.println("Total Patients     : " + patientCount);
        System.out.println("Total Appointments : " + appointmentCount);

        int confirmed = 0, completed = 0, cancelled = 0;
        for (int i = 0; i < appointmentCount; i++) {
            String s = appointments[i].getStatus().toLowerCase();
            if      (s.equals("confirmed"))  confirmed++;
            else if (s.equals("completed"))  completed++;
            else if (s.equals("cancelled"))  cancelled++;
        }
        System.out.println("\nAppointment Breakdown:");
        System.out.println("  Confirmed  : " + confirmed);
        System.out.println("  Completed  : " + completed);
        System.out.println("  Cancelled  : " + cancelled);

        if (doctorCount == 0) return;

        Doctor[] sorted = new Doctor[doctorCount];
        for (int i = 0; i < doctorCount; i++) sorted[i] = doctors[i];

        // Bubble sort descending
        for (int i = 0; i < doctorCount - 1; i++) {
            for (int j = 0; j < doctorCount - i - 1; j++) {
                if (sorted[j].getAppointmentCount() < sorted[j + 1].getAppointmentCount()) {
                    Doctor temp = sorted[j];
                    sorted[j]   = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }

        System.out.println("\nTop 3 Doctors by Appointments:");
        int limit = Math.min(3, doctorCount);
        for (int i = 0; i < limit; i++) {
            System.out.println("  " + (i + 1) + ". " + sorted[i].getDisplayName()
                    + " (ID: " + sorted[i].getId() + ")"
                    + " - " + sorted[i].getAppointmentCount() + " appointments");
        }
    }

    private String resolveTimeConflict(String doctorId, String date, String time) {
        String finalTime = time;
        boolean conflict = true;
        while (conflict) {
            conflict = false;
            for (int i = 0; i < appointmentCount; i++) {
                Appointment a = appointments[i];
                if (a.getDoctor().getId().equals(doctorId)
                        && a.getDate().equals(date)&& a.getTime().equals(finalTime)) {
                    String[] parts = finalTime.split(":");
                    int h = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]) + 20;
                if (m >= 60) { h++; m -= 60; }
                    if (h >= 24)   h = 0;
                    finalTime = String.format("%02d:%02d", h, m);
                    System.out.println("  Time conflict at " + time
                            + ", shifted to: " + finalTime);
                    conflict = true;
                    break;
                }
            }
        }
        return finalTime;
    }
}
