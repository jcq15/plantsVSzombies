package pvz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Iterator;

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

        ground = new Ground();
    }

    public MainWindow(){
        init();     // initial something about game

        addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e){
                if(e.getButton() != MouseEvent.BUTTON1){
                    return;     // not left key, return
                }
                int xVal = e.getX();
                int yVal = e.getY();

                if(xVal >= x1 && xVal < x2 && yVal >= y2 && yVal < y3){
                    // ground
                    int row = (yVal - y2) / groundDelta;
                    int col = (xVal - x1) / groundDelta;
                    if(plantOnMouse != null){
                        ground.setPlant(plantOnMouse, row, col);
                        plantOnMouse = null;
                        /*
                        if(){
                            //plant plant
                            Graphics g = getGraphics();    //获取组件绘图环境
                            g.drawImage(PlantList.images[PlantList.str2index(plantOnMouse)],
                                        x1+col*groundDelta, y2+row*groundDelta,
                                        groundDelta, groundDelta, MainWindow.this);
                            plantOnMouse = null;
                        }
                        */
                    }else{      // plantOnMouse == null
                        if(ground.plants[row][col].plant instanceof SunPlant){
                            SunPlant sp = (SunPlant)(ground.plants[row][col].plant);
                            if(sp.haveSun()){
                                ground.sun += sp.takeSun();
                            }
                        }
                    }
                }else if(yVal < y1 && xVal >= x4 && xVal < x5){
                    // head plant
                    // if same, put return, else, get
                    int index = (xVal - x4) / headDelta;
                    if(plantOnMouse != null && plantOnMouse.equals(PlantList.names[index])){
                        plantOnMouse = null;
                    }else{
                        Plant testCost = PlantList.generate(PlantList.names[index]);
                        if(ground.sun >= testCost.getCost()){
                            plantOnMouse = PlantList.names[index];
                        }
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
            public void mouseClicked(MouseEvent e){}
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
    public void paintChildren(Graphics g){
        g.setColor(Color.RED);
        g.drawRect(x1, y2, x2-x1, y3-y2);
        g.drawRect(x4, 0, x5-x4, y1);

        // draw shop
        g.drawImage(PlantList.images[0], x4, 0, headDelta, headDelta, this);
        g.drawImage(PlantList.images[1], x4+headDelta, 0, headDelta, headDelta, this);
        g.drawImage(PlantList.images[2], x4+2*headDelta, 0, headDelta, headDelta, this);
        g.drawImage(PlantList.images[3], x4+3*headDelta, 0, headDelta, headDelta, this);

        // draw sun
        g.drawString(ground.sun+"", x3/2, y1/2);

        // draw plants and plant appendixs
        for(int i=0;i<6;i++){
            for(int j=0;j<9;j++){
                Ground.GPlant plt = ground.plants[i][j];
                if(plt.plant != null){
                    g.drawImage(plt.plant.getImg(), x1+j*groundDelta, 
                                y2+i*groundDelta, groundDelta, groundDelta, this);
                    // pea
                    if(plt.plant instanceof PeaPlant){
                        for(Tnt t : ((PeaPlant)(plt.plant)).getTnt()){
                            g.drawImage(t.im, t.x, t.y, this);
                        }
                    }
                    // sun
                    if(plt.plant instanceof SunPlant){
                        if(((SunPlant)(plt.plant)).haveSun()){
                            g.drawImage(PlantList.sunImg, x1+j*groundDelta+groundDelta/2,
                                        y2+i*groundDelta+groundDelta/2, groundDelta/2, 
                                        groundDelta/2, this);
                        }
                    }
                }
            }
        }
        
        // draw zombies
        for(Ground.GZombie z : ground.zombies){
            g.drawImage(z.zombie.getImg(), z.posX, z.posY, groundDelta, groundDelta, this);
        }

    }

    // call timer clock
    class ListenTimer implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //System.out.println("call tick!");
            ground.tick();
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

    // inner class -- game controller
    class Ground{
        int tickGenZom;     // generate zombie's tick
        int tickGenZomRaw;
        public int sun;

        public GPlant[][] plants;
        //public GZombie[] zombies;
        public LinkedList<GZombie> zombies;

        // call this each Time Clock
        public void tick(){
            // generate zombie
            tickGenZom -= delta;
            if(tickGenZom <= 0){     // generate zombie
                if(zombies.size() < numOfZombie){
                    int r = (int)(Math.random()*6);
                    Zombie z = ZombieList.generate();
                    zombies.add(new GZombie(z, (int)(1000.0f/z.getAtkSpd()), width, y2+r*groundDelta, r));
                    tickGenZom = tickGenZomRaw;   // reset
                }else{
                    tickGenZom = 2147483647;     // no need it
                }
            }

            // zombie walk/eat
            for(GZombie z : zombies){
                GPlant p = checkEatPlant(z);
                if(p != null){
                    z.tickTime -= delta;
                    if(z.tickTime <= 0){
                        hurtPlant(p, z.zombie.getAtk());
                        z.tickTime = z.tickTimeRaw;     // reset
                    }
                }else{
                    z.move();
                }
            }

            // plant event (attack / generate sun)
            for(int i=0;i<6;i++){
                for(int j=0;j<9;j++){
                    if(plants[i][j].plant != null){
                        // tnt move and fuck zombie
                        if(plants[i][j].plant instanceof PeaPlant){
                            PeaPlant plt = (PeaPlant)(plants[i][j].plant);
                            Iterator it = plt.getTnt().iterator();
                            while(it.hasNext()){
                                Tnt t = (Tnt)it.next();
                                t.x += t.speed * groundDelta * delta / 1000;
                                GZombie z = checkDuangZombie(t);
                                if(z != null){
                                    hurtZombie(z, plt.getAtk());
                                    plt.skill(z.zombie);                   // skill
                                    it.remove();                           // remove Tnt
                                }
                            }
                        }

                        // do things(generate sun/tnt)
                        plants[i][j].tickTime -= delta;
                        if(plants[i][j].tickTime <= 0){     // and have zombie
                            plants[i][j].event();       // do thing
                            plants[i][j].tickTime = plants[i][j].tickTimeRaw;   // reset
                        }
                    }
                }
            }

            // playground: sun from the sky

            repaint();
        }

        public Ground(){   // how many zombies
            sun = 500;
            tickGenZom = (int)(1000.0 / freqOfZombie);
            tickGenZomRaw = tickGenZom;
            zombies = new LinkedList<GZombie>();
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
            int tickTimeRaw;
            int row;
            int col;
            public GPlant(int r, int c, Plant p){
                row = r;
                col = c;
                plant = p;
            }
            public void event(){    // do event
                plant.tickEvent(x1+col*groundDelta+groundDelta/2, 
                                y2+row*groundDelta+groundDelta/2);
            }
        }
        class GZombie{
            public Zombie zombie;
            public int posX;              // position
            public int posY;
            public int row;
            int tickTime;
            int tickTimeRaw;
            //public boolean active;         // alive and inground
            public GZombie(Zombie z, int t, int x, int y, int r){
                //active = false;
                zombie = z;
                tickTime = t;
                tickTimeRaw = t;
                posX = x;
                posY = y;
                //active = true;
                row = r;
            }
            public void move(){
                // if attach plant, eat, else, go
                posX -= (int)(zombie.getMvSpd()*groundDelta*delta / 1000.0f);
            }
        }

        public boolean setPlant(String pltName, int r, int c){
            if(plants[r][c].plant == null){
                plants[r][c].plant = PlantList.generate(pltName);
                sun -= plants[r][c].plant.getCost();
                plants[r][c].tickTime = (int)(1000.0f/plants[r][c].plant.getSpeed());
                System.out.println(plants[r][c].plant.getSpeed());
                plants[r][c].tickTimeRaw = plants[r][c].tickTime;
                return true;
            }
            return false;
        }
        void hurtZombie(GZombie z, int num){
            if(z.zombie.hurt(num) <= 0){
                zombieDeath(z);
            }
        }
        void zombieDeath(GZombie z){
            zombies.remove(z);
        }
        GZombie checkDuangZombie(Tnt t){
            for(GZombie z : zombies){
                if(t.y - (z.posY + groundDelta/2) < groundDelta/2 &&
                   t.y - (z.posY + groundDelta/2) > (-1)*groundDelta/2 &&
                   t.x - (z.posX + groundDelta/2) < groundDelta/2 &&
                   t.x - (z.posX + groundDelta/2) > (-1)*groundDelta/2){
                    return z;
                }   
            }
            return null;
        }
        GPlant checkEatPlant(GZombie z){
            int col = (z.posX - x1) / groundDelta;
            if(0 <= col && col <= 8){
                if(plants[z.row][col].plant == null){
                    return null;
                }else{
                    return plants[z.row][col];
                }
            }else{
                return null;
            }
        }
        void hurtPlant(GPlant p, int num){
            if(p.plant.hurt(num) <= 0){
                plantDeath(p);
            }
        }
        void plantDeath(GPlant p){
            p.plant = null;
        }
    }
}
