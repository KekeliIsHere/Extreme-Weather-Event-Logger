import java.time.LocalDateTime;

public class Hurricane extends Weather {
    private double windSpeed;                   // Wind speed in mph
    private double pressure;                    // Pressure in millibars
    private double size;                        // Radius in miles
    private int category;                       // Hurricane category (0-5)

    public Hurricane(String location,
                     LocalDateTime startDateTime,
                     double durationInHours,
                     int intensity,
                     String cause,
                     double windSpeed,
                     double pressure,
                     double size) {

        super(EventType.HURRICANE, location, startDateTime,
                durationInHours, intensity,
                cause);

        // Set fields - windSpeed setter will calculate category
        setWindSpeed(windSpeed);
        setPressure(pressure);
        setSize(size);
    }

    // Category ranges from 0-5 based on wind speed
    private int calculateCategory(double windSpeed) {
        if (windSpeed >= 157) return 5;
        else if (windSpeed >= 130) return 4;
        else if (windSpeed >= 111) return 3;
        else if (windSpeed >= 96) return 2;
        else if (windSpeed >= 74) return 1;
        else return 0;                            // not a hurricane (tropical storm)
    }

    // Getters
    public double getWindSpeed() { return windSpeed; }
    public double getPressure() { return pressure; }
    public double getSize() { return size; }
    public int getCategory() { return category; }

    // Setters
    public void setWindSpeed(double windSpeed) {
        if (windSpeed < 0) {
            throw new IllegalArgumentException("Wind speed cannot be negative");
        }
        this.windSpeed = windSpeed;
        this.category = calculateCategory(windSpeed);    // update category
    }

    public void setPressure(double pressure) {
        if (pressure <= 0) {
            throw new IllegalArgumentException("Pressure must be positive");
        }
        this.pressure = pressure;
    }

    public void setSize(double size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }

    // Alternative risk level that considers both category AND intensity
    @Override
    public String getRiskLevel() {
        if (category == 5 || getIntensity() >= 9) return "EXTREME";
        else if (category >= 3 || getIntensity() >= 7) return "HIGH";
        else if (category >= 1 || getIntensity() >= 4) return "MEDIUM";
        else return "LOW";
    }

    // Detailed string for console output
    @Override
    public String toString() {
        return "Hurricane{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", cause='" + getCause() + '\'' +
                ", duration=" + getDurationInHours() + " hrs" +
                ", intensity=" + getIntensity() + "/10" +
                ", windSpeed=" + getWindSpeed() + " mph" +
                ", centralPressure=" + getPressure() + " mb" +
                ", size=" + getSize() + " miles radius" +
                ", category=" + getCategory() +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}
