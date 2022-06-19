/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointedlist;

import java.util.Iterator;

/**
 *
 * @author Leonardo
 */
class Nodo<T>{
    T dato;
    Nodo<T> link;
    
    Nodo(){
        dato=null;
        link=null;
    }
    
    Nodo(T dato){
        this.dato=dato;
    }
    
}
