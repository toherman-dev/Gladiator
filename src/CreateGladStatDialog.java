import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CreateGladStatDialog extends JDialog{

    private FightStatTable _fsTable;
    private JPanel _dialogPanel;
    private JTextArea _nameInput;
    private JButton _randomName;
    private JButton _accept;
    private JButton _cancel;

    public CreateGladStatDialog(JFrame parentFrame, FightStatTable fsTable) {
        super(parentFrame, "Neuer Gladiator", true);
        _fsTable = fsTable;

        _dialogPanel = new JPanel(new BorderLayout());

        add(_dialogPanel);

        _dialogPanel.add(new JLabel("Name: "), java.awt.BorderLayout.NORTH);
        _nameInput = new JTextArea(1,1);
        _nameInput.setText(GladStat.getRandomName());
        _dialogPanel.add(_nameInput,  java.awt.BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(1,2));
        _dialogPanel.add(southPanel, java.awt.BorderLayout.SOUTH);

        _randomName = new JButton("Zufälliger Name");
        _randomName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _nameInput.setText(GladStat.getRandomName());
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        _accept = new JButton("Annehmen");
        _accept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(_nameInput.getText().trim().length() != 0) {
                        _fsTable.addGladStat(new GladStat(_nameInput.getText().trim()));
                        dispose();
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

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

        southPanel.add(_randomName);
        southPanel.add(_accept);
        southPanel.add(_cancel);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(400, 200);
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}
