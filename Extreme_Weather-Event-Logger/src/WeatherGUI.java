import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;

public class WeatherGUI extends JFrame {
    private final WeatherManager manager;
    private final DefaultTableModel tableModel;
    private final JTextArea analysisArea;

    public WeatherGUI() {
        manager = new WeatherManager();

        setTitle("Weather Event Logger");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Table
        String[] columns = {"ID", "Type", "Location", "Date", "Risk"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(4).setCellRenderer(new RiskRenderer());

        // Analysis area
        analysisArea = new JTextArea();
        analysisArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        analysisArea.setEditable(false);

        setupUI(table);
    }

    private void setupUI(JTable table) {
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("WEATHER EVENT LOGGER", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0, 100, 200));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Events"));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBorder(BorderFactory.createTitledBorder("Analysis"));
        JScrollPane scroll = new JScrollPane(analysisArea);
        scroll.setPreferredSize(new Dimension(800, 250));
        analysisPanel.add(scroll, BorderLayout.CENTER);

        splitPane.setTopComponent(tablePanel);
        splitPane.setBottomComponent(analysisPanel);
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);

        // Buttons
        add(createButtons(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(10, 10, 10));

        JButton addBtn = createButton("Add Event", Color.BLUE);
        JButton statsBtn = createButton("Statistics", Color.GREEN);
        JButton freqBtn = createButton("Analyze Frequency", Color.ORANGE);
        JButton predictBtn = createButton("Prediction",Color.BLUE);

        addBtn.addActionListener(_ -> addEvent());
        statsBtn.addActionListener(_ -> analysisArea.setText(manager.getStatistics()));
        freqBtn.addActionListener(_ -> analysisArea.setText(manager.analyzeFrequency()));
        predictBtn.addActionListener(_ -> analysisArea.setText(manager.getPrediction()));

        panel.add(addBtn);
        panel.add(statsBtn);
        panel.add(freqBtn);
        panel.add(predictBtn);

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Object[][] data = manager.getTableData();
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private void addEvent() {
        String[] types = {"FLOOD", "EARTHQUAKE", "WILDFIRE", "HURRICANE"};
        String type = (String) JOptionPane.showInputDialog(this,
                "Select event type:", "Add Event",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if (type == null) return;

        try {
            // Basic info
            JTextField location = new JTextField(15);
            JTextField date = new JTextField(LocalDateTime.now().toLocalDate().toString());
            JSpinner intensity = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
            JTextField cause = new JTextField(15);

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.add(new JLabel("Location:"));
            panel.add(location);
            panel.add(new JLabel("Date (YYYY-MM-DD):"));
            panel.add(date);
            panel.add(new JLabel("Intensity (1-10):"));
            panel.add(intensity);
            panel.add(new JLabel("Cause:"));
            panel.add(cause);

            if (JOptionPane.showConfirmDialog(this, panel, "Event Details",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                LocalDateTime eventDate = LocalDateTime.parse(date.getText() + "T12:00:00");
                Weather event = null;

                switch (type) {
                    case "FLOOD":
                        double water = Double.parseDouble(JOptionPane.showInputDialog("Water Level (m):"));
                        event = new Flood(location.getText(), eventDate,
                                (int) intensity.getValue(), cause.getText(), water);
                        break;
                    case "EARTHQUAKE":
                        double mag = Double.parseDouble(JOptionPane.showInputDialog("Magnitude:"));
                        event = new Earthquake(location.getText(), eventDate,
                                (int) intensity.getValue(), cause.getText(), mag);
                        break;
                    case "WILDFIRE":
                        double area = Double.parseDouble(JOptionPane.showInputDialog("Area (ha):"));
                        int contain = Integer.parseInt(JOptionPane.showInputDialog("Containment %:"));
                        event = new WildFire(location.getText(), eventDate,
                                (int) intensity.getValue(), cause.getText(), area, contain);
                        break;
                    case "HURRICANE":
                        double wind = Double.parseDouble(JOptionPane.showInputDialog("Wind Speed (mph):"));
                        double pressure = Double.parseDouble(JOptionPane.showInputDialog("Pressure (mb):"));
                        event = new Hurricane(location.getText(), eventDate,
                                (int) intensity.getValue(), cause.getText(), wind, pressure);
                        break;
                }

                if (event != null) {
                    manager.addEvent(event);
                    refreshTable();


                    String details = """
                            Event added successfully!
                            
                            Click Statistics button to see all events.""";

                    JOptionPane.showMessageDialog(this, details,
                            "Event Added", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
// for coloring the cell based on risklevel
   public  static class RiskRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if (value instanceof RiskLevel risk) {
            
                switch (risk) {
                    case LOW:
                        c.setBackground(Color.GREEN.brighter());
                        break;
                    case MEDIUM:
                        c.setBackground(Color.YELLOW);
                        break;
                    case HIGH:
                        c.setBackground(Color.ORANGE);
                        break;
                    case EXTREME:
                        c.setBackground(Color.RED);
                        break;
                }
            }
            return c;
        }
    }
}
