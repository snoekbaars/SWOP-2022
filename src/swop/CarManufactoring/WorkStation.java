package swop.CarManufactoring;

import java.util.*;

import swop.Car.Car;
import swop.Listeners.TaskCompletedListener;
/**
 * This class represents a workstation
 */
public class WorkStation {
	private final String name;
	private Car car;
	private int currentWorkingTime = 0;
	private final List<TaskCompletedListener> listeners = new ArrayList<>();

	/**
	 * Create a new work station with given name
	 * @param name string of name
	 */
	public WorkStation(String name) {
		if (!isValidName(name)) {
			throw new IllegalArgumentException("Not a valid work station name"); 
		}
		this.name = name;
		for(Task t:this.getTasks()) t.setWorkStation(this);

	}

	/**
	 * Add a new listener to the list of listeners
	 * @param listener the listener to add
	 */
	public void addListener(TaskCompletedListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * all listeners getting triggered and execute taskCompleted()
	 */
	public void triggerListenersTaskCompletion() {
		for (TaskCompletedListener l:this.listeners) l.taskCompleted();
	}

	/**
	 * Returns the list of uncompleted tasks of current car in workstation
	 * @return a list of uncompleted tasks
	 */
	public List<Task> getUncompletedTasks() {
		if(this.getCar() == null) {
			return null;
		}
		List<Task> tasks = this.getTasks();
		tasks.retainAll(this.getCar().getUncompletedTasks()); 
		return tasks;
	}

	/**
	 * Returns the list of completed tasks of current car in workstation
	 * @return a list of completed tasks
	 */
	public List<Task> getCompletedTasks() {
		if(this.getCar() == null) {
			return null;
		}
		List<Task> tasksOfWorkstation = this.getTasks();
		List<Task> allTasksOfCar = new LinkedList<>(this.getCar().getAllTasks());
		tasksOfWorkstation.retainAll(allTasksOfCar);
		tasksOfWorkstation.removeAll(this.getCar().getUncompletedTasks());
		return tasksOfWorkstation;
	}

	/**
	 * checks if a name is valid to be a name for workstation
	 * @param name a name for the workstation
	 * @return whether the naming string is valid
	 */
	private boolean isValidName(String name) {
		return (name.equals("Car Body Post")) ||
				(name.equals("Drivetrain Post")) || (name.equals("Accessories Post"));
	}

	/**
	 * returns the tasks that are part of this workstation
	 * @return tasks of workstation
	 */
	public List<Task> getTasks() {
		return switch (this.getName()) {
			case "Car Body Post" -> new LinkedList<>(Arrays.asList(Task.AssemblyCarBody, Task.PaintCar));
			case "Drivetrain Post" -> new LinkedList<>(Arrays.asList(Task.InsertEngine, Task.InstallGearbox));
			case "Accessories Post" -> new LinkedList<>(Arrays.asList(Task.InstallSeats, Task.InstallAirco, Task.MountWheels, Task.InstallSpoiler));
			default -> throw new IllegalArgumentException("No tasks could be given: Invalid name Workstation");
		};
	}

	/**
	 * returns the name of the workstation
	 * @return the name of the workstation
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * returns current car in workstation
	 * @return the car in the workstation
	 */
	public Car getCar() {
		return this.car;
	}

	/**
	 * setter for the car in the workstation
	 * @param car the car to be placed in the workstation
	 */
	public void setCar(Car car) {
		this.currentWorkingTime = 0;
		this.car = car;
	}
	
	/**
	 * Returns how long a car is currently in the workstation
	 * @return this.currentWorkingTime
	 */
	public int getCurrentWorkingTime() {
		return this.currentWorkingTime;
	}
	
	/**
	 * Checks if the part is chosen off the current car in workstation. (defined in the chosenOptions)
	 * @param part check if a part is chosen for current car
	 * @return boolean whether the part is chosen or not
	 */
	public boolean isPartOfCurrentCarInWorkStation(String part) {
		Car car = this.getCar();
		if(car == null) return false;
		return car.getCarModel().getCarModelSpecification().isPartInChosenOptions(part);
	}
	
	/**
	 * Tries to get value of a carOptionCategory
	 * @param category the given carOptionCategory to get the value of
	 * @throws IllegalArgumentException if car == null || part == null
	 * @return the chosen option for a category
	 */
	public String getValueOfPart(String category) {
		if(this.car == null) throw new IllegalArgumentException("No car in station");
		if (category == null) throw new IllegalArgumentException("part is null");
		return this.getCar().getValueOfPart(category);

	}
	
	/**
	 * Tries to complete a task 
	 * @param task to complete
	 * @param time Time it took to complete the task
	 * @throws IllegalArgumentException if car == null || task == null
	 */
	public void completeTask(Task task, int time) {
		if(car == null) throw new IllegalArgumentException("No car in station");
		if (task == null) throw new IllegalArgumentException("task is null");
		this.getCar().completeTask(task);
		this.currentWorkingTime += time;
		this.triggerListenersTaskCompletion();
	}

	/**
	 * You can check if all tasks are completed of current work station.
	 * @return true if there is no car or all tasks are completed
	 */
	public boolean stationTasksCompleted() {
		return this.getCar() == null || Collections.disjoint(this.getCar().getUncompletedTasks(),
				this.getTasks()); //returns true if no tasks are uncompleted
	}
	
}
