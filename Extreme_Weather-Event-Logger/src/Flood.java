public class Flood extends Weather{
    private EventType eventType;
    private double waterLevel;

    public Flood(int eventId, String location, String date, int duration, double intensity, double waterLevel){
        super(eventId, location, date, duration,intensity);
        this.eventType=EventType.FLOOD;
        this.waterLevel = waterLevel;
    }

    public double getWaterLevel(){
        return waterLevel;
    }
}
