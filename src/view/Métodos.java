package view;
import javax.imageio.ImageIO;
import pointedlist.PointedList;

/**
 * Replicación de algunos métodos de la clase String y otras funciones adicionales
 * @author LeonardoVergara
 */
public class Métodos {

    public static String join(String sep, String split[]){
        String join="";
        
        for(int i=0; i<split.length; i++){
            join+=split[i];
            if(i<split.length-1) join+=sep;
        }
        
        return join;
    }
    
    public static String[] split(String string, String sep){
        
        PointedList<String> pl=new PointedList();
        int i=0, j=0, n=0, stringLength=string.length(), sepLength=sep.length();
        
        while(i<stringLength){
            
            if(i+sepLength>stringLength){
                pl.add(string.substring(j, stringLength));
                
                i=stringLength;
                n++; j=-1;
            }else{
                String s=string.substring(i, i+sepLength);
                
                if(compareTo(s, sep)==0){
                    pl.add(string.substring(j, i));
                    
                    i+=sepLength;
                    j=i; n++;
                }else i++;
            }
        }
        if(j!=-1){
            pl.add(string.substring(j, i));
            n++;
        }
        
        String[] split=new String[n];
        for(int k=0; k<n; k++) split[k]=pl.get(k);
        
        return split;
    }
    
    public static String[] splitParameters(String string, String sep){
        
        PointedList<String> pl=new PointedList();
        int i=0, j=0, n=0, stringLength=string.length(), sepLength=sep.length();
        int parentAp=0, parentCie=0, parentApPend=0;
        
        while(i<stringLength){
            String par=string.substring(i, i+1);
            if(compareTo(par, "(")==0){
                parentAp++;
                parentApPend++;
            }else if(compareTo(par, ")")==0){
                if(parentApPend==0) return null;
                else{
                    parentCie++;
                    parentApPend--;
                }
            }
            
            if(i+sepLength>stringLength){
                pl.add(string.substring(j, stringLength));
                
                i=stringLength;
                n++; j=-1;
            }else{
                String s=string.substring(i, i+sepLength);
                
                if(parentApPend==0 && compareTo(s, sep)==0){
                    pl.add(string.substring(j, i));
                    
                    i+=sepLength;
                    j=i; n++;
                }else i++;
            }
        }
        if(parentAp!=parentCie) return null;
        if(j!=-1){
            pl.add(string.substring(j, i));
            n++;
        }
        
        String[] split=new String[n];
        for(int k=0; k<n; k++) split[k]=pl.get(k);
        
        return split;
    }
    
    public static int length(String s){
        int n=-1;
        try{
            while(true) s.substring(++n, n+1);
        }catch(IndexOutOfBoundsException e){}
        return n;
    }
    
    public static int compareTo(String s1, String s2){
        int n1=s1.length(), n2=s2.length(), min=n1<n2?n1:n1==n2?n1:n2;
        
        for(int i=0; i<min; i++){
            char c1=s1.charAt(i), c2=s2.charAt(i);
            if(c1>c2) return 1;
            if(c2>c1) return -1;
        }
        if(n1>n2) return 1;
        if(n1<n2) return -1;
        
        return 0;
    }
    
    public static String trim(String string){
        int n=string.length(), i=-1, j=n+1;
        String trim=" \n\t\r";
        while((++i)<n && in(string.substring(i, i+1), trim));
        while((--j)>0 && j!=i && in(string.substring(j-1, j), trim));
        
        return string.substring(i, j);
    }
    
    public static boolean in(String sub, String string){
        int stringLength=sub.length(), inLength=string.length();
        
        int i=-1;
        while((++i)<inLength){
            if(i+stringLength>inLength) return false;
            if(compareTo(sub, string.substring(i, i+stringLength))==0) return true;
        }
        
        return false;
    }
    
    public static int find(String sub, String string){
        if(compareTo(sub, "")==0) return 0;
        int subLength=sub.length(), stringLength=string.length();
        if(subLength>stringLength) return -1;
        if(subLength==stringLength) return compareTo(sub, string)==0?0:-1;
        
        int i=subLength-1;
        while((++i)<=stringLength) if(compareTo(string.substring(i-subLength, i), sub)==0)
            return i-subLength;
        
        return -1;
    }
    
    public static int findInverse(String sub, String string){
        if(compareTo(sub, "")==0) return 0;
        int subLength=sub.length(), stringLength=string.length();
        if(subLength>stringLength) return -1;
        if(subLength==stringLength) return compareTo(sub, string)==0?0:-1;
        
        int i=stringLength-subLength+1;
        while((--i)>=0) if(compareTo(string.substring(i, i+subLength), sub)==0) return i;
        
        return -1;
    }
    
    public static String lower(String string){
        String lower="";
        int n=string.length(), i=-1;
        
        while((++i)<n){
            char c=string.substring(i, i+1).charAt(0);
            if(c>=65 && c<=90) c+=32;
            lower+=c;
        }
        
        return lower;
    }
    
    public static String upper(String string){
        String upper="";
        int n=string.length(), i=-1;
        
        while((++i)<n){
            char c=string.substring(i, i+1).charAt(0);
            if(c>=97 && c<=122) c-=32;
            upper+=c;
        }
        
        return upper;
    }
    
    public static boolean isAlpha(String string){
        int n=string.length(), i=-1;
        
        while((++i)<n){
            char c=string.substring(i, i+1).charAt(0);
            if(c<65 || c>90 && c<97 || c>122){
                String a="";
                a+=c;
                if(!in(a, " \n\t")) return false;
            }
        }
        
        return true;
    }
    
    public static boolean findCoincidence(Object o1, Object o2){
        if(o1 instanceof Integer) return (Integer)o1==(Integer)o2;
        if(o1 instanceof String) return in(String.valueOf(o1), String.valueOf(o2));
        if(o1 instanceof Double) return (Double)o1==(Double)o2;
        return false;
    }
    
    public static int máximoEntero(PointedList<Integer> p){
        int max=p.get(0);
        for(Object o: p){
            int d=(int)o;
            if(d>max) max=d;
        }
        
        return max;
    }
    
    public static int mínimoEntero(PointedList<Integer> p){
        int min=p.get(0);
        for(Object o: p){
            int d=(int)o;
            if(d<min) min=d;
        }
        
        return min;
    }
    
    public static double promedioEntero(PointedList<Integer> p){
        int prom=0;
        int n=0;
        for(Object o: p){
            prom+=(int)o;
            n++;
        }
        
        return prom/n;
    }
    
    public static double máximoReal(PointedList<Double> p){
        double max=p.get(0);
        for(Object o: p){
            double d=(double)o;
            if(d>max) max=d;
        }
        
        return max;
    }
    
    public static double mínimoReal(PointedList<Double> p){
        double min=p.get(0);
        for(Object o: p){
            double d=(double)o;
            if(d<min) min=d;
        }
        
        return min;
    }
    
    public static double promedioReal(PointedList<Double> p){
        double prom=0;
        int n=0;
        for(Object o: p){
            prom+=(double)o;
            n++;
        }
        
        return prom/n;
    }
    
    /*
    public static double máximo(PointedList p){
        double max;
        if(p.get(0) instanceof Integer){
            max=(double)(int)(p.get(0));
        }else{
            max=(double)p.get(0);
        }
        
        for(Object o: p){
            double d=(double)o;
            if(d>max) max=d;
        }
        
        return max;
    }
    
    public static double mínimo(PointedList p){
        double min=(double)p.get(0);
        for(Object o: p){
            double d=(double)o;
            if(d<min) min=d;
        }
        
        return min;
    }
    
    public static double promedio(PointedList p){
        double prom=0;
        int n=0;
        for(Object o: p){
            prom+=(double)o;
            n++;
        }
        
        return prom/n;
    }
    */
    public static String máximoString(PointedList<String> p){
        String max=p.get(0);
        for(Object o: p){
            String s=(String)o;
            if(compareTo(s, max)>0) max=s;
        }
        
        return max;
    }
    
    public static String mínimoString(PointedList<String> p){
        String min=p.get(0);
        for(Object o: p){
            String s=(String)o;
            if(compareTo(s, min)<0) min=s;
        }
        
        return min;
    }
    
    static java.awt.image.BufferedImage imageFrom(String src){
        try{
            return ImageIO.read(new java.io.File(src));
        }catch(java.io.IOException e){
            return null;
        }
    }
    
}