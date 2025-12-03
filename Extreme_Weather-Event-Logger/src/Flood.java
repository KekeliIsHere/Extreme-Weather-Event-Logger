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

    public String toString() {
        return super.toString() + ", Water Level = " + getWaterLevel();
    }

}

