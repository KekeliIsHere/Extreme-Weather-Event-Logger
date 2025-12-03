import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Weather {
    private String eventId;
    private EventType eventType ;
    private String location;
    private LocalDateTime startDateTime;
    private double durationInHours;
    private int intensity;     //val from 1-10
    private String cause;

    /* Static map to keep track of the number of events per type
    to Declare and initialize a HashMap
    Map<KeyType, ValueType> map = new HashMap<>();
    Hash Map work like Dictionaries in python.
    so I used it here because I want to be able to generate the eventId
    based on the weather type say if earthquake E001 or Flood F001
    I also want to be able to keep track of how many of these events are occurring.
    it could help in data analysis
     */

    private static Map<EventType, Integer> typeCounters = new HashMap<>();

    public Weather(){}

    public Weather(EventType eventType,String location, LocalDateTime startDateTime, double durationInHours,
                    int  intensity, String cause ){

        this.eventType = eventType;                     // pass in and store the type
        this.eventId = generateEventId(eventType);     // auto-generate ID
        this.startDateTime = startDateTime;
        setIntensity(intensity);
        setLocation(location);
        setDurationInHours(durationInHours);
        setCause(cause);
    }

    public Weather(EventType eventType, String location, LocalDateTime startDateTime, double durationInHours, int intensity){
        this.eventType = eventType;
        this.location = location;
        this.startDateTime = startDateTime;
        this.durationInHours = durationInHours;
        this.intensity = intensity;
    }




    public String getEventId() {return eventId;}

    public EventType getEventType() {return eventType;}

    public String getLocation() {return location;}

    public LocalDateTime getStartDateTime() {return startDateTime;}

    public double getDurationInHours() {return durationInHours;}

    public int getIntensity() {return intensity;}

    public String getCause() {return cause;}

    public LocalDateTime getEndDateTime() {

        // Convert hours to minutes (to handle fractional hours)
        long minutesToAdd = (long) (durationInHours * 60);
        return startDateTime.plusMinutes(minutesToAdd);
    }

    public void setIntensity(int intensity) {
        if (intensity < 1 || intensity > 10) {
            throw new IllegalArgumentException("Intensity must be between 1 and 10");
        }
        this.intensity = intensity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDurationInHours(double durationInHours) {
        if (durationInHours < 0) {
            throw new IllegalArgumentException("Duration must be non-negative");
        }
        this.durationInHours = durationInHours;
    }

    public void setCause(String cause){
        this.cause = cause;
    }

    //method to generate the ID based on type

    public String generateEventId(EventType eventType){
        int count = typeCounters.getOrDefault(eventType, 0) + 1;
        typeCounters.put(eventType,count);

        String prefix =  eventType.name().substring(0,1);
        return prefix + String.format("%03d",count);
    }


    @Override
    public String toString() {
        return "Weather{" +
                "eventId='" + getEventId() + '\'' +
                ", eventType=" + getEventType() +
                ", location='" + getLocation() + '\'' +
                ", startDateTime=" + getStartDateTime() +
                ", durationInHours=" + getDurationInHours() +
                ", endDateTime=" + getEndDateTime() +
                ", intensity=" + getIntensity() +
                ", cause='" + getCause() + '\'' +
                '}';
    }


}
