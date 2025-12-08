import java.util.*;

public class WeatherManager {
    private final ArrayList<Weather> events = new ArrayList<>();

    public void addEvent(Weather event) {
        events.add(event);
    }

    public boolean deleteEvent(String eventId) {
        return events.removeIf(e -> e.getEventId().equals(eventId));
    }

    public List<Weather> searchByLocation(String location) {
        List<Weather> results = new ArrayList<>();
        String searchTerm = location.toLowerCase();
        for (Weather event : events) {
            if (event.getLocation().toLowerCase().contains(searchTerm)) {
                results.add(event);
            }
        }
        return results;
    }

    public Map<EventType, Integer> getEventCountByType() {
        Map<EventType, Integer> counts = new HashMap<>();
        for (Weather event : events) {
            EventType type = event.getEventType();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        return counts;
    }

    public Map<String, Integer> getEventsByRiskLevel() {
        Map<String, Integer> risks = new HashMap<>();
        for (Weather event : events) {
            String risk = event.getRiskLevel();
            risks.put(risk, risks.getOrDefault(risk, 0) + 1);
        }
        return risks;
    }

    public String getAnalysisSummary() {
        if (events.isEmpty()) return "No events to analyze";

        StringBuilder summary = new StringBuilder();
        summary.append("Total Events: ").append(events.size()).append("\n\n");

        summary.append("Events by Type:\n");
        getEventCountByType().forEach((type, count) ->
                summary.append("  ").append(type).append(": ").append(count).append("\n"));

        summary.append("\nRisk Distribution:\n");
        getEventsByRiskLevel().forEach((risk, count) ->
                summary.append("  ").append(risk).append(": ").append(count).append("\n"));

        return summary.toString();
    }

    public ArrayList<Weather> getAllEvents() {
        return new ArrayList<>(events);
    }

    public int getTotalEvents() {
        return events.size();
    }
}
