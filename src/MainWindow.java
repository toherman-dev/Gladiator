import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.FlowLayout;
import java.awt.event.*;

public class MainWindow extends JFrame{

    private FightStatTable _fsTable;
    private DefaultTableModel _tableModel;
    private final JFileChooser _filechooser;

    //Controls
    private JButton _newTable;
    private JButton _resetTable;
    private JButton _saveTable;
    private JButton	_newGlad;
    private JButton _deleteGlad;
    private JButton _newFight;
    private JButton _loadTable;
    private JTable _gladTable;

    private FightFrame _fightFrame;
    private StatPanel _statPanel;

    public MainWindow() {
        super("Gladiatorenliste");
        _fsTable = new FightStatTable((IReporter)_fightFrame);

        _statPanel = new StatPanel();
        add(_statPanel, java.awt.BorderLayout.EAST);

        _filechooser = new JFileChooser();

        // JTable
        _tableModel = new DefaultTableModel(new Object[]{"Gladiator","Siege","Niederlagen","Siegquote","Status"},1);
        _gladTable = new JTable(_tableModel);
        JScrollPane tablePane = new JScrollPane(_gladTable);
        add(tablePane, java.awt.BorderLayout.CENTER);
        _gladTable.setFillsViewportHeight(true);

        updateJTable();

        JPanel eastControls = new JPanel(new FlowLayout());
        add(eastControls, java.awt.BorderLayout.NORTH);

        _newTable = new JButton("Neue Tabelle");
        eastControls.add(_newTable);
        _newTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _fsTable = new FightStatTable((IReporter)_fightFrame);
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {

                }
            }
        });

        _resetTable = new JButton("Tabelle zuruecksetzen");
        eastControls.add(_resetTable);
        _resetTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _fsTable.resetTable();
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        _loadTable = new JButton("Tabelle laden");
        eastControls.add(_loadTable);
        _loadTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int option = _filechooser.showOpenDialog(MainWindow.this);

                    if(option == JFileChooser.APPROVE_OPTION) {
                        _fsTable.loadTable(_filechooser.getSelectedFile());
                    }
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MainWindow.this, "Die Datei konnte nicht geladen werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        _saveTable = new JButton("Tabelle speichern");
        eastControls.add(_saveTable);
        _saveTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int option = _filechooser.showSaveDialog(MainWindow.this);

                    if(option == JFileChooser.APPROVE_OPTION) {
                        _fsTable.saveTable(_filechooser.getSelectedFile());
                    }
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        _newGlad = new JButton("Neuer Gladiator");
        eastControls.add(_newGlad);
        _newGlad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new CreateGladStatDialog(MainWindow.this, MainWindow.this._fsTable);
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        _deleteGlad = new JButton("Gladiator loeschen");
        eastControls.add(_deleteGlad);
        _deleteGlad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(MainWindow.this._tableModel.getRowCount() < 1) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Es gibt nicht genügend Gladiatoren.\nBitte füge weitere Gladiatoren hinzu.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new DeleteGladStatDialog(MainWindow.this, MainWindow.this._fsTable);
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        _newFight = new JButton("Neuer Kampf");
        eastControls.add(_newFight);
        _newFight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(MainWindow.this._fsTable.getFilteredTable().length < 2) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Es gibt nicht genügend lebendige Gladiatoren.\nBitte füge weitere Gladiatoren hinzu.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    MainWindow.this._fightFrame = new FightFrame(MainWindow.this, MainWindow.this._fsTable);
                    MainWindow.this.updateJTable();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        _gladTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = MainWindow.this._gladTable;
                int selectedRow = table.getSelectedRow();
                if(selectedRow >= 0) {
                    MainWindow.this._statPanel.update(MainWindow.this._fsTable.getTable()[selectedRow]);
                }
            }
        });
        /*
         * Funktioniert leider nicht so konsistent und ist wahrscheinlich auch eher nicht so performant,
         * aber wollte es trotzdem mal einbauen.
         */

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1920, 1080);
        setVisible(true);
    }

    public void updateJTable() {
        _tableModel.setRowCount(0);

        GladStat[] gsArray = _fsTable.getTable();
        for(int i=0; i<gsArray.length; i++) {
            if(gsArray[i] == null) {
                break;
            }
            GladStat gs = gsArray[i];
            _tableModel.addRow(new Object[] {gs.getName(), gs.getWinCount(), gs.getDefeatCount(),gs.getWinRate(), gs.getStatus() ? "lebendig" : "tot"});
        }
    }
}