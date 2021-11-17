package eu.personalfactory.cloudfab.macchina.utility;
 
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class FabCloudFramePesaComplementare extends JFrame {

    //COSTRUTTORE
    public FabCloudFramePesaComplementare() {
        super();

        setFrame();

    }

    private void setFrame() {

        //Impostazione Titolo Finestra
        setTitle("Finestra Pesa Componenti Complementari");

        //Dimensionamento Finestra
        setSize(200, 400);

        //Operazione in Seguito alla Chiusura della Finestra
        setDefaultCloseOperation(JFrame.NORMAL);

        //Centraggio della Finestra
        setLocationRelativeTo(null); 
        
        //Impostazione Ordine di Visualizzazione del Frame
        setAlwaysOnTop(true);

        //Eliminazione Decorazioni Finestra 
        setUndecorated(true);


    }
}