package com.yourapp.expense;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

 public class FileExporter {
    public static void saveToFile(JTable table, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                writer.write(table.getColumnName(i) + "\t");
            }
            writer.write("\n");

            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    writer.write(table.getValueAt(i, j).toString() + "\t");
                }
                writer.write("\n");
            }

            JOptionPane.showMessageDialog(null, "File saved as " + filename);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving file.");
        }
    }

    public static void exportTableToCSV(JTable table,String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                writer.write(table.getColumnName(i) + ",");
            }
            writer.write("\n");

            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    writer.write(table.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }

            JOptionPane.showMessageDialog(null, "File saved as" + filename);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving CSV file.");
        }
    }
}
