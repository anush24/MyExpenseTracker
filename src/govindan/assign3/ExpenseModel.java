package govindan.assign3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observable;

/*
 * This is the model class where all the updates to the database is done and the both the table and the chart views are updated simultaneously
 * using observer pattern. getExpenseDetail() is the important method here that is used to get details from the updated database and resets
 * all the parameter of this class which is used for updating the views.
 * 
 */
class ExpenseModel extends Observable {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/expensetracker";
	static final String USERNAME = "root";
	static final String PASSWORD = "";
	private Connection con;
	private Statement stmt;
	private String sql;
	private ResultSet rs;
	private HashMap<String, Double> hmExpense;
	private Double dblExpenseLimit = 0.0, dblCumulative = 0.0;
	private String strDuration, status = "Safe", curCategory;
	private HashMap<String, ExpenseModel> objstatus;
	private Date startDate, endDate;

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param curCategory
	 *            the curCategory to set
	 */
	public void setCurCategory(String curCategory) {
		this.curCategory = curCategory;
	}

	/**
	 * @return the strDuration
	 */
	public String getStrDuration() {
		return strDuration;
	}

	/**
	 * @return the dblExpenseLimit
	 */
	public Double getDblExpenseLimit() {
		return dblExpenseLimit;
	}

	/**
	 * Sets the limit and updates the limit details in the database
	 * 
	 * @param dblExpenseLimit
	 *            the dblExpenseLimit to set
	 */
	public void setLimitDetails(Double dblExpenseLimit, String strDuration) {
		Calendar calendar = Calendar.getInstance();
		this.startDate = new java.sql.Date(calendar.getTime().getTime());
		calendar.setTime(this.startDate);
		//Checks the duration updates the start date as today and end date as start date plus the number of days based on the duration
		if (strDuration.equals("Day")) {
			this.endDate = this.startDate;
		} else if (strDuration.equals("Week")) {
			calendar.setTime(this.startDate);
			calendar.add(Calendar.DATE, 7);
			endDate = convertUtilToSql(calendar.getTime());
		} else if (strDuration.equals("Month")) {
			calendar.setTime(this.startDate);
			calendar.add(Calendar.MONTH, 1);
			endDate = convertUtilToSql(calendar.getTime());
		}
		try {
			this.getConnection();
			sql = "delete from limitdetails where 1";
			stmt.executeUpdate(sql);
			sql = "insert into limitdetails values('" + this.startDate + "','"
					+ this.endDate + "', " + dblExpenseLimit + ")";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.dblExpenseLimit = dblExpenseLimit;
		this.strDuration = strDuration;
		objstatus = new HashMap<String, ExpenseModel>();
		objstatus.put("set", this);
		//notifying observers
		setChanged();
		notifyObservers(objstatus);
	}

	/*
	 * Converts the util date to sql date
	 */
	private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

	private void getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			this.con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = this.con.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Constructor resets the hashmap
	 */
	public ExpenseModel() {
		hmExpense = new LinkedHashMap<String, Double>();
		hmExpense.put("Dining", 0.0);
		hmExpense.put("Grocery", 0.0);
		hmExpense.put("Entertainment", 0.0);
		hmExpense.put("Gasoline", 0.0);
		hmExpense.put("Miscellaneous", 0.0);
	}

	/*
	 * Updates the added expense to the database and resets the parameters of
	 * this class to the updated values
	 */
	public void doneAddExpense(String category, Double amount) {
		try {
			int exp = 0;
			this.getConnection();
			sql = "select count(*)as expense from expensedetails where Date=curdate() and Category='"
					+ category + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next())
				exp = rs.getInt("expense");
			if (exp == 0) {
				sql = "insert into expensedetails(Category,Amount,Date) values ('"
						+ category + "'," + amount + ",curdate())";
				stmt.executeUpdate(sql);
			} else {
				sql = "update expensedetails set Amount=Amount+" + amount
						+ " where Category='" + category
						+ "' and Date=curdate()";
				stmt.executeUpdate(sql);
			}
			//Used to set the current values
			this.getExpenseDetails();
			this.getInitialVal();
			this.curCategory = category;
			objstatus = new HashMap<String, ExpenseModel>();
			objstatus.put("add", this);
			//notify the observers
			super.setChanged();
			notifyObservers(objstatus);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the curCategory
	 */
	public String getCurCategory() {
		return curCategory;
	}

	/**
	 * Returns the current cumulative expenses from the set start date till
	 * today
	 * 
	 * @return the dblCumulative
	 */
	public Double getDblCumulative() {
		try {
			// TODO Auto-generated method stub
			this.getConnection();
			sql = "select sum(Amount) as amount from expensedetails where curdate()<=(select enddate from limitdetails) and date between(select startdate from limitdetails) and (select enddate from limitdetails)";
			rs = stmt.executeQuery(sql);
			while (rs.next())
				dblCumulative = rs.getDouble("amount");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return dblCumulative;
	}
	/**
	 * Returns the current cumulative expenses from the set start date till
	 * today. This is used to load the values initiaially
	 * 
	 * @return the dblCumulative
	 */
	public void getInitialVal() {
		try {
			// TODO Auto-generated method stub
			this.getConnection();
			if(!strDuration.equals("notset")){
				sql = "select Category, sum(Amount) as Amount from expensedetails where date between (select startdate from limitdetails) and curdate() group by Category";
				rs = stmt.executeQuery(sql);
				hmExpense.clear();
				while (rs.next())
					hmExpense.put(rs.getString("Category"), rs.getDouble("Amount"));
			}else
				getExpenseDetails();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
	 * @return the hmExpense
	 */
	public HashMap<String, Double> getHmExpense() {
		return hmExpense;
	}

	/**
	 * This method sets the current values from the database to be displayed in
	 * the views
	 */
	public void getInitialValues() {
		getExpenseDetails();
		getInitialVal();
		objstatus = new HashMap<String, ExpenseModel>();
		objstatus.put("get", this);
		super.setChanged();
		// // Passes the old price (before change)
		notifyObservers(objstatus);
	}

	/**
	 * This method gets the data from the database and set all the parameters to
	 * the current values
	 */
	public void getExpenseDetails() {
		int days = 0;
		Calendar calendar = Calendar.getInstance();
		Date dateToday = new java.sql.Date(calendar.getTime().getTime());
		try {
			// TODO Auto-generated method stub
			this.getConnection();
			sql = "select Category,Amount,Date from expensedetails where date=curdate()";
			rs = stmt.executeQuery(sql);
			while (rs.next())
				hmExpense.put(rs.getString("Category"), rs.getDouble("Amount"));
			sql = "select * from limitdetails";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				dblExpenseLimit = rs.getDouble("limit");
				startDate = rs.getDate("startdate");
				endDate = rs.getDate("enddate");
				days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
			}
			if (days == 7)
				strDuration = "Week";
			else if (days > 7)
				strDuration = "Month";
			else if (days < 2)
				strDuration = "Day";
			int st = (int) ((dateToday.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24));
			if (st >= 1) {
				strDuration = "notset";
			}
			getDblCumulative();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the expense details from the given start date to end date
	 */
	public void getSelectedExpenses(String stDate, String enDate,
			String selectedItem) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			this.getConnection();
			if (selectedItem.equals("All")) {
				sql = "select category,sum(amount) as Amount from expensedetails where date between '"
						+ stDate + "' and '" + enDate + "' group by category";
			} else {
				sql = "select category,sum(amount) as Amount from expensedetails where category='"
						+ selectedItem
						+ "' and date between '"
						+ stDate
						+ "' and '" + enDate + "' group by category";

			}
			rs = stmt.executeQuery(sql);
			hmExpense.clear();
			while (rs.next())
				hmExpense.put(rs.getString("Category"), rs.getDouble("Amount"));
			objstatus = new HashMap<String, ExpenseModel>();
			objstatus.put("go", this);

			super.setChanged();
			notifyObservers(objstatus);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks whether the current expenses are within the expense limit set.
	 */
	public String checkWithinLimit() {
		// TODO Auto-generated method stub
		if (this.dblExpenseLimit != null)
			if (this.dblCumulative < this.dblExpenseLimit)
				status = "Safe";
			else
				status = "Exceeded";
		else
			status = "Not Set";
		return status;
	}

	/**
	 * Gets today's total expenses in all categories
	 * 
	 * @return the dblAmount
	 */
	public Double getTodayExpenses() {
		// TODO Auto-generated method stub
		double dblAmount = 0;
		try {
			// TODO Auto-generated method stub
			this.getConnection();
			sql = "select sum(Amount) as amount from expensedetails where date=curdate()";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				dblAmount = rs.getDouble("amount");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dblAmount;
	}

}