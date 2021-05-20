public class Main {
    public static void main(String[] args){

        Data d = new Data();

        new Worker(2, d);
        new Worker(1, d);
        new Worker(3, d);
        System.out.println("end of main...");
    }
}
