/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.gestore.password;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author francescodigaudio
 */
public class GestorePassword {

    private static int USERNAME_LEN = 8;
    private static int PASSWORD_LEN = 10;

    //Restituisce la user_name Codificata per l'accesso al Database
    public static String userName() {
        
        
        //Impostazione runtime di username e password
////        connnectionProperties.put("hibernate.connection.username", "root");//GestorePassword.userName());
////        connnectionProperties.put("hibernate.connection.password", "isolmix1503");//GestorePassword.passWord());
        //return codificaUser();
        return "root";

    }

    //Restituisce la password Codificata per l'accesso al Database
    public static String passWord() {

       // return codificaPassword();
        return "isolmix1503";

    }

    //Codifica UserName
    private static String codificaUser() {
        String hostNameAddress = "";
        String hostName = "";

        //Lettura dell'hostName
        try {

            hostNameAddress = java.net.InetAddress.getLocalHost().toString();

        } catch (UnknownHostException ex) {
        }

        //Estrazione del Nome del Computer dall'indirizzo e codifica
        int j = 0;

        while (hostNameAddress.charAt(j) != '/') {

            if (j % 2 != 0) {
                hostName += (int) hostNameAddress.charAt(j);
                //Si scelgono solo i caratteri ad indice dispari e si convertono in decimale
            }
            j++;
        }

        return hostName.substring(0, USERNAME_LEN);
    }

    //Codifica Password
    private static String codificaPassword() {
        String res = "";
        String hostNameAddress = "";

        //Lettura dell'hostName
        try {

            hostNameAddress = java.net.InetAddress.getLocalHost().toString();

        } catch (UnknownHostException ex) {
        }

        //Codifica: ad ogni indice pari viene sostituito la conversione in 
        //Ascii del carattere divisa per la lunghezza della stringa
        for (int i = 0; i < hostNameAddress.length(); i++) {

            if (i % 2 == 0) {

                res += (int) hostNameAddress.charAt(i) / hostNameAddress.length();
            } else {
                res += hostNameAddress.charAt(i);
            }

        }
        return res.substring(0, PASSWORD_LEN);
    }

    public static void creaUtente(String rootU, String passU, String user, String password, String nomeDatabase, String host) {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://" + host + ":3306/" + nomeDatabase;

        try {
            con = DriverManager.getConnection(url, rootU, passU);
            st = con.createStatement();
            rs = st.executeQuery("GRANT ALL PRIVILEGES ON *.* TO '" + user + "'@'" + host + "' IDENTIFIED BY '" + password + "' WITH GRANT OPTION;");

        } catch (SQLException ex) {

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
            }
        }

    }

    public static boolean testConnessione(String user, String password, String nomeDatabase, String host) {

        Boolean result = false;

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://" + host + ":3306/" + nomeDatabase;

        try {
            con = DriverManager.getConnection(url, codificaUser(), codificaPassword());
            st = con.createStatement();
            result = con.isValid(1000);

        } catch (SQLException ex) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
            }
        }

        return result;
    }

}
