import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddItem extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    int gamePrice=0;
    int gameQuantity=0;
    static String location;
   String loc;
    CRUD database=new CRUD();
    AddItem(){
        this.setTitle("Games E-Commerce");
        this.setSize(screenWidth, screenHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color customColor = Color.decode("#171D25");
        Font font=new Font("Helvetica", Font.BOLD, 18);
        Font font1=new Font("Helvetica", Font.PLAIN, 18);
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

        JPanel parentPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, -15, 5, 0));
        titlePanel.setBackground(customColor);
        //Empty space to make the logo centered, added before the logo
        JLabel space=new JLabel("                                                         Add Item                                                        ");
        space.setFont(font);
        space.setForeground(Color.WHITE);
        titlePanel.add(back);
        titlePanel.add(space);



        JPanel contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(700, 1500));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));
        contentPanel.setBackground(customColor);
        JLabel text=new JLabel("Title:");
        text.setBounds(100,10,150,15);
        text.setFont(font1);
        contentPanel.add(text);
        text.setForeground(Color.WHITE);
        JTextField title=new JTextField();
        title.setForeground(Color.white);
        title.setBounds(100,40,400,40);
        title.setBackground(customColor);
        title.setCaretColor(Color.white);
        contentPanel.add(title);

        JLabel text1=new JLabel("Genre:");
        text1.setBounds(100,110,150,15);
        text1.setFont(font1);
        contentPanel.add(text1);
        text1.setForeground(Color.WHITE);
        JTextField genre=new JTextField();
        genre.setForeground(Color.white);
        genre.setBounds(100,140,400,40);
        genre.setBackground(customColor);
        genre.setCaretColor(Color.white);
        contentPanel.add(genre);

        JLabel text2=new JLabel("Price:");
        text2.setBounds(100,210,150,15);
        text2.setFont(font1);
        contentPanel.add(text2);
        text2.setForeground(Color.WHITE);
        JTextField price=new JTextField();
        price.setForeground(Color.white);
        price.setBounds(100,240,400,40);
        price.setBackground(customColor);
        price.setCaretColor(Color.white);
        contentPanel.add(price);

        JLabel text3=new JLabel("Quantity:");
        text3.setBounds(100,310,150,22);
        text3.setFont(font1);
        contentPanel.add(text3);
        text3.setForeground(Color.WHITE);
        JTextField availability=new JTextField();
        availability.setForeground(Color.white);
        availability.setBounds(100,340,400,40);
        availability.setBackground(customColor);
        availability.setCaretColor(Color.white);
        contentPanel.add(availability);
        JLabel text4=new JLabel("Image:");
        text4.setBounds(100,390,150,22);
        text4.setFont(font1);
        text4.setForeground(Color.WHITE);
        contentPanel.add(text4);
        JButton signUp=new JButton("Select a file");
        signUp.setBounds(150, 430, 150, 30);
        signUp.setBackground(customColor);
        signUp.setForeground(Color.WHITE);  // Set the text color of the Record
        signUp.setFocusable(false);
        signUp.setFont(font1);


        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                location=showFileChooserAndSetLocation();
                if(location!=null) {
                    loc = imageLocation(location, title.getText());

                }
            }
        });
        JButton Add=new JButton("Add");
        Add.setBounds(300, 470, 150, 30);
        Add.setBackground(customColor);
        Add.setForeground(Color.WHITE);  // Set the text color of the Record
        Add.setFocusable(false);
        Add.setFont(font1);
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameTitle=title.getText();
                String gameGenre=genre.getText();

                if((!price.getText().isEmpty() && !Pattern.compile("[a-zA-Z]").matcher(price.getText()).find()) && !availability.getText().isEmpty() && !Pattern.compile("[a-zA-Z]").matcher(availability.getText()).find()) {
                    gamePrice = Integer.parseInt(price.getText());
                    gameQuantity = Integer.parseInt(availability.getText());
                }
                    database.insertingGame(gameTitle, gameGenre, gamePrice, gameQuantity, loc);
                    database.closeConnection();
//                    new AddItem();
//                    dispose();

            }
        });

        contentPanel.add(signUp);
        contentPanel.add(Add);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        parentPanel.add(titlePanel, BorderLayout.NORTH);
        parentPanel.add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(parentPanel);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
    public static String showFileChooserAndSetLocation() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();

        }
        return null;
    }

    public String imageLocation(String currentLocation,String title){
        String destinationPath="src/Images/"+title;
        String returnPath= "/Images/"+title;
        for(int i=0;i<currentLocation.length();i++){
            if(currentLocation.charAt(i)=='.'){
                destinationPath+=currentLocation.substring(i);
                returnPath+=currentLocation.substring(i);
                break;
            }
        }

        // Copy the image file
        copyImage(currentLocation, destinationPath);
        return returnPath;
    }

    private static void copyImage(String sourcePath, String destinationPath)  {
        // Using Paths and Files to perform the file copy
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        // StandardCopyOption.REPLACE_EXISTING allows overwriting an existing file
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



