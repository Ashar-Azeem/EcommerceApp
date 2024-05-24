import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginView extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon2.png")));
    Image image=icon.getImage();
    Image modifiedImage=image.getScaledInstance(200,200, Image.SCALE_SMOOTH);
    JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
    JTextField email;
    JPasswordField password;
    JButton login=new JButton("Login");
    Color customColor = Color.decode("#171D25");


    public LoginView(){
        CRUD database=new CRUD();
        this.setTitle("Games E-Commerce");
        Font font2=new Font("Helvetica", Font.BOLD, 30);
        JLabel CenterTitle =new JLabel("STEAM");
        CenterTitle.setBounds((screenWidth/2)-50,210,350,50);
        CenterTitle.setFont(font2);
        CenterTitle.setForeground(Color.decode("#FFFFFF"));
        //dialogue box for error
        JOptionPane optionPane = new JOptionPane("Invalid Credentials", JOptionPane.CANCEL_OPTION);
        optionPane.setBackground(Color.decode("#EEEEEE"));
        JDialog customDialog = optionPane.createDialog(null, "Error");
        customDialog.getContentPane().setBackground(Color.decode("#EEEEEE"));
        email=new JTextField();
        password=new JPasswordField();
        //Email
        email.setForeground(Color.white);
        email.setBounds(280, 330, 240, 30);
        email.setBackground(customColor);
        //changes color of the typing blinker
        email.setCaretColor(Color.white);
        //Password
        password.setForeground(Color.white);
        password.setBounds(280, 390, 240, 30);
        password.setBackground(customColor);
        password.setCaretColor(Color.white);
        // text field Email
        Font font=new Font("Helvetica", Font.PLAIN, 15);
        JLabel text=new JLabel("Email:");
        text.setBounds(280,310,150,15);
        text.setFont(font);
        text.setForeground(Color.WHITE);
        //text for password
        JLabel text1=new JLabel("Password:");
        text1.setBounds(280,370,150,15);
        text1.setFont(font);
        text1.setForeground(Color.WHITE);

        //Login button functionality and design
        login.setBounds(360, 430, 80, 30);
        login.setBackground(customColor);
        login.setForeground(Color.WHITE);  // Set the text color of the Record
        login.setFocusable(false);
        login.setFont(font);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail=email.getText();
                String userPassword=password.getText();
                boolean verfied=database.loginUser(userEmail,userPassword);
                if(verfied){
                    new HomeView(database.getUser(userEmail));
                    dispose();
                }else{
                    email.setText("");
                    password.setText("");
                    customDialog.setVisible(true);
                }

            }
        });
        JButton signUp=new JButton("Sign up");
        signUp.setBounds(350, 470, 100, 30);
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
        newBackground.setBounds(300,10,200,200);
        this.add(newBackground);
        this.add(CenterTitle);
        this.setSize(screenWidth, screenHeight);
        this.add(email);
        this.add(password);
        this.add(text);
        this.add(text1);
        this.add(login);
        this.add(signUp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
