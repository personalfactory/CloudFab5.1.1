package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.panels.MyStepPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod_Alter;

/**
 *
 *
 * Gestore Eventi Mouse Per lo Scambio con il Pannello Alternativo
 *
 *
 */
public class GestoreScambioPanelMouseListener implements MouseListener {

    private int xArea, yArea, lArea, aArea;
    private MyStepPanel pannelloCorrente;

    public GestoreScambioPanelMouseListener(
            int xArea, 
            int yArea,
            int lArea, 
            int aArea, 
            MyStepPanel pannelloCorrente) {

        super();
        this.xArea = xArea;
        this.yArea = yArea;
        this.lArea = lArea;
        this.aArea = aArea;
        this.pannelloCorrente = pannelloCorrente;;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if ((e.getX() >= xArea & e.getX() <= xArea + lArea)
                & (e.getY() >= yArea & e.getY() <= yArea + aArea)) {
            if (pannelloCorrente.pannelliCollegati.get(2) instanceof Pannello03_FiltroProdCod_Alter) {
               
                ((Pannello03_FiltroProdCod_Alter)pannelloCorrente.pannelliCollegati.get(2)).initPanel();
                
            } else if (pannelloCorrente.pannelliCollegati.get(2)instanceof Pannello03_FiltroProdCod) {
                
                ((Pannello03_FiltroProdCod)pannelloCorrente.pannelliCollegati.get(2)).initPanel();
            }

            pannelloCorrente.pannelliCollegati.get(2).setVisible(true);
            pannelloCorrente.setVisible(false);
            CloudFabFrame.getFRAME().setContentPane(pannelloCorrente.pannelliCollegati.get(2));
            pannelloCorrente.pannelliCollegati.get(2).requestFocus();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}