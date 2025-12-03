public class MainWeather {
    public static void main(String[] args) {

     // Create the manager that stores all the events
            WeatherManager manager = new WeatherManager();

            // Create the GUI and link it to the manager
            WeatherGUI gui = new WeatherGUI(manager);

            // Show the main window (this builds and displays the GUI)
            gui.showMainWindow();
    }

}
