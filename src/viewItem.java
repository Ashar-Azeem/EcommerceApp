import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class viewItem extends JFrame {
    static final int screenHeight = 600;
    static final int screenWidth = 800;
    Font titleFont=new Font("Helvetica", Font.BOLD, 20);
    Font priceFont=new Font("Helvetica", Font.PLAIN, 19);
    Font cartFont=new Font("Helvetica", Font.ITALIC, 19);
    Font review=new Font("Helvetica", Font.PLAIN, 16);
    CRUD database=new CRUD();
    User user;
    Games game;
    Color customColor = Color.decode("#171D25");
    JPanel contentPanel;
    viewItem(Games game, User user){
        Font font=new Font("Helvetica", Font.BOLD, 23);
        this.user=user;
        this.game=game;
        this.setTitle("Games E-Commerce");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel parentPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(customColor);

        JLabel topTitle=new JLabel("                                                         Details");
        topTitle.setForeground(Color.WHITE);
        topTitle.setFont(titleFont);

        JLabel inCart=new JLabel("                                                            "+database.getNoOfItemInCart(user,game));
        inCart.setForeground(Color.WHITE);
        inCart.setFont(cartFont);
        JButton back=new JButton("  <");
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
        titlePanel.add(topTitle);
        if (database.getNoOfItemInCart(user,game)>0) {
            titlePanel.add(inCart);
        }
        contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(700, 2000));
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
       displayStars();
        JOptionPane optionPane0 = new JOptionPane("Can't add to cart as item is 'Out Of Stock'", JOptionPane.CLOSED_OPTION);
        optionPane0.setBackground(Color.decode("#EEEEEE"));
        JDialog customDialog0 = optionPane0.createDialog(null, "Error");
        customDialog0.getContentPane().setBackground(Color.decode("#EEEEEE"));
       JButton addToCart=new JButton("Add To Cart");
        addToCart.setBackground(customColor);
        addToCart.setForeground(Color.WHITE);
        addToCart.setFocusable(false);
        addToCart.setFont(titleFont);
        addToCart.setPreferredSize(new Dimension(250,50));
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(game.availability>0) {
                    database.buyingGame(user.user_id, game.gameID, game.price);
                    new viewItem(game, user);
                    dispose();
                }else{
                    customDialog0.setVisible(true);
                }
            }
        });
        JOptionPane optionPane = new JOptionPane("Comment should be less then 150 words", JOptionPane.CANCEL_OPTION);
        optionPane.setBackground(Color.decode("#EEEEEE"));
        JDialog customDialog = optionPane.createDialog(null, "Error");
        customDialog.getContentPane().setBackground(Color.decode("#EEEEEE"));
        JLabel comment=new JLabel("Comments: ");
        comment.setForeground(Color.WHITE);
        comment.setBackground(customColor);
        comment.setFont(titleFont);
        comment.setBounds(20,810,200,100);
        contentPanel.add(comment);
        ArrayList<Recommendation> recom;
        recom=database.getRecommendation(game);
        int x=20;
        int y=800;
        for(Recommendation recommendation: recom){
            JLabel usersRecommendation=recommendationDisplay(recommendation);
            usersRecommendation.setBounds(x,y,850,300);
            contentPanel.add(usersRecommendation);
            y+=80;

        }
        JLabel addComment=new JLabel("Add your comment:");
        addComment.setForeground(Color.WHITE);
        addComment.setFont(review);
        addComment.setBounds(20,y+140,200,30);
        JTextField comments=new JTextField();
        comments.setForeground(Color.white);
        comments.setBounds(20, y+180, 700, 40);
        comments.setBackground(customColor);
        //changes color of the typing blinker
        comments.setCaretColor(Color.white);
        JButton post=new JButton("Post");
        post.setBounds(300,y+230,100,40);
        post.setBackground(customColor);
        post.setForeground(Color.WHITE);  // Set the text color of the Record
        post.setFocusable(false);
        post.setFont(review);
        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comments.getText().length()>150){
                    customDialog.setVisible(true);
                }
                else if(!comments.getText().isEmpty()){
                    database.insertRecommendation(game, user, comments.getText());
                    comments.setText("");
                    new viewItem(game, user);
                    dispose();
                }
                else{
                    //
                }
            }
        });

        contentPanel.add(comments);
        contentPanel.add(addComment);
        contentPanel.add(post);


        addToCart.setBounds(260,655,250,50);
        availability.setBounds(starting1,440,150,100);
        price.setBounds(starting,400,150,100);
        title.setBounds(startingPos,360,350,100);
        newBackground.setBounds(250,15,260,360);
        contentPanel.add(newBackground);
        contentPanel.add(title);
        contentPanel.add(price);
        contentPanel.add(availability);
        contentPanel.add(addToCart);



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
    public JLabel recommendationDisplay(Recommendation recommendation){
        Font font=new Font("Helvetica", Font.PLAIN, 18);
        String newLine=lineBreaker(recommendation.comment);
        String text="<html>"+"<b>   "+recommendation.name.toUpperCase(Locale.ROOT)+"</b> <br>"+newLine+"</html>";
        JLabel recom=new JLabel(text);
        recom.setBackground(customColor);
        recom.setForeground(Color.lightGray);
        recom.setFont(font);
        return recom;
    }

    public JButton getLogoButton(String loc,int width,int height,int review){
        ImageIcon icon1=new ImageIcon(Objects.requireNonNull(getClass().getResource(loc)));
        Image image1=icon1.getImage();
        Image modifiedImage1=image1.getScaledInstance(width,height, Image.SCALE_SMOOTH);
        JButton starIcon=new JButton();
        starIcon.setIcon(new ImageIcon(modifiedImage1));
        starIcon.setBackground(customColor);
        starIcon.setBorder(null);
        starIcon.setFocusPainted(false);
        starIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int getReview=getReview(review);
                if(database.userReview(game,user)==false) {
                    database.review(user, game, getReview);
                    new viewItem(game,user);
                    dispose();
                }
            }
        });
        return starIcon;
    }
    public int getReview(int review){
        return review;
    }

    public JLabel getLogo(String loc,int width,int height){
        ImageIcon icon1=new ImageIcon(Objects.requireNonNull(getClass().getResource(loc)));
        Image image1=icon1.getImage();
        Image modifiedImage1=image1.getScaledInstance(width,height, Image.SCALE_SMOOTH);
        JLabel logo=new JLabel(new ImageIcon(modifiedImage1));
        return logo;

    }
    public void displayStars(){
        int ratings=database.getReview(game).rating;
        int y=530;
        int x= 280;
        for(int i=1;i<=5;i++){
            if (ratings>=i){
                if (database.userReview(game,user)){
                    JLabel fullStar=getLogo("/Images/star colored.png",32,32);
                    fullStar.setBounds(x,y,32,32);
                    contentPanel.add(fullStar);
                }
                else {
                    JButton fullStar = getLogoButton("/Images/star colored.png", 32, 32, i);
                    fullStar.setBounds(x, y, 32, 32);
                    contentPanel.add(fullStar);
                }
            }else{
                if (database.userReview(game,user)){
                    JLabel Star=getLogo("/Images/star.png",32,32);
                    Star.setBounds(x,y,32,32);
                    contentPanel.add(Star);
                }
                else {
                    JButton star = getLogoButton("/Images/star.png", 32, 32, i);
                    star.setBounds(x, y, 32, 32);
                    contentPanel.add(star);
                }
            }
            x+=40;
        }
        JLabel noOfReview=new JLabel(database.getReview(game).numberOfReviews+" Reviews");
        noOfReview.setFont(review);
        noOfReview.setForeground(Color.WHITE);
        noOfReview.setBackground(customColor);
        noOfReview.setSize(noOfReview.getPreferredSize());
        int starting2=((760-noOfReview.getWidth())/2);
        noOfReview.setBounds(starting2,560,100,50);
        contentPanel.add(noOfReview);
    }

    public String lineBreaker(String text){
        int i=0;
        int j=1;
        int start=0;
        String newLine="";
        while(i<text.length()){
            if (j>50 && text.charAt(i)==' ' || i==text.length()-1){
                newLine=newLine+text.substring(start,i+1)+"<br>";
                j=0;
                start=i+1;
            }
            i++;
            j++;
        }
        return newLine;
    }

}