import java.time.LocalDateTime;
public class Wildfire extends Weather {
    private double areaBurned;
    private int containmentPercent;    // 1-100%

    public Wildfire(String location,
                    LocalDateTime startDateTime,
                    int intensity,
                    String cause,
                    double areaBurned,
                    int containmentPercent) {

        super(EventType.WILDFIRE, location, startDateTime, intensity, cause);

        setAreaBurned(areaBurned);
        setContainmentPercent(containmentPercent);
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

    @Override
    public RiskLevel getRiskLevel() {
        if (areaBurned >= 1000 && containmentPercent < 30) {
            return RiskLevel.EXTREME;
        } else if (areaBurned >= 500 && containmentPercent < 50) {
            return RiskLevel.HIGH;
        } else if (areaBurned >= 50 && containmentPercent < 70) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }

    @Override
    public String toString() {
        return "Wildfire{" +
                "eventId='" + getEventId() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", cause=" + getCause() +
                ", areaBurned=" + getAreaBurned() + " ha" +
                ", containmentPercent=" + getContainmentPercent() + "%" +
                ", riskLevel=" + getRiskLevel() +
                '}';
    }
}

