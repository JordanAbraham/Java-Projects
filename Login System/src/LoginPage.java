import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class LoginPage implements ActionListener{
	
	int attempts = 0;
	
	JFrame frame = new JFrame();
	JButton loginButton = new JButton("Login");
	JButton resetButton = new JButton("Reset");
	JButton registerButton = new JButton("Register");
	JTextField userIDField = new JTextField();
	JPasswordField userPasswordField = new JPasswordField();
	JLabel userIDLabel = new JLabel("userID:");
	JLabel userPasswordLabel = new JLabel("password:");
	JLabel messageLabel = new JLabel();
	JCheckBox rememberMeBox = new JCheckBox("Remember Me");
	File rememberFile = new File("remember.txt");

	HashMap<String,String> logininfo = new HashMap<String,String>();
	
	LoginPage(HashMap<String,String> loginInfoOriginal){
		
		logininfo = loginInfoOriginal;
		
		userIDLabel.setBounds(50,100,75,25);
		userPasswordLabel.setBounds(50,150,75,25);
		
		messageLabel.setBounds(50, 250, 320, 35); // wider box
		messageLabel.setFont(new Font(null, Font.BOLD, 16)); // readable font
		messageLabel.setHorizontalAlignment(JLabel.CENTER); // center align

		
		userIDField.setBounds(125,100,200,25);
		userPasswordField.setBounds(125,150,200,25);
		
		loginButton.setBounds(125,200,100,25);
		loginButton.setFocusable(false);
		loginButton.addActionListener(this);
		
		resetButton.setBounds(225,200,100,25);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		
		rememberMeBox.setBounds(125, 230, 150, 25);
		frame.add(rememberMeBox);
		
		// Load remembered username if exists
		if (rememberFile.exists()) {
		    try (BufferedReader br = new BufferedReader(new FileReader(rememberFile))) {
		        String rememberedUser = br.readLine();
		        userIDField.setText(rememberedUser);
		        rememberMeBox.setSelected(true);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}


		
		registerButton.setBounds(150, 280, 100, 25);
		registerButton.setFocusable(false);
		registerButton.addActionListener(this);
		frame.add(registerButton);

		
		frame.add(userIDLabel);
		frame.add(userPasswordLabel);
		frame.add(messageLabel);
		frame.add(userIDField);
		frame.add(userPasswordField);
		frame.add(loginButton);
		frame.add(resetButton);
		frame.add(registerButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420,420);
		frame.setLayout(null);
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	    if (e.getSource() == resetButton) {
	        userIDField.setText("");
	        userPasswordField.setText("");
	    }

	    if (e.getSource() == loginButton) {

	        String userID = userIDField.getText();
	        String password = String.valueOf(userPasswordField.getPassword());

	        if (logininfo.containsKey(userID)) {
	            if (logininfo.get(userID).equals(password)) {
	                messageLabel.setForeground(Color.green);
	                messageLabel.setText("Login successful");
	             // Save username if Remember Me is checked
	                if (rememberMeBox.isSelected()) {
	                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(rememberFile))) {
	                        bw.write(userID);
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                    }
	                } else {
	                    // If unchecked, delete the file
	                    if (rememberFile.exists()) {
	                        rememberFile.delete();
	                    }
	                }

	                frame.dispose();
	                WelcomePage welcomePage = new WelcomePage(userID);
	            } else {
	                attempts++;
	                messageLabel.setForeground(Color.red);
	                if (attempts >= 3) {
	                    loginButton.setEnabled(false);
	                    messageLabel.setText("Account locked.");
	                } else {
	                    messageLabel.setText("Wrong password. Attempts left: " + (3 - attempts));
	                }
	            }
	        } else {
	            attempts++;
	            messageLabel.setForeground(Color.red);
	            if (attempts >= 3) {
	                loginButton.setEnabled(false);
	                messageLabel.setText("Account locked.");
	            } else {
	                messageLabel.setText("Username not found. Attempts left: " + (3 - attempts));
	            }
	        }
	    }

	    //  Register button logic here
	    if (e.getSource() == registerButton) {
	        new RegisterPage(frame);
	    }
	}
}
