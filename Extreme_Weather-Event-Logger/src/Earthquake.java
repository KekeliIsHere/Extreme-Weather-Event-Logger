public class Earthquake extends Weather{
    private EventType eventType;
    private double magnitude;
    private double depth;
    private String epicenter;


    public Earthquake(int eventId, String location, String date, int duration, double intensity, double depth, double magnitude, String epicenter) {
        super(eventId, location, date, duration, intensity);
        this.eventType=EventType.EARTHQUAKE;
        setDepth(depth);
        setMagnitude(magnitude);
        setEpicenter(epicenter);
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getEpicenter() {
        return epicenter;
    }

    public void setEpicenter(String epicenter) {
        this.epicenter = epicenter;
    }
}
