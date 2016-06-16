package manager;

import datastorage.TableDAO;
import domain.Table;

/**
 *
 * @author Dennis
 */
public class TableManager {
    private Table table;
    private TableDAO tableDAO;
    
    public TableManager() {
        tableDAO = new TableDAO();
    }
    
    //Getters
    public Table getTable() {
        return table;
    }
    
    //Setters
    public void setTable(Table table) {
        this.table = table;
    }
    
    public void needHelp() {
        tableDAO.changeHelpNeeded(table.getTableNr());
    }
}
