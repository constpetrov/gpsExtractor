package gpsExtractor.tools.chart;

import gpsExtractor.storage.PreferencesHolder;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.Calculator;
import gpsExtractor.tools.trk.ShortTrackInfo;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackSegment;
import gpsExtractor.tools.trk.TrackSegmentPart;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 14.10.2010
 * Time: 14:08:05
 * To change this template use File | Settings | File Templates.
 */
public class HeightsChart {
    private HeightsChart(){}
    private final static int leftMargin = 50;
    private final static int rightMargin = 30;
    private final static int topMargin = 30;
    private final static int bottomMargin = 30;
    private static int chartHeight;
    private static int chartWidth;
    public static void createHeidhtsChart(TrackID tid, Graphics g, Dimension dim){
        ShortTrackInfo info = Calculator.getShortTrackInfo(tid);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        chartHeight = (int)dim.getHeight()/*graphics.getClipBounds().getSize().height*/ - topMargin - bottomMargin;
        chartWidth = (int)dim.getWidth()/*graphics.getClipBounds().getSize().width*/ - leftMargin - rightMargin;
        if(PreferencesHolder.isDrawTitle()){
            drawTitle(tid, graphics);
        }
        if(PreferencesHolder.isDrawGridLines()){
            drawGridLines(info, graphics);
        }
        drawHeightProfile(tid, info, graphics);
        if(PreferencesHolder.isDrawSpeedLegend()){
            drawSpeedLegend(info, graphics);
        }
    }

    private static void drawTitle(TrackID tid, Graphics2D graphics){
        Font oldFont = graphics.getFont();
        graphics.setFont(new Font("SansSerif",Font.PLAIN,16));
        graphics.drawString(tid.getFileName()+"-"+tid.getTrackName(), leftMargin, topMargin - 10);
        graphics.setFont(oldFont);

    }

    private static void drawGridLines(ShortTrackInfo info, Graphics2D graphics){
        int hDiap;
        int lDiap;
        if (info.getMaxHeight()-info.getMinHeight() <= 160){
            hDiap = 10;
        }else{
            hDiap = 100;
        }
        if (info.getDistance() <= 16000){
            lDiap = 1000;
        }else{
            lDiap = 10000;
        }
        graphics.setColor(new Color(0x7f7f7f));
        graphics.drawRect(leftMargin-1,
                topMargin-1,
                chartWidth+1,
                chartHeight+1);
        int hStart = (((int)(double)info.getMinHeight())/hDiap)*hDiap;
        int hLine;

        for (double h = hStart + hDiap; h < info.getMaxHeight(); h += hDiap){
            hLine = (int)((h-info.getMinHeight())*vMultiplier(info));
            graphics.drawString(String.valueOf(h), 2, topMargin + chartHeight - hLine + graphics.getFontMetrics().getHeight()/2);
            graphics.drawLine(leftMargin,
                    topMargin + chartHeight - hLine,
                    leftMargin + chartWidth,
                    topMargin +  chartHeight - hLine);
        }

        int lStart = 0;
        int lLine;
        for (double l = lStart; l < info.getDistance(); l += lDiap){
            lLine = (int)(l*hMultiplier(info))-1;
            graphics.drawString(String.valueOf(l/1000), leftMargin + lLine, topMargin + chartHeight + 2 + graphics.getFontMetrics().getHeight());
            graphics.drawLine(leftMargin + lLine,
                    topMargin,
                    leftMargin + lLine,
                    topMargin +  chartHeight);
        }
    }

    private static void drawHeightProfile(TrackID tid, ShortTrackInfo info, Graphics2D graphics) {
        LinkedList<TrackSegment> segments = TrackStorage.getTrackByID(tid).getSegments();
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        double startX = leftMargin;
        double startY = ((segments.getFirst().getSegmentParts().getFirst().getStartPoint().getElv()- info.getMinHeight())* vMultiplier(info))+1;
        for(TrackSegment seg: segments){
            for(TrackSegmentPart part: seg.getSegmentParts()){
                graphics.setColor(chooseColorFromSpeed(part.getSpeed(), info.getAvgMove(), info.getMaxSpeed()));
                graphics.drawLine((int)startX,
                        topMargin + chartHeight-(int)startY,
                        (int)(startX + (part.getPathDelta()* hMultiplier(info))),
                        topMargin + chartHeight-(int) (startY +(part.getElevDelta()* vMultiplier(info))));
                startX += (part.getPathDelta()* hMultiplier(info));
                startY += (part.getElevDelta()* vMultiplier(info));
            }
        }
        graphics.setStroke(oldStroke);
    }

    private static void drawSpeedLegend(ShortTrackInfo info, Graphics2D graphics) {
        for (int i = chartHeight; i > 0; i--){
            graphics.setColor(chooseColorFromSpeed(info.getMaxSpeed()*(double)i/(double)chartHeight ,info.getAvgMove(), info.getMaxSpeed()));
            graphics.drawLine(leftMargin + chartWidth + 3,
                    topMargin + chartHeight+1-i,
                    leftMargin + chartWidth + rightMargin ,
                    topMargin + chartHeight+1-i);
        }
        Font oldFont = graphics.getFont();
        graphics.setColor(Color.black);
        graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 8));
        graphics.drawString(String.format("%3.1f",PreferencesHolder.getMinBikeSpeed()),
                leftMargin + chartWidth + 5,
                topMargin + chartHeight - (int)(chartHeight*PreferencesHolder.getMinBikeSpeed()/info.getMaxSpeed())+graphics.getFontMetrics().getHeight()+1);
        graphics.drawString(String.format("%3.1f",info.getAvgMove()),
                leftMargin + chartWidth + 5,
                topMargin + chartHeight - (int)(chartHeight*info.getAvgMove()/info.getMaxSpeed())+graphics.getFontMetrics().getHeight()+1);
        graphics.drawString(String.format("%3.1f",info.getMaxSpeed()),
                        leftMargin + chartWidth + 5,
                        topMargin + graphics.getFontMetrics().getHeight()-1);
        graphics.drawLine(leftMargin + chartWidth + 3,
                    topMargin + chartHeight+1-(int)(chartHeight*PreferencesHolder.getMinBikeSpeed()/info.getMaxSpeed()),
                    leftMargin + chartWidth + rightMargin ,
                    topMargin + chartHeight+1-(int)(chartHeight*PreferencesHolder.getMinBikeSpeed()/info.getMaxSpeed()));
        graphics.drawLine(leftMargin + chartWidth + 3,
                    topMargin + chartHeight+1-(int)(chartHeight*info.getAvgMove()/info.getMaxSpeed()),
                    leftMargin + chartWidth + rightMargin ,
                    topMargin + chartHeight+1-(int)(chartHeight*info.getAvgMove()/info.getMaxSpeed()));
        graphics.setFont(oldFont);
    }


    private static double hMultiplier(ShortTrackInfo info) {
        return ((double)chartWidth)/info.getDistance();
    }

    private static double vMultiplier(ShortTrackInfo info) {
        return ((double)chartHeight)/((info.getMaxHeight() - info.getMinHeight()));
    }



    static Color chooseColorFromSpeed(double speed, double avgSpeed, double maxSpeed){
        if (speed < PreferencesHolder.getMinBikeSpeed()){
            return new Color(0xff0000);
        }
        if (speed > avgSpeed){
            return new Color(0x00ff00);
        }
        int measure = (int)(((speed-PreferencesHolder.getMinBikeSpeed())/(avgSpeed-PreferencesHolder.getMinBikeSpeed()))*255.0);
        measure = 255 < measure ? 255 : measure;
        return measure <= 127 ? new Color(0x000200*measure + 0xff0000) : new Color(0x00ff00 + 0xff0000 - 0x010000 * 2 * (measure-127) + 0x010000);
    }
}
