package app.services;

import app.models.Products;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    // header : id , name , brand , price , ram , balance

    private String pathToProductsTable = "/mnt/data/Projects/Pixel-Store/tables/Products.csv";

    //Product Insertion
    public void registerProduct(Products product, String productDesc) {
        String[] item = {String.valueOf(product.getId()) , product.getName() , product.getBrand() ,
                String.valueOf(product.getPrice()) , String.valueOf(product.getRam()) ,
                String.valueOf(product.getBalance())};

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable , true))){
            writer.writeNext(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String descDir = "src/main/resources/FileDescs/" + product.getId() + ".txt";
        File txtFile = new File(descDir);
        try {
            txtFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(descDir));
            writer.write(productDesc);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Registered a new product");
    }

    //Product Deletion
    public void deleteProduct(int ID) {
        List<String[]> allRows = List.of();
        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[0].equals(String.valueOf(ID))) {
                    allRows.remove(row);
                    System.out.println("Removed the product with id: " + ID);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable))){
            writer.writeAll(allRows);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Find By Name
    public ArrayList<Products> findByName(String searchWord) {
        List<String[]> allRows;
        ArrayList<Products> results = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[1].toLowerCase().contains(searchWord.toLowerCase())){
                    results.add(new Products(Integer.parseInt(row[0]) , row[1] ,
                            row[2] , Integer.parseInt(row[3]) , Integer.parseInt(row[4]) ,
                            Integer.parseInt(row[5])));
                }
            }

            System.out.println("Found " + results.size() + " results for " + searchWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    //Find By ID
    public ArrayList<Products> findByID(int id) {
        List<String[]> allRows;
        ArrayList<Products> results = null;

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[0].equals(String.valueOf(id))){
                    results.add(new Products(Integer.parseInt(row[0]) , row[1] ,
                            row[2] , Integer.parseInt(row[3]) , Integer.parseInt(row[4]) ,
                            Integer.parseInt(row[5])));
                    break;
                }
            }
            System.out.println("Found " + allRows.size() + " results for id " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;

    }

    //Filter by parameters
    public ArrayList<Products> filterResults(ArrayList<String> brands,
                                             int priceBoundBottom , int priceBoundUp,
                                             int ramBoundBottom , int ramBoundUp) {
        List<String[]> allRows;
        ArrayList<Products> results = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();
            String brand;

            //Brand check
            if (!brands.isEmpty()) {
                for (String[] row : allRows){
                    for (String filter : brands) {
                        if (filter.equals(row[2])) {
                            results.add(new Products(Integer.parseInt(row[0]) , row[1], row[2],
                                    Integer.parseInt(row[3]) , Integer.parseInt(row[4]) , Integer.parseInt(row[5])));
                            break;
                        }
                    }
                }
            }

            //Price check
            if (priceBoundBottom != -1 && priceBoundUp != -1) {
                if (!results.isEmpty()) {
                    for (Products filteredProduct : results) {
                        if (!(priceBoundBottom < filteredProduct.getPrice() &&
                                filteredProduct.getBalance() < priceBoundUp)) results.remove(filteredProduct);
                    }
                } else {
                    for (String[] row : allRows) {
                        if (priceBoundBottom < Integer.parseInt(row[3])
                                && Integer.parseInt(row[3]) < priceBoundUp)
                            results.add(new Products(Integer.parseInt(row[0]), row[1], row[2],
                                    Integer.parseInt(row[3]), Integer.parseInt(row[4]), Integer.parseInt(row[5])));
                    }
                }
            }

            //RAM check
            if(ramBoundBottom != -1 && ramBoundUp != -1) {
                if (!results.isEmpty()) {
                    for (Products filteredProduct : results) {
                        if (!(ramBoundBottom < filteredProduct.getRam()
                                && filteredProduct.getRam() < ramBoundUp)) results.remove(filteredProduct);
                    }
                } else {
                    for (String[] row : allRows) {
                        if (ramBoundBottom < Integer.parseInt(row[4])
                                && Integer.parseInt(row[4]) < ramBoundUp)
                            results.add(new Products(Integer.parseInt(row[0]), row[1], row[2],
                                    Integer.parseInt(row[3]), Integer.parseInt(row[4]), Integer.parseInt(row[5])));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    //update balance
    public void updateBalance(int newBalance , int productID){
        List<String[]> allRows = null;

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) if (row[0].equals(String.valueOf(productID))) {
                row[5] = String.valueOf(newBalance);
                break;
            }
            System.out.println("Updated the product balance");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable)) ) {
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update price
    public void updatePrice(int newPrice , int productID){
        List<String[]> allRows = null;

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) if (row[0].equals(String.valueOf(productID))) {
                row[3] = String.valueOf(newPrice);
                break;
            }
            System.out.println("Updated the product price");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable)) ) {
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update name
    public void updateName(String newName , int productID){
        List<String[]> allRows = null;

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) if (row[0].equals(String.valueOf(productID))) {
                row[1] = newName;
                break;
            }
            System.out.println("Updated the product name");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable)) ) {
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch All products
    public ArrayList<Products> fetchAll(){
        List<String[]> allRows;
        ArrayList<Products> allProducts = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                allProducts.add(new Products(Integer.parseInt(row[0]) , row[1] , row[2],
                        Integer.parseInt(row[3]) , Integer.parseInt(row[4]) , Integer.parseInt(row[5])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    public ArrayList<Products> fetchLowBal(){
        List<String[]> allRows;
        ArrayList<Products> lowBal = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (Integer.parseInt(row[5]) < 6) lowBal.add(new Products(Integer.parseInt(row[0]) , row[1] , row[2],
                        Integer.parseInt(row[3]) , Integer.parseInt(row[4]) , Integer.parseInt(row[5])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lowBal;
    }

    public boolean idIsUnique(int ID){
        List<String[]> allRows = List.of();
        try(CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))) {allRows = reader.readAll();}
        catch (Exception e) {e.printStackTrace();}
        boolean isUnique = true;

        for (String[] row : allRows) {
            if (row[0].equals(String.valueOf(ID))) isUnique = false;
        }

        return isUnique;
    }


}
