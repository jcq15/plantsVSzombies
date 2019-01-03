package pvz;

import java.awt.Image;
import java.io.*;
import javax.imageio.*;
import java.util.LinkedList;

//register all kinds of plants, don't instant it!
final class PlantList{
    static String[] names;
    static Image[] images;
    static Image[] tntImgs;
    static Image sunImg;
    static int kinds;
    
    static{
        names = new String[4];
        names[0] = "Pea Shooter";
        names[1] = "Frozen Shooter";
        names[2] = "Wall Nut";
        names[3] = "Sun Flower";
        kinds = names.length;
        images = new Image[kinds];
        tntImgs = new Image[2];
        try{
            images[0] = ImageIO.read(new File("assets/image/PeaShooter.png"));
            images[1] = ImageIO.read(new File("assets/image/IceShooter.png"));
            images[2] = ImageIO.read(new File("assets/image/Nut.png"));
            images[3] = ImageIO.read(new File("assets/image/Sunflower.png"));

            tntImgs[0] = ImageIO.read(new File("assets/image/OrdinaryBullet.gif"));
            tntImgs[1] = ImageIO.read(new File("assets/image/IceBullet.gif"));
        
            sunImg = ImageIO.read(new File("assets/image/Sun.png"));
        }catch(Exception e){
            System.out.println("Image not found!");
        }
    }

    // change string to index
    static int str2index(String s){
        for(int i=0;i<kinds;i++){
            if(s.equals(names[i])){
                return i;
            }
        }
        return -1;          // error
    }

    private PlantList(){}   // can't construct

    // generate a plant
    public static Plant generate(String name){
        if(name.equals(names[0])){
            return new PeaShooter();
        }else if(name.equals(names[1])){
            return new FrozenShooter();
        }else if(name.equals(names[2])){
            return new WallNut();
        }else if(name.equals(names[3])){
            return new SunFlower();
        }else{
            System.out.println("Wrong! Wanna generate plant " + name);
            return null;
        }
    }
}

abstract class Tnt{
    public int x;
    public int y;
    public Image im;
    public double speed;

    public Tnt(int x, int y){
        speed = 1;
        this.x = x;
        this.y = y;
    }
}

class OrdTnt extends Tnt{
    public OrdTnt(int x, int y){
        super(x, y);
        im = PlantList.tntImgs[0];
    }
}

class FrozenTnt extends Tnt{
    public FrozenTnt(int x, int y){
        super(x, y);
        im = PlantList.tntImgs[1];
    }
}

abstract class Plant{
    int health;
    int cost;
    String name;
    Image im;
    float speed;

    //default properties
    public Plant(){
        health = 100;
    }

    public int hurt(int num){
        health -= num;
        return health;
    }

    public float getSpeed(){
        return speed;
    }
    public Image getImg(){
        return im;
    }
    public int getCost(){
        return cost;
    }
    public String getName(){
        return name;
    }
    public void tickEvent(int x, int y){

    }
}

// plants which like pea
abstract class PeaPlant extends Plant{
    int atk;        // attack number 
    LinkedList<Tnt> tnts;
    
    public PeaPlant(){
        speed = 1.0f; // one second attack how many times
        atk = 10;
        tnts = new LinkedList<Tnt>();
    }

    public LinkedList<Tnt> getTnt(){
        return tnts;
    }
    public int getAtk(){
        return atk;
    }

    //hit skill
    public void skill(Zombie zmb){
        System.out.println("I hit someone!");
    }

    @Override
    public void tickEvent(int x, int y){

    }
}

// pea shooter
class PeaShooter extends PeaPlant{
    public PeaShooter(){
        cost = 100;
        name = PlantList.names[0];
        im = PlantList.images[0];
    }

    @Override
    public void tickEvent(int x, int y){
        tnts.add(new OrdTnt(x, y));     // Li Yunlong, you shoot!
    }
}

// froze pea shooter
class FrozenShooter extends PeaPlant{
    public FrozenShooter(){
        cost = 150;
        name = PlantList.names[1];
        atk = 8;
        im = PlantList.images[1];
    }

    @Override
    public void skill(Zombie zmb){
        zmb.changeSpeed(0.5f);
    }
    @Override
    public void tickEvent(int x, int y){
        tnts.add(new FrozenTnt(x, y));     // Li Yunlong, you shoot!
    }
}

// plants like a wall
abstract class WallPlant extends Plant{}

// wall nut
class WallNut extends WallPlant{
    public WallNut(){
        health = 300;
        speed = 0.000001f;   // 1e-06, so 1000/speed = 1e09 < 2147483647
        cost = 50;
        name = PlantList.names[2];
        im = PlantList.images[2];
    }
}

// plants generate sun
abstract class SunPlant extends Plant{
    int sun;        // how many sun
    int accumSun;   // accumulated sun now
    boolean haveSunLogo;
    //float speed;    // 1s generate how many times

    public SunPlant(){
        haveSunLogo = false;
        accumSun = 0;
    }

    public boolean haveSun(){
        return haveSunLogo;
    }
    public int takeSun(){
        int temp = accumSun;
        accumSun = 0;
        haveSunLogo = false;
        return temp;
    }
}

// sunflower
class SunFlower extends SunPlant{
    public SunFlower(){
        cost = 50;
        name = PlantList.names[3];
        sun = 25;
        speed = 0.1f;
        im = PlantList.images[3];
    }
    @Override
    public void tickEvent(int x, int y){
        haveSunLogo = true;
        accumSun += sun;
    }
}