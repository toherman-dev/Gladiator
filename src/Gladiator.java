public class Gladiator {
    private String _name;
    private int _ap;				// Angriffspunkte (attack points)
    private int _pa;				// Paradefaehigkeit (parry ability)
    private int _av;				// Ruestungswert (armor value)
    private int _hp;				// Gesundheitspunkte (health points)
    private String _status;

    // Getter

    public String getName() {
        return _name;
    }

    public String getStatus() {
        return _status;
    }

    public int[] getStats() {
        int[] stats = {_ap, _pa, _av, _hp};

        return stats;
    }

    // Setter

    public void setStatus(String newStatus) {
        _status = newStatus;
    }

    public Gladiator(String name) {
        Dice d6 = new Dice(6);

        _name = name;
        _av = 0;
        _ap = 5 + d6.roll();
        _pa = 5 + d6.roll();
        _hp = 30 + d6.roll();
    }

    public boolean attack() {
        Dice d20 = new Dice(20);

        if ((d20.roll()) <= _ap) {
            return true;
        }
        return false;
    }

    public boolean parry() {
        Dice d20 = new Dice(20);

        if (d20.roll() <= _pa) {
            return true;
        }
        return false;
    }

    public String takeDamage(int hitPoints) {
        int damage = hitPoints - _av;
        _hp -= damage;

        if(_hp <= 0) {
            return "dead"; //Kann nicht mehr kaempfen.
        } else if (_hp <= 5) {
            return "ko"; //Kann nicht mehr kaempfen.
        }
        return "";
    }
}