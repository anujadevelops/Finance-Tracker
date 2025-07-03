package com.yourapp.expense;

import org.jfree.data.general.DefaultPieDataset;


import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class ExpenseTrackerGUI extends JFrame {
    private double totalIncome = 0;
    private DefaultTableModel expenseModel;
    private JTable expenseTable;
    private JTextField incomeField, categoryField, amountField;
    private JFormattedTextField dateField;
    private DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
    private StatisticsPanel statisticsPanel;
    private final Map<String, Double> budgetMap = new HashMap<>();
    JButton saveToFileBtn1;
    JButton exportCsvBtn1;
    private InvestmentTrackerPanel investmentPanel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private double totalExpense = 0.0;
private double balance = 0.0;
private JPanel summaryPanel;
public static int loggedInUserId = -1;
    public static String loggedInUsername = "";


private BudgetPanel budgetPanel ;


    private static final Map<String, String> categoryEmojis = new HashMap<>();
    static {
        categoryEmojis.put("Food", "🍔");
        categoryEmojis.put("Transport", "🚗");
        categoryEmojis.put("Utilities", "💡");
        categoryEmojis.put("Shopping", "🛖️");
        categoryEmojis.put("Health", "💊");
        categoryEmojis.put("Entertainment", "🎮");
        categoryEmojis.put("Others", "📦");
    }

    public ExpenseTrackerGUI() {
        setTitle("📊 Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        SwingUtilities.updateComponentTreeUI(this);
        statisticsPanel = new StatisticsPanel();
        investmentPanel = new InvestmentTrackerPanel(statisticsPanel, this);
        budgetPanel = new BudgetPanel(statisticsPanel, this);

        JTabbedPane tabbedPane = new JTabbedPane();
        setJMenuBar(buildMenuBar());

        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setForeground(new Color(50, 50, 50));
        tabbedPane.addTab(" Expenses Management", buildExpensePanel());
        tabbedPane.addTab(" Statistics", statisticsPanel);
        tabbedPane.addTab(" Investment Management", investmentPanel);
        tabbedPane.addTab(" Budget Management", budgetPanel);
       
        tabbedPane.addChangeListener(e -> {
             int selectedIndex = tabbedPane.getSelectedIndex();
             if (selectedIndex == 0) { 
             expenseLabel.setVisible(true);
            } else {
                  expenseLabel.setVisible(false);
                 }
        });

        add(tabbedPane, BorderLayout.CENTER);
        add(buildSummaryPanel(), BorderLayout.SOUTH);

    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setForeground(Color.WHITE);
        menuBar.setBackground(new Color(47, 84, 150)); // Blue color
        menuBar.setOpaque(true);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.WHITE);
        JMenuItem helpItem = new JMenuItem("Help");
        helpItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Finance Tracker v1.0\nDeveloped by Anuja Patole\nTrack income, expenses, and investments easily.",
                "Help", JOptionPane.INFORMATION_MESSAGE));
       
        JMenuItem howToUseItem = new JMenuItem("How to use");
          howToUseItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                """
                        ➤ Add Income and Expenses
                        ➤ Manage Investments and Budgets
                        ➤ Save or Export data
                        """, "How to Use", JOptionPane.INFORMATION_MESSAGE);
    });

        helpMenu.add(howToUseItem);
        helpMenu.add(helpItem);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JPanel buildExpensePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 255));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Expense Input"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel incomeLabelField = new JLabel("Monthly Income:");
        incomeField = new JTextField(15);

        JButton incomeButton = new JButton("Add Income");
        incomeButton.setBackground(new Color(76, 175, 80));
        incomeButton.setForeground(Color.WHITE);
        makeRoundedButton(incomeButton, new Color(40, 180, 99), Color.white);

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(15);

        JLabel amountLabel = new JLabel("Amount (₹):");
        amountField = new JTextField(15);

        JLabel dateLabel = new JLabel("Date:");
        dateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        dateField.setValue(new Date());

        JDateChooser calendar = new JDateChooser();
        calendar.setDate(new Date());
        calendar.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                Date selectedDate = calendar.getDate();
                dateField.setValue(selectedDate);
            }
        });

        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete");
        addButton.setBackground(new Color(33, 150, 243));
        addButton.setForeground(Color.WHITE);
        makeRoundedButton(addButton, new Color(52, 152, 219), Color.white);

        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        makeRoundedButton(deleteButton, new Color(231, 76, 60), Color.white);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(incomeLabelField, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(incomeField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(incomeButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(categoryLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(categoryField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(amountLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(dateLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; inputPanel.add(dateField, gbc);
        gbc.gridx = 2; gbc.gridy = 3; inputPanel.add(calendar, gbc);

        gbc.gridx = 1; gbc.gridy = 4; inputPanel.add(addButton, gbc);
        gbc.gridx = 2; gbc.gridy = 4; inputPanel.add(deleteButton, gbc);

        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteSelectedRow());

        expenseModel = new DefaultTableModel(new String[]{"Category", "Amount", "Budget", "Date"}, 0);
        expenseTable = new JTable(expenseModel);
        expenseTable.setRowHeight(28);
        expenseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane tableScroll = new JScrollPane(expenseTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Expense Table"));

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
       
        saveToFileBtn1 = new JButton("💾 Save to File");
        saveToFileBtn1.setBackground(new Color(52, 152, 219));
        saveToFileBtn1.setForeground(Color.WHITE);
        makeRoundedButton(saveToFileBtn1, new Color(40, 180, 99), Color.white);

        exportCsvBtn1 = new JButton("📄 Export to Excel");
        exportCsvBtn1.setBackground(new Color(52, 152, 219));
        exportCsvBtn1.setForeground(Color.WHITE);
        makeRoundedButton(exportCsvBtn1, new Color(52, 152, 219), Color.white);
       
        saveToFileBtn1.addActionListener(e -> FileExporter.saveToFile(expenseTable, "expenses.txt"));
        exportCsvBtn1.addActionListener(e -> FileExporter.exportTableToCSV(expenseTable,"expenses.csv"));
     
        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtons.setOpaque(false);
        bottomButtons.add(saveToFileBtn1);
        bottomButtons.add(exportCsvBtn1);
        panel.add(bottomButtons, BorderLayout.SOUTH);

        incomeButton.addActionListener(e -> {
              try {
    double incomeInput = Double.parseDouble(incomeField.getText().trim());
    totalIncome += incomeInput;
    incomeField.setText("");
    calculateTotals();
    updateSummary();
    updatePieChart();
} catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Invalid income amount.");
}
    calculateTotals();
    updateSummary();
    updatePieChart();
        });

        return panel;
    }
    private void addExpense() {
    String category = categoryField.getText().trim();
    String emoji = categoryEmojis.getOrDefault(category, "📦");
    String displayCategory = emoji + " " + category;

    try {
        double amount = Double.parseDouble(amountField.getText().trim());
        double budget = budgetMap.getOrDefault(category, 0.0);
        String date = dateField.getText().trim();

        // ✅ Add to Table
        expenseModel.addRow(new Object[]{displayCategory, amount, budget, date});
saveExpense(category, String.valueOf(amount), budget, date);

        // ✅ Budget Warning
        if (budget > 0 && amount > budget) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Warning: Expense exceeds the budget for " + category,
                    "Budget Exceeded",
                    JOptionPane.WARNING_MESSAGE);
        }

        // ✅ Reset Input Fields
        categoryField.setText("");
        amountField.setText("");
        dateField.setValue(new Date());

        // ✅ Update Summary and Chart
        calculateTotals();
        updateSummary();
        updatePieChart();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
    }
}

public JTextField getIncomeField() {
    return incomeField;
}

private void calculateTotals() {
    totalExpense = 0.0;
    for (int i = 0; i < expenseModel.getRowCount(); i++) {
        totalExpense += Double.parseDouble(expenseModel.getValueAt(i, 1).toString());
    }
    balance = totalIncome - totalExpense;
}
private void updateSummary() {
    double totalExpense = 0.0;
    for (int i = 0; i < expenseModel.getRowCount(); i++) {
        totalExpense += Double.parseDouble(expenseModel.getValueAt(i, 1).toString());
    }

    incomeLabel.setText("Income: ₹" + totalIncome);
    expenseLabel.setText("Total Expense: ₹" + totalExpense);
    balanceLabel.setText("Balance: ₹" + (totalIncome - totalExpense));
}

private JPanel buildSummaryPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));

    incomeLabel = new JLabel();
    expenseLabel = new JLabel();
    balanceLabel = new JLabel();

    incomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    expenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

    panel.add(incomeLabel);
    panel.add(expenseLabel);
    panel.add(balanceLabel);

    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    panel.setBackground(new Color(224, 247, 250));

    updateSummary(); 
    return panel;
}

public JTable getExpenseTable() {
    return expenseTable;
}

public JLabel getIncomeLabel() {
    return incomeLabel;
}

public JLabel getExpenseLabel() {
    return expenseLabel;
}

public JLabel getBalanceLabel() {
    return balanceLabel;
}

 
public void reduceIncome(double amount) {
    totalIncome -= amount;
    if (totalIncome < 0) totalIncome = 0;  // Optional safety
    updateSummary();
}
public void addBackToIncome(double amount) {
    totalIncome += amount;
    updateSummary();

}


 public static void makeRoundedButton(JButton button, Color background, Color foreground) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(foreground);
        button.setBackground(background);

        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();

                g2.setColor(background);
                g2.fillRoundRect(0, 0, width, height, height, height);

                super.paint(g, c);
                g2.dispose();
            }
        });
    }
private boolean saveExpense(String category, String amount,double budget ,String date) {
    String sql = "INSERT INTO expenses (category, amount,budget, date, user_id) VALUES (?, ?, ?, ?, ?)";
    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, category);
        ps.setDouble(2, Double.parseDouble(amount));
        ps.setDouble(3, budget);
        ps.setDate(4, java.sql.Date.valueOf(date));
        ps.setInt(5, loggedInUserId);

        int rowsInserted = ps.executeUpdate();
        ps.close();
        return rowsInserted > 0;

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        return false;
    }
}


    private void deleteSelectedRow() {
        int selected = expenseTable.getSelectedRow();
        if (selected != -1) {
            expenseModel.removeRow(selected);
            updateSummary();
            updatePieChart();
        }
    }


 private void updatePieChart(){
        pieDataset.clear();
        Map<String, Double> data = new HashMap<>();
           for (int i = 0; i < expenseModel.getRowCount(); i++) {
             String category = expenseModel.getValueAt(i, 0).toString();
             double amount = Double.parseDouble(expenseModel.getValueAt(i, 1).toString());
             data.put(category, data.getOrDefault(category, 0.0) + amount);
    }
    for (Map.Entry<String, Double> entry : data.entrySet()) {
        pieDataset.setValue(entry.getKey(), entry.getValue());
    }
        statisticsPanel.updateExpenseChart(data);
  }
    public Map<String, Double> getBudgetMap() {
    return budgetMap;
   }
}
