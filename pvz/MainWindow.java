package pvz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame{
    Timer clock;

    // GUI
    int width, height;      // screen
    JPanel pContainer;      // control the panel's visibility
    CardLayout layout;

    JPanel pEntry;          // start window
    JPanel p0Main;          // main window
    JButton btnStart;       // start btn

    JPanel p1Ground;        // 6*9 ground
    JButton[] btnGround;
    ListenGround[] lsnGround;

    JPanel p1PlantLs;
    JButton[] btnPlantLs;

    String plantOnMouse;    // now plant on mouse

    Ground ground;          // ground

    void init(){
        ground = new Ground(100);
    }

    public MainWindow(){
        super("Plants VS Zombies");

        plantOnMouse = null;      // at first, no plant on mouse

        clock = new Timer(50, new ListenTimer());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        width = scrnsize.width;
        height = scrnsize.height;

        layout  = new CardLayout();
        pContainer = new JPanel(layout);

        pEntry = new JPanel(new BorderLayout());
        btnStart = new JButton("Start");

        ActionListener lsnStart = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                layout.show(pContainer, "c2");
                clock.start();
            }
        };
        btnStart.addActionListener(lsnStart);
        
        pEntry.add(btnStart);

        // main window
        p0Main = new JPanel();

        // plant list at top
        ActionListener ListenPlantLs = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String btnLabel = ((JButton) e.getSource()).getText();
                // if same, put return, else, get
                if(plantOnMouse != null && plantOnMouse.equals(btnLabel)){
                    plantOnMouse = null;
                    // drawout from mouse
                }else{
                    plantOnMouse = btnLabel;
                    System.out.println(plantOnMouse);
                    // draw from mouse
                }
            }
        };
        p1PlantLs = new JPanel(new GridLayout(1,PlantList.kinds));
        btnPlantLs = new JButton[PlantList.kinds];
        for(int i=0;i<PlantList.kinds;i++){
            btnPlantLs[i] = new JButton(PlantList.names[i]);
            btnPlantLs[i].addActionListener(ListenPlantLs);
            p1PlantLs.add(btnPlantLs[i]);
        }

        // play ground
        p1Ground = new JPanel(new GridLayout(6,9));
        p1Ground.setPreferredSize(new Dimension((int)(width*0.8), (int)(height*0.8)));
        
        btnGround = new JButton[54];
        lsnGround = new ListenGround[54];
        for(int i=0;i<54;i++){
            btnGround[i] = new JButton();
            lsnGround[i] = new ListenGround(i);
            btnGround[i].addActionListener(lsnGround[i]);
            p1Ground.add(btnGround[i]);
        }

        p0Main.add(p1PlantLs, BorderLayout.NORTH);
        p0Main.add(p1Ground);

        pContainer.add(pEntry, "c1");
        pContainer.add(p0Main, "c2");
        layout.show(pContainer, "c1");

        init();     // initial something about game

        setUndecorated(true);
        setSize(width, height);
        setSize(1000,1000);
        setContentPane(pContainer);
        setVisible(true);
    }

    //inner class about btnGround listener
    class ListenGround implements ActionListener{
        int index;      //0-53
        public ListenGround(int n){
            index = n;
        }
        @Override
        public void actionPerformed(ActionEvent e){
            if(plantOnMouse != null){
                if(ground.setPlant(plantOnMouse, index/9, index%9)){
                    btnGround[index].setText(plantOnMouse);
                    plantOnMouse = null;
                }
            }
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
        MainWindow app = new MainWindow();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // call this each Time Clock
    public void tick(){
        // System.out.println("tick was called!");
        // plant's activity: attack/generate sun

        // zombie's activity: go/eat
        // playground's activity: generate zombie/sun from the sky
    }
}

class Ground{
    int numOfZombie;

    public Ground(int n){   // how many zombies
        numOfZombie = n;
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
        Zombie zombie;
        int posX;              // position
        int posY;
        int tickTime;
        boolean active;         // alive and inground
        public GZombie(Zombie z, int t){
            active = false;
            zombie = z;
            tickTime = t;
        }
    }
    GPlant[][] plants;
    GZombie[] zombies;

    public boolean setPlant(String pltName, int r, int c){
        if(plants[r][c].plant == null){
            plants[r][c].plant = PlantList.generate(pltName);
            plants[r][c].tickTime = (int)(1000.0f/plants[r][c].plant.getSpeed());
            return true;
        }
        return false;
    }

}