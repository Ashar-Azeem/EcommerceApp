import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class HomeView extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon.png")));
    Image image=icon.getImage();
    Image modifiedImage=image.getScaledInstance(260,50, Image.SCALE_SMOOTH);
    JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
    JButton order=new JButton("Orders");
    JButton checkOut=new JButton("Check Out");
    JButton logOut=new JButton("Log Out");
    CRUD database=new CRUD();
    User user;
    HomeView(User user){
        this.user=user;
        this.setTitle("Games E-Commerce");
        this.setSize(screenWidth, screenHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color customColor = Color.decode("#171D25");
        Font font=new Font("Helvetica", Font.BOLD, 12);
        order.setPreferredSize(new Dimension(80,28));
        order.setBackground(customColor);
        order.setForeground(Color.WHITE);  // Set the text color of the Record
        order.setFocusable(false);
//        order.setBorder(null);
        order.setFont(font);
        order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderListView(user);
                dispose();
            }
        });
        checkOut.setPreferredSize(new Dimension(100,28));
        checkOut.setBackground(customColor);
        checkOut.setForeground(Color.WHITE);  // Set the text color of the Record
        checkOut.setFocusable(false);
//        checkOut.setBorder(null);
        checkOut.setFont(font);
        checkOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckOutView(user);
                dispose();
            }
        });
        logOut.setPreferredSize(new Dimension(80,28));
        logOut.setBackground(customColor);
        logOut.setForeground(Color.WHITE);  // Set the text color of the Record
        logOut.setFocusable(false);
        logOut.setFont(font);
//        logOut.setBorder(null);
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginView();
                dispose();
            }
        });
        JPanel parentPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, -15, 5, 0));
        titlePanel.setBackground(customColor);
        //Empty space to make the logo centered, added before the logo
        JLabel space=new JLabel("                                              ");
        titlePanel.add(logOut);
        titlePanel.add(space);
        titlePanel.add(newBackground);
        JLabel space2=new JLabel("                 ");
        titlePanel.add(space2);
        titlePanel.add(order);
        titlePanel.add(checkOut);



        JPanel contentPanel = new JPanel(new GridLayout(0, 2));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, -20, 25, 0));
        contentPanel.setBackground(customColor);

        ArrayList<Games> Games=database.getAllGames();
        for(Games game: Games){
            contentPanel.add(createPanel(game));
        }
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        parentPanel.add(titlePanel, BorderLayout.NORTH);
        parentPanel.add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(parentPanel);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
    }
    public JPanel createPanel(Games game){
        Font font=new Font("Helvetica", Font.BOLD, 19);
        Font font1=new Font("Helvetica", Font.PLAIN, 14);
        ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource(game.imageLocation)));
        Image image=icon.getImage();
        Image modifiedImage=image.getScaledInstance(220,280, Image.SCALE_SMOOTH);
        JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
        JPanel gamePanel=new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.decode("#171D25"));
        gamePanel.add(newBackground,BorderLayout.CENTER);
        JPanel info=new JPanel();
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(10, 120-game.title.length(), 30, 30));
        info.setBackground(Color.decode("#171D25"));
        JLabel title=new JLabel(game.title);
        title.setForeground(Color.white);
        title.setFont(font);
        info.add(title);
        info.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel availability=new JLabel("Price: "+game.price+" Rs");
        availability.setForeground(Color.WHITE);
        availability.setFont(font1);
        info.add(availability);
        info.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton view =new JButton("VIEW");
        view.setPreferredSize(new Dimension(150,40));
        view.setBackground(Color.decode("#171D25"));
        view.setForeground(Color.WHITE);  // Set the text color of the Record
        view.setFocusable(false);
        view.setFont(font);
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new viewItem(game,user);
                dispose();

            }
        });
        info.add(view);
        gamePanel.add(info,BorderLayout.SOUTH);


        return gamePanel;

    }
}
