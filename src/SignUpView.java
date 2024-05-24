import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SignUpView extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon2.png")));
    Image image=icon.getImage();
    Image modifiedImage=image.getScaledInstance(200,200, Image.SCALE_SMOOTH);
    JLabel newBackground=new JLabel(new ImageIcon(modifiedImage));
    JTextField name;
    JTextField email;
    JPasswordField password;
    JButton signup=new JButton("Sign up");
    Color customColor = Color.decode("#171D25");


    public SignUpView(){
        CRUD database=new CRUD();
        this.setTitle("Games E-Commerce");
        Font font2=new Font("Helvetica", Font.BOLD, 30);
        Font font=new Font("Helvetica", Font.PLAIN, 15);
        JLabel CenterTitle =new JLabel("STEAM");
        CenterTitle.setBounds((screenWidth/2)-50,210,350,50);
        CenterTitle.setFont(font2);
        CenterTitle.setForeground(Color.decode("#FFFFFF"));
        //dialogue box for error
        JOptionPane optionPane = new JOptionPane("Email already in use", JOptionPane.CANCEL_OPTION);
        optionPane.setBackground(Color.decode("#EEEEEE"));
        JDialog customDialog = optionPane.createDialog(null, "Error");
        customDialog.getContentPane().setBackground(Color.decode("#EEEEEE"));
        name =new JTextField();
        name.setForeground(Color.white);
        name.setBounds(280, 350, 240, 30);
        name.setBackground(customColor);
        name.setCaretColor(Color.white);
        JLabel text2=new JLabel("Name:");
        text2.setBounds(280,330,150,15);
        text2.setFont(font);
        text2.setForeground(Color.WHITE);
        //Password
        email=new JTextField();
        password=new JPasswordField();
        //Email
        email.setForeground(Color.white);
        email.setBounds(280, 410, 240, 30);
        email.setBackground(customColor);
        //changes color of the typing blinker
        email.setCaretColor(Color.white);
        //Password
        password.setForeground(Color.white);
        password.setBounds(280, 470, 240, 30);
        password.setBackground(customColor);
        password.setCaretColor(Color.white);
        // text field Email

        JLabel text=new JLabel("Email:");
        text.setBounds(280,390,150,15);
        text.setFont(font);
        text.setForeground(Color.WHITE);
        //text for password
        JLabel text1=new JLabel("Password:");
        text1.setBounds(280,450,150,15);
        text1.setFont(font);
        text1.setForeground(Color.WHITE);

        JOptionPane optionPane1 = new JOptionPane("User registered successfully", JOptionPane.CANCEL_OPTION);
        optionPane1.setBackground(Color.decode("#EEEEEE"));
        JDialog customDialog1 = optionPane1.createDialog(null, "Message");
        customDialog1.getContentPane().setBackground(Color.decode("#EEEEEE"));
        //Login button functionality and design
        signup.setBounds(360, 520, 110, 30);
        signup.setBackground(customColor);
        signup.setForeground(Color.WHITE);  // Set the text color of the Record
        signup.setFocusable(false);
        signup.setFont(font);
        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail=email.getText();
                String userPassword=password.getText();
                String userName=name.getText();
                if (!userEmail.isEmpty() && !userPassword.isEmpty() && !userName.isEmpty()) {
                    User register = database.registerUser(userEmail, userPassword, userName);
                    if (register != null) {
                        customDialog1.setVisible(true);
                        new LoginView();
                        dispose();
                    } else {
                        email.setText("");
                        customDialog.setVisible(true);
                    }
                }
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
        this.add(signup);
        this.add(name);
        this.add(text2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

