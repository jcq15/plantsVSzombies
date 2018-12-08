package pvz;

public class GameController{
    int numOfZombie;
    int frequencyOfZombie;  //how many zombies each second
    int numOfPlant;         //how many plants now

    Zombie[] zombies;
    Plant[][] plants;       //the playground

    public GameController(int n, int f){
        numOfZombie = n;
        frequencyOfZombie = f;
        generateZombie(numOfZombie);
        plants = new Plant[6][9];    //6 lines
        for(int i=0;i<6;i++){
            for(int j=0;j<9;j++){
                plants[i][j] = null; //null means no plant here
            }
        }
    }

    // call this each Time Clock
    public void tick(){
        System.out.println("tick was called!");
        // plant's activity: attack/generate sun
        // zombie's activity: go/eat
        // playground's activity: generate zombie/sun from the sky
    }

    void generateZombie(int num){
        System.out.println("generate zombies!");
    }
}