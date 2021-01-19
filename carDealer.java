import java.sql.*;
import java.util.*;

public class carDealer {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/cardealership?characterEncoding=latin1";

    //  Database credentials
    static final String USER = "";
    static final String PASS = "";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        Scanner keyboard = new Scanner(System.in);
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Please enter your ID:");
            int user = keyboard.nextInt();
            keyboard.nextLine();
            System.out.print("Please enter your Password:");
            String password = keyboard.nextLine();
            String sql = "SELECT SalesID, SalesPassword FROM SalesAccount WHERE SalesID = " + user + " AND SalesPassword = '" + password + "';";
            stmt = conn.createStatement();
            ResultSet rs0 = stmt.executeQuery(sql);

            while(!rs0.next())
            {
                    System.out.println("ID and Password entered incorrectly, program will now shut down");
                    System.exit(0);
            }
            rs0.close();

            System.out.println("Connected to database successfully...");
            boolean loop = true;
            do {
                System.out.println("You now have access to the Car Inventory Database. Please Select an option from our list of features or type 0 to exit");
                System.out.println("1. Input a new vehicle type to database");
                System.out.println("2. Input Vehicle into Database");
                System.out.println("3. Delete ('sell') Vehicle From Database");
                System.out.println("4. List all Vehicles");
                System.out.println("5. List Available Vehicles");
                System.out.println("6. Search Vehicle by VIN");
                System.out.println("7. Check availability by Make and Model");
                System.out.println("8. Update Order Status");
                System.out.println("9. Add sales account");
                System.out.println("10. Remove sales account");

                //Choose Vehicle Selection
                int selection = keyboard.nextInt();

                if (selection == 0) {
                    System.exit(0);
                }
                switch (selection) {
                    case 2:

                        //Assuming vehicle exists in GenericCar
                        System.out.println("Enter Vehicle VIN:");
                        String VIN1 = keyboard.next();

                        System.out.println("Enter the Generic Car ID");
                        int genCarID = keyboard.nextInt();

                        System.out.println("Enter minimum price:");
                        int minPrice = keyboard.nextInt();

                        System.out.println("Enter the color of the vehicle:");
                        String color = keyboard.next();

                        System.out.println("Enter the condition of the car:");
                        String cond = keyboard.next();

                        System.out.println("Enter the mileage of the car:");
                        int mileage = keyboard.nextInt();

                        //years on lot is default set to 0 so keep at 0 here


                        stmt = conn.createStatement();

                        String sql1 = "INSERT INTO CarOnLot VALUES ('" + VIN1 + "', " + genCarID + ", " + minPrice + ", '" + color + "', '" + cond + "', " + mileage + ", 0);";

                        stmt.executeUpdate(sql1);

                        break;
                    case 3:
                        System.out.println("Please enter the VIN of the vehicle that will be deleted from the system");
                        String VINDelete = keyboard.next();
                        stmt = conn.createStatement();
                        ResultSet rs00 = stmt.executeQuery("SELECT GenericCarID FROM CarOnLot WHERE VIN = '" + VINDelete + "';");
                        int identification = 0;
                        while(rs00.next()){
                            identification = rs00.getInt("GenericCarID");
                        }
                        rs00.close();
                        String sql2 = "UPDATE GenericCar, CarOnLot SET Quantity = Quantity-1 WHERE GenericCar.GenericCarID = CarOnLot.GenericCarID AND VIN = '" + VINDelete + "';";
                        stmt.executeUpdate(sql2);
                        sql2 = "DELETE FROM CarOnLot WHERE VIN ='" + VINDelete + "';";
                        stmt.executeUpdate(sql2);

                        if(identification == 0){
                            System.out.println("Generic Car does not exist");
                        }
                        else{
                            System.out.println("Enter the owner name: ");
                            keyboard.nextLine();
                            String ownerName = keyboard.nextLine();
                            System.out.println("Enter date of sale: ");
                            String dateOfSale = keyboard.nextLine();
                            sql2 = "INSERT INTO SoldCar VALUES ('" + VINDelete + "', " + identification + ", '" + ownerName + "', '" + dateOfSale + "');";
                            stmt.executeUpdate(sql2);
                            System.out.println("Vehicle successfully deleted");
                        }


                        break;

                    case 4:
                        String sql3 = "SELECT make, model, yearMade FROM genericCar";
                        stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql3);
                        while (rs.next()) {
                            //retrieve values by column name
                            String make1 = rs.getString("Make");
                            String model1 = rs.getString("Model");
                            int year1 = rs.getInt("YearMade");

                            System.out.print("Make: " + make1);
                            System.out.print(", Model: " + model1);
                            System.out.println(", Year: " + year1);
                        }
                        rs.close();
                        break;
                    case 5:
                        String sql4 = "SELECT make, model, yearMade, Quantity FROM genericCAR WHERE Quantity > 0";
                        stmt = conn.createStatement();
                        ResultSet rs2 = stmt.executeQuery(sql4);
                        while (rs2.next()) {
                            //retrieve values by column name
                            String make1 = rs2.getString("Make");
                            String model1 = rs2.getString("Model");
                            int year1 = rs2.getInt("YearMade");
                            int quantity = rs2.getInt("Quantity");

                            System.out.print("Make: " + make1);
                            System.out.print(", Model: " + model1);
                            System.out.print(", Year: " + year1);
                            System.out.println(", Quantity: " + quantity);
                        }
                        rs2.close();
                        break;
                    case 6:

                        System.out.println("Please enter the VIN of the vehicle that you would like to search for:");
                        String VINSearch = keyboard.next();

                        String sql5 = "SELECT * From GenericCar AS GC, CarOnLot AS C WHERE VIN = '" + VINSearch + "' AND C.GenericCarID = GC.GenericCarID";

                        ResultSet rs3 = stmt.executeQuery(sql5);

                        //retrieve values by column name
                        while (rs3.next()) {
                            String VIN = rs3.getString("VIN");
                            int carID = rs3.getInt("GenericCarID");
                            String make1 = rs3.getString("Make");
                            String model1 = rs3.getString("Model");
                            int year1 = rs3.getInt("YearMade");
                            int quantity = rs3.getInt("Quantity");
                            String description = rs3.getString("CarDescription");
                            String style = rs3.getString("BodyStyle");
                            int price = rs3.getInt("minPrice");
                            int onOrder = rs3.getInt("OnOrder");
                            String carColor = rs3.getString("color");
                            String condition = rs3.getString("carCondition");
                            int miles = rs3.getInt("mileage");
                            int years = rs3.getInt("YearsOnLot");

                            System.out.print("VIN: " + VIN);
                            System.out.print(", Generic Car ID: " + carID);
                            System.out.print(", Make: " + make1);
                            System.out.print(", Model: " + model1);
                            System.out.print(", Year: " + year1);
                            System.out.print(", Quantity: " + quantity);
                            System.out.print(", Description: " + description);
                            System.out.print(", Body Style: " + style);
                            System.out.println(", Min Price: " + price);
                            if (onOrder == 1)
                                System.out.print(", On Order: YES");
                            else
                                System.out.print(", On Order: NO");
                            System.out.print(", Car Color: " + carColor);
                            System.out.print(", Condition: " + condition);
                            System.out.print(", Mileage: " + miles);
                            System.out.println(", Years On Lot: " + years);
                        }
                        rs3.close();

                        break;
                    case 7:

                        System.out.println("Please enter the Make of the Vehicle being searched");
                        String make2 = keyboard.next();
                        System.out.println("Please enter the Model of the Vehicle being searched");
                        String model2 = keyboard.next();

                        String sql6 = "SELECT Make, Model FROM GenericCar WHERE Quantity > 0 AND Make ='" + make2 + "' AND Model = '" + model2 + "';";
                        stmt = conn.createStatement();
                        ResultSet rs4 = stmt.executeQuery(sql6);

                        int count = 0;
                        while (rs4.next() && count < 1) {
                            count++;
                            System.out.println("We have that make and model in stock!");
                            break;
                        }
                        if (count == 0) {
                            System.out.println("We do not have that vehicle in stock");
                        }
                        rs4.close();
                        break;
                    case 8:
                        System.out.println("Enter the Generic Car ID of the vehicle you are updating status of:");
                        int GenID = keyboard.nextInt();

                        System.out.println("Type 1 for On Order, type 0 for Received Order");
                        int order = keyboard.nextInt();
                        if (order > 1 || order < 0) {
                            System.out.println("wrong input");
                            System.exit(0);
                        }
                        String sql7 = "SELECT Quantity, OnOrder FROM GenericCar WHERE GenericCarID = " + GenID + ";";
                        stmt = conn.createStatement();
                        ResultSet rs7 = stmt.executeQuery(sql7);
                        while (rs7.next()) {
                            int orderStatus = rs7.getInt("OnOrder");
                            if (orderStatus == order) {
                                System.out.println("Cannot update order status because new order status = current order status");
                                break;
                            } else if (order == 0) {
                                String sqlElse = "UPDATE GenericCar SET Quantity = Quantity+1 WHERE GenericCarID = " + GenID + ";";
                                stmt = conn.createStatement();
                                stmt.executeUpdate(sqlElse);
                            }
                            System.out.println("Order Updated Successfully!");
                        }
                        sql7 = "UPDATE GenericCar SET OnOrder = " + order + " WHERE GenericCarID = " + GenID + ";";
                        stmt.executeUpdate(sql7);
                        rs7.close();
                        break;
                    case 9:

                        System.out.print("Please Enter your new User's USER ID:");
                        int userID = keyboard.nextInt();
                        keyboard.nextLine();
                        System.out.print("Please Enter your new User's PASSWORD:");
                        String pass = keyboard.nextLine();
                        System.out.print("Please Enter your new User's NAME:");
                        String Name = keyboard.nextLine();
                        System.out.print("Please Enter your new User's ADDRESS:");
                        String Address = keyboard.nextLine();
                        System.out.print("Please Enter your new User's PHONE NUMBER:");
                        String Number = keyboard.nextLine();
                        System.out.print("Please Enter your new User's EMAIL:");
                        String email = keyboard.nextLine();

                        String sql8 = "INSERT INTO SalesAccount VALUES (" + userID + ", '" + pass + "', '" + Name + "', '" + Address + "', '" + Number + "', '" + email + "');";
                        stmt = conn.createStatement();
                        stmt.executeUpdate(sql8);

                        break;
                    case 10:

                        System.out.println("Please Enter the User User ID of the user that will be deleted");
                        int DeleteUser = keyboard.nextInt();

                        String sql9 = "DELETE FROM SalesAccount WHERE SalesID =" + DeleteUser + ";";
                        stmt = conn.createStatement();
                        stmt.executeUpdate(sql9);

                        break;
                    case 1:

                        System.out.println("Please Enter the new Generic Car ID:");
                        int genID = keyboard.nextInt();

                        System.out.println("Please Enter the make of the new Vehicle:");
                        String newMake = keyboard.next();

                        System.out.println("Please Enter the Model of the new Vehicle;");
                        String newModel = keyboard.next();

                        System.out.println("Please Enter the year of the new Vehicle:");
                        int yearNew = keyboard.nextInt();
                        keyboard.nextLine();

                        System.out.print("Please Enter the vehicle description: ");
                        String desc = keyboard.nextLine();

                        System.out.println("Please Enter the body style of the new Vehicle:");
                        String body = keyboard.next();

                        System.out.println("Please Enter the amount of Vehicles we are adding");
                        int quant = keyboard.nextInt();

                        stmt = conn.createStatement();
                        String sql10 = "INSERT INTO GenericCar VALUES (" + genID + ", '" + newMake + "', '" + newModel + "', " + yearNew + ", '" + desc + "', '" + body + "', " + quant + ", 0);";
                        stmt.executeUpdate(sql10);

                        break;

                }
                int stop;
                do {
                    System.out.println("To Continue using the program enter 1, to stop enter 0");
                    stop = keyboard.nextInt();
                }while(stop < 0 || stop > 1);
                if(stop == 0)
                    loop = false;

            }while(loop);



        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end JDBC
