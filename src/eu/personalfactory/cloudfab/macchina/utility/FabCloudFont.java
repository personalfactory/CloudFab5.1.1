package eu.personalfactory.cloudfab.macchina.utility;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;


public class FabCloudFont
{ 
	private static Font fo;
        private static Font fontRev; 
    
    public static Font setDimensione(double d){
        try {

            fo = Font.createFont(
                    Font.TRUETYPE_FONT,
                    ClassLoader.getSystemResourceAsStream(
                    PATH_FONT
                    + FONT));
            fontRev = fo.deriveFont(
                    RISOLUZIONE_ALTEZZA_PANNELLO / ((float) d));
        } catch (FontFormatException | IOException e) {
            
            System.err.println(e);
        }

       return fontRev;      
    }
     
	
}
