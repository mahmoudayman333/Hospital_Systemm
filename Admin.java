public class Admin extends User {

    public Admin(String name, String id, String username, String password) {
        super(name, id, username, password);
    }

    @Override
    public void viewPersonalInformation() {
        System.out.println("=== Admin Info ===");
        System.out.println("Name     : " + getName());
        System.out.println("ID       : " + getId());
        System.out.println("Username : " + getUsername());
    }
}
