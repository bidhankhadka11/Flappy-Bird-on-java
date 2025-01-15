import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;


public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    //Loading variables
    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backgroundImage;
    Image bottomPipeImage;
    Image topPipeImage;
    Image flappyBird;


    //bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x =  birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }


    //Pipes
    int pipeX = boardWidth;  //This is the position where the pipes start
    int pipeY = 0;
    int pipeWidth = 64;  //Scaled by 1/6
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }


    //Game Logic
    Bird bird; 
    int velocityX = -4;
    int velcotiyY = -13;
    int gravity = 1;

    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer placePipesTimer;

    Random random = new Random();

    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //Load Images
        backgroundImage = new ImageIcon(getClass().getResource("/img/flappybirdbg.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("/img/bottompipe.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("/img/toppipe.png")).getImage();
        flappyBird = new ImageIcon(getClass().getResource("/img/flappybird.png")).getImage();
        
        //Bird
        bird = new Bird(flappyBird);

        pipes = new ArrayList<Pipe>();
        //Place Pipes Timer
        placePipesTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        }); 
        placePipesTimer.start();

        //Game Timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes() {
        // 0 - 128 - random(0-258)
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = boardHeight / 4; 

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + topPipe.height + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
    
        //Pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        //bird
        velcotiyY += gravity;
        bird.y += velcotiyY;
        bird.y = Math.max(bird.y, 0);

        //pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; //0.5 becuase there are two pipes;
            }

            if(collison(bird, pipe)){
                gameOver = true;
            }
        }

        if(bird.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collison(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
         }

     @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velcotiyY = -12;
            if(gameOver) {
                //restart the game by setting up the conditions
                bird.y = birdY;
                velcotiyY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

