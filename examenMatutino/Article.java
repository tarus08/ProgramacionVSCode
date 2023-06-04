package examenMatutino;

public class Article {
    private int id;
    private String name;
    private int price;
    private Suppliers suppliers;

    protected Article(int id, String name, Suppliers suppliers, int price) {
        this.id = id;
        this.name = name;
        this.suppliers = suppliers;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Suppliers getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Suppliers suppliers) {
        this.suppliers = suppliers;
    }

}