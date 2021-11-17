/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

/**
 *
 * @author francescodigaudio
 */
public class IO {
    
   private Integer type;
   private Integer device;
   private Integer position;
   private Boolean soft_state;  //Stato dell'IO sul software
   private Boolean dev_state;   //Stato dell'IO sulla periferica
   private String name;

   //Costruttore
    public IO(Integer type, String name, Integer device, Integer position, Boolean soft_state, Boolean dev_state) {
        this.type = type;
        this.name = name;
        this.device = device;
        this.position = position;
        this.soft_state = soft_state;
        this.dev_state = dev_state;
        
    }

    //////////////////////////
    // GETTERS AND SETTERS ///
    //////////////////////////
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDevice() {
        return device;
    }

    public void setDevice(Integer device) {
        this.device = device;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    } 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSoft_state() {
        return soft_state;
    }

    public void setSoft_state(Boolean soft_state) {
        this.soft_state = soft_state;
    }

    public Boolean getDev_state() {
        return dev_state;
    }

    public void setDev_state(Boolean dev_state) {
        this.dev_state = dev_state;
    }
   
    
   
   
}
