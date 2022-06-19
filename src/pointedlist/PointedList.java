package pointedlist;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 *
 * @author Leonardo L.
 * 
 */

public class PointedList<T> implements Iterable{
    
    Nodo<T> raiz;
    Nodo<T> ult;
    public int tam;
    /**
     * apuntador que recorre la lista.
     * Referencias: 
     * {@link #getPointedItem()}
     * {@link #next()}
     * {@link #rewind()}
     */
    Nodo<T> pointer;
    
    /**
     * Constructor Vacio Xd
     */
    public PointedList(){
        raiz=ult=null;
        tam=0;
    }
    
    /**
     * Añade un elemento a la Lista al final
     * @param dato El dato a ingresar
     * 
     */
    public void add(T dato){
        Nodo<T> p=new Nodo(dato);
        if(raiz==null){
            pointer=raiz=ult=p;
        }else{
            ult.link=p;
            ult=p;
        }
        tam++;
    }
    
    /**
     * Avanza el puntero al siguiente nodo.
     */
    public void next(){
        if(pointer!=null)pointer=pointer.link;
    }
    
    /**
     * Retorna el elemento al que hace referencia el puntero
     * @return 
     * devuelve el item apuntado.
     */
    public T getPointedItem(){
        if(pointer==null)return null;
        else return (T) pointer.dato;
    }
    
    /**
     * Reinicia el puntero devolviendolo a la raiz.
     */
    public void rewind(){
        pointer=raiz;
    }
    
    /**
     * Una función que retorna el tamaño de la lista
     * @return 
     * El tamaño de la lista
     */
    public int size(){
        return tam;
    }
    
    /**
     * Este método retorna un elemento en la posición i de la lista
     * Puede arrojar un IllegalArgumentException o un ArrayIndexOutOfBounds
     * @param i
     * La posición o index del elemento a buscar
     * @return 
     * El elemento en la posición i
     * @Throws
     * Puede arrojar un IllegalArgumentException o un ArrayIndexOutOfBounds
     */
    public T get(int i){
        if(i>=tam){
            throw new ArrayIndexOutOfBoundsException();
        }else if(i<0){
            throw new IllegalArgumentException();
        }else{
            Nodo p=raiz;
            for (int j = i; j > 0; j--) {
                p=p.link;
            }
            return (T) p.dato;
        }
    }
    
    /**
     * Esta función busca un elemento mediante su mpetodo equals y retorna el index de la primera aparición
     * @param dato
     * el elemento a buscar en la lista
     * @return 
     * el index de la primera aparición del elemento dato
     * 
     */
    public int indexOf(T dato){
        if(raiz==null)return -1;
        Nodo p=raiz;
        int i=0;
        while(p.link!=null && !p.dato.equals(dato)){
            p=p.link;
            i++;
        }
        if(p.dato.equals(dato)) return i;
        else return -1;
    }
    
    /**
     * Esta función verifica si la lista está vacia
     * @return 
     * Retorna true si la lista está vacia, retorna false si hay por lo menos un elemento en la lista
     */
    public boolean isEmpty(){
        if(raiz==null)return true;
        else return false;
    }
    
    /**
     * Este metodo retorna la lista actual como un array de objetos
     * @return 
     * Retorna un array de objetos con todos los elementos de la lista
     */
    public Object[] toArray(){
        
        Object t[]=new Object[tam];
        Nodo p=raiz;
        for (int i = 0; i < tam; i++) {
            t[i]=p.dato;
            p=p.link;
        }
        
        return t;
    }
    
    /**
     * Se supone que debería devolver un arreglo del mismo tipo que el parametro pero no sé si funcione
     * @param <G>
     * @param data
     * @return 
     */
    public <G> G[] toArray(G[] data){
        
        Object[] o=new Object[tam];
        Nodo p=raiz;
        for (int i = 0; i < tam; i++) {
            o[i]=p.dato;
            p=p.link;
        }
        
        return (G[])o; 
   }
    
    /**
     * Heredado de la interfaz Iterable, devuelve un Iterator de la actual lista
     * @return 
     * Una representación Iterator de esta lista
     */
    @Override
    public Iterator iterator() {
        return new Iter();
    }
    
    /**
     * Sobreescrito de la interfaz Iterable Habilita la función de foreach para esta lista
     * @param cnsmr 
     * La acción a ralizar, creo
     */
    @Override
    public void forEach(Consumer cnsmr) {
        //Iterable.super.forEach(cnsmr); //To change body of generated methods, choose Tools | Templates.
        Nodo p=raiz;
        while(p!=null){
            cnsmr.accept(p.dato);
            p=p.link;
        }
    }
    
    
    class Iter implements Iterator<T>{
        
        Nodo base;
        
        Iter(){
            base=new Nodo();
            base.link=raiz;
        }
        
        /**
         * Sobreescrito de la interfaz Iterator
         * Verifica si se tiene un siguiente elemento
         * @return 
         * Retorna true si hay un siguiente elemento
         */
        @Override
        public boolean hasNext() {
            
            if(base.link!=null)return true;
            else return false;
            
        }

        /**
         * Sobreescrito de la interfaz Iterator
         * Retorna el siguiente elemento en la lista, si es la primera vez que se llama retornará el primer elemento
         * @return 
         * Retorna el siguiente elemento
         */
        @Override
        public T next() {
            
            if(hasNext()){
                base=base.link;
                return (T) base.dato;
            }
            return null;
        }
        
    }
    
    public void sort(Comparing c, int orden){
        if(raiz==null || orden==0) return;
        
        Nodo n=raiz.link, antN=raiz;
        while(n!=null){
            
            Nodo q=raiz, antQ=null, r=n;
            boolean done=false;
            
            while(q!=null && !q.equals(n) && !done){
                
                
                if(orden>0?c.compareTo((T)n.dato, (T)q.dato)<0:c.compareTo((T)n.dato, (T)q.dato)>0){
                    r=antN;
                    
                    antN.link=n.link;
                    if(n.link==null) ult=antN;
                    
                    n.link=q;
                    if(antQ!=null) antQ.link=n;
                    else raiz=n;
                    
                    done=true;
                }
                antQ=q;
                q=q.link;
            }
            
            antN=r;
            n=r.link;
        }
        
    }
    
    public void set(int i, T t){
        if(i==tam) add(t);
        else if(i>tam){
            throw new ArrayIndexOutOfBoundsException();
        }else if(i<0){
            throw new IllegalArgumentException();
        }else{
            Nodo n=new Nodo(t);
            if(i==0){
                n.link=raiz;
                raiz=n;
            }else{
                Nodo p=raiz;
                int j=0;
                while(p.link!=null && ++j<i-1){
                    p=p.link;
                }
                n.link=p.link;
                p.link=n;
            }
            tam++;
        }
    }
    
}