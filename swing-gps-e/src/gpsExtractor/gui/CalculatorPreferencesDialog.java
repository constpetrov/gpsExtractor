package gpsExtractor.gui;

import gpsExtractor.storage.PreferencesHolder;
import gpsExtractor.tools.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 11.10.2010
 * Time: 15:06:52
 * To change this template use File | Settings | File Templates.
 */
public class CalculatorPreferencesDialog extends PreferencesDialog {
    private JTextField minBikeSpeedField;
    private JTextField stopSpeedField;

    public CalculatorPreferencesDialog(){
        super("Calculator preferences");
        this.add(createCentralPanel(), BorderLayout.CENTER);
    }

    private Component createCentralPanel(){
        centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));

        JPanel minBikeSpeedPanel = new JPanel();
        minBikeSpeedPanel.setLayout(new BoxLayout(minBikeSpeedPanel, BoxLayout.LINE_AXIS));
        minBikeSpeedPanel.setMaximumSize(new Dimension(250,25));

        JPanel stopSpeedPanel = new JPanel();
        stopSpeedPanel.setLayout(new BoxLayout(stopSpeedPanel, BoxLayout.LINE_AXIS));
        stopSpeedPanel.setMaximumSize(new Dimension(250,25));

        minBikeSpeedField = new JTextField(String.valueOf(PreferencesHolder.getMinBikeSpeed()));
        stopSpeedField = new JTextField(String.valueOf(PreferencesHolder.getStopSpeed()));

        minBikeSpeedPanel.add(new JLabel("Min \"on-bike\" speed: "));
        minBikeSpeedPanel.add(minBikeSpeedField);
        minBikeSpeedPanel.add(Box.createHorizontalGlue());

        stopSpeedPanel.add(new JLabel("\"Stop\" speed: "));
        stopSpeedPanel.add(stopSpeedField);
        stopSpeedPanel.add(Box.createHorizontalGlue());

        centralPanel.add(minBikeSpeedPanel);
        centralPanel.add(stopSpeedPanel);
        centralPanel.setAlignmentX(0);
        centralPanel.add(Box.createVerticalGlue());

        return centralPanel;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==getOkButton()){
            PreferencesHolder.setMinBikeSpeed(Double.parseDouble(minBikeSpeedField.getText()));
            PreferencesHolder.setStopSpeed(Double.parseDouble(stopSpeedField.getText()));
            this.setVisible(false);
            this.dispose();
        }
        if(e.getSource()==getCancelButton()){
            this.setVisible(false);
            this.dispose();
        }
    }
}
