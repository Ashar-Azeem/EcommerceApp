import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class AdminView extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon.png")));
    Image image=icon.getImage();
    Image modifiedImage=image.getScaledInstance(260,50, Image.SCALE_SMOOTH);
    JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
    JButton logOut=new JButton("Log Out");
    CRUD database=new CRUD();
    AdminView(){
        this.setTitle("Games E-Commerce");
        this.setSize(screenWidth, screenHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color customColor = Color.decode("#171D25");
        Font font=new Font("Helvetica", Font.BOLD, 12);
        Font font1=new Font("Helvetica", Font.BOLD, 180);

        logOut.setPreferredSize(new Dimension(80,28));
        logOut.setBackground(customColor);
        logOut.setForeground(Color.WHITE);  // Set the text color of the Record
        logOut.setFocusable(false);
        logOut.setFont(font);
//        logOut.setBorder(null);
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new mainScreen();
                dispose();
            }
        });
        JPanel parentPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, -15, 5, 0));
        titlePanel.setBackground(customColor);
        //Empty space to make the logo centered, added before the logo
        JLabel space=new JLabel("                                        ");
        titlePanel.add(logOut);
        titlePanel.add(space);
        titlePanel.add(newBackground);
        JLabel space2=new JLabel("                                                         ");
        titlePanel.add(space2);



        JPanel contentPanel = new JPanel(new GridLayout(0, 2));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, -20, 25, 0));
        contentPanel.setBackground(customColor);

        JPanel addButton=new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton.setBackground(customColor);
        JButton add =new JButton("+");
        add.setBackground(customColor);
        add.setForeground(Color.WHITE);
        add.setFocusable(false);
        add.setSize(new Dimension(700,700));
        add.setBorderPainted(false);
        add.setFont(font1);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddItem();
                dispose();
            }
        });
        addButton.add(add);
        ArrayList<Games> Games=database.getAllGames();
        System.out.println("hehehehehe no error");
        for(Games game: Games){
            contentPanel.add(createPanel(game));
        }
        contentPanel.add(addButton);

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
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(game.imageLocation)));
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
        JButton view =new JButton("Edit");
        view.setPreferredSize(new Dimension(150,40));
        view.setBackground(Color.decode("#171D25"));
        view.setForeground(Color.WHITE);  // Set the text color of the Record
        view.setFocusable(false);
        view.setFont(font);
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateItems(game);
                dispose();

            }
        });
        info.add(view);
        gamePanel.add(info,BorderLayout.SOUTH);


        return gamePanel;

    }
}
