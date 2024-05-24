import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class mainScreen extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon2.png")));
    Image image=icon.getImage();
    Image modifiedImage=image.getScaledInstance(250,250, Image.SCALE_SMOOTH);
    JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));

    JButton login=new JButton("Login");
    Color customColor = Color.decode("#171D25");


    public mainScreen(){
        CRUD database=new CRUD();
        this.setTitle("Games E-Commerce");
        Font font2=new Font("Helvetica", Font.BOLD, 50);
        JLabel CenterTitle =new JLabel("STEAM");
        CenterTitle.setBounds((screenWidth/2)-90,280,350,50);
        CenterTitle.setFont(font2);
        CenterTitle.setForeground(Color.decode("#FFFFFF"));
        //dialogue box for error
        JOptionPane optionPane = new JOptionPane("Invalid Credentials", JOptionPane.CANCEL_OPTION);
        optionPane.setBackground(Color.decode("#EEEEEE"));
        JDialog customDialog = optionPane.createDialog(null, "Error");
        customDialog.getContentPane().setBackground(Color.decode("#EEEEEE"));

        Font font=new Font("Helvetica", Font.PLAIN, 15);
        //Login button functionality and design
        login.setBounds(250, 410, 80, 30);
        login.setBackground(customColor);
        login.setForeground(Color.WHITE);  // Set the text color of the Record
        login.setFocusable(false);
        login.setFont(font);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new LoginView();
              dispose();
            }
        });
        JButton signUp=new JButton("Sign up");
        signUp.setBounds(400, 410, 100, 30);
        signUp.setBackground(customColor);
        signUp.setForeground(Color.WHITE);  // Set the text color of the Record
        signUp.setFocusable(false);
        signUp.setFont(font);
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpView();
                dispose();

            }
        });


        this.setLayout(null);
        this.getContentPane().setBackground(customColor);
        //Logo
        newBackground.setBounds(270,10,250,250);
        this.add(newBackground);
        this.add(CenterTitle);
        this.setSize(screenWidth, screenHeight);
        this.add(login);
        this.add(signUp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
