import java.util.ArrayList;
import java.util.Comparator;

public class WeatherManager{
    private ArrayList<Weather> events = new ArrayList<>();

//    WeatherManager Constructor
    public WeatherManager(){};

    public void addEvent( Weather event){
        events.add(event);
    }

    public ArrayList<Weather> getAllEvents(){
        return events;
    }

    //Get events by location
    public ArrayList<Weather> getEventsByLocation(String location){
        ArrayList<Weather> eventsByLocation = new ArrayList<>();

        for (Weather i : events) {
            if (i.getLocation().equals(location)) {
                eventsByLocation.add(i);
            }
        }

        if (!eventsByLocation.isEmpty()) {
            return eventsByLocation;
        } else {
            System.out.println("No events found with this location");
            return new ArrayList<>(); // return an empty list
        }
    }


    // Get HighLight Intensity Events
    public ArrayList<Weather> getHighLightIntensityEvents(double threshold){
        ArrayList<Weather> highLightIntensityEvents = new ArrayList<>();
        for(Weather i: events){
            if(i.getIntensity() ==threshold)
                highLightIntensityEvents.add(i);
        }

        if (!highLightIntensityEvents.isEmpty())
            return highLightIntensityEvents;

        else{
            System.out.println("No events found with this location");
            return highLightIntensityEvents;
        }

    }

    public void sortByDate(){
        events.sort(Comparator.comparing(Weather::getStartDateTime).reversed());

    }

    public void sortByLocation(){
        events.sort(Comparator.comparing(Weather::getLocation));

    }

    public void sortByDuration(){
        events.sort(Comparator.comparingDouble(Weather::getDurationInHours));

    }

}
