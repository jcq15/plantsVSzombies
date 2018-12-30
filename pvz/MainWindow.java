package pvz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JPanel{
    // Layout, see readme.md
    int x1,x2,x3,x4,x5,y1,y2,y3,y4,groundDelta,headDelta;
    int width, height;      // screen

    // game control
    Timer clock;
    String plantOnMouse;    // now plant on mouse
    Ground ground;          // ground
    int numOfZombie;
    double freqOfZombie;    // one second how many
    int delta;         // tick delta (ms)

    // background
    ImageIcon icon;
    Image img;


    void init(){
        // temporary
        numOfZombie = 100;
        freqOfZombie = 0.2; // 5s a zombie

        delta = 50;
        ground = new Ground(numOfZombie);
        plantOnMouse = null;      // at first, no plant on mouse
        clock = new Timer(50, new ListenTimer());

        // screen size and layout
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        width = scrnsize.width;
        height = scrnsize.height;

        y1 = (int)(1.0 / 9.0 * height);
        y2 = (int)(y1 + 0.5 / 9.0 * height);
        y3 = (int)(y2 + 1.0 * 6 / 9.0 * height);
        y4 = (int)(y3 + 0.5 / 9.0 * height);
        x1 = (int)(2.0 / 16.0 * width);
        x2 = (int)(x1 + (9.0/6.0*(y3-y2)/height*9.0/16.0) * width);
        x3 = (int)(1.5 / 16.0 * width);
        x4 = (int)(x3 + 1.0 / 16.0 * width);
        x5 = (int)(x4 + 4.0 / 16.0 * width);
        groundDelta = (int)(height / 9.0);
        headDelta = (int)(width / 16.0);
    }

    public MainWindow(){
        init();     // initial something about game

        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getButton() != MouseEvent.BUTTON1){
                    return;     // not left key, return
                }
                int xVal = e.getX();
                int yVal = e.getY();

                if(xVal >= x1 && xVal < x2 && yVal >= y2 && yVal < y3){
                    // ground
                    if(plantOnMouse != null){
                        int row = (yVal - y2) / groundDelta;
                        int col = (xVal - x1) / groundDelta;
                        if(ground.setPlant(plantOnMouse, row, col)){
                            //plant plant
                            System.out.println("Woc! Mouse Pressed!" + xVal + " " + yVal);
                            Graphics g = getGraphics();    //获取组件绘图环境
                            g.drawImage(PlantList.images[PlantList.str2index(plantOnMouse)],
                                        x1+col*groundDelta, y2+row*groundDelta,
                                        groundDelta, groundDelta, MainWindow.this);
                            plantOnMouse = null;
                        }
                    }
                }else if(yVal < y1 && xVal >= x4 && xVal < x5){
                    // head plant
                    // if same, put return, else, get
                    int index = (xVal - x4) / headDelta;
                    if(plantOnMouse != null && plantOnMouse.equals(PlantList.names[index])){
                        plantOnMouse = null;
                        // drawout from mouse
                    }else{
                        plantOnMouse = PlantList.names[index];
                        System.out.println(PlantList.names[index]);
                        // draw from mouse
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e){}
            @Override
            public void mouseEntered(MouseEvent e){}
            @Override
            public void mouseReleased(MouseEvent e){}
            @Override
            public void mousePressed(MouseEvent e){}
        });

        /*
        ActionListener lsnStart = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                layout.show(pContainer, "c2");
                clock.start();
            }
        };
        btnStart.addActionListener(lsnStart);
        */
    }

    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.RED);
        g.drawRect(x1, y2, x2-x1, y3-y2);
        g.drawRect(x4, 0, x5-x4, y1);

        // draw shop
        g.drawImage(PlantList.images[0], x4, 0, headDelta, headDelta, this);
        g.drawImage(PlantList.images[1], x4+headDelta, 0, headDelta, headDelta, this);
        g.drawImage(PlantList.images[2], x4+2*headDelta, 0, headDelta, headDelta, this);
        g.drawImage(PlantList.images[3], x4+3*headDelta, 0, headDelta, headDelta, this);
    }

    //inner class about btnGround listener
    class ListenGround implements ActionListener{
        int index;      //0-53
        public ListenGround(int n){
            index = n;
        }
        @Override
        public void actionPerformed(ActionEvent e){

        }
    }

    // call gamecontroller 
    class ListenTimer implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //System.out.println("call tick!");
            tick();
        }
    }

    public static void main(String[] args){
        // load source
        MainWindow app = new MainWindow();
        JFrame f = new JFrame("Plants VS Zombies");
        f.setContentPane(app);
        f.setUndecorated(true);
        f.setSize(app.width, app.height);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        app.clock.start();
    }

    // call this each Time Clock
    public void tick(){
        System.out.println("tick was called!");

        Graphics g = getGraphics();

        ground.tickGenZom -= delta;
        for(int i=0;i<ground.howZomNow;i++){
            ground.zombies[i]
        }

        if(ground.tickGenZom <= 0){     // generate zombie
            if(howZomNow < 100){
                int r = (int)(Math.random()*6);
                Zombie z = ground.zombies[howZomNow].zombie;
                g.drawImage(z.getImg(), width, y2+r*groundDelta, 
                            groundDelta, groundDelta, this);
                howZomNow ++;
                ground.tickGenZom = ground.tickGenZomRaw;   // reset
            }else{
                ground.tickGenZom = 2147483647;     // no need it
            }
        }
        // plant's activity: attack/generate sun
        // zombie's activity: go/eat
        // playground's activity: generate zombie/sun from the sky
    }
}

class Ground{
    public int numOfZombie;
    public double freqOfZombie;

    public int tickGenZom;     // generate zombie's tick
    public int tickGenZomRaw;

    public GPlant[][] plants;
    public GZombie[] zombies;
    public int howZomNow;       // how zombies now(1-100)

    public Ground(int n, double f){   // how many zombies
        numOfZombie = n;
        freqOfZombie = f;
        howZomNow = 0;
        tickGenZom = (int)(1000.0 / freqOfZombie);
        tickGenZomRaw = tickGenZom;

        zombies = new GZombie[n];
        Zombie[] rawZombies = ZombieList.generate(numOfZombie);
        for(int i=0;i<n;i++){
            zombies[i] = new GZombie(rawZombies[i], 
                            (int)(1000.0f/rawZombies[i].getAtkSpd()));
        }
        plants = new GPlant[6][9];    //6 lines
        for(int i=0;i<6;i++){
            for(int j=0;j<9;j++){
                plants[i][j] = new GPlant(i, j, null);
            }
        }
    }

    class GPlant{
        Plant plant;
        int tickTime;       // dao ji shi
        int row;
        int col;
        public GPlant(int r, int c, Plant p){
            row = r;
            col = c;
            plant = p;
        }
    }
    class GZombie{
        public Zombie zombie;
        int posX;              // position
        int posY;
        int tickTime;
        int tickTimeRaw;
        //public boolean active;         // alive and inground
        public GZombie(Zombie z, int t){
            //active = false;
            zombie = z;
            tickTime = t;
            tickTimeRaw = t;
        }
        public tick(){
            // if plant, eat
        }
    }


    public boolean setPlant(String pltName, int r, int c){
        if(plants[r][c].plant == null){
            plants[r][c].plant = PlantList.generate(pltName);
            plants[r][c].tickTime = (int)(1000.0f/plants[r][c].plant.getSpeed());
            return true;
        }
        return false;
    }

}