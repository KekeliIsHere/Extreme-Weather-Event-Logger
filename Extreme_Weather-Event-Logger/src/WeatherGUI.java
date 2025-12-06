import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class WeatherGUI extends JFrame {

    // from UML
    private WeatherManager manager;

    // internal GUI components
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JButton addEventButton;
    private JButton sortByDateButton;
    private JButton sortByLocationButton;
    private JButton sortByDurationButton;
    private JButton showGraphsButton;   // NEW: button to open graphs

    // ----------------- constructor from UML -----------------
    public WeatherGUI(WeatherManager manager) {
        this.manager = manager;
    }

    // ----------------- showMainWindow() from UML -----------------
    public void showMainWindow() {
        // window setup
        setTitle("Extreme Weather Event Logger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // create components
        String[] columnNames = {"ID", "Type", "Location", "Start Date/Time", "Duration (hrs)", "Intensity", "Risk"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(tableModel);

        addEventButton = new JButton("Add Event");
        sortByDateButton = new JButton("Sort by Date");
        sortByLocationButton = new JButton("Sort by Location");
        sortByDurationButton = new JButton("Sort by Duration");
        showGraphsButton = new JButton("Show Graphs");   // NEW

        // layout components
        JScrollPane scrollPane = new JScrollPane(eventTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addEventButton);
        buttonPanel.add(sortByDateButton);
        buttonPanel.add(sortByLocationButton);
        buttonPanel.add(sortByDurationButton);
        buttonPanel.add(showGraphsButton);  // NEW
        add(buttonPanel, BorderLayout.NORTH);

        // connect buttons to UML methods
        addEventButton.addActionListener(e -> handleAddEvent());
        sortByDateButton.addActionListener(e -> handleSortByDate());
        sortByLocationButton.addActionListener(e -> handleSortByLocation());
        sortByDurationButton.addActionListener(e -> handleSortByDuration());
        showGraphsButton.addActionListener(e -> showGraphs());    // NEW

        // show current events
        showEventList();

        setVisible(true);
    }

    // ----------------- showAddEventForm() from UML -----------------
    public void showAddEventForm() {
        // Ask for basic fields
        String typeInput = JOptionPane.showInputDialog(this,
                "Event type (FLOOD / HURRICANE / EARTHQUAKE):");
        if (typeInput == null || typeInput.isBlank()) return;

        EventType eventType;
        try {
            eventType = EventType.valueOf(typeInput.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid type. Use FLOOD, HURRICANE or EARTHQUAKE.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String location = JOptionPane.showInputDialog(this,
                "Location (e.g. Accra, Ghana):");
        if (location == null || location.isBlank()) return;

        String startStr = JOptionPane.showInputDialog(this,
                "Start date and time (yyyy-MM-dd HH:mm)\nExample: 2025-11-30 13:00");
        if (startStr == null || startStr.isBlank()) return;

        String durationStr = JOptionPane.showInputDialog(this,
                "Duration (in hours):");
        if (durationStr == null || durationStr.isBlank()) return;

        String intensityStr = JOptionPane.showInputDialog(this,
                "Intensity (1–10):");
        if (intensityStr == null || intensityStr.isBlank()) return;

        String cause = JOptionPane.showInputDialog(this,
                "Cause (e.g. natural / human):");
        if (cause == null || cause.isBlank()) return;

        try {
            // parse values
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDateTime = LocalDateTime.parse(startStr.trim(), formatter);
            double durationInHours = Double.parseDouble(durationStr.trim());
            int intensity = Integer.parseInt(intensityStr.trim());

            Weather event;

            // If it's a hurricane, ask extra questions and create a Hurricane object
            if (eventType == EventType.HURRICANE) {
                String windStr = JOptionPane.showInputDialog(this,
                        "Wind speed (mph):");
                String pressureStr = JOptionPane.showInputDialog(this,
                        "Central pressure (millibars):");
                String sizeStr = JOptionPane.showInputDialog(this,
                        "Size (radius in miles):");

                if (windStr == null || pressureStr == null || sizeStr == null ||
                        windStr.isBlank() || pressureStr.isBlank() || sizeStr.isBlank()) {
                    return;
                }

                double windSpeed = Double.parseDouble(windStr.trim());
                double pressure = Double.parseDouble(pressureStr.trim());
                double size = Double.parseDouble(sizeStr.trim());

                event = new Hurricane(location, startDateTime, durationInHours,
                        intensity, cause, windSpeed, pressure, size);

            } else {
                // For now, generic Weather for FLOOD / EARTHQUAKE
                // Later you can replace with Flood/Earthquake subclasses.
                event = new Weather(eventType, location, startDateTime,
                        durationInHours, intensity, cause);
            }

            manager.addEvent(event);
            showEventList();   // refresh table

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Duration, intensity and numeric fields must be valid numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Date/time format must be yyyy-MM-dd HH:mm, e.g. 2025-11-30 13:00",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------- showEventList() from UML -----------------
    public void showEventList() {
        tableModel.setRowCount(0);  // clear table
        List<Weather> events = manager.getAllEvents();
        for (Weather event : events) {
            Object[] row = {
                    event.getEventId(),
                    event.getEventType(),                 // enum value
                    event.getLocation(),
                    event.getStartDateTime(),
                    event.getDurationInHours(),
                    event.getIntensity(),
                    event.getRiskLevel()                  // nice extra column
            };
            tableModel.addRow(row);
        }
    }

    // ----------------- showGraphs() from UML -----------------
    // Opens a small window with a simple bar chart of risk levels
    public void showGraphs() {
        JDialog dialog = new JDialog(this, "Risk Level Chart", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        GraphPanel graphPanel = new GraphPanel(manager);
        dialog.add(graphPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    // ----------------- handleAddEvent() from UML -----------------
    public void handleAddEvent() {
        showAddEventForm();
    }

    // ----------------- handleSortByDate() from UML -----------------
    public void handleSortByDate() {
        manager.sortByDate();
        showEventList();
    }

    // ----------------- handleSortByLocation() from UML -----------------
    public void handleSortByLocation() {
        manager.sortByLocation();
        showEventList();
    }

    // ----------------- handleSortByDuration() from UML -----------------
    public void handleSortByDuration() {
        manager.sortByDuration();
        showEventList();
    }

    // ================== Inner class for drawing graphs ==================
    // Simple bar chart of events per risk level (LOW, MEDIUM, HIGH, EXTREME)
    private static class GraphPanel extends JPanel {

        private final WeatherManager manager;

        public GraphPanel(WeatherManager manager) {
            this.manager = manager;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Get counts from the manager
            int low = manager.countEventsByRiskLevel("LOW");
            int medium = manager.countEventsByRiskLevel("MEDIUM");
            int high = manager.countEventsByRiskLevel("HIGH");
            int extreme = manager.countEventsByRiskLevel("EXTREME");

            int[] values = {low, medium, high, extreme};
            String[] labels = {"LOW", "MEDIUM", "HIGH", "EXTREME"};

            int width = getWidth();
            int height = getHeight();

            int max = 0;
            for (int v : values) {
                if (v > max) max = v;
            }
            if (max == 0) max = 1; // avoid divide by zero

            int margin = 50;
            int barWidth = (width - 2 * margin) / values.length;

            // Draw axes
            g.drawLine(margin, height - margin, width - margin, height - margin); // X axis
            g.drawLine(margin, margin, margin, height - margin);                  // Y axis

            // Draw bars
            for (int i = 0; i < values.length; i++) {
                int barHeight = (int) ((height - 2 * margin) * (values[i] / (double) max));
                int x = margin + i * barWidth + barWidth / 8;
                int y = height - margin - barHeight;

                g.fillRect(x, y, barWidth - barWidth / 4, barHeight);

                // Label under each bar
                g.drawString(labels[i], x + 5, height - margin + 15);
                // Value on top of bar
                g.drawString(String.valueOf(values[i]), x + 5, y - 5);
            }

            // Title
            g.drawString("Number of Events by Risk Level", margin, margin - 10);
        }
    }
}
