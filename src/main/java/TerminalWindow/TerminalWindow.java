/**
 * 
 */
package TerminalWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Tobias Kaiser
 *
 */
public class TerminalWindow {
    
    private static HttpClientClass client = new HttpClientClass();
    private static LinkedList<DepartureModel> models = new LinkedList<DepartureModel>();

    public static void main(String[] args) throws IOException, InterruptedException {
        
        JFrame window = new JFrame("Info Terminal");
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), BorderFactory.createLineBorder(Color.GREEN));
        String clock = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
        System.out.println(d);
        //Toolkit.getDefaultToolkit().beep();
        
        // label that shows time:
        final JLabel dateLabel = new JLabel(clock, SwingConstants.LEFT);
        dateLabel.setVerticalAlignment(SwingConstants.TOP);
        dateLabel.setForeground(Color.GREEN);
        dateLabel.setFont(dateLabel.getFont().deriveFont(40f));
        JPanel datePanel = new JPanel();
        datePanel.setLayout(null);
        dateLabel.setBounds(384 - 81, 216 - 26, 162, 52);
        dateLabel.repaint();
        datePanel.add(dateLabel);
        datePanel.setBackground(Color.BLACK);
        datePanel.setBorder(border);
        
        // label that shows departures:
        final JList<String> trainName = new JList<String>(createDepStr().getFirst());
        final JList<String> destination = new JList<String>((String[]) createDepStr().get(1));
        final JList<String> time = new JList<String>((String[]) createDepStr().get(2));
        final JList<String> delay = new JList<String>((String[]) createDepStr().get(3));
        
        trainName.setFont(trainName.getFont().deriveFont(20f));
        destination.setFont(destination.getFont().deriveFont(20f));
        time.setFont(time.getFont().deriveFont(20f));
        delay.setFont(delay.getFont().deriveFont(20f));
        trainName.setBackground(Color.BLACK);
        destination.setBackground(Color.BLACK);
        time.setBackground(Color.BLACK);
        delay.setBackground(Color.BLACK);
        trainName.setForeground(Color.GREEN);
        destination.setForeground(Color.GREEN);
        time.setForeground(Color.GREEN);
        delay.setForeground(Color.GREEN);
        //delay.setPreferredSize(new Dimension(26, 120));

        // JPanel on the left of the screen:
        JPanel mainPanel = new JPanel();
        final JPanel departurePanel = new JPanel();
        //departurePanel.add(new JLabel("Bahnen ab Untergrombach Bf:"));
        departurePanel.add(trainName);
        departurePanel.add(destination);
        departurePanel.add(time);
        departurePanel.add(delay);
        //departurePanel.setPreferredSize(new Dimension(400, 400));
        departurePanel.setBackground(Color.BLACK);
        //departurePanel.setPreferredSize(dim);
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(null);
        departurePanel.setBounds(384 - 159, 216 - 159, 330, 318);
        mainPanel.add(departurePanel);
        JLabel headline = new JLabel("Untergrombach Bf:");
        headline.setBackground(Color.BLUE);
        headline.setForeground(Color.GREEN);
        headline.setBounds(192, 20, 300, 30);
        headline.setFont(headline.getFont().deriveFont(20f));
        mainPanel.add(headline);
        mainPanel.setBorder(border);
        
        
        // new Panel:
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.BLACK);
        panel2.setBorder(border);
        
        // timer that updates clock every second:
        timeIntervall(dateLabel, trainName, destination, time, delay);
        
        
        // add components to window:
        window.setLayout(new GridLayout(2, 2));
        window.getContentPane().add(datePanel);
        window.getContentPane().add(panel2);
        window.getContentPane().add(mainPanel); 
        
        // decorate window and set to visible:
        window.setUndecorated(true);
        window.getContentPane().setBackground(Color.BLACK);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(d);
        
//        System.out.println("headline: " + headline.getSize());
//        headline.setBounds(0, 0, headline.getWidth(), headline.getHeight());
//        headline.repaint();
//        System.out.println("headline: " + headline.getSize());

        
        System.out.println("DateLabel size:" + dateLabel.getSize());
        System.out.println("Destination size: " + destination.getSize());
        System.out.println("time size: " + time.getSize());
        System.out.println("delay size: " + delay.getSize());
        System.out.println("panel size: " + departurePanel.getSize());

    }
    
    
    
    

    private static void timeIntervall(final JLabel dateLabel, final JList<String> trainName, final JList<String> destination,
            final JList<String> time, final JList<String> delay) {
        Timer timer1 = new Timer();
        TimerTask taskClock = new TimerTask() {  
            @Override
            public void run() {
               String updatedClock = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
               dateLabel.setText(updatedClock);
            }
        };
        Timer timer2 = new Timer();
        TimerTask taskHttp = new TimerTask() {
            
            @Override
            public void run() {
                try {
                    LinkedList<String[]> list = createDepStr();
                    trainName.setListData((String[]) list.get(0));
                    destination.setListData((String[]) list.get(1));
                    time.setListData((String[]) list.get(2));
                    delay.setListData((String[]) list.get(3));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer1.schedule(taskClock, 1000, 1000);
        timer2.schedule(taskHttp, 1000, 5000);
    }

//    private static JLabel createDepartureLabel() throws IOException, InterruptedException {
//        // label that shows departures:
//        String depStr = createDepStr();
//        JLabel departureLabel = new JLabel(depStr, SwingConstants.LEFT); //TODO: try catch
//        departureLabel.setVerticalAlignment(SwingConstants.CENTER);
//        departureLabel.setForeground(Color.GREEN);
//        return departureLabel;
//    }

    private static LinkedList<String[]> createDepStr() throws IOException, InterruptedException {
        models = client.sendGet();
        LinkedList<String[]> depList = new LinkedList<String[]>();
        LinkedList<String> trainNameList = new LinkedList<String>();
        LinkedList<String> destinationList = new LinkedList<String>();
        LinkedList<String> timeList = new LinkedList<String>();
        LinkedList<String> delayList = new LinkedList<String>();

        String depStr = "";
        for (DepartureModel model : models) {
            trainNameList.add(model.getTrain());
            destinationList.add(model.getDestination());
            timeList.add(model.getDeparture());
            if (Integer.parseInt(model.getDelay()) >= 0) {
                delayList.add("+" + model.getDelay());
            } else {
                delayList.add(model.getDelay());
            }
        }
        depList.add(Arrays.copyOf(trainNameList.toArray(), trainNameList.size(), String[].class));
        depList.add(Arrays.copyOf(destinationList.toArray(), destinationList.size(), String[].class));
        depList.add(Arrays.copyOf(timeList.toArray(), timeList.size(), String[].class));
        depList.add(Arrays.copyOf(delayList.toArray(), delayList.size(), String[].class));
        System.out.print(depStr);
        return depList;
    }
}
