import java.sql.*;

public class CategoryDataBase {
    private final Connection connection;
    CategoryDataBase(final Connection connection) {
        this.connection = connection;
    }
    public void update(String s, String input, String searchBy, String val){
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate("update 'categories' set " + s + " = '" + input +
                    "' where " + searchBy + " = '" + val + "'");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update table!", e);
        }
    }
    public Category read(String name) {
        try (final Statement statement = connection.createStatement()) {
            final String    query     = "SELECT * FROM 'categories' WHERE name = '" + name + "'";
            final ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next() ? new Category(
                    resultSet.getString("name")): null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(final String name) {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete from 'categories' where name = '" + name + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int addCategory(final Category category) {
        try (PreparedStatement insertStatement = connection.prepareStatement(
                "insert into 'categories'('name') values (?)")) {
            insertStatement.setString(1, category.getName());
            insertStatement.execute();
            final ResultSet result = insertStatement.getGeneratedKeys();
            return result.getInt("categoryId()");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
