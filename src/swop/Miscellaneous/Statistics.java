package swop.Miscellaneous;

import swop.Car.Car;
import swop.Listeners.StatisticsListener;
import swop.Records.allStats;

import java.util.*;

public class Statistics {
    private final Map<Integer, List<Integer>> carDelayMap = new HashMap<>();

    public Statistics() {}
    public final StatisticsListener statisticsListener = this::updateDelay;

    /**
     * Updates the delay map given a Car
     * @param car of the type Car
     */
    void updateDelay(Car car) {
        int finishDay = car.getCompletionTime().getDay();
        int delayedDays = finishDay - car.getInitialCompletionTime().getDay();
        int delayedMinutes = car.getCompletionTime().getMinutes() - car.getInitialCompletionTime().getMinutes();

        int minutes = 0;

        minutes += delayedDays * 3600;
        minutes += delayedMinutes;

        this.finishOrder(minutes, finishDay);
    }

    /**
     * update the delays map when a car is finished
     * @param delayedMinutes the given delay in minutes
     * @param finishDay the given day that the car was finished on
     */
    public void finishOrder(int delayedMinutes, int finishDay) {
        Integer day_I = finishDay;
        if (this.getCarDelayMap().containsKey(day_I)) {
            this.getCarDelayMap().get(day_I).add(delayedMinutes);
        } else {
            this.getCarDelayMap().put(day_I, new ArrayList<>(List.of(delayedMinutes)));
        }
    }

    /**
     * Get the average delay over all the days
     * @return the average of the delays for all finished cars
     */
    public double getAvgDelay() {
        double total = 0;
        for (List<Integer> delays : this.getCarDelayMap().values())
            for (Integer delay : delays) {
                total += delay;
            }
        return this.getCarDelayMap().size() > 0 ? total / (double) this.getCarDelayMap().size() : 0;
    }

    /**
     * Get the median delay over all the days
     * @return the median of the delays for all finished cars
     */
    public double getMdnDelay() {
        List<Integer> total = new ArrayList<>();
        for (List<Integer> delays : this.getCarDelayMap().values())
            total.addAll(delays);
        Collections.sort(total);
        if (total.size() > 0) {
            if (total.size() % 2 == 0)
                return ((double) total.get(total.size() / 2) + (double) total.get(total.size() / 2 - 1)) / 2;
            else
                return (double) total.get(total.size() / 2);
        } else return 0;
    }

    /**
     * get all the delays for the last two days
     * @return a list containing all the delays from the last two days
     */
    public List<Integer> getDelayLast2(){
        List<Integer> result = new ArrayList<>();
        int max = 0;
        if (!this.getCarDelayMap().isEmpty())
            max = Collections.max(this.getCarDelayMap().keySet());
        if(this.getCarDelayMap().size() > 1) result.addAll(this.getCarDelayMap().get(max-1));
        if(this.getCarDelayMap().size() > 0) result.addAll(this.getCarDelayMap().get(max));
        return result;
    }

    /**
     * Get the average number of orders over all the days
     * @return the average number of orders for all finished cars
     */
    public double getAvgOrders(){
        double total = 0;
        for(List<Integer> delays: this.getCarDelayMap().values())
            total+= delays.size();
        return this.getCarDelayMap().size()>0 ? total/(double) this.getCarDelayMap().size(): 0;
    }

    /**
     * Get the median number of orders over all the days
     * @return the median number of orders for all finished cars
     */
    public double getMdnOrders(){
        List<Integer> orders = new ArrayList<>();
        for(List<Integer> delays: this.getCarDelayMap().values()){
            orders.add(delays.size());
        }
        Collections.sort(orders);
        if (orders.size() > 0) {
            if (orders.size() % 2 == 0)
                return ((double) orders.get(orders.size() / 2) + (double) orders.get(orders.size() / 2 - 1)) / 2;
            else
                return (double) orders.get(orders.size() / 2);
        } else return 0;
    }


    /**
     * get the number of orders for the last two days
     * @return a map containing the day and the number of orders for that day for the last 2 days
     */
    public Map<Integer, Integer> getOrdersLast2(){
        Map<Integer, Integer> result = new HashMap<>();
        int max = 0;
        if (!this.getCarDelayMap().isEmpty())
            max = Collections.max(this.getCarDelayMap().keySet());

        if(this.getCarDelayMap().size() > 1) result.put(max-1, this.getCarDelayMap().get(max-1).size());
        if(this.getCarDelayMap().size() > 0) result.put(max, this.getCarDelayMap().get(max).size());
        return result;
    }
    
    /**
     * Returns all the statistics.
     * @return a map containing the stat as a string and the value
     */
    public allStats getOrderStats(){
    	
    	return new allStats(getAvgOrders(), getMdnOrders(), getOrdersLast2(), getAvgDelay(),getMdnDelay(),getDelayLast2());
    }

    public Map<Integer, List<Integer>> getCarDelayMap() {
        return carDelayMap;
    }
}
