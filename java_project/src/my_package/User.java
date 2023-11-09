package my_package;

public class User {
    String name;
    int id; // auto generated
    String dateOfBirth;
    String email;
    String phoneNumber;
    String address;
    String password;
    // ArrayList<String> initiatives;
    // ArrayList<String> volunteeringJobs;

    // Constructor
    public User(String name, String dateOfBirth, String email, String phoneNumber, String address, String password) {
        this.name = name;
        this.id = generateId(); // Auto generate id
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        // this.initiatives = new ArrayList<>();
        // this.volunteeringJobs = new ArrayList<>();
    }
    
    // Method to add an initiative to the user's list
    public void registerInitiative(String initiative) {
        // initiatives.add(initiative);
    }

    // Method to add a volunteering job to the user's list
    public void registerVolunteeringJob(String job) {
        // volunteeringJobs.add(job);
    }

    // Method to generate an auto-incremented id (for demonstration purposes)
    private static int idCounter = 1;

    private int generateId() {
        return idCounter++;
    }
}
