import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class FightStatTable {
    private GladStat[] _table;
    private GladStat[] _filteredTable;
    private IReporter _reporter;

    public FightStatTable(IReporter reporter) {
        this._table = new GladStat[20];
        _reporter = reporter;
    }

    public void addGladStat(GladStat gs) {
		/* 	_table wird von vorne durchgegangen. Im ersten Index, der nicht auf ein GladStat-Objekt referenziert,
			wird das neue Objekt gs gespeichert.
		*/
        for(int i=0; i < (this._table).length; i++) {
            if(this._table[i] == null) {
                this._table[i] = gs;
                return;
            }
        }
        // Wenn die gesamte Laenge von _table durchgegangen wurde, ohne einen "leeren" Index zu finden, wird die Arraygroesse verdoppelt.

        GladStat[] temp = new GladStat[(this._table).length * 2];

        for(int i=0; i < (this._table).length; i++) {
            temp[i] = (this._table)[i];
        }
        if(this._table.length == 0) {
            temp[0] = gs;
        }else {
            temp[(this._table).length] = gs;	// Laenge des alten arrays _table = erster "neuer" Index von temp.
        }
        this._table = temp;
    }

    public GladStat[] getTable() {
        return this._table;
    }

    public GladStat[] getFilteredTable() {
        filterTable();
        return _filteredTable;
    }

    public void printTable() {
        StringBuilder row = new StringBuilder(100);

        sortTable();

        System.out.println("Gladiator\tSiege\tNiederlagen\tSiegquote\tStatus");
        for(int i=0; i<(this._table).length; i++) {
            if((this._table)[i] != null) {
                row.append(_table[i].getName());
                row.append("\t\t");
                row.append(_table[i].getWinCount());
                row.append("\t");
                row.append(_table[i].getDefeatCount());
                row.append("\t\t");
                row.append(_table[i].getWinRate());
                row.append("\t\t");
                if(_table[i].getStatus()) {
                    row.append("lebendig" );
                } else {
                    row.append("tot");
                }

                System.out.println(row);
                row.delete(0, row.length());
            }
        }
        System.out.println(this._table.length+" Plaetze insgesamt.");
    }

    public void sortTable() {
        for(int i = _table.length; i != 0; i--) {
            for(int j = 0; (j+1)<i; j++) {
                if(_table[j+1] != null) {
                    if(_table[j].compareTo(_table[j+1]) == -1) {
                        GladStat tmp = _table[j];
                        _table[j] = _table[j+1];
                        _table[j+1] = tmp;
                    }
                }

            }
        }
    }

    public int searchGladStat(GladStat gs) {
        sortTable();
        int j = 0;
        int k = _table.length-1;
        int m = 0;

        while(j<=k) {
            m = (j+k)/2;
            if(_table[m] == null) {
                k = m-1;
            }else if(_table[m].equals(gs)) {
                return m;
            }else if(_table[m].compareTo(gs) == -1) {
                k = m-1;
            } else {
                j = m+1;
            }
        }
        return -1;
    }

    public void removeGladStat(GladStat gs) {
        int gsIndex = searchGladStat(gs);
        int i = 0; 								// Zaehlvariable fuer for-Schleife

        if(gsIndex == -1) {						// Falls das Objekt existiert, aber nicht in FightStatTable vorhanden ist.
            System.out.println("Der gesuchte Gladiator steht nicht auf der Liste.");
            return;
        }
        for(i = gsIndex; _table.length > 1 && (_table[i+1]) != null; i++) {
            _table[i] = _table[i+1];
        }
        _table[i] = null;

        // _table halbieren, falls _table.length/2 > i
        if(i-1 <= _table.length/2) {
            GladStat[] temp = new GladStat[_table.length / 2];

            for(int j = 0; j < temp.length; j++) {
                temp[j] = (_table)[j];
            }
            _table = temp;
        }
    }

    public void resetTable() {
        for(int i = 0; i < _table.length; i++) {
            if(_table[i] == null) {
                return;
            }
            _table[i].resetGladStat();
            sortTable();
        }
    }

    public void saveTable(File saveFile) {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))){
            bw.write("Gladiator;Siege;Niederlagen;Status");
            bw.newLine();
            for(int i=0; i < _table.length; i++) {
                if(_table[i] == null) {
                    break;
                }
                bw.write(_table[i].toCSV());
                bw.newLine();
            }
            bw.flush();
        } catch(FileNotFoundException notFoundEx) {

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadTable(File saveFile) throws Exception{

        _table = new GladStat[20];

        try(BufferedReader br = new BufferedReader(new FileReader (saveFile))) {
            br.readLine();
            String line = br.readLine();
            while(line != null){
                addGladStat(processLine(line));
                line = br.readLine();
            }
        }catch (Exception ex) {
            throw ex;
        }
    }

    private GladStat processLine(String line) {
        String[] values = line.split(";");

        String name = values[0];
        int winCount = Integer.parseInt(values[1]);
        int defeatCount = Integer.parseInt(values[2]);
        boolean alive;
        if(values[3].equals("lebendig")) {
            alive = true;
        }else {
            alive = false;
        }
        return new GladStat(name, winCount, defeatCount, alive);
    }

    // Filtert alle toten Gladiatoren heraus
    public void filterTable() {
        GladStat[] temp = _table;
        int k = 0;

        // GS-Objekte mit alive == true zählen
        for(int i = 0; i<temp.length; i++) {
            if(temp[i] == null) {
                break;
            }
            if(temp[i].getStatus()) {
                k++;
            }
        }

        // Gefilterte Liste mit k Elementen
        _filteredTable = new GladStat[k];
        k = 0;

        for(int i=0; i < temp.length; i++) {
            if(temp[i] == null) {
                break;
            }
            if(temp[i].getStatus()) {
                _filteredTable[k] = temp[i];
                k++;
            }
        }
    }
}