package gpsExtractor.gui;

import gpsExtractor.storage.TrackStorage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
     * Created by IntelliJ IDEA.
     * User: constant.petrov
     * Mailto: constant.petrov@gmail.com
     * Date: 09.04.2010
     * Time: 22:25:40
     * To change this template use File | Settings | File Templates.
     */
    public class MainFrameMenu extends JMenuBar implements ActionListener{
        private JMenu fileMenu;
        private JMenu optionsMenu;
        private JMenu helpMenu;
        private JMenuItem openFileItem;
        private JMenuItem drawOnFormItem;
        private JMenuItem exitItem;
        private JMenuItem aboutItem;
        private JMenuItem calcPrefsItem;
        private JMenuItem chartPrefsItem;
        private JMenuItem pointsFilterItem;
        private JMenuItem colorOptionsItem;
        Logger logger;

    private MainFrame parentFrame;
        public MainFrameMenu(MainFrame frame) {
            fileMenu = new JMenu("File");
            optionsMenu = new JMenu("Options");
            helpMenu = new JMenu("Help");

            openFileItem = new JMenuItem("Open");
            openFileItem.addActionListener(this);
            drawOnFormItem = new JMenuItem("Draw");
            drawOnFormItem.addActionListener(this);
            exitItem = new JMenuItem("Exit");
            exitItem.addActionListener(this);

            fileMenu.add(openFileItem);
            fileMenu.add(exitItem);
            this.add(fileMenu);


            calcPrefsItem = new JMenuItem("Calc");
            calcPrefsItem.addActionListener(this);
            optionsMenu.add(calcPrefsItem);

            chartPrefsItem = new JMenuItem("Chart");
            chartPrefsItem.addActionListener(this);
            optionsMenu.add(chartPrefsItem);

            pointsFilterItem = new JMenuItem("Points filter");
            pointsFilterItem.addActionListener(this);
            optionsMenu.add(pointsFilterItem);

            colorOptionsItem = new JMenuItem("Track color");
            colorOptionsItem.addActionListener(this);
            optionsMenu.add(colorOptionsItem);


            this.add(optionsMenu);

            aboutItem = new JMenuItem("About");
            aboutItem.addActionListener(this);
            helpMenu.add(aboutItem);
            this.add(helpMenu);
            parentFrame = frame;
            parentFrame.add(this, BorderLayout.NORTH);
        }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== openFileItem){
            JFileChooser fileChooser = new JFileChooser(".\\");
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Google Earth track files (kml)","kml","KML"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Ozi Explorer plt files (plt)","plt","PLT"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Garmin track files (gpx)","gpx","GPX"));
            int result = fileChooser.showOpenDialog(MainFrameMenu.this);
            if (result == JFileChooser.APPROVE_OPTION){
                TrackStorage.add(fileChooser.getSelectedFile());
                parentFrame.showTracksTable();
            }
        }
        if(e.getSource()== drawOnFormItem){
            try {
                parentFrame.draw();
            } catch (IOException e1) {
                logger=Logger.getLogger("GUI");
                logger.log(Level.WARNING,"No fileMenu found");
            }
        }
        if(e.getSource()== exitItem){
            System.exit(0);    
        }
        if(e.getSource()== aboutItem){
            new AboutDialog();
        }
        if(e.getSource()== calcPrefsItem){
            new CalculatorPreferencesDialog();
        }
        if(e.getSource()== chartPrefsItem){
            new ChartPreferencesDialog();
        }
        if(e.getSource()== pointsFilterItem){
            new InPointsFilterPreferencesDialog();
        }
        if(e.getSource()== colorOptionsItem){
            new ColorSettingsDialog();
        }

    }
}
