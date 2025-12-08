import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class WeatherGUI extends JFrame {
    // Only 4 colors used throughout
    private static final Color BLUE = Color.BLUE;      // For: header, add button, chart button
    private static final Color GREEN = Color.GREEN;    // For: search button, LOW risk
    private static final Color ORANGE = Color.ORANGE;  // For: stats button, MEDIUM risk
    private static final Color RED = Color.RED;        // For: delete button, HIGH/EXTREME risk

    private final WeatherManager manager;
    private static final String[] COLUMNS = {"ID", "Type", "Location", "Date", "Intensity", "Risk"};
    private final DefaultTableModel model = new DefaultTableModel(COLUMNS, 0);

    public WeatherGUI(WeatherManager manager) {
        this.manager = manager;

        setTitle("Weather Event Logger");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel header = new JLabel("WEATHER EVENT LOGGER", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(BLUE);  // Use BLUE
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Table
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getColumnModel().getColumn(5).setCellRenderer(new RiskRenderer());

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        String[] labels = {"Add Event", "Delete", "Search", "Statistics", "Chart"};
        Color[] colors = {BLUE, RED, GREEN, ORANGE, BLUE};  // Reuse colors

        // Create and add all buttons
        for (int i = 0; i < labels.length; i++) {
            buttonPanel.add(createButton(labels[i], colors[i]));
        }

        // Action listeners
        ((JButton)buttonPanel.getComponent(0)).addActionListener(_ -> addEvent());
        ((JButton)buttonPanel.getComponent(1)).addActionListener(_ -> deleteEvent());
        ((JButton)buttonPanel.getComponent(2)).addActionListener(_ -> searchEvents());
        ((JButton)buttonPanel.getComponent(3)).addActionListener(_ -> showStatistics());
        ((JButton)buttonPanel.getComponent(4)).addActionListener(_ -> showChart());

        // Layout
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // ========== ALL YOUR ORIGINAL METHODS BELOW - UNCHANGED ==========

    private void refreshTable() {
        model.setRowCount(0);
        for (Weather event : manager.getAllEvents()) {
            model.addRow(new Object[]{
                    event.getEventId(),
                    event.getEventType(),
                    event.getLocation(),
                    event.getStartDateTime().toLocalDate(),
                    event.getIntensity(),
                    event.getRiskLevel()
            });
        }
    }

    private void addEvent() {
        String[] types = {"FLOOD", "HURRICANE", "EARTHQUAKE", "WILDFIRE"};
        String type = (String) JOptionPane.showInputDialog(this,
                "Select Event Type:", "Add Event", JOptionPane.QUESTION_MESSAGE,
                null, types, types[0]);

        if (type == null) return;

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField location = new JTextField("Accra, Ghana");
        JTextField date = new JTextField(LocalDateTime.now().toLocalDate().toString());
        JTextField time = new JTextField("12:00");
        JSpinner intensity = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        JTextField cause = new JTextField("eg natural/human");

        panel.add(new JLabel("Location:")); panel.add(location);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(date);
        panel.add(new JLabel("Time (HH:MM):")); panel.add(time);
        panel.add(new JLabel("Intensity (1-10):")); panel.add(intensity);
        panel.add(new JLabel("Cause:")); panel.add(cause);

        if (JOptionPane.showConfirmDialog(this, panel, "Add " + type,
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime start = LocalDateTime.parse(date.getText() + "T" + time.getText() + ":00");
                Weather event = createEvent(type, location.getText(), start,
                        (int) intensity.getValue(), cause.getText());

                if (event != null) {
                    manager.addEvent(event);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Event added successfully!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: Invalid input");
            }
        }
    }

    private Weather createEvent(String type, String location, LocalDateTime start,
                                int intensity, String cause) {
        try {
            switch (type) {
                case "FLOOD":
                    String water = JOptionPane.showInputDialog("Water Level (m):");
                    return new Flood(location, start, 24, intensity, cause, Double.parseDouble(water));
                case "HURRICANE":
                    String wind = JOptionPane.showInputDialog("Wind Speed (mph):");
                    return new Hurricane(location, start, 24, intensity, cause,
                            Double.parseDouble(wind), 980, 150);
                case "EARTHQUAKE":
                    String mag = JOptionPane.showInputDialog("Magnitude:");
                    return new Earthquake(location, start, 0.5, intensity, cause,
                            Double.parseDouble(mag), 10, "Epicenter");
                case "WILDFIRE":
                    String area = JOptionPane.showInputDialog("Area Burned (ha):");
                    String contain = JOptionPane.showInputDialog("Containment %:");
                    String fuel = JOptionPane.showInputDialog("Fuel Type:");
                    return new WildFire(location, start, 48, intensity, cause,
                            Double.parseDouble(area), Integer.parseInt(contain), fuel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
        }
        return null;
    }

    private void deleteEvent() {
        JTable table = (JTable) ((JViewport) ((JScrollPane) getContentPane().getComponent(1)).getComponent(0)).getView();
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event first");
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        if (manager.deleteEvent(id)) {
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Event deleted");
        }
    }

    private void searchEvents() {  // FIXED: This method was missing
        String term = JOptionPane.showInputDialog(this, "Enter location to search:");
        if (term != null && !term.trim().isEmpty()) {
            var results = manager.searchByLocation(term);
            if (!results.isEmpty()) {
                JTextArea area = new JTextArea();
                area.append("Found " + results.size() + " events:\n\n");
                for (Weather event : results) {
                    area.append("• " + event.getEventId() + " - " +
                            event.getLocation() + " (" + event.getEventType() + ")\n");
                }
                JOptionPane.showMessageDialog(this, new JScrollPane(area));
            } else {
                JOptionPane.showMessageDialog(this, "No events found");
            }
        }
    }

    private void showStatistics() {
        // Use the manager's built-in analysis method
        String statsText = "=== WEATHER EVENT STATISTICS ===\n\n" +
                manager.getAnalysisSummary();

        // Create display area
        JTextArea statsArea = new JTextArea(statsText);
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Show in dialog
        JOptionPane.showMessageDialog(this,
                new JScrollPane(statsArea),
                "Weather Statistics",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showChart() {  // FIXED: This method was missing
        JDialog chartDialog = new JDialog(this, "Risk Level Chart", true);
        chartDialog.setSize(600, 400);
        chartDialog.setLocationRelativeTo(this);

        ChartPanel chartPanel = new ChartPanel(manager);
        chartDialog.add(chartPanel);
        chartDialog.setVisible(true);
    }

    // Risk Renderer - uses same 4 colors
    static class RiskRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if (value != null) {
                String risk = value.toString();
                c.setFont(new Font("Arial", Font.BOLD, 11));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);

                switch (risk) {
                    case "LOW": c.setBackground(GREEN); break;
                    case "MEDIUM": c.setBackground(ORANGE); break;
                    case "HIGH": c.setBackground(RED); break;
                    case "EXTREME": c.setBackground(RED.darker());
                        c.setForeground(Color.WHITE); break;
                }
                if (isSelected) c.setBackground(c.getBackground().darker());
            }
            return c;
        }
    }

    // Chart Panel
    public static class ChartPanel extends JPanel {
        private final WeatherManager manager;

        public ChartPanel(WeatherManager manager) {
            this.manager = manager;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Map<String, Integer> riskData = manager.getEventsByRiskLevel();
            String[] risks = {"LOW", "MEDIUM", "HIGH", "EXTREME"};
            Color[] colors = {GREEN, ORANGE, RED, RED.darker()};  // Same 4 colors

            int width = getWidth();
            int height = getHeight();

            int max = 1;
            for (int value : riskData.values()) {
                if (value > max) max = value;
            }

            int barWidth = 80;
            int spacing = 40;
            int startX = 60;

            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.BLACK);
            g2d.drawString("Risk Level Distribution", width/2 - 100, 40);

            for (int i = 0; i < risks.length; i++) {
                int value = riskData.getOrDefault(risks[i], 0);
                int barHeight = (value * (height - 120)) / max;
                int x = startX + i * (barWidth + spacing);
                int y = height - 80 - barHeight;

                g2d.setColor(colors[i]);
                g2d.fillRect(x, y, barWidth, barHeight);

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.valueOf(value), x + barWidth/2 - 5, y - 5);
                g2d.drawString(risks[i], x + barWidth/2 - 15, height - 60);
            }
        }
    }

    public void showMainWindow() {
        setVisible(true);
    }
}
