import java.time.LocalDateTime;

public class Earthquake extends Weather {
    private double magnitude;
    private double depth; // in kilometers
    private String epicenter;

    // Constructor - reordered parameters for logical flow
    public Earthquake(String location,
                      LocalDateTime startDateTime,
                      double durationInHours,
                      int intensity,
                      String cause,
                      double magnitude,    // magnitude before depth (more important)
                      double depth,
                      String epicenter) {

        super(EventType.EARTHQUAKE,
                location,
                startDateTime,
                durationInHours,
                intensity,
                cause);

        setMagnitude(magnitude);
        setDepth(depth);
        setEpicenter(epicenter);
    }

    // Getters and setters with validation
    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        if (magnitude < 0) {
            throw new IllegalArgumentException("Magnitude cannot be negative");
        }
        this.magnitude = magnitude;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Depth cannot be negative");
        }
        this.depth = depth;
    }

    public String getEpicenter() {
        return epicenter;
    }

    public void setEpicenter(String epicenter) {
        if (epicenter == null || epicenter.trim().isEmpty()) {
            throw new IllegalArgumentException("Epicenter cannot be empty");
        }
        this.epicenter = epicenter.trim();
    }

    // Risk level that considers BOTH magnitude AND intensity
    @Override
    public String getRiskLevel() {
        if (magnitude >= 7.0 || getIntensity() >= 9) return "EXTREME";
        else if (magnitude >= 5.0 || getIntensity() >= 7) return "HIGH";
        else if (magnitude >= 3.0 || getIntensity() >= 4) return "MEDIUM";
        else return "LOW";
    }

    // Override toString() to include Earthquake-specific info
    @Override
    public String toString() {
        return "Earthquake{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", duration=" + getDurationInHours() + " hrs" +
                ", intensity=" + getIntensity() + "/10" +  // Added intensity
                ", cause='" + getCause() + '\'' +
                ", magnitude=" + getMagnitude() +
                ", depth=" + getDepth() + " km" +
                ", epicenter='" + getEpicenter() + '\'' +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}
