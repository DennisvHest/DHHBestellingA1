package datastorage;

/**
 *
 * @author Dennis
 */
public class TableDAO {

    public TableDAO() {

    }

    public void changeHelpNeeded(int tableNr) {

        // First open the database connection.
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {

            connection.executeSQLIUDStatement(
                    "UPDATE `table` SET helpNeeded = 1 WHERE tableNr = " + tableNr + ";");
        }
    }
}
