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

    // Game control
    int numOfZombie;
    int frequencyOfZombie;  //how many zombies each second
    int numOfPlant;         //how many plants now
    Zombie[] zombies;
    Plant[][] plants;       //the playground

    void init(){
        zombies = ZombieList.generate(numOfZombie);
        plants = new Plant[6][9];    //6 lines
        for(int i=0;i<6;i++){
            for(int j=0;j<9;j++){
                plants[i][j] = null; //null means no plant here
            }
        }
    }

    public MainWindow(){
        super("Plants VS Zombies");

        plantOnMouse = "";      // at first, no plant on mouse

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
                if(plantOnMouse.equals(btnLabel)){
                    plantOnMouse = "";
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

    // game control
    public void setDifficult(int n, int f){
        numOfZombie = n;
        frequencyOfZombie = f;
    }

    // call this each Time Clock
    public void tick(){
        // System.out.println("tick was called!");
        // plant's activity: attack/generate sun
        // zombie's activity: go/eat
        // playground's activity: generate zombie/sun from the sky
    }

    // this point is filled or not
    public boolean isFilled(int index){
        return (plants[index/6][index%9] == null);
    }

    void generateZombie(int num){
        System.out.println("generate zombies!");
    }
}