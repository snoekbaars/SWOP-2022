package swop.UI;
import swop.CarManufactoring.CarOrder;
import swop.Exceptions.CancelException;

import java.util.*;
import java.util.List;

public class GarageHolderUI implements UI {

	// Dummy init
	public static void init(String id) {
		System.out.println("Welcome Garage Holder: " + id);
	}

	/**
	 * Displays all orders ordered by current garage holder
	 */
	public static void displayOrders(Set<CarOrder> carOrders) {
		if (carOrders == null) {
			System.out.println("No carOrders placed yet.");
			return;
		}
		System.out.printf("%n============ Orders ============%n");
		System.out.println("Pending:");
		carOrders.stream()
				.filter(o -> !o.isCompleted())
				.forEach(p ->{
					System.out.print("Order: "+ p.getID());
					System.out.println("Est. completion at: " + p.getEstimatedCompletionTime());
				});
		System.out.println();
		System.out.println("Completed:");

		Set<CarOrder> completedSet = new TreeSet<>();

		// Add to sorted set
		carOrders.stream()
				.filter(CarOrder::isCompleted)
				.forEach(completedSet::add);

		// Print sorted set
		completedSet.forEach(c -> {
					System.out.println("Order: " + c.getID());
				});
		System.out.println("=======================================");
	}
	
	/**
	 * Asks confirmation for placing order
	 */
	public static String indicatePlaceOrder() throws CancelException {
		return UI.indicateYesNo("place an order");
	}

	public static int indicateCarModel() throws CancelException {
		System.out.printf("%n============ Car Models ============%n");
		System.out.println("[0] car");
		System.out.println("=======================================");
		System.out.println();
		System.out.printf("Which model would you like to order?%n");
		return scanner.scanNextLineOfTypeInt(0,1); ///we kunnen ook hun de naam laten typen
	}

	/**
	 * Displays the ordering from given a list of components and its options
	 * @param optionsMap list of components and its options
	 */
	public static void displayOrderingForm(Map<String, List<String>> optionsMap) {
		System.out.printf("%n============ Ordering Form ============%n");
		optionsMap.forEach((key, value) -> {
			System.out.print(key + ": ");
			final int[] itemNumber = {-1};
			value.forEach(v -> System.out.printf("[%s] %s, ", itemNumber[0] += 1, v));
			System.out.printf("%n");
		});
		System.out.println("=======================================");
	}


	public static int askOption (int leftBound, int rightBound, String option) throws CancelException {
		System.out.print("Choose " + option + ": ");
			return scanner.scanNextLineOfTypeInt(leftBound, rightBound);
	}

	/**
	 * Prints the given estimated completion time.
	 * @param order the estimated completion time to be displayed
	 */
    public static void displayEstimatedTime(CarOrder order) {
		System.out.printf("%n============ Estimated Completion Time ============%n");
		System.out.println(order.getEstimatedCompletionTime());
		System.out.println("=======================================");
	}

}
