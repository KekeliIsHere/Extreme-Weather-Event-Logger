import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WeatherGUI extends JFrame {

    // from UML
    private WeatherManager manager;

    // internal GUI components (still allowed, just not shown in UML)
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JButton addEventButton;
    private JButton sortByDateButton;
    private JButton sortByLocationButton;
    private JButton sortByDurationButton;

    // ----------------- constructor from UML -----------------
    public WeatherGUI(WeatherManager manager) {
        this.manager = manager;
    }

    // ----------------- showMainWindow() from UML -----------------
    public void showMainWindow() {
        // window setup
        setTitle("Extreme Weather Event Logger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // create components
        String[] columnNames = {"ID", "Type", "Location", "Date", "Duration", "Intensity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(tableModel);

        addEventButton = new JButton("Add Event");
        sortByDateButton = new JButton("Sort by Date");
        sortByLocationButton = new JButton("Sort by Location");
        sortByDurationButton = new JButton("Sort by Duration");

        // layout components
        JScrollPane scrollPane = new JScrollPane(eventTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addEventButton);
        buttonPanel.add(sortByDateButton);
        buttonPanel.add(sortByLocationButton);
        buttonPanel.add(sortByDurationButton);
        add(buttonPanel, BorderLayout.NORTH);

        // connect buttons to UML methods
        addEventButton.addActionListener(e -> handleAddEvent());
        sortByDateButton.addActionListener(e -> handleSortByDate());
        sortByLocationButton.addActionListener(e -> handleSortByLocation());
        sortByDurationButton.addActionListener(e -> handleSortByDuration());

        // show current events
        showEventList();

        setVisible(true);
    }

    // ----------------- showAddEventForm() from UML -----------------
    public void showAddEventForm() {
        String type = JOptionPane.showInputDialog(this,
                "Event type (Flood/Hurricane/Wildfire/Earthquake):");
        if (type == null || type.isBlank()) return;

        String location = JOptionPane.showInputDialog(this,
                "Location (e.g. Accra, Ghana):");
        if (location == null || location.isBlank()) return;

        String date = JOptionPane.showInputDialog(this,
                "Date (month/year, e.g. 10/2025):");
        if (date == null || date.isBlank()) return;

        String durationStr = JOptionPane.showInputDialog(this,
                "Duration (in days):");
        String intensityStr = JOptionPane.showInputDialog(this,
                "Intensity (1–10):");

        try {
            int duration = Integer.parseInt(durationStr);
            double intensity = Double.parseDouble(intensityStr);
            int newId = manager.getAllEvents().size() + 1;

            // simple version: generic Weather object
            Weather event = new Weather(newId, location, date, duration, intensity);
            manager.addEvent(event);

            showEventList();   // refresh table

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Duration must be an integer and intensity must be a number.",
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
                    event.getEventType(),      // or event.getClass().getSimpleName()
                    event.getLocation(),
                    event.getStartDateTime(),
                    event.getDurationInHours(),
                    event.getIntensity()
            };
            tableModel.addRow(row);
        }
    }

    // ----------------- showGraphs() from UML -----------------
    public void showGraphs() {
        JOptionPane.showMessageDialog(this,
                "Graphs will show trends in event types and intensities.",
                "Graphs", JOptionPane.INFORMATION_MESSAGE);
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
}
