import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class OrderListView extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    CRUD database = new CRUD();
    Color customColor = Color.decode("#171D25");
    User user;
    Font font = new Font("Helvetica", Font.BOLD, 23);
    Font font1 = new Font("Helvetica", Font.PLAIN, 20);
    Font priceFont = new Font("Helvetica", Font.PLAIN, 22);
    ArrayList<UserOrder> allOrders;

    OrderListView(User user) {
        this.user=user;
        allOrders = database.getAllOrders(user);
        if (allOrders == null) {
            this.setTitle("Games E-Commerce");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel parentPanel = new JPanel(new BorderLayout());
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(customColor);
            JLabel title = new JLabel("                                         Orders");
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
            JLabel noOrders = new JLabel("          No Orders Found");
            noOrders.setBackground(customColor);
            noOrders.setForeground(Color.WHITE);
            noOrders.setFont(font);

            titlePanel.add(back);
            titlePanel.add(title);
            JPanel middle = new JPanel(new BorderLayout());
            middle.setBackground(customColor);
            middle.add(noOrders, BorderLayout.CENTER);
            parentPanel.add(titlePanel, BorderLayout.NORTH);
            parentPanel.add(middle, BorderLayout.CENTER);
            this.getContentPane().add(parentPanel);
            this.setResizable(false);
            this.getContentPane().setBackground(customColor);
            this.setSize(screenWidth, screenHeight);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
        } else {

            this.setTitle("Games E-Commerce");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().setBackground(customColor);
            JPanel parentPanel = new JPanel(new BorderLayout());
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(customColor);
            JLabel title = new JLabel("                                              Orders");
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

            titlePanel.add(back);
            titlePanel.add(title);

            JPanel contentPanel = new JPanel(new GridLayout(0, 1));
            contentPanel.setBackground(customColor);
            for (UserOrder order : allOrders) {
                contentPanel.add(getPanel(order));
            }

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

    public JPanel getPanel(UserOrder order) {
        ArrayList<Cart> fullCart = order.fullCart;
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(customColor);
        int size=fullCart.size();
        int block=0;
        if(size<4){
            block=size;
        }

        JLabel more=new JLabel("   ...");
        more.setForeground(Color.WHITE);
        more.setBackground(customColor);
        more.setFont(font);
        JPanel image = new JPanel();
        if(block>0) {
            image.setBorder(BorderFactory.createEmptyBorder(20, (760-150*block)/2, 0, 30));
        }
        else{
            image.setBorder(BorderFactory.createEmptyBorder(20, 50  , 0, 30));
        }
        image.setBackground(customColor);
        image.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        int i = 1;
        for (Cart cart : fullCart) {
            if(i>4){
                break;
            }
                try {
                    BufferedImage originalImage = ImageIO.read((Objects.requireNonNull(getClass().getResource(cart.loc))));
                    int originalWidth = originalImage.getWidth();
                    double percentageToKeep = 0.90;
                    int widthToKeep = (int) (originalWidth * percentageToKeep);
                    BufferedImage croppedImage;
                    if(block>1) {
                        croppedImage = originalImage.getSubimage(0, 0, widthToKeep, originalImage.getHeight());
                    }else{
                        croppedImage = originalImage.getSubimage(0, 0, originalImage.getWidth(), originalImage.getHeight());
                    }
                    Image img = croppedImage;
                    Image modifiedImage = img.getScaledInstance(160, 250, Image.SCALE_SMOOTH);
                    JLabel subImage = new JLabel(new ImageIcon(modifiedImage));
                    image.add(subImage);
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if(fullCart.size()>4){
            image.add(more);
        }
        gamePanel.add(image, BorderLayout.CENTER);

        JPanel info=new JPanel();
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(10, 280, 30, 30));
        info.setBackground(Color.decode("#171D25"));
        JLabel title=new JLabel("<html><b>Order ID: </b>" + order.order_id + "</html>");
        title.setForeground(Color.white);
        title.setFont(priceFont);
        info.add(title);
        info.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel availability=new JLabel("<html><b>Order Date: </b>" + order.orderDate.toLocaleString() + "</html>");
        availability.setForeground(Color.WHITE);
        availability.setFont(font1);
        info.add(availability);
        info.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel price=new JLabel("<html><b>Total: </b>" + order.total + " Rs</html>");
        price.setForeground(Color.white);
        price.setFont(priceFont);
        info.add(price);
        info.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton view =new JButton("View");
        view.setSize(new Dimension(200,60));
        view.setBackground(customColor);
        view.setForeground(Color.WHITE);  // Set the text color of the Record
        view.setFocusable(false);
        view.setFont(font);
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckOutView(order,user);
                dispose();

            }
        });
        info.add(view);

        gamePanel.add(info,BorderLayout.SOUTH);
        return gamePanel;
    }

}