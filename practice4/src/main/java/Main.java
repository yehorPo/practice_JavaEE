public class Main {
    public static void main(String[] args) {
        StoreDataBase storeDataBase = StoreDataBase.getStoreDataBase();
        Category category1 = new Category("Alcohol");

        storeDataBase.addCategory(category1);
        System.out.println(storeDataBase.readCategory(category1.getName()));
        storeDataBase.updateCategory("name", "Diary", "name", category1.getName());
        storeDataBase.deleteCategory(category1.getName());
        Category category2 = new Category("Alcohol");
        storeDataBase.addCategory(category2);
        Product product = new Product("Revo",
                1488, 4, category2.getName());
        storeDataBase.addProduct(product);
        System.out.println(storeDataBase.readProduct(product.getName()));

        storeDataBase.updateProduct("name", "Pivo", "name", product.getName());
        System.out.println(storeDataBase.readProduct(product.getName()));
        storeDataBase.deleteProduct(product.getName());
        System.out.println(storeDataBase.readProduct(product.getName()));
    }
}