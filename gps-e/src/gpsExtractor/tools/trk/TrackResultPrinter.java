package gpsExtractor.tools.trk;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 17.12.2008
 * Time: 19:05:11
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public abstract class TrackResultPrinter {
    private static String resFileName;
    private static String resFileNumber;

    public static void setResFileName(String res){
        resFileName=res;
    }

    public static void setResFileNumber(int n){
        resFileNumber= "_" + n;
    }

    public static void printConsole(TrackResult res){
        TrackDistance fullDist=new TrackDistance(0,0);
        double avSpeedAll=0;
        double avSpeedFoot=0;
        double avSpeedBike=0;

        for(int i=0;i<res.getNum();i++){
            if(res.distList[i].gradMax==Double.POSITIVE_INFINITY){
                System.out.println("�������� � ���� � ������� ����� "+res.distList[i].gradMin+"%        "+String.format("%8.2f",res.distList[i].all.dist/1000)+" ��");
                System.out.println("                                �� ��� �� ����� "+String.format("%8.2f",res.distList[i].onBike.dist/1000)+" ��");
            }

            if(res.distList[i].gradMax<Double.POSITIVE_INFINITY&&res.distList[i].gradMin>Double.NEGATIVE_INFINITY){
                if(res.distList[i].gradMax*res.distList[i].gradMin>0){
                    if(res.distList[i].gradMax>0){
                        System.out.println("�������� � ���� � ������� �� "+Math.abs(res.distList[i].gradMin)+"% �� "+Math.abs(res.distList[i].gradMax)+"%   "+String.format("%8.2f",res.distList[i].all.dist/1000)+" ��");
                        System.out.println("                                �� ��� �� ����� "+String.format("%8.2f",res.distList[i].onBike.dist/1000)+" ��");
                    }
                    else{
                        System.out.println("�������� ��� ���� � ������� �� "+Math.abs(res.distList[i].gradMax)+"% �� "+Math.abs(res.distList[i].gradMin)+"%     "+String.format("%8.2f",res.distList[i].all.dist/1000)+" ��");
                        System.out.println("                                �� ��� �� ����� "+String.format("%8.2f",res.distList[i].onBike.dist/1000)+" ��");
                    }
                }
                else{
                    System.out.println("�������� �� ��������� (����� ����� "+Math.abs(res.distList[i].gradMax)+"%)          "+String.format("%8.2f",res.distList[i].all.dist/1000)+" ��");
                    System.out.println("                                �� ��� �� ����� "+String.format("%8.2f",res.distList[i].onBike.dist/1000)+" ��");
                }
            }

            if(res.distList[i].gradMin==Double.NEGATIVE_INFINITY){
                System.out.println("�������� ��� ���� � ������� ����� "+Math.abs(res.distList[i].gradMax)+"%        "+String.format("%8.2f",res.distList[i].all.dist/1000)+" ��");
                System.out.println("                                �� ��� �� ����� "+String.format("%8.2f",res.distList[i].onBike.dist/1000)+" ��");
            }

            fullDist.all.addPart(res.distList[i].all);
            fullDist.onBike.addPart(res.distList[i].onBike);
            fullDist.onFoot.addPart(res.distList[i].onFoot);
            fullDist.stop.addPart(res.distList[i].stop);
        }

        avSpeedAll=(fullDist.all.dist/(fullDist.all.time/1000))*3.6;
        avSpeedBike=(fullDist.onBike.dist/(fullDist.onBike.time/1000))*3.6;
        avSpeedFoot=(fullDist.onFoot.dist/(fullDist.onFoot.time/1000))*3.6;

        System.out.println("����� ��������       "+String.format("%8.2f",fullDist.all.dist/1000)+" ��");
        System.out.println("�� ������� ��������� "+String.format("%8.2f",avSpeedAll)+" ��/���");
        System.out.println("�� ��� �� �����      "+String.format("%8.2f",fullDist.onBike.dist/1000)+" ��");
        System.out.println("�� ������� ��������� "+String.format("%8.2f",avSpeedBike)+" ��/���");

        System.out.println();

        System.out.println("����� � ����    "+fullDist.all.timeToString());
        System.out.println("����� �� �����  "+fullDist.onBike.timeToString());
        System.out.println("����� ������    "+fullDist.onFoot.timeToString());

        System.out.println("����� ��������� "+fullDist.stop.timeToString());
    }

    public static void printXML(TrackResult res) throws XMLStreamException, FileNotFoundException {

        final FileOutputStream str = new FileOutputStream(resFileName+resFileNumber+".xml");
        final XMLOutputFactory output = XMLOutputFactory.newInstance();
        final XMLStreamWriter writer = output.createXMLStreamWriter(str,"WINDOWS-1251");

        final String STYLESHEET = "xml-stylesheet";
        final String STYLEPROCESSINGINSTRUCTION ="type=\"text/xsl\" href=\"";
        final String mStyleUrl="simple"+".xsl";


            TrackDistance fullDist=new TrackDistance(0,0);

            double totalUp=0;
            double totalDown=0;

        writer.writeStartDocument("WINDOWS-1251","1.0");
        writer.writeCharacters("\n");

        writer.writeProcessingInstruction(STYLESHEET, STYLEPROCESSINGINSTRUCTION+mStyleUrl+"\"");
        writer.writeCharacters("\n");

        writer.setDefaultNamespace("results");


        writer.writeStartElement("results");
        writer.writeCharacters("\n");

        writer.writeStartElement("full");
        writer.writeCharacters("\n");





            for(int i=0;i<res.getNum();i++){
                if(res.distList[i].gradMax==Double.POSITIVE_INFINITY){
                    totalUp+=res.distList[i].all.elev;

                    writer.writeStartElement("detailTrack");
                    writer.writeAttribute("caption","� ���� � ������� ����� "+res.distList[i].gradMin+"%");
                    writer.writeCharacters("\n");
                    writeResElemXML(res.distList[i],writer);
                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }

                if(res.distList[i].gradMax<Double.POSITIVE_INFINITY&&res.distList[i].gradMin>Double.NEGATIVE_INFINITY){
                    if(res.distList[i].gradMax*res.distList[i].gradMin>0){
                        if(res.distList[i].gradMax>0){
                            totalUp+=res.distList[i].all.elev;

                            writer.writeStartElement("detailTrack");
                            writer.writeAttribute("caption","� ���� � ������� �� "+Math.abs(res.distList[i].gradMin)+"% �� "+Math.abs(res.distList[i].gradMax)+"%");
                            writer.writeCharacters("\n");
                            writeResElemXML(res.distList[i],writer);
                            writer.writeEndElement();
                            writer.writeCharacters("\n");
                        }
                        else{
                            totalDown+=res.distList[i].all.elev;

                            writer.writeStartElement("detailTrack");
                            writer.writeAttribute("caption","��� ���� � ������� �� "+Math.abs(res.distList[i].gradMax)+"% �� "+Math.abs(res.distList[i].gradMin)+"%");
                            writer.writeCharacters("\n");
                            writeResElemXML(res.distList[i],writer);
                            writer.writeEndElement();
                            writer.writeCharacters("\n");
                        }
                    }
                    else{
                        writer.writeStartElement("detailTrack");
                        writer.writeAttribute("caption","�� ��������� (����� ����� "+Math.abs(res.distList[i].gradMax)+"%)");
                        writer.writeCharacters("\n");
                        writeResElemXML(res.distList[i],writer);
                        writer.writeEndElement();
                        writer.writeCharacters("\n");
                    }
                }

                if(res.distList[i].gradMin==Double.NEGATIVE_INFINITY){
                    totalDown+=res.distList[i].all.elev;

                    writer.writeStartElement("detailTrack");
                    writer.writeAttribute("caption","�������� ��� ���� � ������� ����� "+Math.abs(res.distList[i].gradMax)+"%");
                    writer.writeCharacters("\n");
                    writeResElemXML(res.distList[i],writer);
                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }

                fullDist.all.addPart(res.distList[i].all);
                fullDist.onBike.addPart(res.distList[i].onBike);
                fullDist.onFoot.addPart(res.distList[i].onFoot);
                fullDist.stop.addPart(res.distList[i].stop);


            }
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeCharacters("\n");

        writer.writeStartElement("results","short");
        writer.writeAttribute("uphill",String.format("%8.2f",totalUp));
        writer.writeAttribute("downhill",String.format("%8.2f",Math.abs(totalDown)));
        writer.writeCharacters("\n");
        writeResElemXML(fullDist,writer);
        writer.writeEndElement();
        writer.writeCharacters("\n");

        writer.writeEndElement();
        writer.writeCharacters("\n");


        writer.writeEndDocument();
        writer.flush();
        writer.close();
        }

    private static void writeResElemXML(TrackDistance dist,XMLStreamWriter writer){
        try {

        if(dist.all.time!=0){
            writer.writeStartElement("all");
            writer.writeAttribute("caption","���� ������");
            writer.writeAttribute("dist",String.format("%8.2f",dist.all.dist/1000));
            writer.writeAttribute("time",dist.all.timeToString());
            writer.writeAttribute("avg",String.format("%8.2f",(dist.all.dist/(dist.all.time/1000))*3.6));
            writer.writeEndElement();
            writer.writeCharacters("\n");
            }
        else{
            writer.writeStartElement("all");
            writer.writeAttribute("caption","���� ������");
            writer.writeAttribute("dist","0");
            writer.writeAttribute("time",dist.all.timeToString());
            writer.writeAttribute("avg","0");
            writer.writeEndElement();
            writer.writeCharacters("\n");
            }

        if(dist.onBike.time!=0){
            writer.writeStartElement("onBike");
            writer.writeAttribute("caption","�� �����");
            writer.writeAttribute("dist",String.format("%8.2f",dist.onBike.dist/1000));
            writer.writeAttribute("time",dist.onBike.timeToString());
            writer.writeAttribute("avg",String.format("%8.2f",(dist.onBike.dist/(dist.onBike.time/1000))*3.6));
            writer.writeEndElement();
            writer.writeCharacters("\n");
            }
        else{
            writer.writeStartElement("onBike");
            writer.writeAttribute("caption","�� �����");
            writer.writeAttribute("dist","0");
            writer.writeAttribute("time",dist.onBike.timeToString());
            writer.writeAttribute("avg","0");
            writer.writeEndElement();
            writer.writeCharacters("\n");
            }

        if(dist.onFoot.time!=0){
            writer.writeStartElement("onFoot");
            writer.writeAttribute("caption","������");
            writer.writeAttribute("dist",String.format("%8.2f",dist.onFoot.dist/1000));
            writer.writeAttribute("time",dist.onFoot.timeToString());
            writer.writeAttribute("avg",String.format("%8.2f",(dist.onFoot.dist/(dist.onFoot.time/1000))*3.6));
            writer.writeEndElement();
            writer.writeCharacters("\n");
            }
        else{
            writer.writeStartElement("onFoot");
            writer.writeAttribute("caption","������");
            writer.writeAttribute("dist","0");
            writer.writeAttribute("time",dist.onFoot.timeToString());
            writer.writeAttribute("avg","0");
            writer.writeEndElement();
            writer.writeCharacters("\n");
            }

        }
        catch (XMLStreamException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }
}
