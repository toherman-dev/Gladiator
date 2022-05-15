import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatPanel extends JPanel{

    private JLabel _gladName;
    private JLabel _ap;
    private JLabel _pa;
    private JLabel _av;
    private JLabel _hp;

    public StatPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        _gladName = new JLabel();
        _ap = new JLabel();
        _pa = new JLabel();
        _av = new JLabel();
        _hp = new JLabel();
        add(_gladName);
        add(_hp);
        add(_ap);
        add(_pa);
        add(_av);
    }

    public void update(GladStat gs) {
        _gladName.setText( gs.getName());

        int[] stats = gs.getGladiator().getStats();
        _ap.setText("Angriffspunkte: "+stats[0]);
        _pa.setText("Paradepunkte: "+stats[1]);
        _av.setText("Rüstungswert: "+stats[2]);
        _hp.setText("Lebenspunkte: "+stats[3]);

        repaint();
    }
}
