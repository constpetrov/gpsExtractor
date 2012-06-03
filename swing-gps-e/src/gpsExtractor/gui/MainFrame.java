package gpsExtractor.gui;

import gpsExtractor.tools.Calculator;
import gpsExtractor.tools.trk.ShortTrackInfo;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.TrackID;
//import org.jdesktop.swingx.JXPanel;
//import sun.misc.JavaxSecurityAuthKerberosAccess;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 05.04.2010
 * Time: 21:58:06
 * To change this template use File | Settings | File Templates.
 */
public class MainFrame extends JFrame {
    private JPanel tracksPanel;
    private JPanel trackDetails;
//    private JXPanel mapPanel;
    private JPanel chartPanel;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss Z");
    static private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");
    private final TrackPanelMenu trackPopUpMenu = new TrackPanelMenu();

    public MainFrame(){

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Can not load System look and feel");
        }
        SwingUtilities.updateComponentTreeUI(this);
        setTitle("GPS Extractor");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(450,350));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new MainFrameMenu(this);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0,2));

        tracksPanel = new JPanel();
        tracksPanel.setLayout(new BoxLayout(tracksPanel, BoxLayout.PAGE_AXIS));
        tracksPanel.setBorder(new TitledBorder("Loaded tracks"));
        tracksPanel.setMinimumSize(new Dimension(350,200));

        trackDetails = new JPanel(new BorderLayout());
        trackDetails.setBorder(new TitledBorder("Track details"));
        trackDetails.setMinimumSize(new Dimension(200, 100));


        centerPanel.add(tracksPanel,BorderLayout.CENTER);
        centerPanel.add(trackDetails,BorderLayout.EAST);
        this.add(centerPanel,BorderLayout.CENTER);

        setVisible(true);
    }
    public void draw() throws IOException {  //this is a test function for learn how to draw images
        BufferedImage image = new BufferedImage(320,240,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(new Color(0xff0000));
        graphics.drawLine(0,0,320,240);
        ImageIO.write(image,"jpg",new File("component.jpg"));
    }

    public void showTracksTable(){
        tracksPanel.removeAll();
        Set<TrackID> trackIDSet = TrackStorage.getTrackIDs();

        Object[][] tableData = new Object[trackIDSet.size()][4];
        int currentRow = 0;
        for(TrackID tid:trackIDSet){
            tableData[currentRow][0] = tid.getFileName();
            tableData[currentRow][1] = tid.getTrackName();
            tableData[currentRow][2] = TrackStorage.getTrackStartTime(tid);
            tableData[currentRow][3] = TrackStorage.getTrackPointsSize(tid);
            currentRow++;
        }
        final ArrayList<TrackID> selectedTracks = new ArrayList<TrackID>(0);

        TableModel model = new TrackPanelTableModel(tableData, new Object[] {"File","Track Name","Start Time","points"});
        final JTable trackTable = new JTable(model);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        trackTable.setRowSorter(sorter);
        trackTable.getColumn("Start Time").setCellRenderer(new DateRenderer());

        trackTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                selectedTracks.clear();
                if(trackTable.getSelectedRowCount()==1){
                    int selected = trackTable.getSelectedRow();
                        selectedTracks.add(new TrackID(trackTable.getValueAt(selected, 0).toString(),trackTable.getValueAt(selected, 1).toString(), (Date)trackTable.getValueAt(selected, 2)));
                }else{
                    int[] selected = trackTable.getSelectedRows();
                    for(int i: selected){
                            selectedTracks.add(new TrackID(trackTable.getValueAt(i, 0).toString(),trackTable.getValueAt(i, 1).toString(), (Date)trackTable.getValueAt(i, 2)));
                    }
                }
                showSelectedTrackDetails(selectedTracks);
                trackTable.addMouseListener(new PopupListener(selectedTracks));
            }
        });

        JScrollPane tableScroll = new JScrollPane(trackTable);
        tracksPanel.add(tableScroll);
        tracksPanel.revalidate();

    }

    private void showSelectedTrackDetails(Collection<TrackID> tracks){

        trackDetails.removeAll();
        JTextArea text = new JTextArea();
        if(tracks.size() != 0){
            ArrayList<ShortTrackInfo> tracksInfo = Calculator.getShortTotalInfo(tracks);
            for(int i = 0; i < tracksInfo.size(); i++){
                /*text.append("Northernmost point of track: " + track.getMostNorthPoint() + "\n");
                text.append("Southernmost point of track: " + track.getMostSouthPoint() + "\n");
                text.append("Easternmost point of track: " + track.getMostEastPoint() + "\n");
                text.append("Westernmost point of track: " + track.getMostWestPoint() + "\n");
                */
                if((tracksInfo.size() > 1)&&
                        (i == tracksInfo.size()-1)){
                    text.append("Total: \n");
                }
                for(String part: tracksInfo.get(i).getInfoList()){
                    text.append(part + "\n");
                }
                text.append("\n");
            }
        }

        text.setMinimumSize(new Dimension(200, 100));

        JScrollPane tableScroll = new JScrollPane(text);
        trackDetails.add(tableScroll);
        trackDetails.revalidate();


    }

    class PopupListener extends MouseAdapter {
        private ArrayList<TrackID> selectedTracks;
        PopupListener(ArrayList<TrackID> selectedTracks) {
            this.selectedTracks = selectedTracks;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                trackPopUpMenu.show(e.getComponent(),
                           e.getX(), e.getY(), this.selectedTracks);
            }
        }
    }

    public void revalidateTracksPanel(){
        this.showTracksTable();
        this.showSelectedTrackDetails(new ArrayList<TrackID>());
    }

    private void showSelectedTrackOnMap(TrackID tid){

    }

    static class DateRenderer extends DefaultTableCellRenderer {
    DateFormat formatter = dateTimeFormat;
    public DateRenderer() { super(); }

    public void setValue(Object value) {
        if (formatter==null) {
            formatter = DateFormat.getDateInstance();
        }
        setText((value == null) ? "" : formatter.format((Date)value));
    }
}

}
