package gpsExtractor.gui;

import javax.swing.table.DefaultTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 24.09.2010
 * Time: 15:07:28
 * To change this template use File | Settings | File Templates.
 */
public class TrackPanelTableModel extends DefaultTableModel {
    public TrackPanelTableModel(Object[][] data, Object[] columnNames){
        super(data, columnNames);
    }
    public boolean isCellEditable(int row, int column){
        return false;
    }

}
