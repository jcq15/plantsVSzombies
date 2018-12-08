package pvz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame{
    GameController controller;
    Timer clock;

    int width, height;      // screen
    JPanel pContainer;      // control the panel's visibility
    CardLayout layout;

    JPanel pEntry;          // start window
    JPanel p0Main;          // main window
    JButton btnStart;       // start btn

    JPanel p1Ground;        // 6*9 ground
    JButton[] btnGround;
    ListenGround[] lsnGround;

    public MainWindow(){
        super("Plants VS Zombies");

        controller = new GameController(10,1);
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

        p0Main.add(p1Ground);

        pContainer.add(pEntry, "c1");
        pContainer.add(p0Main, "c2");
        layout.show(pContainer, "c1");

        setUndecorated(true);
        //setSize(width, height);
        setSize(500,500);
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
            // do something
        }
    }

    // call gamecontroller 
    class ListenTimer implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println("call tick!");
            controller.tick();
        }
    }

    public static void main(String[] args){
        MainWindow app = new MainWindow();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}