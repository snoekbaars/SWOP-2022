package swop.CarManufactoring;

import java.util.*;

import swop.Car.Car;
import swop.Car.CarOrder;
import swop.Exceptions.NotAllTasksCompleteException;
import swop.Listeners.StatisticsListener;
import swop.Listeners.TaskCompletedListener;
import swop.Miscellaneous.TimeStamp;

/**
 * A controller class which handles most of the operations fe. advancing the assembly line
 */
public class CarManufacturingController {

	private final LinkedList<Car> carQueue;
	private final AssemblyLine assemblyLine;
	private final Scheduler scheduler;
	private final List<StatisticsListener> statisticsListeners = new ArrayList<>();
	private final TaskCompletedListener taskCompletedListener = () -> {
				try {advanceAssembly();}
				catch (NotAllTasksCompleteException ignored) {}
				};


	/**
	 * initializes the controller with an empty carqueue, a scheduler and an assemblyLine with its workstations
	 */
	public CarManufacturingController() {
		this.carQueue = new LinkedList<>();
		this.assemblyLine = new AssemblyLine(this.createWorkStations());
		this.scheduler = new Scheduler(this);
	}

	/**
	 * Add statisticsListener to statisticsListeners
	 * @param statisticsListener the listener to add
	 */
	public void addListener(StatisticsListener statisticsListener) {
		this.statisticsListeners.add(statisticsListener);
	}

	/**
	 * for all the listeners, update the statistics class
	 * @param car a given car that finished
	 */
	private void updateDelay(Car car) {
		statisticsListeners.forEach(l -> l.updateDelay(car));
	}

	/**
	 * Function creates all the workstations that are part of the assemblyLine as a linked list so that they have the
	 * right order.
	 * @return List of all the workstations of this assemblyLine
	 */
	private LinkedList<WorkStation> createWorkStations() {
		LinkedList<WorkStation> workStations = new LinkedList<>();
		workStations.add(new WorkStation("Car Body Post"));
		workStations.add(new WorkStation("Drivetrain Post"));
		workStations.add(new WorkStation("Accessories Post"));
		workStations.forEach(s -> s.addListener(this.taskCompletedListener));
		return workStations;
	}

	/**
	 * @return the assembly line for this controller
	 */
	public AssemblyLine getAssembly() {
		return this.assemblyLine;
	}
	
	/**
	 * Will try to advance the assemblyline + update the scheduler.
	 * @throws NotAllTasksCompleteException if all available tasks are not completed
	 */
	public void advanceAssembly() throws NotAllTasksCompleteException {
		//there is time to finish another car + there are cars on the queue
		List<WorkStation> workStations = this.getAssembly().getWorkStations();
		List<Integer> workingTimes = workStations.stream()
				.map(WorkStation::getCurrentWorkingTime).toList();
		int maxWorkingMinutes = Collections.max(workingTimes);
		Car finishedCar;
		if(this.canFinishNewCar(maxWorkingMinutes) && !this.getCarQueue().isEmpty()) {
			Car nextCar = this.getScheduler().getNextScheduledCar();
			finishedCar = this.assemblyLine.advance(nextCar);
			this.carQueue.remove(nextCar);
		}
		//else just advance
		else {
			finishedCar = this.assemblyLine.advance(null);
		}

		if (finishedCar != null) {
			this.setFinishedCarDeliveryTime(maxWorkingMinutes, finishedCar);
			this.updateDelay(finishedCar);
		}
		//update schedular time
		this.updateScheduleTime(maxWorkingMinutes);

		// For every car in queue and workstation update the estimated completion time
		this.updateEstimatedCompletionTime();
	}

	/**
	 * For every car in the carqueue, update the estimated completiontime according to the minutes passed.
	 */
	public void updateEstimatedCompletionTime() {
		this.carQueue.forEach(car -> car.setEstimatedCompletionTime(scheduler.getEstimatedCompletionTime(car)));
		this.assemblyLine.getWorkStations().forEach(w -> {
			if (w.getCar() != null)
			w.getCar().setEstimatedCompletionTime(scheduler.getEstimatedCompletionTime(w.getCar()));
		});
	}

	/**
	 * Set the completion time of the finished car according to scheduler
	 * @param minutes the minutes passed in the last workstation rotation
	 * @param finishedCar the car to be finished
	 * @throws IllegalStateException if the car is not completed
	 */
	private void setFinishedCarDeliveryTime(int minutes, Car finishedCar) {
		if (!finishedCar.isCompleted()) throw new IllegalStateException("Car is not completed");
		finishedCar.setDeliveryTime(new TimeStamp(scheduler.getDay(), scheduler.getMinutes()+ minutes));
	}

	/**
	 * Checks if a new car could be finished if it was added to the assemblyLine
	 * @param minutes the minutes that need to be added to the time before checking
	 * @return whether a new car can be finished in time
	 */
	private boolean canFinishNewCar(int minutes) {
		return this.getScheduler().canAddCarToAssemblyLine(minutes);
	}

	/**
	 * updates the scheduler.
	 * @param minutes minutes to add to the current time
	 */
	private void updateScheduleTime(int minutes) {
		this.getScheduler().addTime(minutes);
		//if all work is done for today, skip to next day
		if (!this.canFinishNewCar(0) && this.assemblyLine.isEmptyAssemblyLine()) {
			this.getScheduler().advanceDay();
		}
		
	}

	/**
	 * add the cars of a specific order 2 the queue
	 * @param carOrder order added to the Queue
	 */
	public void addOrderToQueue(CarOrder carOrder) {
		Car car = carOrder.getCar();
		if(car == null) throw new IllegalArgumentException("car is null");
		this.carQueue.add(car);
		carOrder.setOrderTime(getScheduler().getTime());
		// if it is the only order in queue and the first spot is empty -> put it on the assembly line (if possible)
		if (this.getCarQueue().size() == 1 && canFinishNewCar(0) && this.getAssembly().getWorkStations().get(0).getCar() == null) {
			this.getAssembly().getWorkStations().get(0).setCar(car);
			this.carQueue.remove(car);
		}
		car.setEstimatedCompletionTime(getScheduler().getEstimatedCompletionTime(car));
	}

	/**
	 * Returns the scheduler associated with this controller
	 * @return this.scheduler
	 */
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	/**
	 * returns copy of carqueue
	 * @return List.copyOf(this.carQueue)
	 */
	public List<Car> getCarQueue() {
		return List.copyOf(this.carQueue);
	}
}
