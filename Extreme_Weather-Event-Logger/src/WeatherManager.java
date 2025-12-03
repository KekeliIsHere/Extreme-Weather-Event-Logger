import java.util.ArrayList;
import java.util.Comparator;

/*This class will manage all the weather events
it can add, delete, search, sort display and analyze

 */

public class WeatherManager{
    private ArrayList<Weather> events = new ArrayList<>();

//    WeatherManager Constructor
    public WeatherManager(){}

    //method to add event with validation
    public void addEvent(Weather event){
        if (event == null){
            throw new IllegalArgumentException("event cant be null");
        }
        events.add(event);
    }

    /*Delete an event by their iD
    if successful should return true else false if it is not found
     */
    public boolean deleteEvent(String eventId){
      for(int i= 0; i<events.size();i++) {
          Weather event = events.get(i);

          if (event.getEventId().equals(eventId)) {
              events.remove(i);                         // delete the event
              return true;                          // tell user it was deleted
          }
      }
        // If no match was found, return false
        return false;
    }

    //get all events
    public ArrayList<Weather> getAllEvents(){
        return events;      //return new ArrayList<>(events);
    }

    //display all events

    public void displayAllEvents(){
        if (events.isEmpty()){
            System.out.println("No events recorded.");
        } else {
            for (Weather event : events) {
                System.out.println(event);
            }
        }
    }

          //Searching elements

    //Get events by location
    public ArrayList<Weather> getEventsByLocation(String location){
        ArrayList<Weather> eventsByLocation = new ArrayList<>();
        for (Weather i : events) {
            if (i.getLocation().equalsIgnoreCase(location)) {
                eventsByLocation.add(i);
            }
        }
        if (!eventsByLocation.isEmpty()) {
            return eventsByLocation;
        } else {
            System.out.println("No events found with this location");
            return new ArrayList<>();    // return an empty list
        }
    }

    //Get all events that match a given risk level (LOW, MEDIUM, HIGH, EXTREME).

    // Get HighLight Intensity Events
    public ArrayList<Weather> getEventsByRiskLevel(String riskLevel) {
        ArrayList<Weather> eventsByRiskLevel = new ArrayList<>();
        for (Weather i : events) {
            if (i.getRiskLevel().equalsIgnoreCase(riskLevel))
                eventsByRiskLevel.add(i);
        }

        if (!eventsByRiskLevel.isEmpty()) {
            return eventsByRiskLevel;
        }
        else{
            System.out.println("No events found with this location");
            return new ArrayList<>();
        }
    }

      //sorting


     //sort by date from latest to oldest
    public void sortByDate(){
        events.sort(Comparator.comparing(Weather::getStartDateTime).reversed());
    }

    public void sortByLocation(){
        events.sort(Comparator.comparing(Weather::getLocation));
    }
    public void sortByDuration(){
        events.sort(Comparator.comparingDouble(Weather::getDurationInHours));
    }


    //sone features that will help for the data analysis part


    //count how many events belong to a specific weather type
    public int countEventsByType(EventType type) {
        int count = 0;
        for (Weather event : events) {
            if (event.getEventType() == type) {
                count++;
            }
        }
        return count;
    }

    //so there are risk levels right, low medium high
    //so this method counts the events based on their risk levels
    //like how many events were high, low or medium we can use graphs to visualize

    public int countEventsByRiskLevel(String riskLevel){
        int count = 0;
        for (Weather event : events) {
            if (event.getRiskLevel().equalsIgnoreCase(riskLevel)) {
                count++;
            }
        }
        return count;
    }

    // Calculate the average intensity for all the weather events
    public double getAverageIntensity() {
        if (events.isEmpty()) return 0;
        double total = 0;
        for (Weather event : events) {
            total += event.getIntensity();
        }
        return total / events.size();
    }
}
