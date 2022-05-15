public class Dice {
    private int _sides;

    public Dice(int sides) {
        _sides = sides;
    }

    int roll() {
        return (int)((Math.random() * this._sides) + 1);
    }
}