package gpsExtractor.tools.chart;

import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackPoint;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 14.10.2010
 * Time: 17:25:48
 * To change this template use File | Settings | File Templates.
 */
public class ChartTest {
    @Test
    public void testChartDrawing(){
        Track t = new Track();
        t.add(new TrackPoint(55.0000,40.0000,new Date(), 100));
        t.add(new TrackPoint(55.0010,40.0010,new Date(), 132.78688524590163)); //height in feet
        t.add(new TrackPoint(55.0020,40.0020,new Date(), 100)); //height in feet

        TrackID tid = new TrackID("testFile1","testTrack1",t.getStartTime());

        TrackStorage.put(tid,t);

//        HeightsChart.createHeidhtsChart(tid);
    }

    @Test
    public void testColor(){
        for(double i = 0.0; i < 255.1; i++){
            System.out.println(HeightsChart.chooseColorFromSpeed(i, 127, 255));
        }
    }


    public ChartTest() {
        JFrame frame = new JFrame("test");
        frame.setMinimumSize(new Dimension(200,200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new GraphPane();


        Track t = new Track();
        t.add(new TrackPoint(55.0000,40.0000,new Date(), 100));
        t.add(new TrackPoint(55.0010,40.0010,new Date(), 132.78688524590163)); //height in feets
        t.add(new TrackPoint(55.0020,40.0020,new Date(), 100)); //height in feets

        TrackID tid = new TrackID("testFile1","testTrack1",t.getStartTime());

        TrackStorage.put(tid,t);

//        HeightsChart.createHeidhtsChart(tid);


        frame.add(panel);
        frame.setVisible(true);
//        panel.paintComponents(g2);
    }

    public static void main(String[] args){
        new ChartTest();
    }

    public class GraphPane extends JPanel
    {

        public GraphPane ()
        {
            this.setBackground ( Color.blue );
        }

        AffineTransform FLIP_X_COORDINATE = new AffineTransform( 1, 0, 0, -1, 0, getHeight () );

        public void paint ( Graphics graphics )
        {
            // ��� ��������� ����, ��� �� �� ��������� ��� ������
            super.paint ( graphics );

            Graphics2D g = ( Graphics2D ) graphics;
            //g.setTransform ( FLIP_X_COORDINATE );
            /*g.setColor ( new Color ( 213, 23, 45 ) );
            g.drawLine ( 0, 300, 300, 300 );*/
            //g.drawLine(150, 150, 70, 50);
        }
    }
}
