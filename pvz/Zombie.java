package pvz;

//register all kinds of zombies, don't instant it!
final class ZombieList{
    static int kinds = 4;
    static String[] names = {"Normal Zombie", "Iron Bullet Zombie", 
                             "Oral Ball Zombie", "Read Paper Zombie"};
    private ZombieList(){}   // can't construct
}

abstract class Zombie{
    int atk;
    float atkSpd;          //how many attacks per second
    int health;
    float moveSpd;
    String name;

    float spdChangeCoef;    //use this coefficient to change speed
    //float spdChangeTime;    //still how long

    // default settings
    public Zombie(){
        spdChangeCoef = 1;
        atk = 20;
        atkSpd = 1;
        health = 100;
        moveSpd = 0.2;
        spdChangeCoef = 1;
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
        moveSpd = 0.5;
        name = ZombieList.names[2];
    }
}

class ReadPaperZombie extends Zombie{
    public ReadPaperZombie(){
        health = 50;
        atk = 50;
        moveSpd = 0.33;
        name = ZombieList.names[3];
    }
}