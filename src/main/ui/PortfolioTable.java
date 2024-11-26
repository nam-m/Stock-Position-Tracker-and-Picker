package ui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.Account;
import model.AccountEvent;
import model.Stock;
import model.StockPosition;
import observer.Observer;

// Represents portfolio table of type Observer
public class PortfolioTable implements Observer {
    private DefaultTableModel portfolioModel;
    private String[] columns = {"Symbol", "Price", "Quantity", "Total Value", "Actions"};
    private Object[][] data;
    private JTable portfolioTable;

    // EFFECTS: Construct a portfolio table component with corresponding JTable
    public PortfolioTable(Account account) {
        portfolioModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        portfolioTable = new JTable(portfolioModel);
        portfolioTable.setRowHeight(30);     
        portfolioTable.getColumn("Actions").setCellRenderer(new BuySellButtonRenderer());
        updateTableData(account);
    }

    // EFFECTS: Update table data based on event
    @Override
    public void update(AccountEvent event) {
        if (event.getType() == model.EventType.PORTFOLIO_CHANGED) {
            updateTableData(event.getAccount());
        }
    }
 
    // EFFECTS: Update table data with portfolio from account
    public void updateTableData(Account account) {
        SwingUtilities.invokeLater(() -> {
            data = getPortfolioStockData(account);
            portfolioModel.setRowCount(0);
            
            for (Object[] row : data) {
                portfolioModel.addRow(row);
            }
            // Reinitialize buy and sell buttons to attach to new Account instance
            portfolioTable.getColumn("Actions").setCellEditor(
                new BuySellButtonEditor(new JCheckBox(), portfolioTable, account));
        });
    }

    // EFFECTS: Retrieve all portfolio stock positions
    private Object[][] getPortfolioStockData(Account account) {
        if (account == null) {
            return new Object[0][0];
        }
        Map<String, StockPosition> positions = account.getPortfolio().getAllStockPositions();
        // Only create rows for positions with quantity > 0
        List<StockPosition> activePositions = positions.values().stream()
                .filter(position -> position.getQuantity() > 0)
                .collect(Collectors.toList());
        data = new Object[activePositions.size()][5];
        int row = 0;
        for (StockPosition position : activePositions) {
            Stock stock = position.getStock();
            data[row][0] = stock.getSymbol();     
            data[row][1] = stock.getPrice();
            data[row][2] = position.getQuantity();
            data[row][3] = position.getTotalCost();
            data[row][4] = null;
            row++;
        }
        return data;
    }

    // EFFECTS: Get portfolio table
    public JTable getTable() {
        return portfolioTable;
    }
}
