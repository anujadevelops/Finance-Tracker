// ✅ StatisticsPanel.java
package com.yourapp.expense;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Collections;
import java.util.Map;

public class StatisticsPanel extends JPanel {

    private final DefaultPieDataset<String> expenseDataset = new DefaultPieDataset<>();
    private final DefaultCategoryDataset investmentDataset = new DefaultCategoryDataset();

    private final JFreeChart expenseChart;
    private final JFreeChart investmentChart;

    private final ChartPanel expenseChartPanel;
    private final ChartPanel investmentChartPanel;

    private final JPanel chartContainer;
    private final JLabel totalInvestmentLabel;
    private final JLabel totalExpenseLabel;

    public StatisticsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        expenseChart = ChartFactory.createPieChart(
                "\ud83d\udcca Expense Breakdown",
                expenseDataset,
                true, true, false
        );
        PiePlot expensePlot = (PiePlot) expenseChart.getPlot();
        expensePlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        expensePlot.setCircular(true);
        expensePlot.setLabelGap(0.02);
        expensePlot.setNoDataMessage("No Expense Data Available");
        expensePlot.setSimpleLabels(true);
        expensePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: \u20b9{1} ({2})"));

        expenseChartPanel = new ChartPanel(expenseChart);
        expenseChartPanel.setMouseWheelEnabled(true);
        expenseChartPanel.setBorder(createRoundedBorder());

        investmentChart = ChartFactory.createBarChart(
                "\ud83d\udcbc Investment Overview",
                "Investment Type",
                "Amount (\u20b9)",
                investmentDataset
        );

        CategoryPlot barPlot = investmentChart.getCategoryPlot();
        barPlot.setBackgroundPaint(Color.white);
        barPlot.setRangeGridlinePaint(new Color(230, 230, 230));

        CategoryAxis domainAxis = barPlot.getDomainAxis();
        domainAxis.setCategoryMargin(0.2);

        NumberAxis rangeAxis = (NumberAxis) barPlot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        BarRenderer renderer = new GradientBarRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator("{1}: \u20b9{2}", java.text.NumberFormat.getInstance()));
        renderer.setShadowVisible(true);
        renderer.setMaximumBarWidth(0.1);

        barPlot.setRenderer(renderer);
        barPlot.setNoDataMessage("No Investment Data Available");

        investmentChartPanel = new ChartPanel(investmentChart);
        investmentChartPanel.setMouseWheelEnabled(true);
        investmentChartPanel.setBorder(createRoundedBorder());

        totalInvestmentLabel = createTotalLabel("Total Investments: \u20b90");
        totalExpenseLabel = createTotalLabel("Total Expenses: \u20b90");

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        togglePanel.setOpaque(false);

        JCheckBox expenseCheckbox = createStyledCheckbox("Expense Breakdown");
        JCheckBox investmentCheckbox = createStyledCheckbox("Investment Overview");
        expenseCheckbox.setSelected(true);

        togglePanel.add(expenseCheckbox);
        togglePanel.add(investmentCheckbox);

        chartContainer = new RoundedPanel();
        chartContainer.setLayout(new BorderLayout());
        chartContainer.add(expenseChartPanel, BorderLayout.CENTER);
        chartContainer.add(totalExpenseLabel, BorderLayout.SOUTH);

        expenseCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                investmentCheckbox.setSelected(false);
                showExpenseChart();
            }
        });

        investmentCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                expenseCheckbox.setSelected(false);
                showInvestmentChart();
            }
        });

        add(togglePanel, BorderLayout.NORTH);
        add(chartContainer, BorderLayout.CENTER);
    }
public void resetCharts() {
    expenseDataset.clear();
    investmentDataset.clear();
    totalExpenseLabel.setText("Total Expenses: ₹0");
    totalInvestmentLabel.setText("Total Investments: ₹0");
}

    private void showExpenseChart() {
        chartContainer.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(expenseChartPanel, BorderLayout.CENTER);
        panel.add(totalExpenseLabel, BorderLayout.SOUTH);
        chartContainer.add(panel, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private void showInvestmentChart() {
        chartContainer.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(investmentChartPanel, BorderLayout.CENTER);
        panel.add(totalInvestmentLabel, BorderLayout.SOUTH);
        chartContainer.add(panel, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    public void updateExpenseChart(Map<String, Double> categoryAmounts) {
        expenseDataset.clear();

        if (categoryAmounts.isEmpty()) {
            totalExpenseLabel.setText("Total Expenses: \u20b90");
            expenseChart.fireChartChanged();
            return;
        }

        double total = categoryAmounts.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : categoryAmounts.entrySet()) {
            if (entry.getValue() > 0) {
                expenseDataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        totalExpenseLabel.setText("Total Expenses: \u20b9" + String.format("%.2f", total));
        expenseChart.fireChartChanged();
    }

    public void updateInvestmentChart(Map<String, Double> investmentAmounts) {
        investmentDataset.clear();

        if (investmentAmounts.isEmpty()) {
            ((NumberAxis) investmentChart.getCategoryPlot().getRangeAxis()).setRange(0, 100);
            totalInvestmentLabel.setText("Total Investments: \u20b90");
            investmentChart.fireChartChanged();
            return;
        }

        double maxValue = Collections.max(investmentAmounts.values());
        double total = investmentAmounts.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : investmentAmounts.entrySet()) {
            if (entry.getValue() > 0) {
                investmentDataset.setValue(entry.getValue(), "Investment", entry.getKey());
            }
        }

        double upper = Math.ceil(maxValue + maxValue * 0.1);
        if (upper < 100) {
            upper = 100;
        }

        NumberAxis rangeAxis = (NumberAxis) investmentChart.getCategoryPlot().getRangeAxis();
        rangeAxis.setRange(0, upper);

        totalInvestmentLabel.setText("Total Investments: \u20b9" + String.format("%.2f", total));
        investmentChart.fireChartChanged();
    }

    static class GradientBarRenderer extends BarRenderer {
        @Override
        public Paint getItemPaint(int row, int column) {
            Color start = new Color(93, 173, 226);
            Color end = new Color(52, 152, 219);
            return new GradientPaint(0, 0, start, 0, 0, end);
        }
    }

    private JCheckBox createStyledCheckbox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        checkBox.setFocusPainted(false);
        return checkBox;
    }

    private LineBorder createRoundedBorder() {
        return new LineBorder(new Color(200, 200, 200), 1, true);
    }

    static class RoundedPanel extends JPanel {
        private static final int CORNER_RADIUS = 20;

        public RoundedPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(15, 15, 15, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(CORNER_RADIUS, CORNER_RADIUS);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setColor(new Color(0, 0, 0, 30));
            graphics.fillRoundRect(5, 5, width - 10, height - 10, arcs.width, arcs.height);

            graphics.setColor(getBackground() != null ? getBackground() : Color.WHITE);
            graphics.fillRoundRect(0, 0, width - 10, height - 10, arcs.width, arcs.height);
        }
    }

    private JLabel createTotalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(10, 0, 0, 0));
        return label;
    }
}
