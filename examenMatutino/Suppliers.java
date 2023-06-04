package examenMatutino;

public class Suppliers {
    private String name;
    private String address;
    private String nif;

    protected Suppliers(String nif, String name, String address) {
        this.address = address;
        this.name = name;
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public String getNif() {
        return nif;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNif(String cif) {
        this.nif = cif;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
