public class Arena {

    private GladStat _gs1;
    private GladStat _gs2;

    private Gladiator _glad1;
    private Gladiator _glad2;

    private IReporter _reporter;


    public Arena(GladStat gs1, GladStat gs2, IReporter reporter) {
        _gs1 = gs1;
        _gs2 = gs2;

        _glad1 = gs1.getGladiator();
        _glad2 = gs2.getGladiator();

        _reporter = reporter;
    }

    public boolean startRound() {
        Dice d6 = new Dice(6);

        Gladiator attacker = _glad1;
        Gladiator defender = _glad2;
        Gladiator gladTemp = null;

        GladStat attackerStat = _gs1;
        GladStat defenderStat = _gs2;
        GladStat statTemp = null;

        for (int i=0; i<2; i++) {
            int hitPoints = 0;

            if (attacker.attack() == true) {
                attacker.setStatus("X");
                if (defender.parry() == true) {
                    defender.setStatus("O");
                } else {
                    defender.setStatus("-");
                    hitPoints = d6.roll();
                    switch(defender.takeDamage(hitPoints)) {

                        case "ko":
                            printStatus();
                            _reporter.giveNewMessage(defender.getName()+" erleidet "+hitPoints+" Schaden.\n");
                            _reporter.giveNewMessage(defender.getName()+" ist kampfunfaehig.");
                            updateGladStat(attackerStat, defenderStat, true);
                            return false;

                        case "dead":
                            printStatus();
                            _reporter.giveNewMessage(defender.getName()+" erleidet "+hitPoints+" Schaden.\n");
                            _reporter.giveNewMessage(defender.getName()+" ist tot.");
                            updateGladStat(attackerStat, defenderStat, false);
                            return false;

                        case "":
                            break;

                        default:
                            _reporter.giveNewMessage("FEHLER");

                    }
                }
            } else {
                attacker.setStatus("-");
                defender.setStatus("");
            }
            printStatus();
            if (hitPoints != 0) {
                _reporter.giveNewMessage(defender.getName()+" erleidet "+hitPoints+" Schaden.\n");
            }
            if (i == 0) {
                //attacker und defender tauschen.
                gladTemp = attacker;
                attacker = defender;
                defender = gladTemp;

                statTemp = attackerStat;
                attackerStat = defenderStat;
                defenderStat = statTemp;
            }
        }
        return true;	//Runde ist vorbei.
    }

    private void printStatus() {
        _reporter.giveNewMessage(_glad1.getStatus()+"\t\t"+_glad2.getStatus()+"\n");
    }

    public void updateGladStat(GladStat winner, GladStat loser, boolean isAlive) {
        winner.updateWinCount();
        loser.updateDefeatCount();
        loser.setStatus(isAlive);

        winner.calcWinRate();
        loser.calcWinRate();
    }
}
