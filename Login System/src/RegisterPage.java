import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RegisterPage {
    JFrame frame = new JFrame("Register");
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JPasswordField confirmField = new JPasswordField();
    JButton registerButton = new JButton("Register");
    JLabel messageLabel = new JLabel();

    JFrame loginFrame; //  to hold reference of the login screen

    //  Constructor accepts login frame
    RegisterPage(JFrame loginFrame) {
        this.loginFrame = loginFrame;
        loginFrame.dispose(); // close login screen

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmLabel = new JLabel("Confirm Password:");

        usernameLabel.setBounds(50, 50, 100, 25);
        passwordLabel.setBounds(50, 100, 100, 25);
        confirmLabel.setBounds(50, 150, 150, 25);
        usernameField.setBounds(180, 50, 150, 25);
        passwordField.setBounds(180, 100, 150, 25);
        confirmField.setBounds(180, 150, 150, 25);
        registerButton.setBounds(140, 200, 100, 30);
        messageLabel.setBounds(50, 250, 300, 25);

        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setFont(new Font(null, Font.BOLD, 14));

        frame.add(usernameLabel);
        frame.add(passwordLabel);
        frame.add(confirmLabel);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.add(confirmField);
        frame.add(registerButton);
        frame.add(messageLabel);

        registerButton.addActionListener(e -> registerUser());

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Passwords do not match!");
            return;
        }
        
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*")) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Password must be 8+ chars, with a digit and uppercase.");
            return;
        }


        if (isUsernameTaken(username)) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Username already exists!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
            writer.flush();

            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Registration successful!");

            frame.dispose(); // close registration window

            //  Reopen fresh login screen (reloads users.txt)
            new LoginPage(new IDandPasswords().getLoginInfo());

        } catch (IOException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error saving user!");
        }
    }

    private boolean isUsernameTaken(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException ignored) {}
        return false;
    }
}
