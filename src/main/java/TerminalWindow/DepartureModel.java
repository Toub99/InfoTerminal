/**
 * 
 */
package TerminalWindow;

/**
 * @author Tobias Kaiser
 *
 */
public class DepartureModel {
    
    private String departure;
    private String destination;
    private String delay;
    private String train;
    
    public DepartureModel(final String departure, final String destination, final String delay2, final String train) {
        super();
        this.departure = departure;
        this.destination = destination;
        this.delay = delay2;
        this.train = train;
    }
    
    
    
    @Override
    public String toString() {
        return "DepartureModel [departure=" + departure + ", destination=" + destination + ", delay=" + delay
                + ", train=" + train + "]";
    }



    /**
     * @return the departure
     */
    public String getDeparture() {
        return departure;
    }
    /**
     * @param departure the departure to set
     */
    public void setDeparture(final String departure) {
        this.departure = departure;
    }
    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }
    /**
     * @param destination the destination to set
     */
    public void setDestination(final String destination) {
        this.destination = destination;
    }
    /**
     * @return the delay
     */
    public String getDelay() {
        return delay;
    }
    /**
     * @param delay the delay to set
     */
    public void setDelay(final String delay) {
        this.delay = delay;
    }
    /**
     * @return the train
     */
    public String getTrain() {
        return train;
    }
    /**
     * @param train the train to set
     */
    public void setTrain(final String train) {
        this.train = train;
    }
   
}
