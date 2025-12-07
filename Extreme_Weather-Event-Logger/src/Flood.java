import java.time.LocalDateTime;

public class Flood extends Weather {
    private double waterLevel;

    public Flood(String location,
                 LocalDateTime startDateTime,
                 double durationInHours,
                 int intensity,
                 String cause,
                 double waterLevel) {

        super(EventType.FLOOD,
                location,
                startDateTime,
                durationInHours,
                intensity,
                cause);

        setWaterLevel(waterLevel);
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        if (waterLevel < 0) {
            throw new IllegalArgumentException("Water level cannot be less than 0");
        }
        this.waterLevel = waterLevel;
    }

    // Alternative simple risk level
    @Override
    public String getRiskLevel() {
        if (waterLevel >= 5.0 || getIntensity() >= 9) return "EXTREME";
        else if (waterLevel >= 3.0 || getIntensity() >= 7) return "HIGH";
        else if (waterLevel >= 1.0 || getIntensity() >= 4) return "MEDIUM";
        else return "LOW";
    }

    @Override
    public String toString() {
        return "Flood{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", duration=" + getDurationInHours() + " hrs" +
                ", intensity=" + getIntensity() + "/10" +
                ", cause='" + getCause() + '\'' +
                ", waterLevel=" + getWaterLevel() + " m" +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}



