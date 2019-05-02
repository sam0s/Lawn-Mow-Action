import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

public class Game {

    Color groundColor = new Color(86, 129, 86); 
    Color grassColor = new Color(0, 255, 26);
    Color badColor = new Color(255, 26, 26);
    Color goldColor = new Color(255, 185, 0);
    Color bgColor = new Color(0,0,200);
    private int score = 0;
    private long moveTimer = 0;
    long last_time = System.nanoTime();
    private int HudOffset = 70;
    public int[] pos = {0,0};
    private int dx = 0;
    private int dy = 0;
    public int scoreMax = 0;
    
    boolean game = true;
    boolean firstRun = true;
    long startTime = System.currentTimeMillis();
    String won = "";
    
    int enemyFreq = 10;
    int sandFreq = 5;
    int grassFreq = 16;
    int pSpeed = 5;
    char playerChar = '☺';
    
    // borrowed from stack overflow
    public static int fillArea(int x, int y, int[][] arr, int max) {
        int maxX = arr.length - 1;
        int maxY = arr[0].length - 1;
        int[][] stack = new int[(maxX+1)*(maxY+1)][2];
        int index = 0;
        int total = 0;

        int fill = 9;
        int original = 1;

        stack[0][0] = x;
        stack[0][1] = y;
        arr[x][y] = fill;

        while (index >= 0){
            x = stack[index][0];
            y = stack[index][1];
            index--;            

            if ((x > 0) && (arr[x-1][y] == 1 || arr[x-1][y] == 3)){
                arr[x-1][y] = arr[x-1][y]+3;
                total++;
                index++;
                stack[index][0] = x-1;
                stack[index][1] = y;
            }

            if ((x < maxX) && (arr[x+1][y] == 1 || arr[x+1][y] == 3)){
                arr[x+1][y] = arr[x+1][y]+3;
                index++;
                total++;
                stack[index][0] = x+1;
                stack[index][1] = y;
            }

            if ((y > 0) && (arr[x][y-1] == 1 || arr[x][y-1] == 3)){
                arr[x][y-1] = arr[x][y-1]+3;
                index++;
                total++;
                stack[index][0] = x;
                stack[index][1] = y-1;
            }                

            if ((y < maxY) && (arr[x][y+1] == 1 || arr[x][y+1] == 3)){
                arr[x][y+1] = arr[x][y+1]+3;
                index++;
                total++;
                stack[index][0] = x;
                stack[index][1] = y+1;
            }                          
        }
        return -1+(max-total);
    }

    public boolean fillCheck(int[][] mapus, int total){
        int grassTotal = total;
        int btotal = 0;
        int bchange = 0;
        scoreMax = -1;
        if(fillArea(pos[0],pos[1],mapus,grassTotal)==0){
            for(int i=0;i<10;i++){
                for(int ii=0;ii<9;ii++){
                    bchange = 0;
                    if(mapus[i][ii]==1){btotal+=1;bchange=1;}
                    if(mapus[i][ii]==3){btotal+=1;bchange=1;}
                    if (mapus[i][ii]!=2 && bchange == 0){scoreMax+=1;}
                }
            }
            return (btotal==0);
        }
        return false;
    }

    public int[][] newMap(){
        boolean done = false;
    startTime = System.currentTimeMillis();

        int[][]grassNew = {
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}};

        while (!done){
            int indexPh=0;
            int squares = enemyFreq + sandFreq + grassFreq;
            int[] choice = new int[squares];
            for(int i = 0;i<enemyFreq;i++){choice[indexPh]=2;indexPh++;}
            for(int i = 0;i<sandFreq;i++){choice[indexPh]=3;indexPh++;}
            for(int i = 0;i<grassFreq;i++){choice[indexPh]=1;indexPh++;}


            int randChoice = 1;
            int grassTot = 0;

            for(int i = 0;i<grassNew[0].length;i++){
                Arrays.fill(grassNew[i], 0);
            }

            for(int i=0;i<10;i++){
                for(int ii=0;ii<9;ii++){
                    if (randChoice!=2){grassTot+=1;}
                    grassNew[i][ii]=randChoice;
                    randChoice = choice [ThreadLocalRandom.current().nextInt(0, choice.length)];
                }
            }

            pos[0] = ThreadLocalRandom.current().nextInt(0, 10);
            pos[1] = ThreadLocalRandom.current().nextInt(0, 9);

            if(fillCheck(grassNew,grassTot)){done=true;}

        }

        return grassNew;

    }

    int[][] grass = newMap();
    public Game(int ef, int sf, int gf, int gs, char pc) {
        enemyFreq = ef;
        sandFreq = sf;
        grassFreq = gf;
        playerChar = pc;
        pSpeed = gs;
        EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    }        

                    JFrame frame = new JFrame("LAWN MOW ACTION V3");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLayout(new BorderLayout());
                    frame.add(new TestPane());
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.addKeyListener(new TAdapter());
                    frame.setFocusable(true);
                    frame.requestFocusInWindow();
                    frame.setResizable(false);
                }
            });

    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            switch(key){
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                dx=-1;dy=0;
                break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                dx=1;dy=0;
                break;
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                dy=-1;dx=0;
                break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                dy=1;dx=0;
                break; 
                case KeyEvent.VK_R:
                dx=0;dy=0;pos[0]=0;pos[1]=0;score=0;grass = newMap();game=true;startTime = System.currentTimeMillis();
                break;
                case KeyEvent.VK_ESCAPE:
                System.exit(1);
                break;
            }
            //Close keyPressed method
        }
        // Close key adapter class
    }

    public class TestPane extends JPanel {

        public void drawMap(Graphics g){

            for(int i=0;i<9;i++){
                for (int b=0;b<10;b++){
                    if (grass[b][i]==1+3){g.setColor(grassColor);g.fill3DRect(5+b*32, (HudOffset)+i*32, 32, 32,true);}
                    if (grass[b][i]==2){g.setColor(badColor);g.fill3DRect(5+b*32, (HudOffset)+i*32, 32, 32,true);}
                    if (grass[b][i]==3+3){g.setColor(goldColor);g.fill3DRect(5+b*32, (HudOffset)+i*32, 32, 32,true);}

                }
            }

            g.setFont(new Font("TimesRoman", Font.PLAIN, 28)); 
            g.setColor(Color.BLACK);
            char c = playerChar;
            
            g.drawString(String.valueOf(c), (pos[0]*32)+9, ((pos[1]+1)*32)-7+HudOffset);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(320, 288+HudOffset);
        }

        @Override
        public void paintComponent(Graphics g) {
            if(firstRun){dx=0;dy=0;pos[0]=0;pos[1]=0;score=0;grass = newMap();game=true;startTime = System.currentTimeMillis();firstRun=false;}
            if(game){
                super.paintComponent(g);
                g.setColor(bgColor);
                g.fillRect(0, 0, 400, 400);
                g.setColor(groundColor);
                g.fillRect(5, HudOffset, 320, 288);
                drawMap(g);
                g.setColor(bgColor);
                g.fillRect(0, 0, 332, HudOffset);
                g.fillRect(0, 358, 332, 20);
                g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 22)); 
                g.drawString("Score: "+String.valueOf(score), 6,20);
                g.drawString("Blocks Left: "+String.valueOf(scoreMax), 6,39);
        g.drawString("Time: "+String.valueOf((System.currentTimeMillis()-startTime)/1000)+" seconds", 6,60);

                long time = System.nanoTime();
                int delta_time = (int) ((time - last_time) / 100);
                last_time = time;
                moveTimer+=delta_time/5;
                
                if (moveTimer>pSpeed*100000 ){
                    
                    if (pos[0]>9 || pos[0]<0 || pos[1]>8 || pos[1]<0){dx=0;dy=0;pos[0]=0;pos[1]=0;score=0;grass = newMap();}
                    if(grass[pos[0]][pos[1]]==2){dx=0;dy=0;pos[0]=0;pos[1]=0;score=0;grass = newMap();} // dead
                    if(grass[pos[0]][pos[1]]==3+3){dx=0;dy=0;} // sand
                    if(grass[pos[0]][pos[1]]==1+3 || grass[pos[0]][pos[1]]==3+3){scoreMax-=1;score+=grass[pos[0]][pos[1]]*11;grass[pos[0]][pos[1]]=0;}
                    pos[0]+=dx;pos[1]+=dy;moveTimer=0;
                }
                if (scoreMax==0){game=false;won = "Time: "+String.valueOf((System.currentTimeMillis()-startTime)/1000)+" seconds";}
                this.repaint();
            }
            else{
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 400, 400);
                g.setColor(Color.BLACK);
                g.drawString("YOU \nARE THE \nGRASS CUTTING \nCHAMPION!!", 32,32);
                g.drawString(won,29,64);
                g.drawString("Score: "+String.valueOf(score),29,86);
                g.drawString("Press [R] to go again!",29,200);
                g.drawString(("( EnemyFreq: "+enemyFreq+" | SandFreq: "+sandFreq+" | GrassFreq: "+grassFreq+" | Speed: "+pSpeed+" )"),28,230);
                this.repaint();
            }
        }
    }
}