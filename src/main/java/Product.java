import lombok.Builder;
@Builder
public class Product {
    private final Integer id;
    private String name;
    private double price;
    private int number;
    private String category;
    public Product(final Integer id, final String name, final double price, final int number, final String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.number = number;
        this.category = category;
    }
    public Product(final String name, final double price, final int number, final String category) {
        this(null, name, price, number, category);
    }
    @Override
    public String toString() {
        return "ID: " + id +
                "\nName: " + name +
                "\nPrice: " + price +
                "\nNumber: " + number +
                "\nCategory: " + category + "\n";
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }
    public String getCategory() {
        return category;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}