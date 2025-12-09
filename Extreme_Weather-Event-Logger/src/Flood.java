import java.time.LocalDateTime;
public class Flood extends Weather {
    private double waterLevel;   // in meters

    public Flood(String location,
                 LocalDateTime startDateTime,
                 int intensity,
                 String cause,
                 double waterLevel) {

        super(EventType.FLOOD,
                location,
                startDateTime,
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

    @Override
    public RiskLevel getRiskLevel() {
        if (waterLevel >= 5.0) return RiskLevel.EXTREME;
        else if (waterLevel >= 3.0) return RiskLevel.HIGH;
        else if (waterLevel >= 1.0) return RiskLevel.MEDIUM;
        else return RiskLevel.LOW;
    }

    @Override
    public String toString() {
        return "Flood{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", intensity=" + getIntensity() + "/10" +
                ", cause='" + getCause() + '\'' +
                ", waterLevel=" + getWaterLevel() + " m" +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}




