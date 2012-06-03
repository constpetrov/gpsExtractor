package gpsExtractor.gui;

import gpsExtractor.storage.PreferencesHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 18.10.2010
 * Time: 13:18:34
 * To change this template use File | Settings | File Templates.
 */
public class InPointsFilterPreferencesDialog extends PreferencesDialog{
    private JTextField maxDistanceTwoPoints;
    private JTextField minTimeTwoPoints;
    private JTextField maxElevTwoPoints;
    private JPanel centralPanel;

    public InPointsFilterPreferencesDialog() {
        super("Import points filter");
        this.add(createCentralPanel(), BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(300,200));
    }
    private Component createCentralPanel(){
        centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));

        JPanel maxDistancePanel = new JPanel();
        maxDistancePanel.setLayout(new BoxLayout(maxDistancePanel, BoxLayout.LINE_AXIS));
        maxDistancePanel.setMaximumSize(new Dimension(300,25));

        JPanel minTimePanel = new JPanel();
        minTimePanel.setLayout(new BoxLayout(minTimePanel, BoxLayout.LINE_AXIS));
        minTimePanel.setMaximumSize(new Dimension(300,25));

        JPanel maxElevPanel = new JPanel();
        maxElevPanel.setLayout(new BoxLayout(maxElevPanel, BoxLayout.LINE_AXIS));
        maxElevPanel.setMaximumSize(new Dimension(300,25));

        maxDistanceTwoPoints = new JTextField(String.valueOf(PreferencesHolder.getMaxDistanceTwoPoints()));
        minTimeTwoPoints = new JTextField(String.valueOf(PreferencesHolder.getMinTimeTwoPoints()/1000));
        maxElevTwoPoints = new JTextField(String.valueOf(PreferencesHolder.getMaxElevTwoPoints()));

        maxDistancePanel.add(new JLabel("Maximum distance between two points: "));
        maxDistancePanel.add(maxDistanceTwoPoints);
        maxDistancePanel.add(new JLabel(" m"));
        maxDistancePanel.add(Box.createHorizontalGlue());

        minTimePanel.add(new JLabel("Minimum time between two points: "));
        minTimePanel.add(minTimeTwoPoints);
        minTimePanel.add(new JLabel(" sec"));
        minTimePanel.add(Box.createHorizontalGlue());

        maxElevPanel.add(new JLabel("Maximum elevation between two points: "));
        maxElevPanel.add(maxElevTwoPoints);
        maxElevPanel.add(new JLabel(" m"));
        maxElevPanel.add(Box.createHorizontalGlue());

        centralPanel.add(maxDistancePanel);
        centralPanel.add(minTimePanel);
        centralPanel.add(maxElevPanel);
        centralPanel.setAlignmentX(0);
        centralPanel.add(Box.createVerticalGlue());

        return centralPanel;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==getOkButton()){
            PreferencesHolder.setMaxDistanceTwoPoints(Double.parseDouble(maxDistanceTwoPoints.getText()));
            PreferencesHolder.setMinTimeTwoPoints(Long.parseLong(minTimeTwoPoints.getText())*1000);
            PreferencesHolder.setMaxElevTwoPoints(Double.parseDouble(maxElevTwoPoints.getText()));
            this.setVisible(false);
            this.dispose();
        }
        if(e.getSource()==getCancelButton()){
            this.setVisible(false);
            this.dispose();
        }
    }
}
