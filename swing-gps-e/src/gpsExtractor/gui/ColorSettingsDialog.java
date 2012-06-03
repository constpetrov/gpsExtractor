package gpsExtractor.gui;

import gpsExtractor.storage.PreferencesHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 1/10/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorSettingsDialog extends PreferencesDialog{
    private JRadioButton speedButton;
    private JRadioButton gradientButton;
    private JRadioButton noColorButton;
    private JTextField speedField;
    private JTextField gradientField;

    public ColorSettingsDialog() {
        super("Track color settings");
        this.setSize(350,120);
        this.setResizable(false);
        this.add(createCentralPanel(), BorderLayout.CENTER);

    }

    private Component createCentralPanel(){
        DecimalFormat decFormat = new DecimalFormat("##0.000000");
        decFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));

        centralPanel = new JPanel();
        centralPanel.setLayout(new GridLayout(3,2));

        speedButton = new JRadioButton("Use speed intervals");
        gradientButton = new JRadioButton("Use gradient intervals");
        noColorButton = new JRadioButton("Do not apply colors");
        ButtonGroup group = new ButtonGroup();
        group.add(speedButton);
        group.add(gradientButton);
        group.add(noColorButton);
        speedButton.setSelected(PreferencesHolder.isSpeed());
        gradientButton.setSelected(PreferencesHolder.isGradient());
        noColorButton.setSelected(PreferencesHolder.isNoColor());

        StringBuilder builder = new StringBuilder();
        for (int i=0; i < PreferencesHolder.getSpeedThresholds().size();i++){
            builder.append(PreferencesHolder.getSpeedThresholds().get(i));
            if (i != PreferencesHolder.getSpeedThresholds().size() - 1){
                builder.append(";");
            }
        }

        speedField = new JTextField(builder.toString());

        builder = new StringBuilder();
        for (int i=0; i < PreferencesHolder.getGradientThresholds().size();i++){
            builder.append(PreferencesHolder.getGradientThresholds().get(i));
            if (i != PreferencesHolder.getGradientThresholds().size() - 1){
                builder.append(";");
            }
        }

        gradientField = new JTextField(builder.toString());

        centralPanel.add(speedButton);
        centralPanel.add(speedField);
        centralPanel.add(gradientButton);
        centralPanel.add(gradientField);
        centralPanel.add(noColorButton);



        return centralPanel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==getOkButton()){
            PreferencesHolder.setSpeed(speedButton.isSelected());
            PreferencesHolder.setGradient(gradientButton.isSelected());
            PreferencesHolder.setNoColor(noColorButton.isSelected());
            ArrayList<Double> bufferList = new ArrayList<Double>();
            StringTokenizer tokenizer = new StringTokenizer(speedField.getText(), ";");
            while(tokenizer.hasMoreTokens()){
                bufferList.add(Double.parseDouble(tokenizer.nextToken()));
            }
            PreferencesHolder.setSpeedThresholds(bufferList);

            bufferList = new ArrayList<Double>();
            tokenizer = new StringTokenizer(gradientField.getText(), ";");
            while(tokenizer.hasMoreTokens()){
                bufferList.add(Double.parseDouble(tokenizer.nextToken()));
            }
            PreferencesHolder.setGradientThresholds(bufferList);

            this.setVisible(false);
            this.dispose();
        }else if (e.getSource()==getCancelButton()){
            this.setVisible(false);
            this.dispose();
        }
    }
}
