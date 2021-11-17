/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author francescodigaudio
 */
public class FileExtFilter implements FilenameFilter
{
    private static String estensione;

    public FileExtFilter (String estensione)
    {
        FileExtFilter.estensione = estensione;
    }

    @Override
    public boolean accept (File dir, String name)
    {
        return name.endsWith (estensione);
    }
}
  
