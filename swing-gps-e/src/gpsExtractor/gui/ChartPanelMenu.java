package gpsExtractor.gui;

import gpsExtractor.storage.PreferencesHolder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: Oct 16, 2010
 * Time: 11:08:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChartPanelMenu extends JPopupMenu implements ActionListener {
    private final JPanel parent;
    private JMenuItem saveToFileItem;
    private String fileType;
    private ChartPanelMenu(){
        this.parent = new JPanel();
    }

    public ChartPanelMenu(JPanel parent){
        this.parent = parent;
        saveToFileItem = new JMenuItem("Save to file");
        saveToFileItem.addActionListener(this);
        this.add(saveToFileItem);

        if(PreferencesHolder.isJpeg()){
            fileType = "jpg";
        }else{
            fileType = "png";
        }
    }
    public void actionPerformed(ActionEvent e){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(this.fileType.toUpperCase()+" files", this.fileType));
        int result = chooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION){
            String fileName = chooser.getSelectedFile().getAbsolutePath();
            if(!fileName.toLowerCase().endsWith("."+fileType)){
                fileName += "."+fileType;
            }
            parent.getGraphics();
            Rectangle r = parent.getBounds();
            BufferedImage image = (BufferedImage)parent.createImage(r.width,r.height);
            Graphics g = image.getGraphics();
            parent.paint(g);
            try {
                ImageIO.write(image, this.fileType, new File(fileName));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        parent.repaint();
    }

    
}
