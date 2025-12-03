public class Hurricane extends Weather{
    private EventType eventType;
    private double windSpeed;
    private int category;
    private int size;
    private double pressure;
    
    
    public Hurricane(int eventId, String location, String date, int duration, double intensity, double windSpeed, int category, int size, double pressure, double stormSurge){
        super(eventId, location, date, duration, intensity);
        this.eventType=EventType.HURRICANE;
        this.windSpeed = windSpeed;
        this.category = category;
        this.size = size;
        this.pressure = pressure;
    }

    public double getWindSpeed(){
        return windSpeed;
    }

    public int getCategory(){
        return category;
    }

    public int getSize(){
        return size;
    }

    public double getPressure(){
        return pressure;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
