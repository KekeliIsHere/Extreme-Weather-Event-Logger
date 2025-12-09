import java.time.LocalDateTime;

public class Weather {

    private final String eventId;
    private final EventType eventType;
    private String location;
    private final LocalDateTime startDateTime;
    private int intensity;                                //val ranges from 1-10
    private String cause;
    private static int eventCounter = 1;                  //helps to generate IDs

    public Weather(EventType eventType, String location, LocalDateTime startDateTime,
                   int intensity, String cause) {

        this.eventType = eventType;
        this.startDateTime = startDateTime;
        setLocation(location);
        setIntensity(intensity);
        setCause(cause);
        this.eventId = generateEventId();
    }

    //Generates ID like W001, W002
    private String generateEventId() {
        return "W" + String.format("%03d", eventCounter++);
    }

    public String getEventId() { return eventId; }
    public EventType getEventType() { return eventType; }
    public String getLocation() { return location; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public int getIntensity() { return intensity; }
    public String getCause() { return cause; }

    public RiskLevel getRiskLevel() {
        if (intensity <= 3) return RiskLevel.LOW;
        else if (intensity <= 6) return RiskLevel.MEDIUM;
        else if (intensity <= 8) return RiskLevel.HIGH;
        else return RiskLevel.EXTREME;
    }

    //Validation
    public void setIntensity(int intensity) {
        if (intensity < 1 || intensity > 10) {
            throw new IllegalArgumentException("Intensity must be between 1 and 10");
        }
        this.intensity = intensity;
    }

    //Validation
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location.trim();
    }

    //Validation
    public void setCause(String cause) {
        if (cause == null || cause.trim().isEmpty()) {
            throw new IllegalArgumentException("Cause cannot be empty");
        }
        this.cause = cause.trim();
    }

    @Override
    public String toString() {
        return "Weather{" +
                "eventId='" + getEventId() + '\'' +
                ", eventType=" + getEventType() +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", intensity=" + getIntensity() +
                ", riskLevel=" + getRiskLevel() +
                ", cause='" + getCause() + '\'' +
                '}';
    }
}

