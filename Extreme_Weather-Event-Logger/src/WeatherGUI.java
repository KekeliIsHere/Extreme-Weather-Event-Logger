import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;

public class WeatherGUI extends JFrame {
    private static final Color BLUE = Color.BLUE;
    private static final Color GREEN = Color.GREEN;
    private static final Color ORANGE = Color.ORANGE;
    private static final Color RED = Color.RED;

    private final WeatherManager manager;
    private final DefaultTableModel model;
    private final JTable table;

    public WeatherGUI(WeatherManager manager) {
        this.manager = manager;

        setTitle("Weather Event Logger");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create table
        String[] columns = {"ID", "Type", "Location", "Date", "Intensity", "Risk"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(5).setCellRenderer(new RiskRenderer());

        setupUI();
        refreshTable();
    }

    private void setupUI() {
        // Header
        JLabel header = new JLabel("WEATHER EVENT LOGGER", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // Table
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);

        JButton addBtn = createButton("Add Event", BLUE);
        JButton deleteBtn = createButton("Delete", RED);
        JButton searchBtn = createButton("Search", GREEN);
        JButton statsBtn = createButton("Statistics", ORANGE);
        JButton chartBtn = createButton("Chart", BLUE);

        addBtn.addActionListener(e -> addEvent());
        deleteBtn.addActionListener(e -> deleteEvent());
        searchBtn.addActionListener(e -> searchEvents());
        statsBtn.addActionListener(e -> showStatistics());
        chartBtn.addActionListener(e -> showChart());

        panel.add(addBtn);
        panel.add(deleteBtn);
        panel.add(searchBtn);
        panel.add(statsBtn);
        panel.add(chartBtn);

        return panel;
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
        Object[][] data = manager.getTableData();
        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    private void addEvent() {
        String[] types = {"FLOOD", "EARTHQUAKE", "WILDFIRE","HURRICANE"};

        String type = (String) JOptionPane.showInputDialog(this,
                "Select Event Type:", "Add Event", JOptionPane.QUESTION_MESSAGE,
                null, types, types[0]);

        if (type == null) return;

        // Get basic info
        Object[] basicInfo = showInputDialog("Basic Information", type);
        if (basicInfo == null) return;

        String location = (String) basicInfo[0];
        LocalDateTime start = (LocalDateTime) basicInfo[1];
        int intensity = (int) basicInfo[2];
        String cause = (String) basicInfo[3];

        // Create event based on type
        Weather event = null;

        switch (type) {
            case "FLOOD":
                String water = JOptionPane.showInputDialog("Water Level (m):");
                if (water != null) {
                    event = manager.createFloodEvent(location, start, intensity, cause,
                            Double.parseDouble(water));
                }
                break;

            case "EARTHQUAKE":
                String mag = JOptionPane.showInputDialog("Magnitude:");
                String depth = JOptionPane.showInputDialog("Depth (km):");
                String epicenter = JOptionPane.showInputDialog("Epicenter:");
                if (mag != null && depth != null && epicenter != null) {
                    event = manager.createEarthquakeEvent(location, start, intensity, cause,
                            Double.parseDouble(mag),
                            Double.parseDouble(depth),
                            epicenter);
                }
                break;
            case "WILDFIRE":
                String area = JOptionPane.showInputDialog("Area Burned (ha):");
                String contain = JOptionPane.showInputDialog("Containment %:");
                String fuel = JOptionPane.showInputDialog("Fuel Type:");
                if (area != null && contain != null && fuel != null) {
                    event = manager.createWildfireEvent(location, start, intensity, cause,
                            Double.parseDouble(area),
                            Integer.parseInt(contain),
                            fuel);
                }
                break;
            case "HURRICANE":
                String windSpeed = JOptionPane.showInputDialog("Windspeed(kilometers per hour (km/h): ");
                String pressure = JOptionPane.showInputDialog("Pressure(Pa): ");
                String size = JOptionPane.showInputDialog("Radius in miles");

                if (windSpeed != null && pressure != null && size != null) {
                    event = manager.createHurricaneEvent(location, start,intensity, cause,
                            Double.parseDouble(windSpeed),
                            Double.parseDouble(pressure),
                            Double.parseDouble(size)
                            );
                }
                break;
        }

        if (event != null) {
            manager.addEvent(event);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Event added successfully!");
        }
    }

    private Object[] showInputDialog(String title, String type) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField location = new JTextField("");
        JTextField date = new JTextField(LocalDateTime.now().toLocalDate().toString());
        JTextField time = new JTextField(LocalDateTime.now().toLocalTime().toString().substring(0,5));
        JSpinner intensity = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        JTextField cause = new JTextField("eg natural/human");

        panel.add(new JLabel("Location:")); panel.add(location);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(date);
        panel.add(new JLabel("Time (HH:MM):")); panel.add(time);
        panel.add(new JLabel("Intensity (1-10):")); panel.add(intensity);
        panel.add(new JLabel("Cause:")); panel.add(cause);

        if (JOptionPane.showConfirmDialog(this, panel, title,
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime start = LocalDateTime.parse(date.getText() + "T" + time.getText() + ":00");
                return new Object[]{location.getText(), start, intensity.getValue(), cause.getText()};
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date/time format!");
            }
        }
        return null;
    }

    private void deleteEvent() {
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

    private void searchEvents() {
        String term = JOptionPane.showInputDialog(this, "Enter location to search:");
        if (term != null && !term.trim().isEmpty()) {
            String results = manager.getSearchResultsText(term);
            JTextArea area = new JTextArea(results);
            JOptionPane.showMessageDialog(this, new JScrollPane(area));
        }
    }

    private void showStatistics() {
        String statsText = "=== WEATHER EVENT STATISTICS ===\n\n" +
                manager.getAnalysisSummary();

        JTextArea area = new JTextArea(statsText);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Weather Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChart() {
        JDialog dialog = new JDialog(this, "Risk Level Chart", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        // Create simple chart panel
        JPanel chartPanel = new ChartPanel(manager);
        dialog.add(chartPanel);
        dialog.setVisible(true);
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
                    case "LOW": c.setBackground(GREEN); break;
                    case "MEDIUM": c.setBackground(ORANGE); break;
                    case "HIGH": c.setBackground(RED); break;
                    case "EXTREME":
                        c.setBackground(RED.darker());
                        c.setForeground(Color.WHITE);
                        break;
                }

                if (isSelected) c.setBackground(c.getBackground().darker());
            }
            return c;
        }
    }

    // Chart Panel (simplified - no HashMaps)
    static class ChartPanel extends JPanel {
        private final WeatherManager manager;
        private final String[] riskLevels = {"LOW", "MEDIUM", "HIGH", "EXTREME"};
        private final Color[] colors = {GREEN, ORANGE, RED, RED.darker()};

        public ChartPanel(WeatherManager manager) {
            this.manager = manager;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Get data from manager
            int[] counts = manager.getRiskLevelCounts();

            // Find maximum for scaling
            int max = 1;
            for (int count : counts) {
                if (count > max) max = count;
            }

            int width = getWidth();
            int height = getHeight();

            // Draw title
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Risk Level Distribution", width/2 - 100, 40);

            // Draw bars
            int barWidth = 80;
            int spacing = 40;
            int startX = 60;

            for (int i = 0; i < riskLevels.length; i++) {
                int value = counts[i];
                int barHeight = (value * (height - 120)) / max;
                int x = startX + i * (barWidth + spacing);
                int y = height - 80 - barHeight;

                // Draw bar
                g2d.setColor(colors[i]);
                g2d.fillRect(x, y, barWidth, barHeight);

                // Draw value and label
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.drawString(String.valueOf(value), x + barWidth/2 - 5, y - 5);
                g2d.drawString(riskLevels[i], x + barWidth/2 - 20, height - 60);
            }
        }
    }

    public void showMainWindow() {
        setVisible(true);
    }
}
