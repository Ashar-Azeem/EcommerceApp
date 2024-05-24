public class test {
    public static void main(String[] args) {
        String currentLocation = "C:/Users/YourUsername/Desktop/image.jpeg";
        String title="apex";
        String destinationPath = "src/main/resources/"+title;
        for(int i=0;i<currentLocation.length();i++){
            if(currentLocation.charAt(i)=='.'){
                destinationPath+=currentLocation.substring(i);
                System.out.println(destinationPath);
                break;

            }
        }
    }
}
