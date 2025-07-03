package com.yourapp.expense;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.sql.*;

public class BudgetPanel extends JPanel {

    private JTextField categoryField, amountField;
    private JLabel summaryLabel;
    private DefaultTableModel budgetTableModel;
    private JTable budgetTable;
    private ExpenseTrackerGUI expenseTrackerGUI;
    private JTextField incomeField;
    private JTable expenseTable;
    private JLabel expenseLabel;
    private JLabel incomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel totalIncomeLabel;
    private JLabel balanceLabel;
    private StatisticsPanel statisticsPanel;
    private DefaultListModel<String> budgetListModel = new DefaultListModel<>();
    private DefaultTableModel budgetModel;
    private String category;
    private Double amount;
public static int loggedInUserId;


    public BudgetPanel(StatisticsPanel statisticsPanel, ExpenseTrackerGUI expenseTrackerGUI) {
        this.statisticsPanel = statisticsPanel;
        this.expenseTrackerGUI = expenseTrackerGUI;

         incomeField = expenseTrackerGUI.getIncomeField();         
         expenseTable = expenseTrackerGUI.getExpenseTable();
         incomeLabel = expenseTrackerGUI.getIncomeLabel();
        expenseLabel = expenseTrackerGUI.getExpenseLabel();
        balanceLabel = expenseTrackerGUI.getBalanceLabel();

totalExpenseLabel = new JLabel("Total Expense: ₹0");
totalExpenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
totalExpenseLabel.setForeground(Color.BLUE);
add(totalExpenseLabel);
balanceLabel = new JLabel("Balance: ₹0");
balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
balanceLabel.setForeground(new Color(0, 128, 0)); // Green color for positive balance
add(balanceLabel);
totalIncomeLabel = new JLabel("Total Income: ₹0");
totalExpenseLabel = new JLabel("Total Expense: ₹0");
balanceLabel = new JLabel("Balance: ₹0");

// Apply fonts and colors similarly for each

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(250, 250, 255));

        JLabel header = new JLabel("📊 Monthly Budget Manager", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setForeground(new Color(44, 62, 80));
        add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Add Budget",
                TitledBorder.LEFT, TitledBorder.TOP));
        inputPanel.setBackground(new Color(245, 250, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(12);
        JLabel amountLabel = new JLabel("Amount (₹):");
        amountField = new JTextField(10);

       
      JButton addBtn = new JButton("Add Budget");
        JButton deleteBtn = new JButton("Delete Budget");
        ExpenseTrackerGUI.makeRoundedButton(addBtn, new Color(46, 204, 113), Color.WHITE);
        ExpenseTrackerGUI.makeRoundedButton(deleteBtn, new Color(231, 76, 60), Color.WHITE);
       
        JButton saveToFileBtn = new JButton("💾 Save to File");
        JButton exportCsvBtn = new JButton("📄 Export to Excel");
        ExpenseTrackerGUI.makeRoundedButton(saveToFileBtn, new Color(39, 174, 96), Color.WHITE);
        ExpenseTrackerGUI.makeRoundedButton(exportCsvBtn, new Color(52, 152, 219), Color.WHITE);

        saveToFileBtn.setBackground(new Color(52, 152, 219));
        saveToFileBtn.setForeground(Color.WHITE);
        exportCsvBtn.setBackground(new Color(52, 152, 219));
        exportCsvBtn.setForeground(Color.WHITE);

        // Input Panel Layout
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(categoryField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(addBtn, gbc);
        gbc.gridx = 1;
        inputPanel.add(deleteBtn, gbc);

          // Table for budget records
        budgetTableModel = new DefaultTableModel(new String[]{"Category", "Amount (₹)"}, 0);
        budgetTable = new JTable(budgetTableModel);
        JScrollPane scrollPane = new JScrollPane(budgetTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Budget Records"));
     
        summaryLabel = new JLabel("Total Budget: ₹0.00", SwingConstants.LEFT);
        summaryLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(summaryLabel, BorderLayout.CENTER);

        // Add components to main panel
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

                 // Button Actions
        addBtn.addActionListener(this::handleAdd);
        deleteBtn.addActionListener(this::handleDelete);
        saveToFileBtn.addActionListener(_ -> FileExporter.saveToFile(budgetTable, "budget.txt"));
        exportCsvBtn.addActionListener(_ -> FileExporter.exportTableToCSV(budgetTable,"budget.csv"));

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtons.add(saveToFileBtn);
        bottomButtons.add(exportCsvBtn);

        bottomPanel.add(bottomButtons, BorderLayout.EAST);

            // Add components to main panel
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
   
    } 
private void updateSummary() {
    double total = 0;
    for (int i = 0; i < budgetTableModel.getRowCount(); i++) {
        String amountStr = budgetTableModel.getValueAt(i, 1).toString().replace("₹", "").trim();
        try {
            total += Double.parseDouble(amountStr);
        } catch (Exception ignored) {
        }
    }
    summaryLabel.setText(String.format("Total Budget: ₹%.2f", total));
}
  // ✅ Add budget
    private void handleAdd(ActionEvent e) {
        String category = categoryField.getText().trim();
        String amountText = amountField.getText().trim();

        if (category.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both category and amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            budgetTableModel.addRow(new Object[]{category, "₹" + amount});
            expenseTrackerGUI.getBudgetMap().put(category, amount);

            categoryField.setText("");
            amountField.setText("");

            updateSummary();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.");
        }
    }

    // ✅ Delete selected row
    private void handleDelete(ActionEvent e) {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow != -1) {
            String category = budgetTableModel.getValueAt(selectedRow, 0).toString();
            budgetTableModel.removeRow(selectedRow);
            expenseTrackerGUI.getBudgetMap().remove(category);
            updateSummary();
              DefaultTableModel expenseModel = (DefaultTableModel) expenseTrackerGUI.getExpenseTable().getModel();
        for (int i = 0; i < expenseModel.getRowCount(); i++) {
            String expenseCategory = expenseModel.getValueAt(i, 0).toString();

            String[] parts = expenseCategory.split(" ", 2);
            String cleanCategory = parts.length == 2 ? parts[1] : expenseCategory;

            if (cleanCategory.equals(category)) {
                expenseModel.setValueAt(0.0, i, 2);  // ✅ Reset Budget column to 0
            }
        }

        // ✅ Remove row from Budget Table
        budgetTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
        }

    }
   
private boolean manageBudget(String category, Double amount) {
    String sql = "INSERT INTO budgets (category, amount, user_id) VALUES (?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, category);
        ps.setDouble(2, amount);
        ps.setInt(3, LoginPage.loggedInUserId);

        int rowsInserted = ps.executeUpdate();
        return rowsInserted > 0;

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        return false;
    }
}

public JTable getBudgetTable() {
    return budgetTable;
}
}


   


