package pvz;

//register all kinds of zombies, don't instant it!
final class ZombieList{
    static int kinds = 4;
    static String[] names = {"Normal Zombie", "Iron Bullet Zombie", 
                             "Oral Ball Zombie", "Read Paper Zombie"};
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

    float spdChangeCoef;    //use this coefficient to change speed
    //float spdChangeTime;    //still how long

    String image;           //where is the image

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
}

// normal
class NormalZombie extends Zombie{
    public NormalZombie(){
        name = ZombieList.names[0];
    }
}

class IronBulletZombie extends Zombie{
    public IronBulletZombie(){
        health = 200;
        name = ZombieList.names[1];
    }
}

class OralBallZombie extends Zombie{
    public OralBallZombie(){
        atk = 10;
        moveSpd = 0.5f;
        name = ZombieList.names[2];
    }
}

class ReadPaperZombie extends Zombie{
    public ReadPaperZombie(){
        health = 50;
        atk = 50;
        moveSpd = 0.33f;
        name = ZombieList.names[3];
    }
}