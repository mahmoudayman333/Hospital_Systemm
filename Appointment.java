public class Appointment {

    private String appointmentId;
    private Patient patient;
    private Doctor  doctor;
    private String  date;
    private String  time;
    private String  status;

    public Appointment(String appointmentId, Patient patient, String date,
                       Doctor doctor, String time, String status) {
        this.appointmentId = appointmentId;
        this.patient       = patient;
        this.date          = date;
        this.doctor        = doctor;
        this.time          = time;
        this.status        = status;
    }

    public String  getAppointmentId()              { return appointmentId; }
    public void    setAppointmentId(String id)     { this.appointmentId = id; }

    public Patient getPatient()                    { return patient; }
    public void  setPatient(Patient patient)     { this.patient = patient; }

    public Doctor  getDoctor()                     { return doctor; }
    public void    setDoctor(Doctor doctor)        { this.doctor = doctor; }

    public String  getDate()                       { return date; }
    public void    setDate(String date)            { this.date = date; }

    public String  getTime()                       { return time; }
    public void    setTime(String time)            { this.time = time; }

    public String  getStatus()                     { return status; }
    public void    setStatus(String status)        { this.status = status; }

    public void viewAppointment() {
        System.out.println("  Appointment ID : " + appointmentId);
        System.out.println("  Patient  : " + patient.getName() + " (" + patient.getId() + ")");
        System.out.println("  Doctor  : " + doctor.getDisplayName() + " (" + doctor.getId() + ")");
        System.out.println("  Date   : " + date);
        System.out.println("  Time    : " + time);
        System.out.println("  Status   : " + status);
    }
}
