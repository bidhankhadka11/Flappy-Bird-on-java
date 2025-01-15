import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int width = 360;
        int height = 640;

        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);  //Makes the dsiplay at the center
        frame.setResizable(false);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Closes the program when exit the window

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); //Makes the window 360x640 without including the title bar
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
