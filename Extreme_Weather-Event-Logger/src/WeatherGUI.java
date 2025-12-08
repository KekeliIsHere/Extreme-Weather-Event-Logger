import javax.swing.*;                       //for GUI windows, buttons, dialogs
import javax.swing.table.*;                 // for Table components and models
import java.awt.*;                           // for colors , layout and the fonts
import java.time.LocalDateTime;
import java.util.Map;                      //for mapping, ke

public class WeatherGUI extends JFrame {
    private final WeatherManager manager = new WeatherManager();
    private static final String[] COLUMNS = {"ID", "Type", "Location", "Date", "Intensity", "Risk"};
    private final DefaultTableModel model = new DefaultTableModel(COLUMNS, 0);

    // Red

    public WeatherGUI() {
        setTitle("Weather Event Logger");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

//header
        JLabel header = new JLabel("WEATHER EVENT LOGGER", SwingConstants.CENTER);
        // Color palette
        // Blue
        Color PRIMARY = Color.BLUE;
        { // Setup block
            header.setFont(new Font("Arial", Font.BOLD, 22));
            header.setForeground(Color.WHITE);
            header.setOpaque(true);
            header.setBackground(PRIMARY);
            header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        }


        // Table
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
    // Custom coloring for Risk column (High=Red, Medium=Yellow, Low=Green)
        table.getColumnModel().getColumn(5).setCellRenderer(new RiskRenderer());

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Button container
        buttonPanel.setBackground(Color.WHITE);                                     // White background

               // Button labels and colors
        String[] labels = {"Add Event", "Delete", "Search", "Statistics", "Chart"};
        // Light Blue (cyan is light blue)
        // Green
        Color LOW = Color.GREEN;
        // Orange
        Color MEDIUM = Color.ORANGE;
        Color[] colors = {PRIMARY, Color.RED, LOW, MEDIUM, Color.MAGENTA};

           // Create and add all buttons
        for (int i = 0; i < labels.length; i++) {
            buttonPanel.add(createButton(labels[i], colors[i]));  // Blue, Red, Green, Orange, Purple
        }

        // Action listeners: action that is performed when buttons are pressed
        ((JButton)buttonPanel.getComponent(0)).addActionListener(_ -> addEvent());
        ((JButton)buttonPanel.getComponent(1)).addActionListener(_ -> deleteEvent());
        ((JButton)buttonPanel.getComponent(2)).addActionListener(_ -> searchEvents());
        ((JButton)buttonPanel.getComponent(3)).addActionListener(_ -> showStatistics());
        ((JButton)buttonPanel.getComponent(4)).addActionListener(_ -> showChart());

        // Layout
        add(header, BorderLayout.NORTH);      // Adds the title header at the top of window north is up
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);      //Adds button panels down

        refreshTable();                    // Load/update table with current data
        setVisible(true);                       // Make window visible on screen
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

        // Add to panel: Label then Field
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
        // Get the table component from the window (complex casting to access the JTable)
        JTable table = (JTable) ((JViewport) ((JScrollPane) getContentPane().getComponent(1)).getComponent(0)).getView();
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event first");  // Show error: need selection
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        if (manager.deleteEvent(id)) {      // If deletion successful
            model.removeRow(row);           // Remove row from table model
            JOptionPane.showMessageDialog(this, "Event deleted");  // Show success message
        }
    }

    private void searchEvents() {
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
        StringBuilder stats = new StringBuilder();
        stats.append("=== WEATHER EVENT STATISTICS ===\n\n");
        stats.append("Total Events: ").append(manager.getTotalEvents()).append("\n\n");

        stats.append("Events by Type:\n");
        manager.getEventCountByType().forEach((type, count) ->
                stats.append("  ").append(type).append(": ").append(count).append("\n"));

        stats.append("\nRisk Level Distribution:\n");
        manager.getEventsByRiskLevel().forEach((risk, count) ->
                stats.append("  ").append(risk).append(": ").append(count).append("\n"));

        JTextArea area = new JTextArea(stats.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChart() {
        JDialog chartDialog = new JDialog(this, "Risk Level Chart", true);
        chartDialog.setSize(600, 400);
        chartDialog.setLocationRelativeTo(this);

        ChartPanel chartPanel = new ChartPanel(manager);
        chartDialog.add(chartPanel);
        chartDialog.setVisible(true);
    }

    // Risk Renderer
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
                    case "LOW":c.setBackground(Color.GREEN.brighter());      // Light green
                        break;
                    case "MEDIUM":c.setBackground(Color.YELLOW.brighter());     // Light yellow
                        break;
                    case "HIGH":c.setBackground(Color.ORANGE);                // Orange
                        break;
                    case "EXTREME": c.setBackground(Color.RED.darker());          // Dark red
                        c.setForeground(Color.WHITE);                 // White text
                        break;
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
            Color[] colors = {Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED};

            int width = getWidth();
            int height = getHeight();

            // Find max value
            int max = 1;
            for (int value : riskData.values()) {
                if (value > max) max = value;
            }

            // Draw chart
            int barWidth = 80;     // Bar width
            int spacing = 40;      //space b/w bars
            int startX = 60;          //left margin

            // Draw title
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.BLACK);
            g2d.drawString("Risk Level Distribution", width/2 - 100, 40);

            // Draw bars
            for (int i = 0; i < risks.length; i++) {
                int value = riskData.getOrDefault(risks[i], 0);
                int barHeight = (value * (height - 120)) / max;
                int x = startX + i * (barWidth + spacing);
                int y = height - 80 - barHeight;

                // Draw bar
                g2d.setColor(colors[i]);
                g2d.fillRect(x, y, barWidth, barHeight);

                // Draw value on top
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.valueOf(value), x + barWidth/2 - 5, y - 5);

                // Draw label
                g2d.drawString(risks[i], x + barWidth/2 - 15, height - 60);
            }
        }
    }
}
