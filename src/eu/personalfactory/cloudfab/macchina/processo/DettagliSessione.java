/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

/**
 *
 * @author francescodigaudio
 */
public class DettagliSessione {

    //Dichiarazione Codice Operatore
    private static int idFigura = 0;
    private static String codiceFigura = "";
    private static String nominativoFigura = "";
    private static String figura = "";
    private static int idFiguraTipo = 0; 
    private static boolean superUser = false;

    public static boolean isSuperUser() {
        return superUser;
    }

    public static void setSuperUser(boolean superUser) {
        DettagliSessione.superUser = superUser;
    }

    public static String getNominativoFigura() {
        return nominativoFigura;
    }

    public static void setNominativoFigura(String nominativoFigura) {
        DettagliSessione.nominativoFigura = nominativoFigura;
    }

    public static String getFigura() {
        return figura;
    }

    public static void setFigura(String figura) {
        DettagliSessione.figura = figura;
    }

    public static int getIdFiguraTipo() {
        return idFiguraTipo;
    }

    public static void setIdFiguraTipo(int idFiguraTipo) {
        DettagliSessione.idFiguraTipo = idFiguraTipo;
    }

    //Restituzione Codice Operatore
    public static String getCodiceFigura() {
        return codiceFigura;
    }

    //Restituzione Codice Operatore
    public static int getIdFigura() {
        return idFigura;
    }

    //Impostazione Codice Figura
    public static void setCodiceFigura(String codiceFigura) {
        DettagliSessione.codiceFigura = codiceFigura;
    }

    //Impostazione Id Figura
    public static void setIdFigura(int idFigura) {
        DettagliSessione.idFigura = idFigura;
    }

}
