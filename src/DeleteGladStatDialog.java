import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DeleteGladStatDialog extends JDialog{

    private JPanel _dialogPanel;
    private JButton _accept;
    private JButton _cancel;
    private JComboBox<GladStat> _selectGS;

    private FightStatTable _fsTable;

    public DeleteGladStatDialog(JFrame parentFrame, FightStatTable fsTable) {
        super(parentFrame, "Gladiator löschen", true);

        _fsTable = fsTable;

        _selectGS = new JComboBox<GladStat>(_fsTable.getTable());

        _dialogPanel = new JPanel(new BorderLayout());
        _dialogPanel.add(_selectGS, java.awt.BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(1,2));
        _accept = new JButton("Annehmen");
        _accept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    GladStat selectedGS = (GladStat) _selectGS.getSelectedItem();
                    int option = JOptionPane.showConfirmDialog(DeleteGladStatDialog.this, "Sind Sie sicher, dass sie "+selectedGS.getName()+" löschen möchten?", "Achtung", JOptionPane.WARNING_MESSAGE);
                    if(option == JOptionPane.OK_OPTION) {
                        _fsTable.removeGladStat(selectedGS);
                    }
                    dispose();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        southPanel.add(_accept);
        _cancel = new JButton("Abbrechen");
        _cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        southPanel.add(_cancel);
        _dialogPanel.add(southPanel, java.awt.BorderLayout.SOUTH);

        add(_dialogPanel);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(400, 200);
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}
