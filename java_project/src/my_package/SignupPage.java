package my_package;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SignupPage extends JFrame implements ActionListener {

    JLabel headingLabel, nameLabel, dobLabel, emailLabel, phoneLabel, addressLabel, passwordLabel;
    JTextField nameField, dobField, emailField, phoneField, addressField;
    JPasswordField passwordField;
    JButton signupButton, switchToLoginButton;

    public SignupPage() {
        setTitle("Signup Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        headingLabel = new JLabel("Signup");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(headingLabel, constraints);

        nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(nameLabel, constraints);

        nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, constraints);

        dobLabel = new JLabel("Date of Birth (dd-MM-yyyy):");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel.add(dobLabel, constraints);

        dobField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(dobField, constraints);

        emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(emailLabel, constraints);

        emailField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, constraints);

        phoneLabel = new JLabel("Phone Number:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(phoneLabel, constraints);

        phoneField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(phoneField, constraints);

        addressLabel = new JLabel("Address:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(addressLabel, constraints);

        addressField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(addressField, constraints);

        passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, constraints);

        signupButton = new JButton("Signup");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(signupButton, constraints);

        switchToLoginButton = new JButton("Switch to Login Page");
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(switchToLoginButton, constraints);

        signupButton.addActionListener(this);

        switchToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String dobText = dobField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String password = new String(passwordField.getPassword());

        // Input validation
        if (name.isEmpty() || dobText.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()
                || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            dateFormat.parse(dobText);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User(name, dobText, email, phone, address, password);
        DatabaseConnection.addUser(newUser);

        // For demonstration, you can also display a success message
        JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
