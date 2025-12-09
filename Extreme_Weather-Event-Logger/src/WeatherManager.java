import java.util.*;

public class WeatherManager {
    private final ArrayList<Weather> events = new ArrayList<>();

    public void addEvent(Weather event) {
        events.add(event);
    }
//5 cols ID, Type, location, date, risk
    public Object[][] getTableData() {
        Object[][] data = new Object[events.size()][5];
        for (int i = 0; i < events.size(); i++) {
            Weather event = events.get(i);
            data[i][0] = event.getEventId();
            data[i][1] = event.getEventType();
            data[i][2] = event.getLocation();
            data[i][3] = event.getStartDateTime().toLocalDate();
            data[i][4] = event.getRiskLevel();
        }
        return data;
    }

    // fxn to count events by type-used by other methods
    private int[] countEventsByType() {
        int[] counts = new int[4];        // [Flood, Earthquake, Wildfire, Hurricane]
        for (Weather event : events) {
            switch (event.getEventType()) {
                case FLOOD: counts[0]++; break;
                case EARTHQUAKE: counts[1]++; break;
                case WILDFIRE: counts[2]++; break;
                case HURRICANE: counts[3]++; break;
            }
        }
        return counts;
    }

    //method to count events by risk
    private int[] countEventsByRisk() {
        int[] counts = new int[4]; // [Low, Medium, High, Extreme]
        for (Weather event : events) {
            switch (event.getRiskLevel()) {
                case LOW: counts[0]++; break;
                case MEDIUM: counts[1]++; break;
                case HIGH: counts[2]++; break;
                case EXTREME: counts[3]++; break;
            }
        }
        return counts;
    }

    // Statistics and analyses
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\tWEATHER EVENT STATISTICS\n\n");

        if (events.isEmpty()) {
            sb.append("No events to analyze.\n");
            return sb.toString();
        }

        sb.append("Total Events: ").append(events.size()).append("\n\n");

        // will display count of event based on type
        int[] typeCounts = countEventsByType();
        sb.append("Events by Type:\n");
        sb.append("Floods: ").append(typeCounts[0]).append("\n");
        sb.append("Earthquakes: ").append(typeCounts[1]).append("\n");
        sb.append("Wildfires: ").append(typeCounts[2]).append("\n");
        sb.append("Hurricanes: ").append(typeCounts[3]).append("\n\n");

        //will display count of event based on type
        int[] riskCounts = countEventsByRisk();
        sb.append("Events by Risk Level:\n");
        sb.append("Low: ").append(riskCounts[0]).append("\n");
        sb.append("Medium: ").append(riskCounts[1]).append("\n");
        sb.append("High: ").append(riskCounts[2]).append("\n");
        sb.append("Extreme: ").append(riskCounts[3]).append("\n\n");

        // Find High risk locations : one whose risk level could be high or extreme
        sb.append("High Risk Locations:\n");
        HashMap<String, Integer> highRisk = new HashMap<>();
        for (Weather event : events) {
            if (event.getRiskLevel() == RiskLevel.HIGH ||
                    event.getRiskLevel() == RiskLevel.EXTREME) {
                String loc = event.getLocation();
                highRisk.put(loc, highRisk.getOrDefault(loc, 0) + 1);
            }
        }

        if (highRisk.isEmpty()) {
            sb.append("No high-risk events.\n\n");
        } else {
            for (Map.Entry<String, Integer> entry : highRisk.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" events\n");
            }
            sb.append("\n");
        }

        // All event details- call the toString
        sb.append("\t\tALL EVENT DETAILS\n\n");
        for (Weather event : events) {
            sb.append(event.toString()).append("\n\n");
        }

        return sb.toString();
    }

    // Frequency analysis - focuses on location frequency only
    public String analyzeFrequency() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\tFREQUENCY ANALYSIS\n\n");

        if (events.isEmpty()) {
            sb.append("No events to analyze.\n");
            return sb.toString();
        }

        sb.append("Total Events: ").append(events.size()).append("\n\n");

        // location frequency
        sb.append("Event Frequency by Location:\n");
        HashMap<String, Integer> locationCount = new HashMap<>();

        for (Weather event : events) {
            String loc = event.getLocation();
            locationCount.put(loc, locationCount.getOrDefault(loc, 0) + 1);
        }

        // Show locations with multiple events
        boolean hasMultiple = false;
        for (Map.Entry<String, Integer> entry : locationCount.entrySet()) {
            if (entry.getValue() > 1) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" events (repeated)\n");
                hasMultiple = true;
            } else {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" event\n");
            }
        }

        if (!hasMultiple) {
            sb.append("\nNote: No locations have repeated events yet.\n");
        }

        return sb.toString();
    }

    // Prediction
    public String getPrediction() {
        if (events.isEmpty()) {
            return "Add events to get predictions.\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\t\tPREDICTION\n\n");
        int[] typeCounts = countEventsByType();

        // Find most common type
        int maxIndex = 0;
        for (int i = 1; i < 4; i++) {
            if (typeCounts[i] > typeCounts[maxIndex]) {
                maxIndex = i;
            }
        }

        String[] typeNames = {"Flood", "Earthquake", "Wildfire", "Hurricane"};
        String predictedType = typeNames[maxIndex];
        int maxCount = typeCounts[maxIndex];

        sb.append("Based on ").append(events.size()).append(" past events:\n\n");
        sb.append("Most Likely Next Event: ").append(predictedType).append("\n");
        sb.append("Reason: ").append(maxCount).append(" of ").append(events.size())
                .append(" past events were ").append(predictedType).append("s\n\n");

        sb.append("Preparation Advice:\n");
        switch (predictedType) {
            case "Flood":
                sb.append("- Prepare sandbags and water pumps\n");
                sb.append("- Check and clear drainage systems\n");
                break;
            case "Earthquake":
                sb.append("- Secure heavy furniture and appliances\n");
                sb.append("- Practice drop, cover, and hold drills\n");
                break;
            case "Wildfire":
                sb.append("- Clear dry vegetation around properties\n");
                break;
            case "Hurricane":
                sb.append("- Reinforce windows and doors\n");
                sb.append("- Stock emergency supplies for 3+ days\n");
                break;
        }

        return sb.toString();
    }
}
