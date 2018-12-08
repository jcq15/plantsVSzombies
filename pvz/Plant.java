package pvz;

//register all kinds of plants, don't instant it!
final class PlantList{
    static int kinds = 4;
    static String[] names = {"Pea Shooter", "Frozen Shooter", 
                             "Wall Nut", "Sun Flower"};
    private PlantList(){}   // can't construct
}

abstract class Plant{
    int health;
    int cost;
    String name;

    //default properties
    public Plant(){
        health = 100;
    }
}

// plants which like pea
abstract class PeaPlant extends Plant{
    int atk;        // attack number 
    float spd;        // one second attack how many times
    
    public PeaPlant(){
        spd = 1.0f;
        atk = 10;
    }

    //hit skill
    public void skill(Zombie zmb){
        System.out.println("I hit someone!");
    }
}

// pea shooter
class PeaShooter extends PeaPlant{
    public PeaShooter(){
        cost = 100;
        name = PlantList.names[0];
    }
}

// froze pea shooter
class FrozenShooter extends PeaPlant{
    public FrozenShooter(){
        cost = 150;
        name = PlantList.names[1];
        atk = 8;
    }

    @Override
    public void skill(Zombie zmb){
        zmb.changeSpeed(0.5f);
    }
}

// plants like a wall
abstract class WallPlant extends Plant{}

// wall nut
class WallNut extends WallPlant{
    public WallNut(){
        health = 300;
        cost = 50;
        name = PlantList.names[2];
    }
}

// plants generate sun
abstract class SunPlant extends Plant{
    int sun;        // how many sun
    float speed;    // 1s generate how many times
}

// sunflower
class SunFlower extends SunPlant{
    public SunFlower(){
        cost = 50;
        name = PlantList.names[3];
        sun = 25;
        speed = 0.1f;
    }
}