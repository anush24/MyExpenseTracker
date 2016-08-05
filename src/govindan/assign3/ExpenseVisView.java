package govindan.assign3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * This class extends AView which has the JPanel. This class draws the chart and takes the data from 
 * ExpenseModel class which updates whenever the expense details changes
 */
class ExpenseVisView extends AView {
	HashMap<String, Double> hmExpense;
	ExpenseModel expModel;
	ChartPanel pnlChart;
	Font font;

	// Visualization goes here
	public ExpenseVisView() {
		this.setLayout(new BorderLayout());
		font = new Font("SansSerif", Font.BOLD, 25);
		// Heading Panel
		JLabel lblHeader = new JLabel("CURRENT EXPENSE STATUS VISUALIZATION",
				JLabel.CENTER);
		lblHeader.setFont(font);
		add(lblHeader, BorderLayout.NORTH);
		// Keys Panel
		KeysPanel pnlKeys = new KeysPanel();
		this.setBorder(BorderFactory.createTitledBorder("Chart View"));
		pnlKeys.addKey();
		add(pnlKeys, BorderLayout.SOUTH);
		// Chart Panel
		pnlChart = new ChartPanel();
		add(pnlChart, BorderLayout.CENTER);
	}

	//This method is called when the Observable modelCurExpenses is changed
	@SuppressWarnings("unchecked")
	public void update(Observable o1, Object items) {
		HashMap<String, ExpenseModel> objectReceived = new HashMap<String, ExpenseModel>();
		if (objectReceived instanceof HashMap)
			objectReceived = (HashMap<String, ExpenseModel>) items;
		if (objectReceived.containsKey("get")) {
			expModel = objectReceived.get("get");
			drawGraph(expModel);
		}
		if (objectReceived.containsKey("add")) {
			expModel = objectReceived.get("add");
			drawGraph(expModel);
		}
		if (objectReceived.containsKey("set")) {
			drawGraph(expModel);
		}
		if (objectReceived.containsKey("go")) {
			// Uncomment this if you wish to see the visualization for selected
			// date
			// expModel = i.get("go");
			// hmExpense = expModel.getHmExpense();
			// pnlChart.drawChart(hmExpense, 0, 0);
			// this.repaint();
		}
	}

	/*
	 * Calls the chart to draw the bars
	 */
	private void drawGraph(ExpenseModel expModel) {
		hmExpense = expModel.getHmExpense();
		if (expModel.getStrDuration().equals("notset"))
			pnlChart.drawChart(hmExpense, 0, expModel.getTodayExpenses());
		else
			pnlChart.drawChart(hmExpense, expModel.getDblExpenseLimit(),
					expModel.getDblCumulative());
		this.repaint();
	}

	/*
	 * Draws the horizontal bar chart based on the expense values
	 */
	class ChartPanel extends JPanel {
		HashMap<String, Double> expense;
		double dblLimit, dblCumulative;

		public ChartPanel() {
			expense = new LinkedHashMap<String, Double>();

			setPreferredSize(new Dimension(600, 600));
		}

		public void drawChart(HashMap<String, Double> hmExpense,
				double dblLimit, double dblCumulative) {
			this.expense.clear();
			for (Map.Entry<String, Double> entry : hmExpense.entrySet())
				this.expense.put(entry.getKey(), entry.getValue());
			this.expense.put("Cumulative", dblCumulative);
			this.expense.put("Limit", dblLimit);
			this.repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			// determine longest bar
			int max = Integer.MIN_VALUE;
			for (Double value : expense.values()) {
				max = Math.max(max, value.intValue());
			}
			// Paint bars based on relative widths
			if (expense.size() != 0) {
				int height = (getHeight() / expense.size()) - 20;
				int y = 395;
				g.drawLine(30, 410 + height, (int) ((getWidth() - 90)),
						410 + height);
				g.drawLine(30, 410 + height, 30, 10);
				for (String category : expense.keySet()) {
					int value = expense.get(category).intValue();
					int width = (int) (((getWidth() - 90)) * ((double) value / max));
					//setting color of the bar for each category
					if (category.equals("Dining"))
						g.setColor(Color.red);
					else if (category.equals("Grocery"))
						g.setColor(Color.green);
					else if (category.equals("Entertainment"))
						g.setColor(Color.blue);
					else if (category.equals("Gasoline"))
						g.setColor(Color.magenta);
					else if (category.equals("Miscellaneous"))
						g.setColor(Color.cyan);
					else if (category.equals("Limit"))
						g.setColor(Color.black);
					else if (category.equals("Cumulative"))
						g.setColor(Color.white);
					g.fillRect(30, y, width, height);
					g.setColor(Color.black);
					g.drawRect(30, y, width, height);
					g.setFont(new Font("SansSerif", Font.PLAIN, 20));
					g.drawString("$" + String.valueOf(value), 35 + width,
							y + 20);
					y -= (height + 10);
				}
				g.setFont(new Font("SansSerif", Font.PLAIN, 20));
				g.drawString("Current Expenses", 200, 430 + height);
			}

		}
	}

	/*
	 * Class creates the legend for the horizontal bar graph and gives color to
	 * each category
	 */
	class KeysPanel extends JPanel {
		int x;
		private Map<Color, String> keys = new LinkedHashMap<Color, String>();

		public KeysPanel() {
			x = 70;
			setPreferredSize(new Dimension(500, 150));
		}

		/*
		 * Adds small boxes filled with the given color for each key and the name of
		 * the category
		 */
		public void addKey() {
			keys.put(Color.red, "Dining");
			keys.put(Color.green, "Grocery");
			keys.put(Color.blue, "Entertainment");
			keys.put(Color.magenta, "Gasoline");
			keys.put(Color.cyan, "Miscellaneous");
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			for (Color color : keys.keySet()) {
				String value = keys.get(color);
				g.setColor(color);
				g.fillRect(x, getHeight() / 4, 20, 20);
				g.setColor(Color.black);
				g.drawRect(x, getHeight() / 4, 20, 20);
				g.setFont(new Font("SansSerif", Font.PLAIN, 20));
				g.drawString(value, x + 25, getHeight() - 95);
				x += 160;
			}
			g.setColor(Color.black);
			g.fillRect(195, 95, 20, 20);
			g.setColor(Color.black);
			g.drawRect(195, 95, 20, 20);
			g.setFont(new Font("SansSerif", Font.PLAIN, 20));
			g.drawString("Limit", 225, 112);
			g.setColor(Color.white);
			g.fillRect(350, 95, 20, 20);
			g.setColor(Color.black);
			g.drawRect(350, 95, 20, 20);
			g.setFont(new Font("SansSerif", Font.PLAIN, 20));
			g.drawString("Current Expenses", 380, 112);
			x = 70;
		}
	}
}