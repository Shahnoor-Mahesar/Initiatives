package my_package;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel userLabel, passwordLabel, roleLabel;
    JTextField userText;
    JPasswordField passwordText;
    JComboBox<String> roleComboBox;
    JButton loginButton;
    JButton signupButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(titleLabel, constraints);

        userLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(userLabel, constraints);

        userText = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(userText, constraints);

        passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        passwordText = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(passwordText, constraints);

        roleLabel = new JLabel("Role:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(roleLabel, constraints);

        String[] roles = { "Admin", "User" };
        roleComboBox = new JComboBox<>(roles);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(roleComboBox, constraints);

        loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(loginButton, constraints);

        signupButton = new JButton("Switch to Signup Page");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        panel.add(signupButton, constraints);

        loginButton.addActionListener(this);

        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignupPage();
            }
        });

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = userText.getText();
        String password = new String(passwordText.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (role.equals("Admin")) {
            if (username.equals("admin") && password.equals("admin2023")) {
                dispose();
                new AdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        else {
            int userId = DatabaseConnection.userLogin(username, password);
            if (userId != -1) {
                dispose();
                new UserDashboard(userId);
            } else {
                // Invalid username or password, show an error message
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

