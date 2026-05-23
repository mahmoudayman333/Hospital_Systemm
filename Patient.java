public class Patient extends User {

    private int    age;
    private String gender;
    private String phone;
    private Doctor assignedDoctor;

    private Appointment[] appointments    = new Appointment[50];
    private int           appointmentCount = 0;

    public Patient(String name, String id, String username, String password,
                   int age, String gender, String phone, Doctor assignedDoctor) {
        super(name, id, username, password);
        this.age            = age;
        this.gender         = gender;
        this.phone          = phone;
        this.assignedDoctor = assignedDoctor;
    }

    public int   getAge()                              { return age; }
    public void  setAge(int age)                       { this.age = age; }

    public String getGender()                           { return gender; }
    public void   setGender(String gender)              { this.gender = gender; }

    public String getPhone()                            { return phone; }
    public void   setPhone(String phone)                { this.phone = phone; }

    public Doctor getAssignedDoctor()                   { return assignedDoctor; }
    public void   setAssignedDoctor(Doctor d)           { this.assignedDoctor = d; }

    public Appointment[] getAppointments()              { return appointments; }
    public int           getAppointmentCount()          { return appointmentCount; }

    @Override
    public void viewPersonalInformation() {
        System.out.println("=== Patient Info ===");
        System.out.println("  ID             : " + getId());
        System.out.println("  Name           : " + getName());
        System.out.println("  Username       : " + getUsername());
        System.out.println("  Age            : " + age);
        System.out.println("  Gender         : " + gender);
        System.out.println("  Phone          : " + phone);
        if (assignedDoctor != null) {
            System.out.println("  Assigned Doctor: " + assignedDoctor.getDisplayName()
                    + " (" + assignedDoctor.getId() + ")");
        } else {
            System.out.println("  Assigned Doctor: Not assigned yet");
        }
    }

    public void viewAssignedDoctor() {
        if (assignedDoctor != null) {
            System.out.println("=== Assigned Doctor ===");
            System.out.println("  Name    : " + assignedDoctor.getDisplayName());
            System.out.println("  ID   : " + assignedDoctor.getId());
            System.out.println("  Specialization : " + assignedDoctor.getSpecialization());
            System.out.println("  Department  : " + assignedDoctor.getDepartment());
            System.out.println("  Phone : " + assignedDoctor.getPhone());
        } else {
            System.out.println("You have not been assigned to a doctor yet.");
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

    public void addAppointment(Appointment appointment) {
        if (appointmentCount < appointments.length) {
            appointments[appointmentCount++] = appointment;
        } else {
            System.out.println("Appointment list is full.");
        }
    }


    public void bookAppointment(String appId, String date, String time,
                                HospitalSystem system) {
        for (int i = 0; i < system.getAppointmentCount(); i++) {
            if (system.getAppointments()[i].getAppointmentId().equals(appId)) {
                System.out.println("Error: Appointment ID " + appId + " already exists.");
                return;
            }
        }

        if (assignedDoctor == null) {
            System.out.println("Error: You are not assigned to any doctor yet.");
            return;
        }
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Appointment date cannot be empty.");
            return;
        }
        if (time == null || time.trim().isEmpty()) {
            System.out.println("Error: Appointment time cannot be empty.");
            return;
        }
        if (!time.matches("\\d{2}:\\d{2}")) {
            System.out.println("Error: Time format must be HH:mm (e.g. 09:30)");
            return;
        }

        String finalTime = resolveTimeConflict(assignedDoctor.getId(), date, time, system);

        Appointment newApp = new Appointment(appId, this, date, assignedDoctor, finalTime, "Confirmed");

        system.getAppointments()[system.getAppointmentCount()] = newApp;
        system.setAppointmentCount(system.getAppointmentCount() + 1);

        this.addAppointment(newApp);
        assignedDoctor.addAppointment(newApp);

        System.out.println("Appointment booked successfully!");
        System.out.println("  Date: " + date + "  |  Time: " + finalTime);
    }


    public void cancelAppointment(String appId) {
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].getAppointmentId().equals(appId)) {
                String currentStatus = appointments[i].getStatus();

                if (currentStatus.equalsIgnoreCase("Completed")) {
                    System.out.println("Error: Cannot cancel a completed appointment.");
                    return;
                }
                if (currentStatus.equalsIgnoreCase("Cancelled")) {
                    System.out.println("This appointment is already cancelled.");
                    return;
                }

                appointments[i].setStatus("Cancelled");
                System.out.println("Appointment " + appId + " has been cancelled.");
                return;
            }
        }
        System.out.println("Error: Appointment ID not found in your list.");
    }

    private String resolveTimeConflict(String doctorId, String date,
                                       String time, HospitalSystem system) {
        String finalTime = time;
        boolean conflict = true;
        while (conflict) {
            conflict = false;
            for (int i = 0; i < system.getAppointmentCount(); i++) {
                Appointment app = system.getAppointments()[i];
                if (app.getDoctor().getId().equals(doctorId)
                        && app.getDate().equals(date)
                        && app.getTime().equals(finalTime)) {
                    String[] parts = finalTime.split(":");
                    int h = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]) + 20;
                   if (m >= 60) { h++; m -= 60; }
                    if (h >= 24) h = 0;   // edge case midnight wrap
                    finalTime = String.format("%02d:%02d", h, m);
                    System.out.println("  Time conflict detected, shifted to: " + finalTime);
                    conflict = true;
                    break;
                }
            }
        }
        return finalTime;
    }
}
