import java.util.ArrayList;

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
        for(Weather i: events){
            if(i.getLocation().equals(location))
                eventsByLocation.add(i);
        }

        if (!eventsByLocation.isEmpty())
            return eventsByLocation;

        else
            System.out.println("No events found with this location");


    }


    // Get HighLight Intensity Events
    public ArrayList<Weather> getHighLightIntensityEvents(double threshold){
        ArrayList<Weather> highLightIntensityEvents = new ArrayList<>();
        for(Weather i: events){
            if(i.getIntensity().equals(threshold))
                highLightIntensityEvents.add(i);
        }

        if (!highLightIntensityEvents.isEmpty())
            return highLightIntensityEvents;

        else
            System.out.println("No events found with this location");

    }

    public void sortByDate(){

    }

    public void sortByLocation(){

    }

    public void sortByDuration(){

    }



}
