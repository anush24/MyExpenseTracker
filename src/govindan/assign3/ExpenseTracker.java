package govindan.assign3;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/*
 * This class implements the Observer interface and 
 * provides an empty implementation for the interface
 */


abstract class AView extends JPanel implements Observer {
	public void update(Observable o1, Object o2) {
	};
}

public class ExpenseTracker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Create a Model
		ExpenseModel model = new ExpenseModel();

		// Create Table View 1
		ExpenseTableView tableView = new ExpenseTableView();
		// Add the View as the Observer on the model
		model.addObserver(tableView);

		// Create Visualization View 2
		ExpenseVisView visView = new ExpenseVisView();

		// Add the View as the Observer on the model
		model.addObserver(visView);

		// Create a Controller and connect it with the model
		ExpenseController controller = new ExpenseController(model);

		// Add the views to the controller so that it displays them
		// in a window
		controller.addAView(tableView);
		controller.addAView(visView);

		// Start the application now
		controller.startApp();
	}

}
