import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FightFrame extends JDialog implements IReporter{

    private FightStatTable _fsTable;
    private GladStat[] _filteredList;

    // Panels
    private JPanel _dialogPanel;
    private JPanel _northPanel;
    private JPanel _centerPanel;
    private JPanel _northPanelMid;
    private JPanel _northPanelLow;

    // Controls
    private JTextArea _fightReport;
    private JComboBox<GladStat> _selectFirstGS;
    private JComboBox<GladStat> _selectSecondGS;
    private JButton _startFight;


    public FightFrame(JFrame parentFrame, FightStatTable fsTable) {
        super(parentFrame, 	"Kampfarena", true);

        _fsTable = fsTable;
        _filteredList = _fsTable.getFilteredTable();

        _dialogPanel = new JPanel(new BorderLayout());
        _northPanel = new JPanel(new GridLayout(3,1));
        _centerPanel = new JPanel(new GridLayout(1, 3));

        _fightReport = new JTextArea("");
        _fightReport.setEditable(false);
        JScrollPane scrollFightReport = new JScrollPane(_fightReport);
        _centerPanel.add(scrollFightReport);

        _northPanel.add(new JLabel(""));
        _northPanelMid = new JPanel(new FlowLayout());
        _selectFirstGS = new JComboBox<GladStat>(_filteredList);
        _northPanelMid.add(_selectFirstGS);
        _northPanelMid.add(new JLabel("VS."));
        _selectSecondGS = new JComboBox<GladStat>(_filteredList);
        _northPanelMid.add(_selectSecondGS);
        _northPanel.add(_northPanelMid);
        _northPanelLow = new JPanel(new FlowLayout());
        _startFight = new JButton("Kämpft!");
        _startFight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(_selectFirstGS.getSelectedItem().equals(_selectSecondGS.getSelectedItem())) {
                        JOptionPane.showMessageDialog(FightFrame.this, "Bitte zwei unterschiedliche Gladiatoren auswählen.", "Fehler", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    startFight((GladStat)_selectFirstGS.getSelectedItem(), (GladStat)_selectSecondGS.getSelectedItem());
                    _startFight.setEnabled(false);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        _northPanelLow.add(_startFight);
        _northPanel.add(_northPanelLow);

        _dialogPanel.add(_northPanel, java.awt.BorderLayout.NORTH);
        _dialogPanel.add(_centerPanel, java.awt.BorderLayout.CENTER);

        add(_dialogPanel);
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    public void giveNewMessage(String message) {
        _fightReport.append(message);
    }

    public void startFight(GladStat gs1, GladStat gs2) {
        boolean fightRunning = true;
        Arena arena = new Arena(gs1, gs2, (IReporter) this);

        giveNewMessage(gs1.getName()+"\t vs \t"+gs2.getName()+"\n");
        while(fightRunning) {
            fightRunning = arena.startRound();
        }
        _fsTable.sortTable();
    }
}
