package govindan.assign3;

import java.awt.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/*
 * This class displays the table view along with the current status of the expenses.
 * It is updated by the ExpenseModel class. It has current status display and the table display.
 * Current status displays the status of the limits and the expenses based on the limit set
 * along with  amount left for the user to spend for the time period. Table displays the category wise expenses. 
 * The same table view is also used to display expense history.
 */
class ExpenseTableView extends AView {
	private ExpenseModel expModel;
	private DefaultTableModel modelCurExpenses;
	private JTable table;
	private ResultSet rs;
	private HashMap<String, Double> hmExpense;
	private Font font;
	private JLabel lblCurExp, lblsDate, lblLimit, lblAmtLe, lblStat, lbleDate;
	private java.sql.Date dateToday;
	private JPanel pnlStartDate, pnlLimit, pnlExpense, pnlAmtLeft, pnlStatus,
			pnlEndDate;

	public ExpenseTableView() {
		printCurrentExpenses();
		this.setBorder(BorderFactory.createTitledBorder("Table View"));
	}

	/*
	 * Prints the current expenses
	 */
	void printCurrentExpenses() {
		font = new Font(("SansSerif"), Font.BOLD, 25);
		Calendar calendar = Calendar.getInstance();
		dateToday = new java.sql.Date(calendar.getTime().getTime());
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel lblHeader = new JLabel("Current Expense Status",
				SwingConstants.CENTER);
		lblHeader.setFont(font);
		add(lblHeader);
		add(Box.createRigidArea(new Dimension(0, 20)));
		font = new Font(("SansSerif"), Font.PLAIN, 20);
		// Date Information
		pnlStartDate = new JPanel();
		pnlStartDate.setLayout(new BoxLayout(pnlStartDate, BoxLayout.X_AXIS));
		JLabel lblStartDate = new JLabel("Start Date:");
		lblsDate = new JLabel();
		lblsDate.setFont(font);
		lblStartDate.setFont(font);
		pnlStartDate.add(lblStartDate);
		pnlStartDate.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlStartDate.add(lblsDate);
		add(pnlStartDate);
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Date Information
		pnlEndDate = new JPanel();
		pnlEndDate.setLayout(new BoxLayout(pnlEndDate, BoxLayout.X_AXIS));
		JLabel lblEndDate = new JLabel("End Date:");
		lbleDate = new JLabel();
		lbleDate.setFont(font);
		lblEndDate.setFont(font);
		pnlEndDate.add(lblEndDate);
		pnlEndDate.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlEndDate.add(lbleDate);
		add(pnlEndDate);
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Limit Information
		pnlLimit = new JPanel();
		pnlLimit.setLayout(new BoxLayout(pnlLimit, BoxLayout.X_AXIS));
		JLabel lblLimitDetail = new JLabel("Limit Info:");
		lblLimit = new JLabel();
		lblLimit.setFont(font);
		lblLimitDetail.setFont(font);
		pnlLimit.add(lblLimitDetail);
		pnlLimit.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlLimit.add(lblLimit);
		add(pnlLimit);
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Current Expense Information
		pnlExpense = new JPanel();
		pnlExpense.setLayout(new BoxLayout(pnlExpense, BoxLayout.X_AXIS));
		JLabel lblCurExpense = new JLabel("Current Expenses:");
		lblCurExp = new JLabel();
		lblCurExp.setFont(font);
		lblCurExpense.setFont(font);
		pnlExpense.add(lblCurExpense);
		pnlExpense.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlExpense.add(lblCurExp);
		add(pnlExpense);
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Amount left to be spent
		pnlAmtLeft = new JPanel();
		pnlAmtLeft.setLayout(new BoxLayout(pnlAmtLeft, BoxLayout.X_AXIS));
		JLabel lblAmtLeft = new JLabel("Amount Left:");
		lblAmtLe = new JLabel();
		lblAmtLe.setFont(font);
		lblAmtLeft.setFont(font);
		pnlAmtLeft.add(lblAmtLeft);
		pnlAmtLeft.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlAmtLeft.add(lblAmtLe);
		add(pnlAmtLeft);
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Status
		pnlStatus = new JPanel();
		pnlStatus.setLayout(new BoxLayout(pnlStatus, BoxLayout.X_AXIS));
		JLabel lblStatus = new JLabel("Status:");

		lblStat = new JLabel();
		lblStat.setFont(font);
		lblStatus.setFont(font);
		pnlStatus.add(lblStatus);
		pnlStatus.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlStatus.add(lblStat);
		add(pnlStatus);
		add(Box.createRigidArea(new Dimension(0, 20)));

		font = new Font(("SansSerif"), Font.BOLD, 25);
		JLabel lblTableHeader = new JLabel("Table View of Selected Data",
				SwingConstants.CENTER);
		lblTableHeader.setFont(font);
		add(lblTableHeader);
		// Creating JTable
		modelCurExpenses = new DefaultTableModel();
		String[] columnNames = { "Category", "Amount", "Date" };
		modelCurExpenses.setColumnIdentifiers(columnNames);

		table = new JTable();

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800, 500));
		add(scrollPane);

	}

	/*
	 * Creates the table based on the updated database
	 */
	public void createTable() {
		table.setModel(modelCurExpenses);
		for (Map.Entry<String, Double> entry : hmExpense.entrySet()) {
			modelCurExpenses.addRow(new Object[] { entry.getKey(),
					entry.getValue(), dateToday });
		}
		table.setFont(new Font("SansSerif", Font.PLAIN, 20));
		table.setFillsViewportHeight(true);
		table.setRowHeight(45);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 20));
		TableColumn column = null;
		for (int i = 0; i < 2; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(100);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}

	/*
	 * This method is called when the Observable ExpenseModel is
	 * changed(non-Javadoc)
	 * 
	 * @see govindan.assign3.AView#update(java.util.Observable,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void update(Observable o1, Object items) {
		HashMap<String, ExpenseModel> objectReceived = null;
		if (items instanceof HashMap)
			objectReceived = (HashMap<String, ExpenseModel>) items;
		//Initially get is invoked to get the current status 
		if (objectReceived.containsKey("get")) {
			expModel = objectReceived.get("get");
			lblsDate.setText(expModel.getStartDate().toString());
			lbleDate.setText(expModel.getEndDate().toString());
			lblLimit.setText("$"+expModel.getDblExpenseLimit().toString());
			lblCurExp.setText("$"+expModel.getDblCumulative().toString());
			lblAmtLe.setText("$"+(String.valueOf(expModel.getDblExpenseLimit()
					- expModel.getDblCumulative())));
			//Checking whether the limit is set or not. If not then the limit details will be set to "not set"
			if (!expModel.getStrDuration().equals("notset")) {
				lblStat.setText(expModel.checkWithinLimit());
				setStatusBackground(expModel.checkWithinLimit());
			} else {
				lblStat.setText("Not Set");
				lblCurExp.setText("$"+expModel.getTodayExpenses().toString());
				lblsDate.setText("Not Set");
				lbleDate.setText("Not Set");
				lblAmtLe.setText("Not Set");
				lblLimit.setText("Not Set");
				pnlStatus.setBackground(pnlLimit.getBackground());
			}
			hmExpense = expModel.getHmExpense();
			int rowCount = modelCurExpenses.getRowCount();
			for (int i1 = rowCount - 1; i1 >= 0; i1--) {
				modelCurExpenses.removeRow(i1);
			}
			createTable();
		}
		//If it is add event then table should update accodingly 
		if (objectReceived.containsKey("add")) {
			expModel = objectReceived.get("add");
			int rowCount = modelCurExpenses.getRowCount();
			// Remove rows one by one from the end of the table and repopulate
			// the table
			for (int i1 = rowCount - 1; i1 >= 0; i1--) {
				modelCurExpenses.removeRow(i1);
			}
			hmExpense = expModel.getHmExpense();
			for (Map.Entry<String, Double> entry : hmExpense.entrySet()) {
				modelCurExpenses.addRow(new Object[] { entry.getKey(),
						entry.getValue(), dateToday });
			}
			if (expModel.getStrDuration().equals("notset")){
				this.lblCurExp.setText("$"+expModel.getTodayExpenses().toString());
				this.lblAmtLe.setText("Not Set");
				this.lblStat.setText("Not Set");
			}
			else{
				lblCurExp.setText("$"+expModel.getDblCumulative().toString());
				double amountLeft = expModel.getDblExpenseLimit()
						- expModel.getDblCumulative();
				this.lblAmtLe.setText("$"+String.valueOf(amountLeft));
				this.lblStat.setText(expModel.checkWithinLimit());
				setStatusBackground(expModel.checkWithinLimit());
			}
			

		}
		// If set limit is invoke the changes to the table view is done here
		if (objectReceived.containsKey("set")) {
			expModel = objectReceived.get("set");
			this.lblsDate.setText(expModel.getStartDate().toString());
			this.lbleDate.setText(expModel.getEndDate().toString());
			this.lblLimit.setText("$"+expModel.getDblExpenseLimit().toString());
			this.lblCurExp.setText("$"+expModel.getDblCumulative().toString());
			this.lblAmtLe.setText(String.valueOf("$"+(expModel.getDblExpenseLimit()
					- expModel.getDblCumulative())));
			this.lblStat.setText(expModel.checkWithinLimit());
			setStatusBackground(expModel.checkWithinLimit());
			hmExpense=expModel.getHmExpense();
			int rowCount = modelCurExpenses.getRowCount();
			// Remove rows one by one from the end of the table and repopulate
			// the table
			for (int i1 = rowCount - 1; i1 >= 0; i1--) {
				modelCurExpenses.removeRow(i1);
			}
			createTable();

		}
		// When selected a duration to access the expenses the changes to the
		// table view is done here by clearing table and reloading it
		if (objectReceived.containsKey("go")) {
			expModel = objectReceived.get("go");
			int rowCount = modelCurExpenses.getRowCount();
			// Remove rows one by one from the end of the table
			for (int i1 = rowCount - 1; i1 >= 0; i1--) {
				modelCurExpenses.removeRow(i1);
			}
			hmExpense = expModel.getHmExpense();
			for (Map.Entry<String, Double> entry : hmExpense.entrySet()) {
				modelCurExpenses.addRow(new Object[] { entry.getKey(),
						entry.getValue(), "-" });
			}

		}
	}

	/*
	 * When status changes the appropriate background color of the panel is set
	 * in this method. If the status is "safe" the background is green and if it
	 * is "exceeded" the background is red.
	 */
	private void setStatusBackground(String strstatus) {
		if (strstatus.equals("Exceeded"))
			pnlStatus.setBackground(Color.red);
		else
			pnlStatus.setBackground(Color.green);
	}
}