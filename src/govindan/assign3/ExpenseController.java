package govindan.assign3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.impl.*;

/*
 * Expense Controller contains the GUI for controls and panel for views. The control in turn has 2 control panels:
 *  Current Expense Panel and Expense History Panel.
 * Current Expense has controls for setting the limit and adding today's expenses whereas the expense history panel has controls
 * to access the expense history between a start date and end date and by category.
 */
public class ExpenseController implements ActionListener {
	private ExpenseModel exModel;
	private JFrame frExpense;
	private JPanel pnlViews;
	private expControlUI expUI;
	private Font font;
	private JTextField txtLimit, txtAmount;
	private JButton btnSet, btnAdd, btnGo;
	private JComboBox<String> cboCategory, cboDuration, cboCat;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl startDate, endDate;
	private Properties p;
	private JLabel lblErrorStatus, lblExpError;

	public ExpenseController(ExpenseModel exModel1) {
		exModel = exModel1;
		expUI = new expControlUI();
		frExpense = new JFrame("My Expense Tracker");
		frExpense.getContentPane().add(expUI, BorderLayout.NORTH);
		pnlViews = new JPanel(new GridLayout(1, 2));
		frExpense.getContentPane().add(pnlViews, BorderLayout.CENTER);

	}
	/*
	 * Adds the table and  the chart view to the main panel
	 */

	public void addAView(AView view) {
		pnlViews.add(view);
	}

	class expControlUI extends JPanel {
		private UtilDateModel model;

		public expControlUI() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			exModel.getInitialValues();
			// Create a Header Panel
			JPanel pnlHeader = new JPanel();
			pnlHeader.setBackground(Color.WHITE);
			JLabel lblHeader = new JLabel("My Expense Tracker", JLabel.CENTER);
			font = new Font(("SansSerif"), Font.BOLD, 35);
			lblHeader.setFont(font);
			pnlHeader.add(lblHeader);

			// Create Controller Panel
			JPanel pnlController = new JPanel(new GridLayout(1, 2));

			// Initializing CurrentExpense Panel
			JPanel pnlCurrentExpense = new JPanel();
			pnlCurrentExpense.setLayout(new BoxLayout(pnlCurrentExpense,
					BoxLayout.Y_AXIS));
			pnlCurrentExpense.setBorder(BorderFactory
					.createTitledBorder("Current Expense Tracker"));
			pnlCurrentExpense.setPreferredSize(new Dimension(getWidth(), 30));
			JPanel pnlExpenseHistory = new JPanel();
			pnlExpenseHistory.setLayout(new BoxLayout(pnlExpenseHistory,
					BoxLayout.Y_AXIS));
			pnlExpenseHistory.setBorder(BorderFactory
					.createTitledBorder("Expense History"));
			pnlController.setPreferredSize(new Dimension(200, 200));

			// Updating Current Expense Tracker panel
			// Creating error label
			lblExpError = new JLabel();
			lblExpError.setFont(new Font(("SansSerif"), Font.PLAIN, 14));
			lblExpError.setForeground(Color.red);

			// 1.Expense Limit Panel
			font = new Font(("SansSerif"), Font.PLAIN, 20);
			JPanel pnlExpenseLimit = new JPanel();
			pnlExpenseLimit.setLayout(new BoxLayout(pnlExpenseLimit,
					BoxLayout.X_AXIS));
			JLabel lblExpenseLimit = new JLabel("Expense Limit");
			txtLimit = new JTextField(3);
			String[] Duration = new String[] { "Day", "Week", "Month" };
			JLabel lblDuration = new JLabel("Duration");
			cboDuration = new JComboBox<String>(Duration);
			btnSet = new JButton(new ImageIcon("images/set.png"));
			btnSet.setOpaque(false);
			btnSet.setContentAreaFilled(false);
			btnSet.setBorderPainted(false);
			lblDuration.setFont(font);
			lblExpenseLimit.setFont(font);
			txtLimit.setFont(font);
			cboDuration.setFont(font);
			txtLimit.setMaximumSize(new Dimension(80, 50));
			cboDuration.setMaximumSize(new Dimension(150, 50));
			if (!exModel.getStrDuration().equals("notset")) {
				txtLimit.setText(exModel.getDblExpenseLimit().toString());
				cboDuration.setSelectedItem(exModel.getStrDuration());
			}

			pnlExpenseLimit.add(lblExpenseLimit);
			pnlExpenseLimit.add(Box.createRigidArea(new Dimension(15, 0)));
			pnlExpenseLimit.add(txtLimit);
			pnlExpenseLimit.add(Box.createRigidArea(new Dimension(40, 0)));
			pnlExpenseLimit.add(lblDuration);
			pnlExpenseLimit.add(Box.createRigidArea(new Dimension(15, 0)));
			pnlExpenseLimit.add(cboDuration);
			pnlExpenseLimit.add(Box.createRigidArea(new Dimension(40, 0)));
			pnlExpenseLimit.add(btnSet);

			// 2.Add Expense Panel
			font = new Font(("SansSerif"), Font.PLAIN, 20);
			JPanel pnlAddExpense = new JPanel();
			pnlAddExpense.setLayout(new BoxLayout(pnlAddExpense,
					BoxLayout.X_AXIS));

			String[] Category = new String[] { "Dining", "Grocery",
					"Entertainment", "Gasoline", "Miscellaneous" };
			JLabel lblCategory = new JLabel("Category");
			cboCategory = new JComboBox<>(Category);
			JLabel lblAmount = new JLabel("Amount");
			txtAmount = new JTextField(3);
			btnAdd = new JButton(new ImageIcon("images/add.png"));
			btnAdd.setOpaque(false);
			btnAdd.setContentAreaFilled(false);
			btnAdd.setBorderPainted(false);
			lblCategory.setFont(font);

			lblAmount.setFont(font);
			txtAmount.setFont(font);
			cboCategory.setFont(font);
			txtAmount.setMaximumSize(new Dimension(80, 50));
			cboCategory.setMaximumSize(new Dimension(150, 50));
			pnlAddExpense.add(Box.createRigidArea(new Dimension(20, 0)));
			pnlAddExpense.add(lblCategory);
			pnlAddExpense.add(Box.createRigidArea(new Dimension(15, 0)));
			pnlAddExpense.add(cboCategory);
			pnlAddExpense.add(Box.createRigidArea(new Dimension(20, 0)));
			pnlAddExpense.add(lblAmount);
			pnlAddExpense.add(Box.createRigidArea(new Dimension(15, 0)));
			pnlAddExpense.add(txtAmount);
			pnlAddExpense.add(btnAdd);

			// Adding subpanels to CurrentExpense Panel
			pnlCurrentExpense.add(lblExpError);
			pnlCurrentExpense.add(pnlExpenseLimit);
			pnlCurrentExpense.add(Box.createRigidArea(new Dimension(20, 40)));
			pnlCurrentExpense.add(pnlAddExpense);

			// Updating pnlExpensHistory
			// Status Panel
			lblErrorStatus = new JLabel();
			lblErrorStatus.setFont(new Font(("SansSerif"), Font.PLAIN, 14));
			lblErrorStatus.setForeground(Color.red);

			//  Start Date panel
			JPanel pnlStartDate = new JPanel();
			JLabel lblstartDate = new JLabel("Start Date");
			lblstartDate.setFont(font);
			model = new UtilDateModel();
			p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			datePanel = new JDatePanelImpl(model, p);
			startDate = new JDatePickerImpl(datePanel, new DateLabelFormatter());
			startDate.setFont(font);
			startDate.getJFormattedTextField().setFont(font);
			pnlStartDate.add(lblstartDate);
			pnlStartDate.add(startDate);

			// End Date Panel
			JPanel pnlEndDate = new JPanel();
			JLabel lblEndDate = new JLabel("End Date");
			lblEndDate.setFont(font);
			model = new UtilDateModel();
			p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			datePanel = new JDatePanelImpl(model, p);
			endDate = new JDatePickerImpl(datePanel, new DateLabelFormatter());
			endDate.getJFormattedTextField().setFont(font);
			pnlEndDate.add(lblEndDate);
			pnlEndDate.add(endDate);

			// Combo Box for Listing Category
			JPanel pnlcboCat = new JPanel();
			String[] strCat = new String[] { "All", "Dining", "Grocery",
					"Entertainment", "Gasoline", "Miscellaneous" };
			JLabel lblcat = new JLabel("Category");
			lblcat.setFont(font);
			cboCat = new JComboBox<>(strCat);
			cboCat.setFont(font);
			pnlcboCat.add(lblcat);
			pnlcboCat.add(cboCat);

			btnGo = new JButton(new ImageIcon("images/go.png"));
			btnGo.setOpaque(false);
			btnGo.setContentAreaFilled(false);
			btnGo.setBorderPainted(false);
			
			//Adding controls to expense history panel
			pnlExpenseHistory.add(lblErrorStatus);
			pnlExpenseHistory.add(pnlStartDate);
			pnlExpenseHistory.add(pnlEndDate);
			pnlExpenseHistory.add(pnlcboCat);
			pnlExpenseHistory.add(btnGo);

			// adding subpanels to controller UI
			pnlController.add(pnlCurrentExpense);
			pnlController.add(pnlExpenseHistory);

			// Adding header and the controller to main panel
			add(pnlHeader);
			add(pnlController);

			// Button Action Command
			btnAdd.setActionCommand("add");
			btnSet.setActionCommand("set");
			btnGo.setActionCommand("go");
		}

		/**
		 * Adds listener for button add , set and Go. Add is to add an expense, set is to set expense limit and go is to 
		 * list category wise expenses in the set duration
		 */
		public void addListener(ActionListener listener) {
			btnAdd.addActionListener(listener);
			btnSet.addActionListener(listener);
			btnGo.addActionListener(listener);
		}

		/**
		 * Formats the JDatePicker with date formatter
		 */
		public class DateLabelFormatter extends AbstractFormatter {

			private String datePattern = "yyyy-MM-dd";
			private SimpleDateFormat dateFormatter = new SimpleDateFormat(
					datePattern);

			@Override
			public Object stringToValue(String text) throws ParseException {
				return dateFormatter.parseObject(text);
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				if (value != null) {
					Calendar cal = (Calendar) value;
					return dateFormatter.format(cal.getTime());
				}

				return "";
			}
		}
	}

	/**
	 * Handles based on the command fired
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		lblErrorStatus.setText(" ");
		lblExpError.setText(" ");
		// Adding a new expense after validating the amount to be numeric
		if (cmd.equals("add")) {
			if (isNumeric(txtAmount.getText()))
				exModel.doneAddExpense(
						cboCategory.getSelectedItem().toString(),
						Double.parseDouble(txtAmount.getText()));
			else
				lblExpError.setText("Amount entered is not a Number");
		} // Setting the limit after validating the expense to be numeric
		else if (cmd.equals("set")) {
			if (isNumeric(txtLimit.getText())){
				exModel.getExpenseDetails();
				exModel.setLimitDetails(Double.parseDouble(txtLimit.getText()),
						cboDuration.getSelectedItem().toString());
			}
				
			else
				lblExpError.setText("Set Limit entered is not a Number");
		}// Getting expenses within a duration. Validates the duration and give error msg if duration is not set
		else if (cmd.equals("go")) {
			String stDate, enDate;
			if (!(startDate.getModel().isSelected()))
				stDate = " ";
			else
				stDate = new SimpleDateFormat("yyyy-MM-dd")
						.format((Date) startDate.getModel().getValue());
			if (!(endDate.getModel().isSelected()))
				enDate = " ";
			else
				enDate = new SimpleDateFormat("yyyy-MM-dd")
						.format((Date) endDate.getModel().getValue());
			if (stDate.equals(" "))
				lblErrorStatus.setText("Start Date Not Set");
			else if (enDate.equals(" "))
				lblErrorStatus.setText("End Date Not Set");
			else
				exModel.getSelectedExpenses(stDate, enDate, cboCat
						.getSelectedItem().toString());
		}
	}

	/**
	 * Checks if the given string is numeric or not
	 * 
	 * @param str
	 * @return true if the string is numeric and false otherwise
	 */
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * This method adds listener and set frame properties
	 */
	public void startApp() {
		expUI.addListener(this);
		frExpense.setSize(1800, 1000);
		frExpense.setLocationRelativeTo(null);
		frExpense.setVisible(true);
		frExpense.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
