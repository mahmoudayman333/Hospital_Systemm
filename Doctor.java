public class Doctor extends User {

    private String specialization;
    private String phone;
    private String department;
    private Patient[]     patients     = new Patient[50];
    private Appointment[] appointments = new Appointment[100];
    private int patientCount     = 0;
    private int appointmentCount = 0;

    public Doctor(String name, String id, String username, String password,
                  String specialization, String phone, String department) {
        super(name, id, username, password);
        this.specialization = specialization;
        this.phone          = phone;
        this.department     = department;
    }

    public String getSpecialization()                 { return specialization; }
    public void   setSpecialization(String s)         { this.specialization = s; }

    public String getPhone()                          { return phone; }
    public void  setPhone(String phone)              { this.phone = phone; }

    public String getDepartment()                     { return department; }
    public void  setDepartment(String department)    { this.department = department; }

    public Patient[]     getPatients()                { return patients; }
    public Appointment[] getAppointments()            { return appointments; }
    public int    getPatientCount()            { return patientCount; }
    public int     getAppointmentCount()        { return appointmentCount; }

    public String getDisplayName() {
        return getName().startsWith("Dr.") ? getName() : "Dr. " + getName();
    }

    @Override
    public void viewPersonalInformation() {
        System.out.println("=== Doctor Info ===");
        System.out.println("  ID   : " + getId());
        System.out.println("  Name  : " + getDisplayName());
        System.out.println("  Username : " + getUsername());
        System.out.println("  Phone   : " + getPhone());
        System.out.println("  Specialization : " + getSpecialization());
        System.out.println("  Department  : " + getDepartment());
        System.out.println("  Patients  : " + patientCount);
        System.out.println("  Appointments : " + appointmentCount);
    }

    public void addPatient(Patient patient) {
        for (int i = 0; i < patientCount; i++) {
            if (patients[i].getId().equals(patient.getId())) return;
        }
        if (patientCount < patients.length) {
            patients[patientCount++] = patient;
        } else {
            System.out.println("Patient  is full for Dr. " + getName());
        }
    }

    public void addAppointment(Appointment appointment) {
        if (appointmentCount < appointments.length) {
            appointments[appointmentCount++] = appointment;
        } else {
            System.out.println("Appointment list is full for Dr. " + getName());
        }
    }

    public void viewAssignedPatients() {
        if (patientCount == 0) {
            System.out.println("No patients assigned.");
            return;
        }
        System.out.println("=== Assigned Patients ===");
        for (int i = 0; i < patientCount; i++) {
            System.out.println((i + 1) + ". " + patients[i].getName()
                    + " (ID: " + patients[i].getId() + ")");
        }
    }

    public void viewAppointments() {
        if (appointmentCount == 0) {
            System.out.println("No appointments found.");
            return;
        }
        System.out.println("=== My Appointments ===");
        for (int i = 0; i < appointmentCount; i++) {
            appointments[i].viewAppointment();
            System.out.println("  --------------------");
        }
    }


    public void updateAppointmentStatus(String appointmentId, int choice) {
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].getAppointmentId().equals(appointmentId)) {

                String currentStatus = appointments[i].getStatus();

                if (currentStatus.equalsIgnoreCase("Cancelled") && choice == 2) {
                    System.out.println("Error: A cancelled appointment cannot be marked as Completed.");
                    return;
                }

                String newStatus;
                switch (choice) {
                    case 1: newStatus = "Confirmed";  break;
                    case 2: newStatus = "Completed";  break;
                    case 3: newStatus = "Cancelled";  break;
                    default:
                        System.out.println("Invalid choice.");
                        return;
                }
                appointments[i].setStatus(newStatus);
                System.out.println("Appointment " + appointmentId
                        + " status updated to: " + newStatus);
                return;
            }
        }
        System.out.println("Error: Appointment ID not found in your list.");
    }
}
