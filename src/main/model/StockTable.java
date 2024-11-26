package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import observer.Observer;
import ui.BuySellButtonEditor;
import ui.BuySellButtonRenderer;
import ui.StockRepository;

public class StockTable implements Observer {
    private DefaultTableModel stockModel;
    private String[] columns = {"Symbol", "Price", "Actions"};
    private Object[][] data;
    private JTable stockTable;

    // EFFECTS: Construct a stock table component with corresponding JTable
    public StockTable() {
        stockModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        stockTable = new JTable(stockModel);
        stockTable.setRowHeight(30);
        stockTable.getColumn("Actions").setCellRenderer(new BuySellButtonRenderer());
    }

    // EFFECTS: Update table data based on event
    @Override
    public void update(AccountEvent event) {
        if (event.getType() == model.EventType.PORTFOLIO_CHANGED) {
            updateTableData(event.getAccount());
        }
    }

    // EFFECTS: Set up table data with account
    public void setAccount(Account account) {
        // Update table data
        updateTableData(account);
        
        // Set up the button editor with the account
        stockTable.getColumn("Actions").setCellEditor(
            new BuySellButtonEditor(new JCheckBox(), stockTable, account));
    }
 
    // EFFECTS: Update table data with portfolio from account
    public void updateTableData(Account account) {
        SwingUtilities.invokeLater(() -> {
            Object[][] newData = getAllStocksData();
            stockModel.setRowCount(0);
            
            for (Object[] row : newData) {
                stockModel.addRow(row);
            }
            // Reinitialize buy and sell buttons to attach to new Account instance
            stockTable.getColumn("Actions").setCellEditor(
                new BuySellButtonEditor(new JCheckBox(), stockTable, account));
        });
    }

    // EFFECTS: Retrieve all stock data from StockRepository
    private Object[][] getAllStocksData() {
        Map<String, Stock> stocks = StockRepository.getAllStocks();
        // Create columns for symbol and price
        Object[][] data = new Object[stocks.size()][3];
        
        // Get symbols as list and sort
        List<String> symbols = new ArrayList<>(stocks.keySet());
        symbols.sort(String::compareTo); 

        int row = 0;
        for (String symbol : symbols) {
            Stock stock = stocks.get(symbol);
            data[row][0] = stock.getSymbol();
            data[row][1] = stock.getPrice();
            data[row][2] = null;
            row++; 
        }
        return data;
    }

    // EFFECTS: Get stock table
    public JTable getTable() {
        return stockTable;
    }

}
