package pvz;

import java.awt.Image;
import java.io.*;
import javax.imageio.*;

//register all kinds of zombies, don't instant it!
final class ZombieList{
    static String[] names;
    static int kinds;
    static Image[] images;

    static{
        names = new String[4];
        names[0] = "Normal Zombie";
        names[1] = "Iron Bullet Zombie"; 
        names[2] = "Oral Ball Zombie";
        names[3] = "Read Paper Zombie";
        kinds = 4;
        images = new Image[4];
        try{
            images[0] = ImageIO.read(new File("assets/image/OrdinaryZombie.png"));
            images[1] = ImageIO.read(new File("assets/image/BarrelZombie.png"));
            images[2] = ImageIO.read(new File("assets/image/FootballZombie.png"));
            images[3] = ImageIO.read(new File("assets/image/NewspaperZombie.png"));
        }catch(Exception e){
            System.out.println("Image not found!");
        }
    }

    private ZombieList(){}   // can't construct

    //generate a zombie
    public static Zombie[] generate(int n){
        Zombie[] wave = new Zombie[n];
        for(int i=0;i<n;i++){
            int r = (int)(Math.random()*kinds);
            switch(r){
                case 0:
                    wave[i] = new NormalZombie();
                    break;
                case 1:
                    wave[i] = new IronBulletZombie();
                    break;
                case 2:
                    wave[i] = new OralBallZombie();
                    break;
                case 3:
                    wave[i] = new ReadPaperZombie();
                    break;
                default:
                    System.out.println("Wrong! Wanna generate zombie number " + r);
            }
        }
        System.out.println("successful generate " + n + " zombies!");
        return wave;
    }
}

abstract class Zombie{
    int atk;
    float atkSpd;          //how many attacks per second
    int health;
    float moveSpd;
    String name;
    Image im;

    float spdChangeCoef;    //use this coefficient to change speed
    //float spdChangeTime;    //still how long

    // default settings
    public Zombie(){
        atk = 20;
        atkSpd = 1.0f;
        health = 100;
        moveSpd = 0.2f;
        spdChangeCoef = 1.0f;
    }

    public void changeSpeed(float coef){
        spdChangeCoef = coef;
        System.out.println("Oh no! I was frozen!");
    }
    public float getAtkSpd(){
        return atkSpd;
    }
    public Image getImg(){
        return im;
    }
}

// normal
class NormalZombie extends Zombie{
    public NormalZombie(){
        name = ZombieList.names[0];
        im = ZombieList.images[0];
    }
}

class IronBulletZombie extends Zombie{
    public IronBulletZombie(){
        health = 200;
        name = ZombieList.names[1];
        im = ZombieList.images[1];
    }
}

class OralBallZombie extends Zombie{
    public OralBallZombie(){
        atk = 10;
        moveSpd = 0.5f;
        name = ZombieList.names[2];
        im = ZombieList.images[2];
    }
}

class ReadPaperZombie extends Zombie{
    public ReadPaperZombie(){
        health = 50;
        atk = 50;
        moveSpd = 0.33f;
        name = ZombieList.names[3];
        im = ZombieList.images[3];
    }
}