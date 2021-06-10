import java.sql.*;

public class ProductDataBase {
    private final Connection connection;
    public ProductDataBase(final Connection connection) {
        this.connection = connection;
    }
    public void update(String s, String inputName, String searchBy, String val) {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "update 'products' set " + s + " = '" + inputName + "' where " + searchBy +
                            " = '" + val + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Product read(String name) {
        try (final Statement statement = connection.createStatement()) {
            final String    query     = "SELECT * FROM 'products' WHERE name = '" + name + "'";
            final ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next() ?
                    new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("number"),
                            resultSet.getString("category"))
                    : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(final String title) {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("delete from 'products' where title = '%s'", title));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int addProduct(final Product product) {
        try (PreparedStatement insertStatement = connection.prepareStatement(
                "insert into 'products'('name', 'price', 'number', 'category') " +
                        "values (?, ?, ?, ?)")) {
            insertStatement.setString(1, product.getName());
            insertStatement.setDouble(2, product.getPrice());
            insertStatement.setInt(3, product.getNumber());
            insertStatement.setString(4, product.getCategory());
            insertStatement.execute();
            final ResultSet result = insertStatement.getGeneratedKeys();
            return result.getInt("genID");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}