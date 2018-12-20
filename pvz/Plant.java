package pvz;

//register all kinds of plants, don't instant it!
final class PlantList{
    static String[] names = {"Pea Shooter", "Frozen Shooter", 
                             "Wall Nut", "Sun Flower"};
    static int kinds = names.length;
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

abstract class Plant{
    int health;
    int cost;
    String name;
    float speed;

    //default properties
    public Plant(){
        health = 100;
    }
    public float getSpeed(){
        return speed;
    }
}

// plants which like pea
abstract class PeaPlant extends Plant{
    int atk;        // attack number 
    
    public PeaPlant(){
        speed = 1.0f; // one second attack how many times
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
        speed = 0.000001f;   // 1e-06, so 1000/speed = 1e09 < 2147483647
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