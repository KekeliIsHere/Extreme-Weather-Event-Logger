import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WeatherGUI extends JFrame implements ActionListener {
    private WeatherManager manager;
    private JButton myButton;
    private JLabel myLabel;

    myButton = new JButton("Batman");
    myLabel = new

    JLabel("Hello, GUI!") {

    }

    public WeatherGUI(WeatherManager manager){
        this.manager=manager;
    }
    public void showMainWindow(){
        super("Weather Event Manager");
        System.out.println("tbd");
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
