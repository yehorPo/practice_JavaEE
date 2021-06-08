import lombok.Builder;

@Builder
public class Category {
    private String name;
    public Category(final String name){
        this.name = name;
    }
   @Override
    public String toString() {
        return "Name: " + name + "\n";
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}