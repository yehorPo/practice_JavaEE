import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class StoreDataBase {
    public static final String fileName = "zlagoda.db";// is this kind of BD reference?
    private static volatile StoreDataBase storeDataBase;
    private final Connection connection;
    private final ProductDataBase productDataBase;
    private final CategoryDataBase categoryDataBase;
    private StoreDataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        createCats();
        createProds();
        productDataBase = new ProductDataBase(connection);
        categoryDataBase = new CategoryDataBase(connection);
    }
    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createCats() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS 'categories' (" +
                            "'name' VARCHAR(200) PRIMARY KEY,"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createProds() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS 'products' (" +
                            "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "'name' VARCHAR(200) NOT NULL UNIQUE," +
                            "'price' DECIMAL(10, 3) NOT NULL," +
                            "'number' INTEGER NOT NULL," +
                            "'category' VARCHAR(200) NOT NULL," +
                            "FOREIGN KEY(category) REFERENCES categories(name) ON UPDATE CASCADE ON DELETE CASCADE)"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static StoreDataBase getStoreDataBase() {
        StoreDataBase localInstance = storeDataBase;
        if (localInstance == null) {
            synchronized (StoreDataBase.class) {
                localInstance = storeDataBase;
                if (localInstance == null) {
                    storeDataBase = localInstance = new StoreDataBase();
                }
            }
        }
        return localInstance;
    }
    public Product readProduct(String title) {
        return productDataBase.read(title);
    }
    public Category readCategory(String title) {
        return categoryDataBase.read(title);
    }
    public int addProduct(final Product product) {
        return productDataBase.addProduct(product);
    }
    public int addCategory(final Category category) {
        return categoryDataBase.addCategory(category);
    }
    public void updateProduct(String updateColumnName, String newValue, String searchColumnName, String searchValue) {
        productDataBase.update(updateColumnName, newValue, searchColumnName, searchValue);
    }
    public void updateCategory(String updateColumnName, String newValue, String searchColumnName, String searchValue) {
        categoryDataBase.update(updateColumnName, newValue, searchColumnName, searchValue);
    }
    public void deleteProduct(final String title) {
        productDataBase.delete(title);
    }
    public void deleteCategory(final String title) {
        categoryDataBase.delete(title);
    }
}