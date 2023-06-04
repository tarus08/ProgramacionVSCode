package examenMatutino;

public class Buyer {
    private String name;
    private String password;
    private String nif;
    private String address;

    protected Buyer(String nif, String name, String password, String address) {
        this.nif = nif;
        this.name = name;
        this.password = password;
        this.address = address;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
