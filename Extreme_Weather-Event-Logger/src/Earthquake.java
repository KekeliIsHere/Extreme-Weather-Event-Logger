import java.time.LocalDateTime;

public class Earthquake extends Weather {

    private double magnitude;
    private double depth;
    private String epicenter;

    // Constructor
    public Earthquake(String location,
                      LocalDateTime startDateTime,
                      double durationInHours,
                      int intensity,
                      String cause,
                      double depth,
                      double magnitude,
                      String epicenter) {


        super(EventType.EARTHQUAKE,
                location,
                startDateTime,
                durationInHours,
                intensity,
                cause);


        setDepth(depth);
        setMagnitude(magnitude);
        setEpicenter(epicenter);
    }

    // Getters and setters with optional validation
    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Depth cannot be negative");
        }
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        if (magnitude < 0) {
            throw new IllegalArgumentException("Magnitude cannot be negative");
        }
        this.magnitude = magnitude;
    }

    public String getEpicenter() {
        return epicenter;
    }

    public void setEpicenter(String epicenter) {
        this.epicenter = epicenter;
    }

    @Override
    public String getRiskLevel() {
        if (magnitude >= 7.0) {
            return "EXTREME";
        } else if (magnitude >= 5.0) {
            return "HIGH";
        } else if (magnitude >= 3.0) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    // Override toString() to include Earthquake-specific info
    @Override
    public String toString() {
        return "Earthquake{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", duration=" + getDurationInHours() + " hrs" +
                ", intensity=" + getIntensity() +
                ", magnitude=" + getMagnitude() +
                ", depth=" + getDepth() + " km" +
                ", epicenter='" + getEpicenter() + '\'' +
                ", riskLevel=" + getRiskLevel() +
                '}';

    }
}
