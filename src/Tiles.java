import java.util.ArrayList;
import java.util.Arrays;

public class Tiles {
    private boolean isPlayedVertical;
    private boolean isPlayedHorizontal;
    private int letterPoint;
    private char letter = '\0';
    private boolean isDoubleLetterMult;
    private boolean isWordMultiplier;
    private int doubleLetterMult;
    private int wordMultiplier;
    private int[] position = new int[2];

    public String getTileString() {
        return tileString;
    }

    public void setTileString(String tileString) {
        this.tileString = tileString;
    }

    private String tileString;

    public Tiles(boolean isPlayedVertical, boolean isPlayedHorizontal, int letterPoint, char letter,
                 boolean isDoubleLetterMult, boolean isWordMultiplier, int doubleLetterMult, int wordMultiplier) {
        this.isPlayedVertical = isPlayedVertical;
        this.isPlayedHorizontal = isPlayedHorizontal;
        this.letterPoint = letterPoint;
        this.letter = letter;
        this.isDoubleLetterMult = isDoubleLetterMult;
        this.isWordMultiplier = isWordMultiplier;
        this.doubleLetterMult = doubleLetterMult;
        this.wordMultiplier = wordMultiplier;
    }

    public Tiles(){
        this.isPlayedVertical = false;
        this.isPlayedHorizontal = false;
        this.letterPoint = 0;
        this.letter = '\0';
        this.isDoubleLetterMult = false;
        this.isWordMultiplier = false;
        this.doubleLetterMult = 1;
        this.wordMultiplier = 1;
    }

    // Getters and setters
    public boolean isPlayedVertical() {
        return isPlayedVertical;
    }

    public int[] getPosition() {return position;}

    public void setPosition(int x, int y) {
        this.position[0] = x;
        this.position[1] = y;
    }

    public void setPlayedVertical(boolean playedVertical) {
        isPlayedVertical = playedVertical;
    }

    public boolean isPlayedHorizontal() {
        return isPlayedHorizontal;
    }

    public void setPlayedHorizontal(boolean playedHorizontal) {
        isPlayedHorizontal = playedHorizontal;
    }

    public int getLetterPoint() {
        return letterPoint;
    }

    public void setLetterPoint(int letterPoint) {
        this.letterPoint = letterPoint;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public boolean isDoubleLetterMult() {
        return isDoubleLetterMult;
    }

    public void setDoubleLetterMult(boolean doubleLetterMult) {
        isDoubleLetterMult = doubleLetterMult;
    }

    public boolean isWordMultiplier() {
        return isWordMultiplier;
    }

    public void setisWordMultiplier(boolean wordMultiplier) {
        isWordMultiplier = wordMultiplier;
    }

    public int getDoubleLetterMult() {
        return doubleLetterMult;
    }

    public void setDoubleLetterMult(int doubleLetterMult) {
        this.doubleLetterMult = doubleLetterMult;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }

    public void setWordMultiplier(int wordMultiplier) {
        this.wordMultiplier = wordMultiplier;
    }

    public boolean hasLetter(){
        if(letter == '\0'){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (letter == '\0'){
            return tileString;
        }
        if ( position[0] == 0 && this.hasLetter()){
            return letter+"";
        }else if(this.hasLetter()){
            return letter+" ";
        }
        return letter+"";
    }

    public String getStats(){
        return "Tiles{" +
                "isPlayedVertical=" + isPlayedVertical +
                ", isPlayedHorizontal=" + isPlayedHorizontal +
                ", letterPoint=" + letterPoint +
                ", letter=" + letter +
                ", isDoubleLetterMult=" + isDoubleLetterMult +
                ", isWordMultiplier=" + isWordMultiplier +
                ", doubleLetterMult=" + doubleLetterMult +
                ", wordMultiplier=" + wordMultiplier +
                ", position=" + Arrays.toString(getPosition()) +
                ", String=" + tileString +
                '}';
    }

    public static void main(String[] args) {
//        ArrayList<Tiles> bagoftiles = new ArrayList<>();
//        System.out.println(bagoftiles.size());
//
//        String letters = "abcdefghijklmnopqrstuvwxyz";
//
//        for (char letter : letters.toCharArray()) {
//            Tiles t = new Tiles(); // Assuming you have a constructor in Tiles class
//            bagoftiles.add(t);
//        }
//
//        System.out.println(bagoftiles.size());



    }
}
