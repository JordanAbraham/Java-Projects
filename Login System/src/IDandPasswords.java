import java.io.*;
import java.util.HashMap;

public class IDandPasswords {

    HashMap<String, String> logininfo = new HashMap<>();

    IDandPasswords() {
        loadCredentialsFromFile("users.txt");
    }

    private void loadCredentialsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    logininfo.put(username, password);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public HashMap<String, String> getLoginInfo() {
        return logininfo;
    }
}
