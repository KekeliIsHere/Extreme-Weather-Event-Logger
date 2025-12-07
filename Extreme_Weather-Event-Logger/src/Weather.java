import java.time.LocalDateTime;
public class Weather {
    // Attributes
    private final String eventId;
    private final EventType eventType;
    private String location;
    private final LocalDateTime startDateTime;
    private double durationInHours;
    private int intensity; // 1-10
    private String cause;

    // Simple counter for IDs
    private static int eventCounter = 1;

    // Constructor - public since Weather is concrete
    public Weather(EventType eventType, String location, LocalDateTime startDateTime,
                   double durationInHours, int intensity, String cause) {

        this.eventType = eventType;
        this.location = location;
        this.startDateTime = startDateTime;

        // Use setters for validation
        setDurationInHours(durationInHours);
        setIntensity(intensity);
        setCause(cause);

        // Generate simple ID
        this.eventId = generateEventId();
    }

    // Generate ID: "W001", "W002", etc.
    private String generateEventId() {
        return "W" + String.format("%03d", eventCounter++);
    }

    // Simple getters
    public String getEventId() { return eventId; }
    public EventType getEventType() { return eventType; }
    public String getLocation() { return location; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public double getDurationInHours() { return durationInHours; }
    public int getIntensity() { return intensity; }
    public String getCause() { return cause; }

    // Derived method: endDateTime
    public LocalDateTime getEndDateTime() {
        long minutesToAdd = (long) (durationInHours * 60);
        return startDateTime.plusMinutes(minutesToAdd);
    }
    // Risk level method - can be overridden by subclasses
    public String getRiskLevel() {
        if (intensity <= 3) return "LOW";
        else if (intensity <= 6) return "MEDIUM";
        else if (intensity <= 8) return "HIGH";
        else return "EXTREME";
    }

    // Validation setters
    public void setIntensity(int intensity) {
        if (intensity < 1 || intensity > 10) {
            throw new IllegalArgumentException("Intensity must be between 1 and 10");
        }
        this.intensity = intensity;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location.trim();
    }

    public void setDurationInHours(double durationInHours) {
        if (durationInHours < 0) {
            throw new IllegalArgumentException("Duration must be non-negative");
        }
        this.durationInHours = durationInHours;
    }

    public void setCause(String cause) {
        if (cause == null || cause.trim().isEmpty()) {
            throw new IllegalArgumentException("Cause cannot be empty");
        }
        this.cause = cause.trim();
    }

    // toString method - matching your style
    @Override
    public String toString() {
        return "Weather{" +
                "eventId='" + getEventId() + '\'' +
                ", eventType=" + getEventType() +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", durationInHours=" + getDurationInHours() +
                ", endDateTime=" + getEndDateTime() +
                ", intensity=" + getIntensity() +
                ", riskLevel=" + getRiskLevel() +
                ", cause='" + getCause() + '\'' +
                '}';
    }
}
