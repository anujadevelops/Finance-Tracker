package com.yourapp.expense;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InvestmentTrackerPanel extends JPanel {

    private JComboBox<String> categoryCombo;
    private JPanel formPanel;
    private DefaultTableModel investmentModel;
    private JTable investmentTable;
    private JLabel summaryLabel;
    private int serialNumber = 1;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private StatisticsPanel statisticsPanel;
    private ExpenseTrackerGUI expenseTrackerGUI;
    private JButton addBtn;
    private JButton deleteBtn;
    private JButton saveToFileBtn;
    private JButton exportCsvBtn;
 

    public InvestmentTrackerPanel(StatisticsPanel statisticsPanel, ExpenseTrackerGUI expenseTrackerGUI) {
        this.statisticsPanel = statisticsPanel;
        this.expenseTrackerGUI = expenseTrackerGUI;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("📈 Investment Management", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createTitledBorder("Select Investment Category"));

        JLabel categoryLabel = new JLabel("Category:");
        categoryCombo = new JComboBox<>(new String[]{
                "Stock Investment", "Mutual Fund", "Real Estate", "Other Investment"
        });

        topPanel.add(categoryLabel);
        topPanel.add(categoryCombo);

        formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Investment Details"));

        categoryCombo.addActionListener(_ -> buildFormFields());
        buildFormFields();

        investmentModel = new DefaultTableModel(new String[]{
                "Sr.No", "Category", "Name", "Details", "Amount Invested", "Current Value", "Return (%)", "Date", "Remarks"
        }, 0);

        investmentTable = new JTable(investmentModel);
        JScrollPane tableScrollPane = new JScrollPane(investmentTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("All Investments"));

        summaryLabel = new JLabel("Total Invested: ₹0.00", SwingConstants.LEFT);
        summaryLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

      saveToFileBtn = new JButton("💾 Save to File");
      exportCsvBtn = new JButton("📄 Export to Excel");
      ExpenseTrackerGUI.makeRoundedButton(saveToFileBtn, new Color(39, 174, 96), Color.white);
      ExpenseTrackerGUI.makeRoundedButton(exportCsvBtn, new Color(52, 152, 219), Color.white);
      saveToFileBtn.setForeground(Color.WHITE);
      saveToFileBtn.setBackground(new Color(39, 174, 96));
      exportCsvBtn.setBackground(new Color(52, 152, 219));
      exportCsvBtn.setForeground(Color.WHITE);

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtons.setOpaque(false);
        bottomButtons.add(saveToFileBtn);
        bottomButtons.add(exportCsvBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.add(summaryLabel, BorderLayout.CENTER);
        bottomPanel.add(bottomButtons, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(topPanel, BorderLayout.NORTH);
        top.add(formPanel, BorderLayout.CENTER);


      saveToFileBtn.addActionListener(_ -> FileExporter.saveToFile(investmentTable, "investments.txt"));
      exportCsvBtn.addActionListener(_ -> FileExporter.exportTableToCSV(investmentTable,"investments.csv"));
     
      add(top, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
   
    public JTable getInvestmentTable() {
    return investmentTable;
}
    private void buildFormFields() {
        formPanel.removeAll();
        String selected = (String) categoryCombo.getSelectedItem();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Vector<JComponent> inputs = new Vector<>();
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setPreferredSize(new Dimension(120, 25));

        int y = 0;
        y = addLabelAndField(formPanel, gbc, y, "Name:", inputs);

        if (selected.equals("Stock Investment")) {
            y = addLabelAndField(formPanel, gbc, y, "Quantity of Shares:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "Price per Share:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "Current Price per Share:", inputs);
            y = addDateField(formPanel, gbc, y, "Date of Investment:", dateChooser);
        } else if (selected.equals("Mutual Fund")) {
            y = addLabelAndField(formPanel, gbc, y, "Amount Invested:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "NAV at Purchase:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "Current NAV:", inputs);
            y = addLabelAndDropdown(formPanel, gbc, y, "Type (SIP/Lump Sum):", new String[]{"SIP", "Lump Sum"}, inputs);
            y = addDateField(formPanel, gbc, y, "Date of Investment:", dateChooser);
        } else if (selected.equals("Real Estate")) {
            y = addLabelAndField(formPanel, gbc, y, "Purchase Price:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "Current Market Value:", inputs);
            y = addDateField(formPanel, gbc, y, "Date of Purchase:", dateChooser);
            y = addLabelAndField(formPanel, gbc, y, "Rental Income:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "Remarks:", inputs);
        } else if (selected.equals("Other Investment")) {
            y = addLabelAndField(formPanel, gbc, y, "Amount Invested:", inputs);
            y = addLabelAndField(formPanel, gbc, y, "Current Value:", inputs);
            y = addDateField(formPanel, gbc, y, "Date of Investment:", dateChooser);
            y = addLabelAndField(formPanel, gbc, y, "Remarks:", inputs);
        }

     
// Buttons Panel under form
JPanel formButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
formButtons.setOpaque(false);

addBtn = new JButton("➕ Add");
addBtn.setBackground(new Color(40, 180, 99));
addBtn.setForeground(Color.WHITE);
ExpenseTrackerGUI.makeRoundedButton(addBtn, new Color(52, 152, 219), Color.WHITE);

deleteBtn = new JButton("❌ Delete");
deleteBtn.setBackground(new Color(231, 76, 60));
deleteBtn.setForeground(Color.WHITE);
ExpenseTrackerGUI.makeRoundedButton(deleteBtn, new Color(231, 76, 60), Color.WHITE);

formButtons.add(addBtn);
formButtons.add(deleteBtn);

// Add form buttons below form

gbc.anchor = GridBagConstraints.EAST;
formPanel.add(formButtons, gbc);
        gbc.gridx = 1;
        gbc.gridy = y;
        formPanel.add(addBtn, gbc);

gbc.anchor = GridBagConstraints.EAST;
formPanel.add(formButtons, gbc);
        gbc.gridx = 1;
        gbc.gridy = y;
        formPanel.add(deleteBtn, gbc);



        addBtn.addActionListener( _-> {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            try {
                String name = ((JTextField) inputs.get(0)).getText();
StringBuilder details = new StringBuilder();
                double invested = 0;
                double current = 0;
                String remarks = "";
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date date = dateChooser.getDate();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                    return;
                }

                if (selectedCategory.equals("Stock Investment")) {
                    double qty = Double.parseDouble(((JTextField) inputs.get(1)).getText());
                    double price = Double.parseDouble(((JTextField) inputs.get(2)).getText());
                    double currentPrice = Double.parseDouble(((JTextField) inputs.get(3)).getText());
                    invested = qty * price;
                    current = qty * currentPrice;
                    details.append("Qty: ").append(qty)
                            .append(" | Price: ").append(price)
                            .append(" | Current Price: ").append(currentPrice);
                } else if (selectedCategory.equals("Mutual Fund")) {
                    invested = Double.parseDouble(((JTextField) inputs.get(1)).getText());
                    double navPurchase = Double.parseDouble(((JTextField) inputs.get(2)).getText());
                    double navCurrent = Double.parseDouble(((JTextField) inputs.get(3)).getText());
                    String type = ((JComboBox<?>) inputs.get(4)).getSelectedItem().toString();
                    current = invested * (navCurrent / navPurchase);

                    details.append("NAV at Purchase: ").append(navPurchase)
                            .append(" | Current NAV: ").append(navCurrent)
                            .append(" | Type: ").append(type);
                } else if (selectedCategory.equals("Real Estate")) {
                    invested = Double.parseDouble(((JTextField) inputs.get(1)).getText());
                    current = Double.parseDouble(((JTextField) inputs.get(2)).getText());
                    String rentalIncome = ((JTextField) inputs.get(3)).getText();
                    remarks = ((JTextField) inputs.get(4)).getText();

                    details.append("Purchase Price: ").append(invested)
                            .append(" | Current Value: ").append(current)
                            .append(" | Rental Income: ").append(rentalIncome);
                } else if (selectedCategory.equals("Other Investment")) {
                    invested = Double.parseDouble(((JTextField) inputs.get(1)).getText());
                    current = Double.parseDouble(((JTextField) inputs.get(2)).getText());
                    remarks = ((JTextField) inputs.get(3)).getText();

                    details.append("Invested: ").append(invested)
                            .append(" | Current Value: ").append(current);
                }

                if (isInCurrentMonth(date)) {
                    expenseTrackerGUI.reduceIncome(invested); // ✅ Deduct from income if in current month
                }
                double returnPercent = ((current - invested) / invested) * 100;

                investmentModel.addRow(new Object[]{
                        serialNumber++, selectedCategory, name, details.toString(),
                        String.format("%.2f", invested),
                        String.format("%.2f", current),
                        String.format("%.2f", returnPercent) + " %",
                        sdf.format(date), remarks
                });
                updateSummary();
                updateInvestmentChart();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Please enter valid numbers.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Something went wrong! Check all inputs.");
            }
        });
deleteBtn.addActionListener(_ -> {
    int selectedRow = investmentTable.getSelectedRow();
    if (selectedRow != -1) {
        investmentModel.removeRow(selectedRow);
        updateSummary();
        updateInvestmentChart();

    } else {
        JOptionPane.showMessageDialog(this, "❗ Please select a row to delete.");
    }
});  
    }

    private boolean isInCurrentMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        Calendar inputCal = Calendar.getInstance();
        inputCal.setTime(date);

        return cal.get(Calendar.YEAR) == inputCal.get(Calendar.YEAR) &&
                cal.get(Calendar.MONTH) == inputCal.get(Calendar.MONTH);
    }

    private void updateInvestmentChart() {
        if (statisticsPanel == null) return;

        Map<String, Double> investmentData = new HashMap<>();

        for (int i = 0; i < investmentModel.getRowCount(); i++) {
            String category = investmentModel.getValueAt(i, 1).toString();
            double amount = Double.parseDouble(investmentModel.getValueAt(i, 4).toString());

            investmentData.put(category, investmentData.getOrDefault(category, 0.0) + amount);
        }

        statisticsPanel.updateInvestmentChart(investmentData);
    }

    private int addLabelAndField(JPanel panel, GridBagConstraints gbc, int y, String label, Vector<JComponent> inputs) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField(15);
        panel.add(field, gbc);
        inputs.add(field);
        return y + 1;
    }

    private int addLabelAndDropdown(JPanel panel, GridBagConstraints gbc, int y, String label, String[] options, Vector<JComponent> inputs) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JComboBox<String> dropdown = new JComboBox<>(options);
        panel.add(dropdown, gbc);
        inputs.add(dropdown);
        return y + 1;
    }

    private int addDateField(JPanel panel, GridBagConstraints gbc, int y, String label, JDateChooser dateChooser) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(dateChooser, gbc);
        return y + 1;
    }

    private void updateSummary() {
        double totalInvested = 0;
        for (int i = 0; i < investmentModel.getRowCount(); i++) {
            totalInvested += Double.parseDouble(investmentModel.getValueAt(i, 4).toString());
        }
        summaryLabel.setText(String.format("Total Invested: ₹%.2f", totalInvested));
    }


}


