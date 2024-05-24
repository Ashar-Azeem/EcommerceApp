import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class CRUD {
    Connection connection;

    CRUD() {
        connection = null;
    }

    public Connection openConnection() {
        try {
            String url = "jdbc:mysql://Ashar:3306/db";
            String username = "mainConnection";
            String password = "azeem786";
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ensureConnectionIsOpen() {
        try {
            if (connection == null || connection.isClosed()) {
                openConnection();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Games> getAllGames() {
        ArrayList<Games> allGames = new ArrayList<>();
        ensureConnectionIsOpen();
        try {
            String query = "SELECT * FROM games";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and populate the list
            while (resultSet.next()) {
                int game_id = resultSet.getInt("game_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int price = resultSet.getInt("price");
                int availability = resultSet.getInt("availability");
                String imageLocation=resultSet.getString("image");

                Games games = new Games(game_id, title, genre, price, availability,imageLocation);
                allGames.add(games);
            }

            resultSet.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allGames;
    }

    public Games getGame(String title) {
        ensureConnectionIsOpen();
        try {
            String query = "SELECT * FROM games WHERE title=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int game_id = resultSet.getInt("game_id");
                String title1 = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int price = resultSet.getInt("price");
                int availability = resultSet.getInt("availability");
                String imageLocation=resultSet.getString("image");

                Games games = new Games(game_id, title1, genre, price, availability,imageLocation);
                resultSet.close();
                statement.close();
                closeConnection();
                return games;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return null;
    }

    public User getUser(String email) {
        ensureConnectionIsOpen();

        try {
            String query = "SELECT * FROM user WHERE email=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String email1 = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(user_id, name, email1, password);

                resultSet.close();
                statement.close();
                closeConnection();
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return null;
    }

    //cart ..
    public void buyingGame(int user_id, int game_id, int price) {
        ensureConnectionIsOpen();
        String query = "INSERT INTO cart (game_id, user_id, price) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, game_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setInt(3, price);

            preparedStatement.executeUpdate();

            closeConnection();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helping method for getUserOrder, used for insertion in the user_order table which will further be
    //used for payment.
    public int insertUserOrder(int user_id, int total, Date date) {
        ensureConnectionIsOpen();
        String query = "INSERT INTO user_order(user_id, total, order_date) VALUES (?, ?, ?)";
        String query2 = "SELECT order_id FROM user_order WHERE order_id = LAST_INSERT_ID()";
        try {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, total);
            preparedStatement.setDate(3, sqlDate);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            ResultSet result = preparedStatement2.executeQuery();

            if (result.next()) {
                int order_id = result.getInt("order_id");
                result.close();
                preparedStatement2.close();
                return order_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insertionIntoOrderDetails(int order_id, String title, int price) {
        String query = "INSERT INTO order_details(order_id, title,price) VALUES (?, ?,?)";
        ensureConnectionIsOpen();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, order_id);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, price);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // This is called everytime when user wants to check out
    //This function will be executed if user proceeds to check out with their order, working
    //is that it inserts the whole total and date into UserOrder table and with that dynamically returns
    //that whole order along with total and list of cart which will have all the items user bought and along
    //with each items detail.
    //This will be used to display the whole order when user wants to check out.
    public UserOrder getUserOrder(User user) {
        ArrayList<Cart> fullCart = new ArrayList<>();
        ensureConnectionIsOpen();
        String query = "SELECT user_id,SUM(price) AS total FROM cart WHERE user_id = ? GROUP BY user_id";
        String query2 = "SELECT cart_id,name,title,price,image FROM (cart NATURAL JOIN games) NATURAL JOIN user WHERE user_id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.user_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int total = resultSet.getInt("total");
                Date date = new Date();
                int order_id = insertUserOrder(user.user_id, total, date);
                resultSet.close();
                statement.close();
                PreparedStatement statement1 = connection.prepareStatement(query2);
                statement1.setInt(1, user.user_id);
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()) {
                    int cart_id = resultSet1.getInt("cart_id");
                    String title = resultSet1.getString("title");
                    String name1 = resultSet1.getString("name");
                    int price = resultSet1.getInt("price");
                    String imgLoc=resultSet1.getString("image");

                    insertionIntoOrderDetails(order_id, title, price);

                    Cart cart = new Cart(cart_id, name1, title, price,imgLoc);
                    fullCart.add(cart);
                }
                resultSet1.close();
                statement1.close();
                UserOrder order = new UserOrder(user.name, total, date, fullCart, order_id);

                String table = "cart";
                String deleteQuery = "DELETE FROM " + table;
                PreparedStatement statement2 = connection.prepareStatement(deleteQuery);
                statement2.executeUpdate();

                statement2.close();
                closeConnection();
                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean loginUser(String email,String password){
        String query="SELECT * FROM user WHERE email=? AND password =?";
        try{
            ensureConnectionIsOpen();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean loginAdmin(String email,String password){
        String query="SELECT * FROM admin WHERE email=? AND password =?";
        try{
            ensureConnectionIsOpen();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getNoOfItemInCart(User user, Games game){
        ensureConnectionIsOpen();
        String query = "SELECT COUNT(game_id) AS total FROM cart WHERE game_id = ? AND user_id = ? GROUP BY game_id";
        try{
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setInt(1,game.gameID);
            statement.setInt(2,user.user_id);
            ResultSet result=statement.executeQuery();
            if(result.next()){
                int totalInCart=result.getInt("total");
                result.close();
                statement.close();
                closeConnection();
                return totalInCart;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public void insertingGame(String title, String genre, int price, int quantity,String loc) {

        if (price > 0 && quantity > 0) {
            ensureConnectionIsOpen();

            if (getGame(title) == null) {
                String query = "INSERT INTO games (title, genre, price,availability,image) VALUES (?, ?, ?,?,?)";
                ensureConnectionIsOpen();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, genre);
                    preparedStatement.setInt(3, price);
                    preparedStatement.setInt(4, quantity);
                    preparedStatement.setString(5, loc);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                ensureConnectionIsOpen();
                String updateSQL = "UPDATE games SET availability = availability + ? WHERE title=?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                    preparedStatement.setString(2, title);
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        } else {
            System.out.println("Insert the correct information");
        }
    }

    public void updatingPrices(String title, int updatedPrice) {
        if (getGame(title) != null) {
            ensureConnectionIsOpen();
            String updateSQL = "UPDATE games SET price = ? WHERE title=?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                preparedStatement.setString(2, title);
                preparedStatement.setInt(1, updatedPrice);
                preparedStatement.executeUpdate();

                preparedStatement.close();
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No such title found!");
        }
    }


    public User registerUser(String email, String password, String name) {
        if (getUser(email) == null ) {
            String query = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
            ensureConnectionIsOpen();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);

                preparedStatement.executeUpdate();
                preparedStatement.close();
                closeConnection();

                return getUser(email);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean deleteUser(String email) {
        if (getUser(email) != null) {
            ensureConnectionIsOpen();
            String deleteSQL = "DELETE FROM user WHERE email = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                preparedStatement.setString(1, email);
                int result = preparedStatement.executeUpdate();
                preparedStatement.close();
                closeConnection();
                return result != 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No such user found");
        }
        return false;
    }

    public boolean ChangePassword(String email, String oldPassword, String newPassword) {
        User user = getUser(email);
        if (user != null) {
            if (user.password.equals(oldPassword)) {
                ensureConnectionIsOpen();
                String updateSQL = "UPDATE user SET password = ? WHERE email=?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(1, newPassword);
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    closeConnection();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Old Password doesn't match");
            }
        } else {
            System.out.println("User not found");
        }
        return false;
    }

    public void review(User user, Games game, int score) {
        String query = "INSERT INTO reviews (user_id, game_id, rating) VALUES (?, ?, ?)";
        ensureConnectionIsOpen();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.user_id);
            preparedStatement.setInt(2, game.gameID);
            preparedStatement.setInt(3, score);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean userReview(Games game,User user ){
        String query="SELECT * FROM reviews WHERE user_id=? AND game_id=?";
        try{
            ensureConnectionIsOpen();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.user_id);
            preparedStatement.setInt(2, game.gameID);
            ResultSet result=preparedStatement.executeQuery();
            if(result.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return false;
    }
    public Review getReview(Games game) {
        String query = "SELECT count(game_id) As noOfReviews,avg(rating) AS Rating from reviews where game_id=?";
        try {
            ensureConnectionIsOpen();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, game.gameID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int noOfReviews = resultSet.getInt("noOfReviews");
                int rating = Math.round(resultSet.getFloat("Rating"));
                resultSet.close();
                statement.close();
                closeConnection();
                Review review = new Review(noOfReviews, game.title, rating);
                return review;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertRecommendation(Games game, User user, String comment) {
        String query = "INSERT INTO recommendations (user_id, game_id, comment) VALUES (?, ?, ?)";
        try {
            ensureConnectionIsOpen();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.user_id);
            preparedStatement.setInt(2, game.gameID);
            preparedStatement.setString(3, comment);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Recommendation> getRecommendation(Games game) {
        ArrayList<Recommendation> gameRecommendation = new ArrayList<>();
        try {
            ensureConnectionIsOpen();
            String query = "SELECT r.recommendation_id,CASE WHEN r.user_id IS NOT NULL THEN u.name ELSE 'anonymous' END AS name,r.comment FROM recommendations r LEFT JOIN  user u on r.user_id=u.user_id WHERE game_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, game.gameID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int recommendation_id = resultSet.getInt("recommendation_id");
                String comment = resultSet.getString("comment");
                String name = resultSet.getString("name");

                Recommendation recom = new Recommendation(recommendation_id, name, comment);
                gameRecommendation.add(recom);
            }

            resultSet.close();
            statement.close();
            closeConnection();
            return gameRecommendation;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateQuantity(Games game) {
        String updateSQL = "UPDATE games SET availability = availability - 1 WHERE title=?";
        if (game.availability > 0) {
            try {
                ensureConnectionIsOpen();
                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                preparedStatement.setString(1, game.title);
                int result = preparedStatement.executeUpdate();
                preparedStatement.close();
                closeConnection();
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Out of stock!");
        }
        return 0;
    }

    public void deleteOrder(UserOrder order) {
        try {
            ensureConnectionIsOpen();
            String deleteQuery = "DELETE FROM user_order WHERE order_id=?";
            PreparedStatement statement2 = connection.prepareStatement(deleteQuery);
            statement2.setInt(1, order.order_id);
            statement2.executeUpdate();
            statement2.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void payment(User user, UserOrder order, Date date) {

        String query = "INSERT INTO payment (user_id, amount, payment_date) VALUES (?, ?, ?)";
        try {
            ensureConnectionIsOpen();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.user_id);
            preparedStatement.setInt(2, order.total);
            preparedStatement.setDate(3, sqlDate);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            closeConnection();
            //Updating quantity of Games on purchase
            ArrayList<Cart> allGames = order.fullCart;
            for (Cart item : allGames) {
                updateQuantity(getGame(item.title));
            }
            //Delete that row in table for which the payment is complete
            deleteOrder(order);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Cart> getOrderDetails(int orderId, User user) {
        ArrayList<Cart> fullcart = new ArrayList<>();
        String query = "SELECT detail_id,title,price,image FROM order_details  Natural JOIN games WHERE order_id=?";
        try {
            ensureConnectionIsOpen();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int detail_id = resultSet.getInt("detail_id");
                String title = resultSet.getString("title");
                int price = resultSet.getInt("price");
                String loc=resultSet.getString("image");
                Cart cart = new Cart(detail_id, user.name, title, price,loc);
                fullcart.add(cart);
            }

            return fullcart;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<UserOrder> getAllOrders(User user) {
        ArrayList<Cart> fullCart;
        ArrayList<UserOrder> allOrders = new ArrayList<>();
        String query = "Select order_id,total,order_date From user_order WHERE user_id=?";
        try {
            ensureConnectionIsOpen();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.user_id);
            ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int order_id = resultSet.getInt("order_id");
                    int total = resultSet.getInt("total");
                    java.sql.Date sqlDate = resultSet.getDate("order_date");
                    java.util.Date javaDate = new java.util.Date(sqlDate.getTime());
                    fullCart = getOrderDetails(order_id, user);
                    UserOrder order = new UserOrder(user.name, total, javaDate, fullCart, order_id);
                    allOrders.add(order);
                }
                closeConnection();
                //Returns all the unpaid orders list
                return allOrders;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

class Games{
    int gameID;
    String title;
    String genre;
    int price;
    int availability;
    String imageLocation;

    public Games(int gameID, String title, String genre, int price, int availability,String imageloc) {
        this.gameID = gameID;
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.availability = availability;
        this.imageLocation=imageloc;
    }

    @Override
    public String toString() {
        return
                "gameID=" + gameID +
                ", title='" + title +
                ", genre='" + genre +
                ", price=" + price +
                ", availability=" + availability+
                ", loc="+imageLocation;
    }
}
class User{
    int user_id;
    String name;
    String email;
    String password;

    public User(int user_id, String name, String email, String password) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Override
    public String toString() {
        return "user_id=" + user_id +
                ", name='" + name +
                ", email='" + email +
                ", password='" + password ;
    }
}
class Cart{
    int cart_id;
    String name;
    String title;
    int price;
    String loc;

    public Cart(int cart_id, String name, String title, int price,String loc) {
        this.cart_id = cart_id;
        this.name = name;
        this.title = title;
        this.price = price;
        this.loc=loc;
    }

    @Override
    public String toString() {
        return "cart_id=" + cart_id +
                ", name='" + name +
                ", title='" + title +
                ", price=" + price ;
    }
}

class UserOrder{
    String name;
    int order_id;
    int total;
    Date orderDate;
    ArrayList<Cart> fullCart;

    public UserOrder(String name, int total, Date orderDate, ArrayList<Cart> fullCart,int order_id) {
        this.name = name;
        this.total = total;
        this.orderDate = orderDate;
        this.fullCart = fullCart;
        this.order_id=order_id;
    }

    @Override
    public String toString() {
        return "name='" + name +
                ", total=" + total +
                ", orderDate=" + orderDate +
                ", fullCart=" + fullCart  +
                ", order ID+ "+ order_id+"\n";
    }
}
class Payment{
    int payment_Id;
    String name;
    int amount ;
    Date paymentDate;

    public Payment(int payment_Id, String name, int amount, Date paymentDate) {
        this.payment_Id = payment_Id;
        this.name = name;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "payment_Id=" + payment_Id +
                ", name='" + name +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate ;
    }
}
class Recommendation{
    int recommendation_id;
    String name;
    String comment;

    public Recommendation(int recommendation_id, String name, String comment) {
        this.recommendation_id = recommendation_id;
        this.name = name;
        this.comment=comment;
    }

    @Override
    public String toString() {
        return "recommendation_id=" + recommendation_id +
                ", name='" + name +
                ", Comment= "+comment;
    }
}

class Review{
    int numberOfReviews;
    String title;
    int rating ;


    public Review(int numberOfReviews, String title, int rating) {
        this.title = title;
        this.rating = rating;
        this.numberOfReviews=numberOfReviews;
    }

    @Override
    public String toString() {
        return "Title= " + title +
                " , Rating= " + rating +
                " , No of Reviews = "+numberOfReviews ;
    }
}