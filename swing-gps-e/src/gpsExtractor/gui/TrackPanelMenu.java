package gpsExtractor.gui;

import gpsExtractor.GpsExtr;
import gpsExtractor.storage.PreferencesHolder;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.gpx.GPXWriter;
import gpsExtractor.tools.kml.KMLColorOption;
import gpsExtractor.tools.kml.KMLWriter;
import gpsExtractor.tools.trk.TrackID;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 21.09.2010
 * Time: 16:55:26
 * To change this template use File | Settings | File Templates.
 */
public class TrackPanelMenu extends JPopupMenu implements ActionListener{
    private JMenuItem deleteItem;
    private JMenuItem joinItem;
    private JMenuItem exportGPXItem;
    private JMenuItem exportKMLItem;
    private JMenuItem createChartItem;
    private ArrayList<TrackID> selectedTracks;

    public TrackPanelMenu() {
        this.deleteItem = new JMenuItem("Delete");
        this.deleteItem.addActionListener(this);
        this.add(deleteItem);

        this.joinItem = new JMenuItem("Join");
        this.joinItem.addActionListener(this);
        this.add(joinItem);

        this.exportGPXItem = new JMenuItem("Export to GPX");
        this.exportGPXItem.addActionListener(this);
        this.add(exportGPXItem);

        this.exportKMLItem = new JMenuItem("Export to KML");
        this.exportKMLItem.addActionListener(this);
        this.add(exportKMLItem);

        this.createChartItem = new JMenuItem("Create a chart");
        this.createChartItem.addActionListener(this);
        this.add(createChartItem);
    }


    public void show(Component comp, int x, int y, ArrayList<TrackID> tracks){
        this.selectedTracks = tracks;
        super.show(comp,x,y);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.deleteItem){
            for(TrackID t: selectedTracks){
                TrackStorage.remove(t);
            }
            GpsExtr.mainFrame.revalidateTracksPanel();
        }

        if(e.getSource() == this.joinItem){
            TrackStorage.joinTracks(selectedTracks);
            GpsExtr.mainFrame.revalidateTracksPanel();
        }

        if(e.getSource() == this.exportGPXItem){
            JFileChooser fileChooser = new JFileChooser(".\\");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Garmin GPX files","gpx"));
            int result = fileChooser.showSaveDialog(TrackPanelMenu.this);
            if (result == JFileChooser.APPROVE_OPTION){
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                if(!fileName.toLowerCase().endsWith(".gpx")){
                    fileName += ".gpx";
                }
                GPXWriter writer = new GPXWriter(selectedTracks, fileName);
                writer.run();
            }



            GpsExtr.mainFrame.revalidateTracksPanel();
        }

        if(e.getSource() == this.exportKMLItem){
            JFileChooser fileChooser = new JFileChooser(".\\");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Google Earth files","kml"));
            int result = fileChooser.showSaveDialog(TrackPanelMenu.this);
            if (result == JFileChooser.APPROVE_OPTION){
                try {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    if(!fileName.toLowerCase().endsWith(".kml")){
                        fileName += ".kml";
                    }
                    KMLColorOption colorOption = KMLColorOption.USE_NO_COLOR;
                    if (PreferencesHolder.isSpeed()){
                        colorOption = KMLColorOption.USE_SPEED;
                    }
                    if (PreferencesHolder.isGradient()){
                        colorOption = KMLColorOption.USE_GRADIENTS;
                    }

                    KMLWriter.writeTracksToKML(selectedTracks, fileName, colorOption);
                } catch (XMLStreamException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }



            GpsExtr.mainFrame.revalidateTracksPanel();
        }

        if(e.getSource() == this.createChartItem){
            JDialog chartDialog = new JDialog(GpsExtr.mainFrame);
            chartDialog.setLayout(new BorderLayout());
            chartDialog.setMinimumSize(new Dimension(1060,330));
            JPanel centralPanel = new ChartPanel(selectedTracks.get(0));
            chartDialog.add(centralPanel, BorderLayout.CENTER);
            chartDialog.setVisible(true);

        }
    }

}
