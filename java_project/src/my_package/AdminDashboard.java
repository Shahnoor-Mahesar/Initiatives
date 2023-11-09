package my_package;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    private JLabel headingLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTable allUsers;
    private JTable allInitiativesTable;
    private JButton approveButton;
    private JLabel approveLabel;
    private JTextField approveTextField;
    private String[][] addUsers = null;
    private String[][] allInitiatives = null;
    private DefaultTableModel allUsersModel;
    private DefaultTableModel allInitiativesModel;

    public AdminDashboard() {
        // Set the layout for the frame
        setLayout(new BorderLayout());

        // Create heading label
        headingLabel = new JLabel("Admin Dashboard");
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(headingLabel, BorderLayout.NORTH);

        // Create left panel for initiatives
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Add sub-heading for initiatives
        JLabel leftSubHeading = new JLabel("All Initiators and volunteer");
        leftSubHeading.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(leftSubHeading, BorderLayout.NORTH);

        // Create a table to display all initiatives (non-editable)
        String[] columnNames = { "Initiative ID", "Name", "Date of Birth", "Email", "Phone", "Address" };
        addUsers = DatabaseConnection.getAllUsers();
        allUsersModel = new DefaultTableModel(addUsers, columnNames);
        allUsers = new JTable(allUsersModel);

        allUsers.setEnabled(false); // Make table non-editable
        JScrollPane allInitiativesScrollPane = new JScrollPane(allUsers);
        leftPanel.add(allInitiativesScrollPane, BorderLayout.CENTER);

        // Create right panel for available initiatives
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Add sub-heading for available initiatives
        JLabel rightSubHeading = new JLabel("Available Initiatives");
        rightSubHeading.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(rightSubHeading, BorderLayout.NORTH);

        // Create a table to display available initiatives (non-editable)
        String[] columnNames2 = { "ID", "Initiator", "Initiative", "Date", "Time", "Status" };
        allInitiatives = DatabaseConnection.getAllInitiatives();
        allInitiativesModel = new DefaultTableModel(allInitiatives, columnNames2);
        allInitiativesTable = new JTable(allInitiativesModel);
        allInitiativesTable.setEnabled(false); // Make table non-editable
        JScrollPane availableInitiativesScrollPane = new JScrollPane(allInitiativesTable);
        rightPanel.add(availableInitiativesScrollPane, BorderLayout.CENTER);

        // Create panel for joining initiatives
        JPanel joinInitiativePanel = new JPanel();
        approveLabel = new JLabel("Initiative ID:");
        approveTextField = new JTextField(10);
        approveButton = new JButton("Approve");
        approveButton.addActionListener(this::approveInitiative);
        joinInitiativePanel.add(approveLabel);
        joinInitiativePanel.add(approveTextField);
        joinInitiativePanel.add(approveButton);
        rightPanel.add(joinInitiativePanel, BorderLayout.SOUTH);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Set frame properties
        setTitle("Admin Dashboard");
        setSize(910, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void approveInitiative(ActionEvent e) {
        String initiativeIdText = approveTextField.getText();

        boolean initiativeExists = false;
        String currentStatus = "";

        for (String[] initiative : allInitiatives) {
            if (initiative[0].equals(initiativeIdText)) {
                initiativeExists = true;
                currentStatus = initiative[5];
                break;
            }
        }

        // Check if the initiative is already approved
        if (initiativeExists) {
            if (currentStatus.equals("Active"))
                JOptionPane.showMessageDialog(this, "Initiative is already approved.", "Already Approved",
                        JOptionPane.INFORMATION_MESSAGE);

            else {
                DatabaseConnection.approveInitiative(Integer.parseInt(initiativeIdText));
                updateInitiativesTableData();
                JOptionPane.showMessageDialog(this, "Initiative approved successfully.", "Approved",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        else {
            JOptionPane.showMessageDialog(this, "No such initiative to approve.", "Invalid Initiative ID",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInitiativesTableData() {
        allInitiatives = DatabaseConnection.getAllInitiatives();
        allInitiativesModel.setDataVector(allInitiatives,
                new String[] { "ID", "Initiator", "Initiative", "Date", "Time", "Status" });
    }
}
