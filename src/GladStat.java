public class GladStat {
    private String _name;						// Name
    private Gladiator _glad;
    private int _fightCount ;					// Anzahl gemachter Kaempfe
    private int _winCount;						// Anzahl eigener Siege
    private int _defeatCount;					// Anzahl eigener Niederlagen
    private float _winRate;						// Siegquote
    private boolean _alive;						// Status

    private static final String[] GLAD_NAMES = {"Spartakus", "Dullius", "Brian", "Murmillo", "Pontarius", "Scaeva", "Brutus", "Scissor", "Gladiatrix", "Provocator", "Thraex", "Gallus", "Commodus", "Flamma", "Pikachu", "Pummeluf", "Antivirus", "Cosinus", "Erdnus", "Taubenus"};

    public GladStat(String name) {
        _name = name;
        _glad = new Gladiator(name);
        _fightCount = 0;
        _winCount = 0;
        _defeatCount = 0;
        _alive = true;
    }

    public GladStat(String name, int winCount, int defeatCount, boolean alive) {
        _name = name;
        _glad = new Gladiator(name);
        _winCount = winCount;
        _defeatCount = defeatCount;
        _fightCount = winCount+defeatCount;
        _alive = alive;
        if(defeatCount != 0) {
            _winRate = (float) winCount / defeatCount;
        } else {
            _winRate = 0;
        }
    }

    // Getter
    public String getName() {
        return _name;
    }

    public Gladiator getGladiator() {
        return _glad;
    }

    public int getFightCount() {
        return _fightCount;
    }

    public int getWinCount() {
        return _winCount;
    }

    public int getDefeatCount() {
        return _defeatCount;
    }

    public float getWinRate() {
        return _winRate;
    }

    public boolean getStatus() {
        return _alive;
    }

    public static String getRandomName() {
        Dice dice = new Dice(GLAD_NAMES.length);
        return GLAD_NAMES[dice.roll()-1];
    }


    // Setter

    public void resetGladStat() {
        _fightCount = 0;
        _winCount = 0;
        _defeatCount = 0;
        _winRate = 0;
        _alive = true;
    }

    public void updateWinCount() {
        _winCount += 1;
    }

    public void updateDefeatCount(){
        _defeatCount += 1;
    }

    public void updateFightCount() {
        _fightCount += 1;
    }

    public void setStatus(boolean isAlive) {
        _alive = isAlive;
    }

    public int compareTo(GladStat gl) {
        // Wenn der aktuelle Gladiator (".this") besser abschneidet, wird ein positiver int zurueckgegeben, sonst ein negativer.

        if(this._winCount > gl._winCount) {							// Vergleich ueber Anzahl Siege
            return 1;
        } else if(this._winCount == gl._winCount) {
            if(this._winRate > gl._winCount) {						// Vergleich ueber Siegquote
                return 1;
            } else if(this._winRate == gl._winCount) {
                if(this._alive && (!gl._alive)) {						// Vergleich ueber Status
                    return 1;
                } else if(this._alive && gl._alive) {					// Vergleich ueber Namen (lexikographisch)
                    if ((this._name).compareTo(gl._name) < 0){
                        return 1;
                    }
                }
            }
        }
        return -1;
    }

    public void calcWinRate() {
        if(_defeatCount != 0) {
            _winRate = _winCount / _defeatCount;
        } else {
            _winRate = _winCount; 					//  Erscheint mir der sinnvollste Wert bei null Niederlagen.
        }

    }

    @Override
    public String toString() {
        return _name;
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._name);
        sb.append(";");
        sb.append(_winCount);
        sb.append(";");
        sb.append(_defeatCount);
        sb.append(";");
        if(_alive) {
            sb.append("lebendig");
        }else {
            sb.append("tot");
        }

        return sb.toString();
    }
}