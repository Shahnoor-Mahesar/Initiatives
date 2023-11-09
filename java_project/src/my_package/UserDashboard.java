package my_package;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class UserDashboard extends JFrame {
    private JLabel headingLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTable allInitiativesTable;
    private JTable availableInitiativesTable;
    private JButton addInitiativeButton;
    private JTextField newInitiativeTextField;
    private JButton joinInitiativeButton;
    private JLabel joinInitiativeLabel;
    private JTextField joinInitiativeTextField;
    private int userId = -1;
    private String[][] allInitatives = null;
    private String[][] availableInitatives = null;
    private DefaultTableModel allInitiativesModel;
    private DefaultTableModel availableInitiativesModel;

    public UserDashboard(int userId) {
        this.userId = userId;
        // Set the layout for the frame
        setLayout(new BorderLayout());

        // Create heading label
        headingLabel = new JLabel("User Dashboard");
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(headingLabel, BorderLayout.NORTH);

        // Create left panel for initiatives
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Add sub-heading for initiatives
        JLabel leftSubHeading = new JLabel("Initiatives");
        leftSubHeading.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(leftSubHeading, BorderLayout.NORTH);

        // Create a table to display all initiatives (non-editable)
        String[] columnNames = { "Initiative ID", "Name", "Date", "Time", "Status" };
        allInitatives = DatabaseConnection.getAllUserInitiatives(this.userId);
        allInitiativesModel = new DefaultTableModel(allInitatives, columnNames);
        allInitiativesTable = new JTable(allInitiativesModel);

        allInitiativesTable.setEnabled(false); // Make table non-editable
        JScrollPane allInitiativesScrollPane = new JScrollPane(allInitiativesTable);
        leftPanel.add(allInitiativesScrollPane, BorderLayout.CENTER);

        // Create panel for adding new initiatives
        JPanel addInitiativePanel = new JPanel();
        addInitiativeButton = new JButton("Add New Initiative");
        newInitiativeTextField = new JTextField(20);
        addInitiativePanel.add(new JLabel("New Initiative Name:"));
        addInitiativePanel.add(newInitiativeTextField);
        addInitiativePanel.add(addInitiativeButton);
        addInitiativeButton.addActionListener(this::addNewInitiative);

        leftPanel.add(addInitiativePanel, BorderLayout.SOUTH);

        // Create right panel for available initiatives
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Add sub-heading for available initiatives
        JLabel rightSubHeading = new JLabel("Available Initiatives");
        rightSubHeading.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(rightSubHeading, BorderLayout.NORTH);

        // Create a table to display available initiatives (non-editable)
        String[] columnNames2 = { "Initiative ID", "Name", "Date", "Time", "Status" };
        availableInitatives = DatabaseConnection.getAvailableInitiativesForUser(userId);
        availableInitiativesModel = new DefaultTableModel(availableInitatives, columnNames2);
        availableInitiativesTable = new JTable(availableInitiativesModel);
        availableInitiativesTable.setEnabled(false); // Make table non-editable
        JScrollPane availableInitiativesScrollPane = new JScrollPane(availableInitiativesTable);
        rightPanel.add(availableInitiativesScrollPane, BorderLayout.CENTER);

        // Create panel for joining initiatives
        JPanel joinInitiativePanel = new JPanel();
        joinInitiativeLabel = new JLabel("Initiative ID:");
        joinInitiativeTextField = new JTextField(10);
        joinInitiativeButton = new JButton("Join Initiative");
        joinInitiativeButton.addActionListener(this::joinInitiative);
        joinInitiativePanel.add(joinInitiativeLabel);
        joinInitiativePanel.add(joinInitiativeTextField);
        joinInitiativePanel.add(joinInitiativeButton);
        rightPanel.add(joinInitiativePanel, BorderLayout.SOUTH);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Set frame properties
        setTitle("User Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addNewInitiative(ActionEvent e) {
        String newInitiativeName = newInitiativeTextField.getText();
        LocalDate currentDate = LocalDate.now(); // Get current date
        LocalTime currentTime = LocalTime.now(); // Get current time

        // Create a new Initiative object
        Initiative newInitiative = new Initiative(0, newInitiativeName, currentDate.toString(),
                currentTime.toString(), "Pending", userId);
        DatabaseConnection.addNewInitiative(newInitiative);
        updateInitiativesTableData();
    }

    public void joinInitiative(ActionEvent e) {
        String initiativeId = joinInitiativeTextField.getText();

        // Check if the initiativeId exists in the available initiatives
        boolean initiativeExists = false;
        for (String[] initiative : availableInitatives) {
            if (initiative[0].equals(initiativeId)) {
                initiativeExists = true;
                break;
            }
        }

        if (initiativeExists) {
            DatabaseConnection.joinInitiative(userId, Integer.parseInt(initiativeId));
            updateAvailableInitiativesTableData();
            JOptionPane.showMessageDialog(null, "Joined Initiative with ID: " + initiativeId);
        } else {
            JOptionPane.showMessageDialog(null, "Initiative with ID " + initiativeId + " does not exist.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInitiativesTableData() {
        allInitatives = DatabaseConnection.getAllUserInitiatives(userId);
        allInitiativesModel.setDataVector(allInitatives,
                new String[] { "Initiative ID", "Name", "Date", "Time", "Status" });
    }

    private void updateAvailableInitiativesTableData() {
        availableInitatives = DatabaseConnection.getAvailableInitiativesForUser(userId);
        availableInitiativesModel.setDataVector(availableInitatives,
                new String[] { "Initiative ID", "Name", "Date", "Time", "Status" });
    }
}
