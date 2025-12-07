import java.time.LocalDateTime;

public class Wildfire extends Weather {
    private double areaBurned; // in hectares
    private int containmentPercent; // 0-100
    private String fuelType; // e.g., grass, forest

    public Wildfire(String location,
                    LocalDateTime startDateTime,
                    double durationInHours,
                    int intensity,
                    String cause,
                    double areaBurned,
                    int containmentPercent,
                    String fuelType) {

        super(EventType.WILDFIRE, location, startDateTime, durationInHours, intensity, cause);

        setAreaBurned(areaBurned);
        setContainmentPercent(containmentPercent);
        setFuelType(fuelType);
    }

    public double getAreaBurned() {
        return areaBurned;
    }

    public void setAreaBurned(double areaBurned) {
        if (areaBurned < 0) throw new IllegalArgumentException("Area burned cannot be negative");
        this.areaBurned = areaBurned;
    }

    public int getContainmentPercent() {
        return containmentPercent;
    }

    public void setContainmentPercent(int containmentPercent) {
        if (containmentPercent < 0 || containmentPercent > 100)
            throw new IllegalArgumentException("Containment percent must be between 0 and 100");
        this.containmentPercent = containmentPercent;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String getRiskLevel() {
        // Simple heuristic combining area and containment
        if (containmentPercent < 30 && areaBurned >= 1000) return "EXTREME";
        else if (areaBurned >= 500) return "HIGH";
        else if (areaBurned >= 50) return "MEDIUM";
        else return "LOW";
    }

    @Override
    public String toString() {
        return "Wildfire{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", duration=" + getDurationInHours() + " hrs" +
                ", cause=" + getCause() +
                ", areaBurned=" + getAreaBurned() + " ha" +
                ", containmentPercent=" + getContainmentPercent() + "%" +
                ", fuelType='" + getFuelType() + '\'' +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}
