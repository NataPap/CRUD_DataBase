package program;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner in=new Scanner((System.in));
    public static void main(String[] args) {
        String strConn="jdbc:mariadb://localhost:3306/vpd912";
        insert(strConn);
        select(strConn);
        update(strConn);
        select(strConn);
        delete(strConn);
        select(strConn);
    }

    private static void insert(String strConn){
        try(Connection con= DriverManager.getConnection(strConn,"root",""))
        {
            System.out.println("Connection is good");
            String query="insert into `products` (`name`, `price`, `description`)"+
                    "values (?,?,?);";
            try(PreparedStatement ps = con.prepareStatement(query)) {
                String name, description;
                double price;
                System.out.print("Enter name: ");
                name = in.nextLine();
                System.out.print("Enter price: ");
                price = Double.parseDouble(in.nextLine());
                System.out.print("Enter description: ");
                description = in.nextLine();

                ps.setString(1, name);
                ps.setBigDecimal(2, new BigDecimal(price));
                ps.setString(3,description);

                int rows=ps.executeUpdate();
                System.out.println("update rows:"+rows);
            }
        } catch (Exception ex) {
            System.out.println("Error connection");
        }
    }

    private static void select(String strConn){
        try(Connection con= DriverManager.getConnection(strConn,"root",""))
        {
            System.out.println("Connection is good");
            String query="select * from products ";
            try(PreparedStatement ps = con.prepareStatement(query)) {


                ResultSet resultSet=ps.executeQuery();
                while (resultSet.next())
                {
                    System.out.print("{ id="+resultSet.getInt("id")+",");
                    System.out.print("name="+resultSet.getString("name")+",");
                    System.out.print("price="+resultSet.getBigDecimal("price")+",");
                    System.out.println("description="+resultSet.getString("description")+"}");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error connection");
        }
    }

    private static void delete(String strConn) {
        try(Connection con = DriverManager.getConnection(strConn, "root", ""))
        {
            System.out.println("Connection is good");
            String query = "DELETE FROM products " +
                    "WHERE id = ?;";
            try(PreparedStatement ps = con.prepareStatement(query)) {
                int id;
                System.out.print("Enter id: ");
                id = Integer.parseInt(in.nextLine());

                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                System.out.println("Update rows: " +rows);
            }
            catch(Exception ex) {
                System.out.println("error statment");
            }
        }
        catch(Exception ex) {
            System.out.println("Error connection");
        }
    }

    private static Product getById(String strConn, int id) {
        try(Connection con = DriverManager.getConnection(strConn, "root", ""))
        {
            System.out.println("Connection is good");
            String query = "SELECT * FROM products where id = ?";
            try(PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next())
                {
                    Product p = new Product();
                    p.setId(resultSet.getInt("id"));
                    p.setName(resultSet.getString("name"));
                    p.setPrice(resultSet.getDouble("price"));
                    p.setDescription(resultSet.getString("description"));
                    return p;
                }
            }
            catch(Exception ex) {
                System.out.println("erro statment");
            }
        }
        catch(Exception ex) {
            System.out.println("Error connection");
        }
        return null;
    }

    private static void update(String strConn) {
        try(Connection con = DriverManager.getConnection(strConn, "root", ""))
        {
            System.out.println("Connection is good");
            String query = "UPDATE products SET name = ?, price=?, description=? " +
                    "WHERE id = ?;";
            try(PreparedStatement ps = con.prepareStatement(query)) {
                int id;
                System.out.print("Enter id: ");
                id = Integer.parseInt(in.nextLine());
                Product p = getById(strConn, id);
                System.out.print("Enter new name: ");
                String tmp = in.nextLine();
                if(tmp != null && !tmp.isEmpty()) {
                    p.setName(tmp);
                }
                System.out.print("Enter price: ");
                tmp = in.nextLine();
                if(tmp != null && !tmp.isEmpty()) {
                    p.setPrice(Double.parseDouble(tmp));
                }
                System.out.print("Enter description: ");
                tmp = in.nextLine();
                if(tmp != null && !tmp.isEmpty()) {
                    p.setDescription(tmp);
                }

                ps.setString(1, p.getName());
                ps.setDouble(2, p.getPrice());
                ps.setString(3,p.getDescription());
                ps.setInt(4, id);
                int rows = ps.executeUpdate();
                System.out.println("Update rows: " +rows);
            }
            catch(Exception ex) {
                System.out.println("Error statment");
            }
        }
        catch(Exception ex) {
            System.out.println("Error connection");
        }
    }
}
