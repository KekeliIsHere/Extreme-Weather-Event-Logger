
import java.util.*;
import java.time.LocalDateTime;

public class WeatherManager {
    private final ArrayList<Weather> events = new ArrayList<>();


    public void addEvent(Weather event) {
        events.add(event);
    }

    public boolean deleteEvent(String eventId) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId().equals(eventId)) {
                events.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Weather> searchByLocation(String location) {
        List<Weather> results = new ArrayList<>();
        if (location == null || location.trim().isEmpty()) {
            return results;
        }

        String searchTerm = location.toLowerCase().trim();
        for (Weather event : events) {
            if (event.getLocation().toLowerCase().contains(searchTerm)) {
                results.add(event);
            }
        }
        return results;
    }

    // GUI Helper Methods
    public String getSearchResultsText(String location) {
        List<Weather> results = searchByLocation(location);
        if (results.isEmpty()) {
            return "No events found for location: " + location;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(results.size()).append(" events:\n\n");
        for (Weather event : results) {
            sb.append("• ").append(event.getEventId())
                    .append(" - ").append(event.getLocation())
                    .append(" (").append(event.getEventType()).append(")\n");
        }
        return sb.toString();
    }

    public Weather createFloodEvent(String location, LocalDateTime start,
                                    int intensity, String cause, double waterLevel) {
        return new Flood(location, start, intensity, cause, waterLevel);
    }

    public Weather createEarthquakeEvent(String location, LocalDateTime start,
                                         int intensity, String cause,
                                         double magnitude, double depth, String epicenter) {
        return new Earthquake(location, start, intensity, cause,
                magnitude, depth, epicenter);
    }

    public Weather createWildfireEvent(String location, LocalDateTime start,
                                       int intensity, String cause,
                                       double areaBurned, int containmentPercent, String fuelType) {
        return new WildFire(location, start, intensity, cause,
                areaBurned, containmentPercent, fuelType);
    }
    public Weather createHurricaneEvent(String location,
                                        LocalDateTime start,                                                                                               int intensity,
                                        String cause,
                                        double windSpeed,
                                        double pressure,
                                        double size) {
        return new Hurricane(location, start,intensity,cause,windSpeed,pressure,size);
    }

    // Get table data for GUI
    public Object[][] getTableData() {
        Object[][] data = new Object[events.size()][6];
        for (int i = 0; i < events.size(); i++) {
            Weather event = events.get(i);
            data[i][0] = event.getEventId();
            data[i][1] = event.getEventType();
            data[i][2] = event.getLocation();
            data[i][3] = event.getStartDateTime().toLocalDate();
            data[i][4] = event.getIntensity();
            data[i][5] = event.getRiskLevel();
        }
        return data;
    }

    // Statistics methods without HashMaps
    public String getEventCountByTypeText() {
        if (events.isEmpty()) return "";

        int floodCount = 0;
        int earthquakeCount = 0;
        int wildfireCount = 0;
        int hurricaneCount = 0;

        for (Weather event : events) {
            switch (event.getEventType()) {
                case FLOOD: floodCount++; break;
                case EARTHQUAKE: earthquakeCount++; break;
                case WILDFIRE: wildfireCount++; break;
                case HURRICANE: hurricaneCount++; break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("FLOOD: ").append(floodCount).append("\n");
        sb.append("EARTHQUAKE: ").append(earthquakeCount).append("\n");
        sb.append("WILDFIRE: ").append(wildfireCount).append("\n");
        sb.append("HURRICANE: ").append(hurricaneCount).append("\n");

        return sb.toString();
    }

    public String getEventsByRiskLevelText() {
        if (events.isEmpty()) return "";

        int lowCount = 0;
        int mediumCount = 0;
        int highCount = 0;
        int extremeCount = 0;

        for (Weather event : events) {
            String risk = event.getRiskLevel();
            switch (risk) {
                case "LOW": lowCount++; break;
                case "MEDIUM": mediumCount++; break;
                case "HIGH": highCount++; break;
                case "EXTREME": extremeCount++; break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LOW: ").append(lowCount).append("\n");
        sb.append("MEDIUM: ").append(mediumCount).append("\n");
        sb.append("HIGH: ").append(highCount).append("\n");
        sb.append("EXTREME: ").append(extremeCount).append("\n");

        return sb.toString();
    }

    public String getAnalysisSummary() {
        if (events.isEmpty()) return "No events to analyze";

        StringBuilder summary = new StringBuilder();
        summary.append("Total Events: ").append(events.size()).append("\n\n");

        summary.append("Events by Type:\n");
        summary.append(getEventCountByTypeText());

        summary.append("\nRisk Level Distribution:\n");
        summary.append(getEventsByRiskLevelText());

        return summary.toString();
    }

    // Get data for chart (without HashMaps)
    public int[] getRiskLevelCounts() {
        int[] counts = new int[4]; // LOW, MEDIUM, HIGH, EXTREME

        for (Weather event : events) {
            String risk = event.getRiskLevel();
            switch (risk) {
                case "LOW": counts[0]++; break;
                case "MEDIUM": counts[1]++; break;
                case "HIGH": counts[2]++; break;
                case "EXTREME": counts[3]++; break;
            }
        }

        return counts;
    }

    public ArrayList<Weather> getAllEvents() {
        ArrayList<Weather> copy = new ArrayList<>();
        for (Weather event : events) {
            copy.add(event);
        }
        return copy;
    }

    public int getTotalEvents() {
        return events.size();
    }
}
