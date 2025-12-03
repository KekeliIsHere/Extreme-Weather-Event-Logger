import java.time.LocalDateTime;


/*Represents a Hurricane event. From research, I  found that
wind speed is rather used to determine category (Saffir-Simpson scale).*/

public class Hurricane extends Weather {
    private double windSpeed;                   // Wind speed in mph
    private double pressure;            // Pressure in millibars
    private double size;                        // Radius in miles
    private int category;                      // Hurricane category (1–5)


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
        setWindSpeed(windSpeed);
        setPressure(pressure);
        setSize(size);
        this.category = calculateCategory(); // auto-set category
    }

    //category ranges from 1-5 based on wind speed
    private int calculateCategory() {
        if (windSpeed >= 157) return 5;
        else if (windSpeed >= 130) return 4;
        else if (windSpeed >= 111) return 3;
        else if (windSpeed >= 96) return 2;
        else if (windSpeed >= 74) return 1;
        else return 0;                            // not a hurricane
    }


    public double getWindSpeed() { return windSpeed; }
    public double getPressure() { return pressure; }
    public double getSize() { return size; }
    public int getCategory() { return category; }


    public void setWindSpeed(double windSpeed) {
        if (windSpeed < 0) {
            throw new IllegalArgumentException("Wind speed cannot be negative");
        }
        this.windSpeed = windSpeed;
        this.category = calculateCategory();    // update category when the  wind speed changes
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


    // Risk level based on category
    @Override
    public String getRiskLevel() {
        if (category == 5) return "EXTREME";
        else if (category >= 3) return "HIGH";
        else if (category >= 1) return "MEDIUM";
        else return "LOW";
    }

    // Detailed string for console output
    @Override
    public String toString() {
        return "Hurricane{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", cause =" + getCause() +
                ", duration=" + getDurationInHours() + " hrs" +
                ", windSpeed=" + getWindSpeed() + " mph" +
                ", centralPressure=" + getPressure() + " mb" +
                ", size=" + getSize() + " miles radius" +
                ", category=" + getCategory() +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}