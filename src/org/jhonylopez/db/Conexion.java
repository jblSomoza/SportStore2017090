/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class Conexion {
    private Connection conexion;
    private static Conexion instancia;
    
    public Conexion(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            conexion = DriverManager.getConnection("jdbc:sqlserver://localhost;DataBaseName=DBSportStore2017090","sa","sa123");
                                                                        
                    
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
            e.getMessage();
        
        }catch(InstantiationException e) {
            e.printStackTrace();
            e.getMessage();
        
        }catch(IllegalAccessException e) {
            e.printStackTrace();
            e.getMessage();
            
        }catch(SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
    public static Conexion getInstancia(){ 
        if(instancia == null){ 
            instancia = new Conexion();
        } 
        return instancia;
    } 


    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}
