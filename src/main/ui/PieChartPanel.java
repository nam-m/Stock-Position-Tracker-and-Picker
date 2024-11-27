package ui;

import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import model.Account;
import model.AccountEvent;
import model.EventType;
import model.StockPosition;
import observer.Observer;

public class PieChartPanel implements Observer {
    private ChartPanel chartPanel;
    private JFreeChart chart;
    private DefaultPieDataset dataset;

    // EFFECTS: Construct a pie chart panel
    public PieChartPanel(Account account) {
        dataset = new DefaultPieDataset();
        updateDataset(account);
        chart = ChartFactory.createPieChart(
                "Investment Portfolio", // chart title
                dataset,                       // data
                true,                         // include legend
                true,                         // tooltips
                false                         // URLs
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0} : {2}", new DecimalFormat("0"), new DecimalFormat("0%"));  
        plot.setLabelGenerator(labelGenerator);
        // Add spacing between sections
        plot.setSectionOutlinesVisible(true);
        chartPanel = new ChartPanel(chart);
    }

    // EFFECTS: Update pie chart based on event
    @Override
    public void update(AccountEvent event) {
        if (event.getType() == EventType.ACCOUNT_LOADED
                || event.getType() == EventType.BALANCE_CHANGED
                || event.getType() == EventType.PORTFOLIO_CHANGED) {
            updatePieChart(chartPanel, event.getAccount());
        }
    }

    private void updatePieChart(ChartPanel chartPanel, Account account) {
        if (chartPanel != null) {
            chart = chartPanel.getChart();
            PiePlot plot = (PiePlot) chart.getPlot();
            dataset = (DefaultPieDataset) plot.getDataset();
            updateDataset(account);
        }
    }

    // EFFECTS: Update table data with cash balance and portfolio from account
    private void updateDataset(Account account) {
        SwingUtilities.invokeLater(() -> {
            dataset.clear();
            // Update cash balance
            double cashBalance = account.getCashBalance().doubleValue();
            dataset.setValue("Cash", cashBalance);
            // Update portfolio
            Map<String, StockPosition> positions = account.getPortfolio().getAllStockPositions();
            for (String symbol : positions.keySet()) {
                dataset.setValue(symbol, positions.get(symbol).getTotalCost());
            }
        });
    }

    public ChartPanel getPanel() {
        return chartPanel;
    }
}
