public class CalculateBoard {

    private final int lengthOfSpace = 160;
    private int firstSpot = -1;
    private int secondSpot = -1;

    public int calculateXCoordinate(int i){
        return (i % 3) * lengthOfSpace + 10 * (i % 3);
    }

    public int calculateYCoordinate(int i){
        return (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3);
    }

    public int calculateXOneCoordinate(){
        return firstSpot % 3 * lengthOfSpace + 10 * firstSpot % 3 + lengthOfSpace / 2;

    }

    public int calculateYOneCoordinate(){
        return (int) (firstSpot / 3) * lengthOfSpace + 10 * (int) (firstSpot / 3) + lengthOfSpace / 2;
    }

    public int calculateXTwoCoordinate(){
        return secondSpot % 3 * lengthOfSpace + 10 * secondSpot % 3 + lengthOfSpace / 2;
    }

    public int calculateYTwoCoordinate(){
        return (int) (secondSpot / 3) * lengthOfSpace + 10 * (int) (secondSpot / 3) + lengthOfSpace / 2;
    }

    public int getFirstSpot() {
        return firstSpot;
    }

    public int getSecondSpot() {
        return secondSpot;
    }

    public void setFirstSpot(int firstSpot) {
        this.firstSpot = firstSpot;
    }

    public void setSecondSpot(int secondSpot) {
        this.secondSpot = secondSpot;
    }

    public int getLengthOfSpace() {
        return lengthOfSpace;
    }
}
