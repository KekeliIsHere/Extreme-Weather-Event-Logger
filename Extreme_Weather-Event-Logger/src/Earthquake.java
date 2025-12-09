import java.time.LocalDateTime;

public class Earthquake extends Weather {

    private double magnitude;
    public Earthquake(String location,
                      LocalDateTime startDateTime,
                      int intensity,
                      String cause,
                      double magnitude) {

        super(EventType.EARTHQUAKE,
                location,
                startDateTime,
                intensity,
                cause);
        setMagnitude(magnitude);
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

    @Override
    public RiskLevel getRiskLevel() {

        if (magnitude >= 7.0) return RiskLevel.EXTREME;
        else if (magnitude >= 5.0) return RiskLevel.HIGH;
        else if (magnitude >= 3.0) return RiskLevel.MEDIUM;
        else return RiskLevel.LOW;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", intensity=" + getIntensity() + "/10" +
                ", cause='" + getCause() + '\'' +
                ", magnitude=" + getMagnitude() +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}

