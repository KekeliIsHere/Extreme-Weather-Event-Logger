import java.time.LocalDateTime;
public class Hurricane extends Weather {
    private double windSpeed;           // Wind speed in mph
    private double pressure;           // Atmospheric pressure in millibars
    private int category;
    public Hurricane(String location,
                     LocalDateTime startDateTime,
                     int intensity,
                     String cause,
                     double windSpeed,
                     double pressure) {

        super(EventType.HURRICANE, location, startDateTime,
                intensity, cause);

        setWindSpeed(windSpeed);
        setPressure(pressure);
    }

    private int calculateCategory(double windSpeed) {
        if (windSpeed >= 157) return 5;
        else if (windSpeed >= 130) return 4;
        else if (windSpeed >= 111) return 3;
        else if (windSpeed >= 96) return 2;
        else if (windSpeed >= 74) return 1;
        else return 0;
    }


    public double getWindSpeed() { return windSpeed; }
    public double getPressure() { return pressure; }
    public int getCategory() { return category; }

    public void setWindSpeed(double windSpeed) {
        if (windSpeed < 0) {
            throw new IllegalArgumentException("Wind speed cannot be negative");
        }
        this.windSpeed = windSpeed;
        this.category = calculateCategory(windSpeed);
    }

    public void setPressure(double pressure) {
        if (pressure <= 0) {
            throw new IllegalArgumentException("Pressure must be positive");
        }
        this.pressure = pressure;
    }

    @Override
    public RiskLevel getRiskLevel() {
        if (category == 5) return RiskLevel.EXTREME;
        else if (category >= 3) return RiskLevel.HIGH;
        else if (category >= 1) return RiskLevel.MEDIUM;
        else return RiskLevel.LOW;
    }

    @Override
    public String toString() {
        return "Hurricane{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", cause='" + getCause() + '\'' +
                ", intensity=" + getIntensity() + "/10" +
                ", windSpeed=" + getWindSpeed() + " mph" +
                ", centralPressure=" + getPressure() + " mb" +
                ", category=" + getCategory() +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}

