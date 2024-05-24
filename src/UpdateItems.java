import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateItems extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    Font titleFont=new Font("Helvetica", Font.BOLD, 20);
    Font priceFont=new Font("Helvetica", Font.PLAIN, 19);
    Font cartFont=new Font("Helvetica", Font.ITALIC, 19);
    Font review=new Font("Helvetica", Font.PLAIN, 16);
    CRUD database=new CRUD();
    Games game;
    Color customColor = Color.decode("#171D25");
    JPanel contentPanel;
    UpdateItems(Games game){
        Font font=new Font("Helvetica", Font.BOLD, 23);
        this.game=game;
        this.setTitle("Games E-Commerce");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel parentPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(customColor);

        JLabel topTitle=new JLabel("                                                         Update");
        topTitle.setForeground(Color.WHITE);
        topTitle.setFont(titleFont);


        JButton back=new JButton("  <");
        back.setBackground(customColor);
        back.setForeground(Color.WHITE);
        back.setFocusable(false);
        back.setBorder(BorderFactory.createEmptyBorder());
        back.setFont(font);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminView();
                dispose();
            }
        });

        titlePanel.add(back);
        titlePanel.add(topTitle);
        contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(700, 900));
        ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource(game.imageLocation)));
        Image image=icon.getImage();
        Image modifiedImage=image.getScaledInstance(260,360, Image.SCALE_SMOOTH);
        JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
        contentPanel.setBackground(customColor);
        JLabel title=new JLabel(game.title.toUpperCase(Locale.ROOT));
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        title.setBackground(customColor);
        title.setSize(title.getPreferredSize());
        //Centralizes the title right below the image.
        int startingPos=(760-title.getWidth())/2;
        JLabel price=new JLabel("Price: "+game.price+" Rs");
        price.setForeground(Color.lightGray);
        price.setBackground(customColor);
        price.setFont(priceFont);
        price.setSize(price.getPreferredSize());
        int starting=((760-price.getWidth())/2);
        JLabel availability;
        if(game.availability!=0) {
            availability = new JLabel("Availability: " + game.availability);
        }else{
            availability = new JLabel("Out Of Stock");
        }
        availability.setForeground(Color.lightGray);
        availability.setBackground(customColor);
        availability.setFont(priceFont);
        availability.setSize(price.getPreferredSize());
        int starting1=((760-availability.getWidth())/2);
        JLabel text=new JLabel("Add items:");
        text.setBounds(250,600,200,15);
        text.setFont(review);
        text.setForeground(Color.lightGray);
        JTextField updateQuatity=new JTextField();
        updateQuatity.setForeground(Color.white);
        updateQuatity.setBounds(250, 625, 250, 30);
        updateQuatity.setBackground(customColor);
        updateQuatity.setCaretColor(Color.white);
        JButton updateQ=new JButton(" Update Quantity");
        updateQ.setBounds(275, 670, 180, 30);
        updateQ.setBackground(customColor);
        updateQ.setForeground(Color.WHITE);  // Set the text color of the Record
        updateQ.setFocusable(false);
        updateQ.setFont(review);
        updateQ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updateQuatity.getText().isEmpty() && !Pattern.compile("[a-zA-Z]").matcher(updateQuatity.getText()).find()) {
                    int quantity = Integer.parseInt(updateQuatity.getText());
                    if (quantity > 0) {
                        database.insertingGame(game.title, game.genre, game.price, quantity, game.imageLocation);
                        new UpdateItems(database.getGame(game.title));
                        dispose();
                    }
                }
            }
        });

        JLabel text1=new JLabel("New Price:");
        text1.setBounds(250,720,150,15);
        text1.setFont(review);
        text1.setForeground(Color.lightGray);
        JTextField updatePrice=new JTextField();
        updatePrice.setForeground(Color.white);
        updatePrice.setBounds(250, 745, 250, 30);
        updatePrice.setBackground(customColor);
        //changes color of the typing blinker
        updatePrice.setCaretColor(Color.white);
        JButton updateP=new JButton(" Update Price");
        updateP.setBounds(280, 790, 150, 30);
        updateP.setBackground(customColor);
        updateP.setForeground(Color.WHITE);  // Set the text color of the Record
        updateP.setFocusable(false);
        updateP.setFont(review);
        updateP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updatePrice.getText().isEmpty() && !Pattern.compile("[a-zA-Z]").matcher(updatePrice.getText()).find()) {
                    int price = Integer.parseInt(updatePrice.getText());
                    if (price > 0) {
                        database.updatingPrices(game.title, price);
                        new UpdateItems(database.getGame(game.title));
                        dispose();
                    }
                }
            }
        });
        //Password
        availability.setBounds(starting1,440,150,100);
        price.setBounds(starting,400,150,100);
        title.setBounds(startingPos,360,350,100);
        newBackground.setBounds(250,15,260,360);
        contentPanel.add(newBackground);
        contentPanel.add(title);
        contentPanel.add(price);
        contentPanel.add(availability);
        contentPanel.add(text);
        contentPanel.add(text1);
        contentPanel.add(updateQuatity);
        contentPanel.add(updatePrice);
        contentPanel.add(updateP);
        contentPanel.add(updateQ);


        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        parentPanel.add(titlePanel, BorderLayout.NORTH);
        parentPanel.add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(parentPanel);
        this.setResizable(false);
        this.setSize(screenWidth, screenHeight);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }


}