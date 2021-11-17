/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

import java.util.ArrayList;

/**
 *
 * @author francesco di gaudio
 */
public class IODevice {

    private Integer id;
    private String address;
    private Integer port;
    private Boolean enabled; 
    private Integer type; // Identifica il tipo di periferica 0 - Mduino - 1 EB80 
    private String name; // Identifica il nome della periferiche per le finestre di visualizzazione 
    private ArrayList<Integer> IndexOutput; // Identifica la posizione delle uscite sull'array Uscite
    private ArrayList<Integer> IndexInput; // Identifica la posizione delle uscite sull'array Uscite
 
    public ArrayList<Integer>  pos_input_dev,pos_output_dev; //Posizione ingressi-uscite periferica
    
    public ClientMDuino clientMduino;
    public ClientEB80 clientEB80;
    
    public IODevice(Integer id, String ip_address, Integer port, Integer timeout, Integer type, String name, Integer dim_pacchetti, String char_iniziale, String char_finale, Integer enabled) {
        this.id = id;
        this.address = ip_address;
        this.port = port;
        this.type = type;
        this.name = name;
        this.enabled = (enabled==1);

        IndexOutput = new ArrayList<>();
        IndexInput= new ArrayList<>();
       
        pos_input_dev= new ArrayList<>();
        pos_output_dev= new ArrayList<>();

        if (type == 0) {
            //////////////
            // MDUINO  ///
            //////////////

            clientMduino = new ClientMDuino(ip_address, port, timeout, dim_pacchetti, char_iniziale, char_finale);

        } else if (type == 1) {
            clientEB80 = new ClientEB80(ip_address, timeout, this.enabled);
            
        }
    }

    ///////////////////////////
    // GETTERS AND SETTERS  ///
    ///////////////////////////
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpAddress() {
        return address;
    }

    public void setIpAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getIndexInput() {
        return IndexInput;
    }

    public void setIndexInput(ArrayList<Integer> IndexInput) {
        this.IndexInput = IndexInput;
    }

    public ArrayList<Integer> getIndexOutput() {
        return IndexOutput;
    }

    public void setIndexOutput(ArrayList<Integer> IndexOutput) {
        this.IndexOutput = IndexOutput;
    }

    public void addToIndexInput(Integer s) {
        this.IndexInput.add(s);
    }

    public void addToIndexOutput(Integer s) {
        this.IndexOutput.add(s);
    }
     public Boolean isEnaled() {
        return enabled;
    }
    

}
