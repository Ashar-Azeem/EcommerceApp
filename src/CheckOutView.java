import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class CheckOutView extends JFrame{
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    CRUD database=new CRUD();
    Color customColor = Color.decode("#171D25");
    User user;
    UserOrder order;
    Font font=new Font("Helvetica", Font.BOLD, 23);
    Font font2=new Font("Helvetica", Font.PLAIN, 17);
    Font priceFont=new Font("Helvetica", Font.PLAIN, 22);
    CheckOutView(UserOrder order,User user){
        this.order=order;
        this.user=user;
        display();
    }
    CheckOutView(User user){
        order = database.getUserOrder(user);
        this.user = user;
        display();
    }
    public void display(){
        if(order==null){
            this.setTitle("Games E-Commerce");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel parentPanel = new JPanel(new BorderLayout());
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(customColor);
            JLabel title = new JLabel("                                         Check Out");
            title.setForeground(Color.WHITE);
            title.setBackground(customColor);
            title.setFont(font);
            JButton back = new JButton("  <  ");
            back.setBackground(customColor);
            back.setForeground(Color.WHITE);
            back.setFocusable(false);
            back.setBorder(BorderFactory.createEmptyBorder());
            back.setFont(font);
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new HomeView(user);
                    dispose();
                }
            });
            JLabel noOrders = new JLabel("                                              Cart is empty!");
            noOrders.setBackground(customColor);
            noOrders.setForeground(Color.WHITE);
            noOrders.setFont(font);

            titlePanel.add(back);
            titlePanel.add(title);
            JPanel middle=new JPanel(new BorderLayout());
            middle.setBackground(customColor);
            middle.add(noOrders,BorderLayout.CENTER);
            parentPanel.add(titlePanel,BorderLayout.NORTH);
            parentPanel.add(middle,BorderLayout.CENTER);
            this.getContentPane().add(parentPanel);
            this.setResizable(false);
            this.getContentPane().setBackground(customColor);
            this.setSize(screenWidth, screenHeight);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
        }
        else {
            JOptionPane optionPane = new JOptionPane("Your order will be saved", JOptionPane.CLOSED_OPTION);
            optionPane.setBackground(Color.decode("#EEEEEE"));
            JDialog customDialog = optionPane.createDialog(null, "Message");
            customDialog.getContentPane().setBackground(Color.decode("#EEEEEE"));

            JOptionPane optionPane1 = new JOptionPane("Order Placed", JOptionPane.CLOSED_OPTION);
            optionPane1.setBackground(Color.decode("#EEEEEE"));
            JDialog customDialog1 = optionPane1.createDialog(null, "Message");
            customDialog1.getContentPane().setBackground(Color.decode("#EEEEEE"));
            this.setTitle("Games E-Commerce");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel parentPanel = new JPanel(new BorderLayout());
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(customColor);
            JLabel title = new JLabel("                                         Check Out");
            title.setForeground(Color.WHITE);
            title.setBackground(customColor);
            title.setFont(font);
            JButton back = new JButton("  <  ");
            back.setBackground(customColor);
            back.setForeground(Color.WHITE);
            back.setFocusable(false);
            back.setBorder(BorderFactory.createEmptyBorder());
            back.setFont(font);
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    customDialog.setVisible(true);
                    new HomeView(user);
                    dispose();
                }
            });

            titlePanel.add(back);
            titlePanel.add(title);
            JPanel contentPanel = new JPanel(new BorderLayout());
            ArrayList<Cart> gamesList = order.fullCart;
            ArrayList<item> updatedCart =updatedCart(gamesList);
            contentPanel.setPreferredSize(new Dimension(700, 400 * updatedCart.size()));
            JPanel games = new JPanel(new GridLayout(0, 1));

            for (item i : updatedCart) {
                JPanel item = itemDisplay(i);
                games.add(item);
            }
            games.setBackground(customColor);
            JLabel orderId = new JLabel("<html><b>Order ID: </b>" + order.order_id + "</html>");
            orderId.setBackground(customColor);
            orderId.setForeground(Color.WHITE);
            orderId.setFont(font2);

            JLabel date = new JLabel("<html><b>Order Date: </b>" + order.orderDate.toLocaleString() + "</html>");
            date.setBackground(customColor);
            date.setForeground(Color.WHITE);
            date.setFont(font2);

            JLabel total = new JLabel("<html><b>Total: </b>" + order.total + " Rs" + "</html>");
            total.setBackground(customColor);
            total.setForeground(Color.WHITE);
            total.setFont(priceFont);

            JOptionPane optionPane0 = new JOptionPane("Some of the items that you want to buy are out of stock for now, PLease try again later", JOptionPane.CLOSED_OPTION);
            optionPane0.setBackground(Color.decode("#EEEEEE"));
            JDialog customDialog0 = optionPane0.createDialog(null, "Error");
            customDialog0.getContentPane().setBackground(Color.decode("#EEEEEE"));

            JButton payment = new JButton("Payment");
            payment.setBackground(customColor);
            payment.setForeground(Color.WHITE);
            payment.setFocusable(false);
            payment.setFont(font);
            payment.setPreferredSize(new Dimension(300, 50));
            payment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(checkBeforePayment(updatedCart)) {
                        database.payment(user, order, order.orderDate);
                        customDialog1.setVisible(true);
                        new HomeView(user);
                        dispose();
                    }
                    else{
                        customDialog0.setVisible(true);
                        new HomeView(user);
                        dispose();
                    }
                }
            });

            JPanel bill = new JPanel();
            bill.setLayout(new BoxLayout(bill, BoxLayout.Y_AXIS));
            bill.setBorder(BorderFactory.createEmptyBorder(200, 50, 0, 0));
            bill.add(orderId);
            bill.add(Box.createRigidArea(new Dimension(0, 15)));
            bill.add(date);
            bill.add(Box.createRigidArea(new Dimension(0, 15)));
            bill.add(total);
            bill.add(Box.createRigidArea(new Dimension(0, 15)));
            bill.add(payment);
            bill.setBackground(customColor);
            contentPanel.setBackground(customColor);

            contentPanel.add(bill, BorderLayout.CENTER);
            contentPanel.add(games, BorderLayout.WEST);




            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            parentPanel.add(titlePanel, BorderLayout.NORTH);
            parentPanel.add(scrollPane, BorderLayout.CENTER);
            this.getContentPane().add(parentPanel);
            this.setResizable(false);
            this.getContentPane().setBackground(customColor);
            this.setSize(screenWidth, screenHeight);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
        }
    }
    public JPanel itemDisplay(item i){
        Font font2=new Font("Helvetica", Font.PLAIN, 19);
        Font font1=new Font("Helvetica", Font.PLAIN, 14);
        JLabel Avail=new JLabel("Availability: "+database.getGame(i.cart.title).availability);
        Avail.setForeground(Color.lightGray);
        Avail.setFont(font1);
        ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource(i.cart.loc)));
        Image image=icon.getImage();
        Image modifiedImage=image.getScaledInstance(220,280, Image.SCALE_SMOOTH);
        JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
        newBackground.setSize(new Dimension(220,280));
        newBackground.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 30));
        JPanel gamePanel=new JPanel(new BorderLayout());
        gamePanel.setBackground(customColor);
        gamePanel.add(newBackground,BorderLayout.CENTER);
        JPanel info=new JPanel();
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(0, 80-i.cart.title.length(), 0, 0));
        info.setBackground(Color.decode("#171D25"));
        JLabel title=new JLabel("<html><b>"+i.cart.title.toUpperCase(Locale.ROOT)+" </b>" +""+"    x"+i.quantity + "</html>");
        title.setForeground(Color.white);
        title.setFont(font2);
        info.add(title);
        info.add(Avail);
        JLabel availability=new JLabel("Price: "+i.cart.price+" Rs");
        availability.setForeground(Color.lightGray);
        availability.setFont(font1);
        info.add(availability);

        info.add(Box.createRigidArea(new Dimension(0, 30)));

        gamePanel.add(info,BorderLayout.SOUTH);

        return gamePanel;
    }
    public boolean checkBeforePayment(ArrayList<item> updatedCart){
        for(item i:updatedCart){
            if(i.quantity>database.getGame(i.cart.title).availability){
               return false;
            }
        }

        return true;

    }
    public ArrayList<item> updatedCart(ArrayList<Cart> cart){
        ArrayList<item> newCart=new ArrayList<>();
        int quantity=1;
        for(int i=0;i<cart.size();i++){
            for (int j=i+1;j<cart.size();j++){
                if(cart.get(i).title.equals(cart.get(j).title)){
                    quantity++;
                    cart.remove(j);
                    j--;
                }
            }
            newCart.add(new item(cart.get(i),quantity));
            quantity=1;
        }
        return newCart;
    }

}

 class item{
    Cart cart;
    int quantity;

     public item(Cart cart, int quantity) {
         this.cart = cart;
         this.quantity = quantity;
     }
 }
