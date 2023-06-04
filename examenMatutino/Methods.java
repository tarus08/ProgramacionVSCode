package examenMatutino;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Methods {

    private static Scanner scanner = new Scanner(System.in);
    // connect to the database
    String username = "root";
    String passwordDB = "admin1234_";
    String url = "jdbc:mysql://localhost/examen1";
    Connection connection = null;

    Map<String, Integer> articleMapCount = new HashMap<>();
    ResultSet resultSetArticles;

    public void connectDB() throws SQLException {

        connection = DriverManager.getConnection(url, username, passwordDB);
    }

    public void logToBuy() throws SQLException {

        try {

            connectDB();
            String sql = "SELECT * FROM CLIENTES WHERE NIF = ? AND PASSWORD = ?";

            // ask for the cif and password of the client
            System.out.println("Enter the nif of the client: ");
            int nif = scanner.nextInt();
            System.out.println("Enter the password of the client: ");
            String password = scanner.next();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, nif);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                System.out.println("You successfully logged in!");
                String usernameName = resultSet.getString("NOMBRE");
                System.out.println("Hi " + usernameName + "!");
                System.out.println();
                addToCart();

            } else {

                System.out.println("Wrong cif or password!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToCart() throws SQLException {
        String sql = "SELECT * FROM ARTICULOS";
        PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        resultSetArticles = statement.executeQuery();

        System.out.println("This is our catalog:");
        System.out.println("\nId \tName \t\tPrice");
        while (resultSetArticles.next()) {
            // get data
            int id = resultSetArticles.getInt(1);
            String name = resultSetArticles.getString(2);
            int price = resultSetArticles.getInt(4);

            // show the data
            System.out.println(id + "\t" + name + "\t" + price);
        }

        String nameProduct = null;
        int quantity = 0;
        scanner.nextLine();

        do {
            System.out.println("\nEnter the name of the product you want to buy: ");
            nameProduct = scanner.nextLine();

            boolean exist = false;
            resultSetArticles.beforeFirst();
            while (resultSetArticles.next()) {
                String nameP = resultSetArticles.getString(2);
                if (nameP.equals(nameProduct)) {
                    exist = true;
                    break;
                }
            }

            if (exist) {
                System.out.println("How many units do you want to buy?");
                quantity = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                int currentQuantity = articleMapCount.getOrDefault(nameProduct, 0);
                if (quantity > 0 && currentQuantity + quantity <= 99) {
                    articleMapCount.put(nameProduct, currentQuantity + quantity);
                    System.out.println("\n" + quantity + " units of the product " + nameProduct
                            + " have been added to the cart !");
                } else if (quantity < 0 && currentQuantity + quantity >= 0) {
                    articleMapCount.put(nameProduct, currentQuantity + quantity);
                    System.out.println("Subtracted " + Math.abs(quantity) + " units of the product " + nameProduct);
                } else if (quantity == 0) {
                    System.out.println("This product won't be added to the cart!");
                }

                for (Map.Entry<String, Integer> entry : articleMapCount.entrySet()) {
                    System.out.println("Name of the product: " + entry.getKey() + ". Quantity: " + entry.getValue());
                }
            } else {
                System.out.println("\nThat product is not in our catalog!");
                if (nameProduct.isEmpty()) {
                    System.out.println("\nThis is your cart:");
                    for (Map.Entry<String, Integer> entry : articleMapCount.entrySet()) {
                        System.out
                                .println(
                                        "Name of the product: " + entry.getKey() + ". Quantity: " + entry.getValue());
                    }
                }
            }

        } while (!nameProduct.isEmpty());
        applyDiscount();
    }

    public void applyDiscount() throws SQLException {
        double totalPrice = 0.0;
        resultSetArticles.beforeFirst();

        System.out.println("\nEnter the discount (inicio, verano, rebaja, other)");
        String discountName = scanner.next();
        double discountAmount = 0.0;
        switch (discountName) {
            case "inicio":
                discountAmount = 0.9;
                break;
            case "verano":
                discountAmount = 0.95;
                break;
            case "rebaja":
                discountAmount = 0.75;
                break;
            case "other":
                System.out.println("Enter the discount amount: ");
                discountAmount = scanner.nextDouble();
                break;
        }

        // Specify the folder path
        String folderPath = "C:/Users/Mario/OneDrive - IES Luis Braille/1DAM/Programacion/ProgramacionVisual/facturas";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean folderCreated = folder.mkdirs();
            if (!folderCreated) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

        int ticketNumber = folder.listFiles().length + 1;
        String fileName = "factura" + ticketNumber + ".txt";
        File ticket = new File(folder, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ticket))) {

            writer.write(fileName + "\t\t" + LocalDateTime.now());
            writer.write("\n");
            writer.write("\n");
            writer.write("Articulo\t\t\tCantidad\tPrecio\t\tTotal");

            while (resultSetArticles.next()) {
                String nameP = resultSetArticles.getString(2);
                int quantity = articleMapCount.getOrDefault(nameP, 0);

                if (quantity > 0) {
                    double price = resultSetArticles.getDouble(4);
                    double articleTotal = price * quantity;
                    totalPrice += articleTotal;

                    // Print individual article details
                    writer.write("\n");
                    writer.write(nameP + "\t\t\t\t" + quantity + "\t\t\t" + price + "\t\t" + articleTotal);
                }
            }

            // Print the total and discount
            writer.write("\n");
            writer.write("\n");
            writer.write("Total: " + totalPrice + "\t\t" + "Descuento: " + discountAmount);

            double discountedTotal = totalPrice * discountAmount;

            writer.write("\n");
            writer.write("\n");
            writer.write("Total con descuento: " + discountedTotal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
