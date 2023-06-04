package examenMatutino;

public class Discount {
    private String id;
    private int discount;

    public Discount(String id, int discount) {
        this.id = id;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setId(String id) {
        this.id = id;
    }

}
