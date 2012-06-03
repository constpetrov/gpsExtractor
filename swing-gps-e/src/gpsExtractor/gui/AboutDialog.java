package gpsExtractor.gui;

import gpsExtractor.GpsExtr;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 07.10.2010
 * Time: 18:03:13
 * To change this template use File | Settings | File Templates.
 */
public class AboutDialog extends JDialog {
    public AboutDialog(){
        this.setAlwaysOnTop(true);
//        this.setModal(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300,150));
        this.setResizable(false);
        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setBackground(this.getBackground());
        text.setFont(new Font(Font.SERIF,Font.PLAIN,16));
        
        text.append("gps Extractor " +
                "version "+ GpsExtr.class.getPackage().getImplementationVersion()+"\n"+
                "e-mail: constant.petrov@gmail.com\n" +
                "http://gpxextractor.sourceforge.net/");
        this.add(text, BorderLayout.CENTER);
    }
}
