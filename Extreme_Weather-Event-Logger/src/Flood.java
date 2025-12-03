import java.time.LocalDateTime;

public class Flood extends Weather {
    private double waterLevel;

    public Flood(String location,
                 LocalDateTime startDateTime,
                 double durationInHours,
                 int intensity, String cause,
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
            throw new IllegalArgumentException("cannot be less than 0");
        }
        this.waterLevel = waterLevel;
    }

    public String getRiskLevel(){
            if (waterLevel >= 5.0) return "EXTREME";
            else if (waterLevel >= 3.0) return "HIGH";
            else if (waterLevel >= 1.0) return "MEDIUM";
            else return "LOW";
        }

        @Override
            public String toString() {
                return "Flood{" +
                        "eventId='" + getEventId() + '\'' +
                        ", location='" + getLocation() + '\'' +
                        ", startDateTime=" + getStartDateTime() +
                        ", duration=" + getDurationInHours() + " hrs" +
                        ", waterLevel=" + getWaterLevel() + " m" +
                        ", riskLevel=" + getRiskLevel() +
                        '}';
            }

        }



