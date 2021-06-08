import java.util.Set;

public class RangeForProducts {
    private Set<Integer> productsList;
    private String query;
    private Double minPrice;
    private Double maxPrice;
    private Integer minNumber;
    private Integer maxNumber;
    public Set<Integer> getProductsList() {
        return productsList;
    }
    public String getQuery() {
        return query;
    }
    public Double getMinPrice() {
        return minPrice;
    }
    public Double getMaxPrice() {
        return maxPrice;
    }
    public Integer getMinNumber(){
        return minNumber;
    }
    public Integer getMaxNumber() {
        return maxNumber;
    }
    public void setProductsList(Set<Integer> productsList) {
        this.productsList = productsList;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }
    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }
}