package view;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import pointedlist.PointedList;
import sied.*;

public class HCI extends javax.swing.JFrame {
    
    private EntidadDeportiva sied;
    private String siedDirPath, generalDirPath;
    private DefaultTableModel tm;
    private int archivoActual;
    
    PointedList<Comando> comandos;
    PointedList<String> history;
    int historyIndex;
    private static final String DEF_SIED_DIR_PATH="src/res/sied/", DEF_GENERAL_DIR_PATH="src/res/general/";
    // Image by <a href="https://pixabay.com/users/stocksnap-894430/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=2616911">StockSnap</a> from <a href="https://pixabay.com/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=2616911">Pixabay</a>
    private static final java.awt.image.BufferedImage BACKGROUND=Métodos.imageFrom("src/res/img/background.jpg");
    
    public HCI() {
        initComponents();
        archivoActual=0;
        siedDirPath=DEF_SIED_DIR_PATH;
        generalDirPath=DEF_GENERAL_DIR_PATH;
        init();
        initComandos();
        initSIED();
        
        recibirComando("info()");
    }
    
    private int generar(int file, int reg){
        //if(sied==null) return -1;
        if(file<0 || file>3) return -1;
        if(reg<0) return -1;
        
        //labelCalculos.setText("Generación de Datos");
        areaCalculos.setText("");
        //areaComandos.setText("Resultados:");
        //initSIED();
        switch(file){
            case 1:
                return genDTs(reg);
            case 2:
                return genPatrocinadores(reg);
            case 3:
                return genSecciones(reg);
            case 0:
                genDTs(reg);
                areaCalculos.append("\n\n");
                genPatrocinadores(reg);
                areaCalculos.append("\n\n");
                genSecciones(reg);
        }
        actualizar(archivoActual);
        return -1;
    }
    
    private void ordenar(int fileNum, int campo, int orden){
        class Registro{
            int campo1;
            String campo2;
            double campo3;
            
            Registro(int c1, String c2, double c3){
                campo1=c1;
                campo2=c2;
                campo3=c3;
            }
        }
        
        if(campo<1 || campo>3) return;
        String cabecera, file;
        if(fileNum==1){
            cabecera="DNI\tNOMBRE\tSALARIO(€)";
            file="directores_tecnicos.txt";
        }else if(fileNum==2){
            cabecera="NIF\tNOMBRE\tRETORNO(€)";
            file="patrocinadores.txt";
        }else if(fileNum==3){
            cabecera="CÓDIGO\tNOMBRE\nPRESUPUESTO(€)";
            file="secciones.txt";
        }else return;
        
        PointedList<Registro> regs=new PointedList(); 
        try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
            br.readLine();
            String l;
            while((l=br.readLine())!=null){
                String[] temp=Métodos.split(l, ";");
                regs.add(new Registro(Integer.parseInt(temp[0]), temp[1], Double.parseDouble(temp[2])));
            }
        }catch(IOException e){}
        
        if(campo==1){
            regs.sort((Object o1, Object o2)->{
                Registro r1=(Registro)o1, r2=(Registro)o2;
                int i1=r1.campo1, i2=r2.campo1;
                return i1>i2?1:i1==i2?0:-1;
            }, orden);
        }else if(campo==2){
            regs.sort((Object o1, Object o2)->{
                Registro r1=(Registro)o1, r2=(Registro)o2;
              return Métodos.compareTo(r1.campo2, r2.campo2);
            }, orden);
        }else if(campo==3){
            regs.sort((Object o1, Object o2)->{
                Registro r1=(Registro)o1, r2=(Registro)o2;
                double d1=r1.campo3, d2=r2.campo3;
                return d1>d2?1:d1==d2?0:-1;
            }, orden);
        }
        
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(siedDirPath+file))){
            bw.write(cabecera);
            for(Object o: regs){
                Registro r=(Registro)o;
                bw.write("\n"+String.valueOf(r.campo1)+";"+r.campo2+";"+String.valueOf(r.campo3));
            }
        }catch(IOException e){}
        
        if(archivoActual==fileNum) actualizar(archivoActual);
    }
    
    private void buscar(int fileNum, int campo, Object reg){
        //if(sied==null) return;
        if(campo<1 || campo>3) return;
        String cabecera, file;
        if(fileNum==1){
            cabecera="DNI\tNOMBRE\tSALARIO(€)";
            file="directores_tecnicos.txt";
        }else if(fileNum==2){
            cabecera="NIF\tNOMBRE\tRETORNO(€)";
            file="patrocinadores.txt";
        }else if(fileNum==3){
            cabecera="CÓDIGO\tNOMBRE\nPRESUPUESTO(€)";
            file="secciones.txt";
        }else return;
        
        //labelCalculos.setText("Búsqueda");
        areaCalculos.setText("Archivo: "+fileNum+", Campo: "+campo+", Registro: "+reg+".");
        boolean f=false;
        
        try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
            br.readLine();
            String l;
            while((l=br.readLine())!=null){
                String[] temp=Métodos.split(l, ";");
                Object o;
                if(campo==1) o=Integer.parseInt(temp[0]);
                else if(campo==2) o=Métodos.lower(temp[1]);
                else o=Double.parseDouble(temp[2]);
                
                if(Métodos.findCoincidence(reg, o)){
                    if(!f) f=true;
                    areaCalculos.append("\n\n"+cabecera+"\n"
                        +temp[0]+"\t"+temp[1]+"\t"+temp[2]);
                }
            }
            if(!f) areaCalculos.append("\n\nEl registro solicitado no existe.");

        }catch(IOException e){
            areaCalculos.append("\n\nEl registro solicitado no existe.");
        }
    }
    
    private void calcular(int fileNum, int campo){
        //if(sied==null) return;
        if(campo<1 || campo>3) return;
        String cabecera, file;
        if(fileNum==1){
            cabecera="DNI\tNOMBRE\tSALARIO(€)";
            file="directores_tecnicos.txt";
        }else if(fileNum==2){
            cabecera="NIF\tNOMBRE\tRETORNO(€)";
            file="patrocinadores.txt";
        }else if(fileNum==3){
            cabecera="CÓDIGO\tNOMBRE\nPRESUPUESTO(€)";
            file="secciones.txt";
        }else return;
        
        //labelCalculos.setText("Cálculo");
        areaCalculos.setText("Archivo: "+file+", Campo: "+campo+":\n\n");
        
        if(campo==1){
            try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
                PointedList<Integer> cods=new PointedList();
                br.readLine();
                String l;
                while((l=br.readLine())!=null)
                    cods.add(Integer.parseInt(Métodos.split(l, ";")[0]));
                areaCalculos.append("Máximo = "+Métodos.máximoEntero(cods)+".\n");
                areaCalculos.append("Mínimo = "+Métodos.mínimoEntero(cods)+".\n");
                areaCalculos.append("Promedio = "+Métodos.promedioEntero(cods)+".\n");
            }catch(IOException e){
                
            }
        }else if(campo==2){
            try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
                PointedList<String> nombres=new PointedList();
                br.readLine();
                String l;
                while((l=br.readLine())!=null)
                    nombres.add(Métodos.split(l, ";")[1]);
                areaCalculos.append("Máximo = "+Métodos.máximoString(nombres)+".\n");
                areaCalculos.append("Mínimo = "+Métodos.mínimoString(nombres)+".\n");
            }catch(IOException e){
                
            }
        }else if(campo==3){
            try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
                PointedList<Double> d=new PointedList();
                br.readLine();
                String l;
                while((l=br.readLine())!=null)
                    d.add(Double.parseDouble(Métodos.split(l, ";")[2]));
                areaCalculos.append("Máximo = "+Métodos.máximoReal(d)+".\n");
                areaCalculos.append("Mínimo = "+Métodos.mínimoReal(d)+".\n");
                areaCalculos.append("Promedio = "+Métodos.promedioReal(d)+".\n");
            }catch(IOException e){
                
            }
        }
    }
    
    private void init(){
        tm = new DefaultTableModel(null, new String[] {"Seleccione un Archivo por medio de Comandos"});
        comandos = new PointedList();
        history = new PointedList();
        historyIndex = -1;
                
        tablaArchivos.setModel(tm);
        areaComandos.setText("Digita 'help()' para obtener ayuda.\n");
        fieldComandos.requestFocus();
        ((DefaultCaret) areaCalculos.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        fieldComandos.addKeyListener(new java.awt.event.KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    recibirComando();
                else if(e.getKeyCode() == KeyEvent.VK_UP) {
                    int n = history.size();
                    if(n != 0) {
                        if(historyIndex == -1) historyIndex = n - 1;
                        else if(historyIndex > 0) historyIndex--;
                        else return;
                        fieldComandos.setText(history.get(historyIndex));
                    }
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int n = history.size();
                    if(n != 0 && historyIndex != -1) {
                        if(historyIndex == n - 1) {
                            historyIndex = -1;
                            fieldComandos.setText("");
                        }
                        else if(historyIndex < n - 1) {
                            historyIndex++;
                            fieldComandos.setText(history.get(historyIndex));
                        }
                    }
                }
                else if(KeyEvent.getKeyText(e.getKeyCode()).length() == 1 && historyIndex != -1)
                    historyIndex = -1;
            }
        });
    }
    
    private void initSIED(){
        String nombre, ubicación;
        double capital, deuda;
        float proyIngresos, infrServ, recSecciones, recHumanos, recDTs, ingEnt, ingPatr, ingVent;
        java.time.LocalDate fundación;
        
        try(FileReader fr=new FileReader(generalDirPath+"general.txt"); BufferedReader br=new BufferedReader(fr)){
            br.readLine();
            String[] general=Métodos.split(br.readLine(), ";");
            
            nombre=general[0];
            String[] temp=Métodos.split(general[1], "/");
            fundación=java.time.LocalDate.of(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            ubicación=general[2];
            capital=Double.parseDouble(general[3]);
            proyIngresos=Float.parseFloat(general[4]);
            deuda=Double.parseDouble(general[5]);
            infrServ=Float.parseFloat(general[6]);
            recSecciones=Float.parseFloat(general[7]);
            recHumanos=Float.parseFloat(general[8]);
            recDTs=Float.parseFloat(general[9]);
            ingEnt=Float.parseFloat(general[10]);
            ingPatr=Float.parseFloat(general[11]);
            ingVent=Float.parseFloat(general[12]);
            
            sied=new EntidadDeportiva(nombre, fundación, ubicación, capital, deuda, infrServ, recSecciones, recHumanos, recDTs, proyIngresos, ingEnt, ingPatr, ingVent);
            
        }catch(IOException e){}
    }
    
    private int genDTs(int reg){
        areaCalculos.append("\'directores_tecnicos.txt\':");
        int numNombres=0;
        //numNombres=285;
        double presSalarios=sied.getCapitalGeneral()*sied.getGastosRecHum()*sied.getGastosDTsRecHum();
        double maxSal=presSalarios/reg, minSal=maxSal*0.9, extra=maxSal-minSal;
        //*
        try{
            FileReader fr=new FileReader(generalDirPath+"nombres.txt");
            BufferedReader br=new BufferedReader(fr);
            
            br.readLine();
            while(br.readLine()!=null && numNombres<reg*2) numNombres++;
            String[] nombres=new String[numNombres], apellidos=new String[numNombres];
            
            fr=new FileReader(generalDirPath+"nombres.txt");
            br=new BufferedReader(fr);
            br.readLine();
            String l;
            for(int i=0; i<numNombres; i++){
                l=br.readLine();
                String[] temp=Métodos.split(l,";");
                nombres[i]=temp[0];
                apellidos[i]=temp[1];
            }
            
            File siedDir = new File(siedDirPath);
            if(!siedDir.exists()) siedDir.mkdir();
            
            FileWriter fw=new FileWriter(new File(siedDirPath+"directores_tecnicos.txt"));
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write("DNI;NOMBRE;SALARIO(€)");
            for(int i=0; i<reg; i++){
                double sal=Math.round(minSal+Math.random()*extra);
                String nombre=nombres[(int)(Math.random()*numNombres)]+" "+nombres[(int)(Math.random()*numNombres)]+" "+
                        apellidos[(int)(Math.random()*numNombres)]+" "+apellidos[(int)(Math.random()*numNombres)];
                
                String dni=String.valueOf(i+1);
                while(dni.length()<10) dni="0"+dni;
                bw.write("\n"+dni+";"+nombre+";"+sal);
            }
            
            br.close();
            bw.close();
            
            areaCalculos.append(" generado exitosamente!");
            if(archivoActual==1) actualizar(1);
            return 1;
        }catch(java.io.IOException e){
            System.out.println("Error nombres.");
            areaCalculos.append(" error al generar archivo.");
            return -1;
        }
    }
    
    private int genPatrocinadores(int reg){
        areaCalculos.append("\'patrocinadores.txt\':");
        int numPatr=0;
        //numPatr=216;
        double retPatr=sied.getCapitalGeneral()*sied.getIngresosPatrocinios();
        double minRet=retPatr/reg, maxRet=minRet*1.5, extra=maxRet-minRet;
        //*
        try{
            FileReader fr=new java.io.FileReader(generalDirPath+"nombres_patrocinadores.txt");
            BufferedReader br=new java.io.BufferedReader(fr);
            
            br.readLine();
            while(br.readLine()!=null && numPatr<reg*2) numPatr++;
            String[] patr=new String[numPatr];
            
            fr=new FileReader(generalDirPath+"nombres_patrocinadores.txt");
            br=new BufferedReader(fr);
            br.readLine();
            for(int i=0; i<numPatr; i++) patr[i]=br.readLine();
            
            FileWriter fw=new java.io.FileWriter(siedDirPath+"patrocinadores.txt");
            BufferedWriter bw=new java.io.BufferedWriter(fw);
            bw.write("NIF;NOMBRE;RETORNO(€)");
            for(int i=0; i<reg; i++){
                double ret=Math.round(minRet+Math.random()*extra);
                String nombre=patr[(int)(Math.random()*numPatr)];
                
                String nif=String.valueOf(i+1);
                while(nif.length()<10) nif="0"+nif;
                bw.write("\n"+nif+";"+nombre+";"+ret);
            }
            
            br.close();
            bw.close();
            //bw.close();
            areaCalculos.append(" generado exitosamente!");
            if(archivoActual==2) actualizar(2);
            return 2;
        }catch(java.io.IOException e){
            System.out.println("Error nombres.");
            areaCalculos.append(" error al generar archivo.");
            return -1;
        }
    }
    
    private int genSecciones(int reg){
        areaCalculos.append("'secciones.txt\':");
        int numSec=0;
        //numSec=393;
        double presSec=sied.getCapitalGeneral()*sied.getGastosSecciones();
        double minPres=presSec/reg, maxPres=minPres*1.5, extra=maxPres-minPres;
        //*
        try{
            FileReader fr=new java.io.FileReader(generalDirPath+"nombres_secciones.txt");
            BufferedReader br=new java.io.BufferedReader(fr);
            
            br.readLine();
            while(br.readLine()!=null && numSec<reg*2) numSec++;
            String[] sec=new String[numSec];
            
            fr=new FileReader(generalDirPath+"nombres_secciones.txt");
            br=new java.io.BufferedReader(fr);
            br.readLine();
            for(int i=0; i<numSec; i++) sec[i]=br.readLine();
            
            FileWriter fw=new java.io.FileWriter(siedDirPath+"secciones.txt");
            BufferedWriter bw=new java.io.BufferedWriter(fw);
            bw.write("CÓDIGO;NOMBRE;PRESUPUESTO(€)");
            for(int i=0; i<reg; i++){
                double pres=Math.round(minPres+Math.random()*extra);
                String nombre=sec[(int)(Math.random()*numSec)];
                
                String nif=String.valueOf(i+1);
                while(nif.length()<10) nif="0"+nif;
                bw.write("\n"+nif+";"+nombre+";"+pres);
            }
            
            br.close();
            bw.close();
            //bw.close();
            areaCalculos.append(" generado exitosamente!");
            if(archivoActual==3) actualizar(3);
            return 3;
        }catch(java.io.IOException e){
            System.out.println("Error nombres.");
            areaCalculos.append(" error al generar archivo.");
            return -1;
        }
    }
    
    private void initComandos(){
        String desc;
        PointedList<Object> params;
        Executing e;
        
        //Generador
        params=new PointedList();
        params.add(0);
        params.add(0);
        e=(Object... o)->{
            return generar((Integer)o[0], (Integer)o[1]);
        };
        desc="'gen(archivo, reg)': Construye uno o varios archivos con "
                + "\n'reg' registros de información aleatoria. 'archivo' "
                + "\nes el número del archivo a generar "
                + "\n(1: directores_tecnicos, 2: patrocinadores, "
                + "\n3: secciones); en caso de ser 0, se generarán los 3 "
                + "\narchivos. En caso de generar sólo un archivo, se "
                + "\nretorna su número.";
        comandos.add(new Comando("gen", desc, params, e));
        
        //Buscador
        params=new PointedList();
        params.add(0); params.add(0); params.add(new Object());
        e=(Object... o)->{
            buscar((Integer)o[0], (Integer)o[1], o[2]);
            return true;
        };
        desc="'search(archivo, campo, reg)': Busca secuencialmente "
                + "\naquellos archivos que coinciden con el valor "
                + "\n'reg' en el archivo número 'archivo' y campo "
                + "\nnúmero 'campo'.";
        comandos.add(new Comando("search", desc, params, e));
        
        //Calculador
        e=(Object... o)->{
            calcular((Integer)o[0], (Integer)o[1]);
            return true;
        };
        params=new PointedList();
        params.add(0); params.add(0);
        desc="'calc(archivo, campo)': Calcula la media, el valor "
                + "\nmáximo y el valor mínimo del campo número "
                + "\n'campo' en el archivo número 'archivo.'";
        comandos.add(new Comando("calc", desc, params, e));
        
        //Ordenador
        e=(Object... o)->{
            ordenar((Integer)o[0], (Integer)o[1], (Integer)o[2]);
            return true;
        };
        params=new PointedList();
        params.add(0); params.add(0); params.add(0);
        desc="'sort(archivo, campo, orden)': Ordena los datos del archivo "
                + "\nnúmero 'archivo' basado en el campo número 'campo'. "
                + "\nSi 'orden' es mayor que 0, el orden es ascendente. "
                + "\nSi es menor que 0, descendente.";
        comandos.add(new Comando("sort", desc, params, e));
        
        //Mostrador
        e=(Object... o)->{
            return actualizar((Integer)o[0]);
        };
        params=new PointedList();
        params.add(0);
        desc="'show(archivo)': Muestra el archivo número 'archivo' en la "
                + "\ntabla y retorna dicho valor.";
        comandos.add(new Comando("show", desc, params, e));
        
        //Limpiador
        e=(Object... o)->{
            limpiarConsola();
            return true;
        };
        params=new PointedList();
        desc="'clear()': Limpia el área de comandos.";
        comandos.add(new Comando("clear", desc, params, e));
        
        //Exit
        e=(Object... o)->{
            exit();
            return true;
        };
        params=new PointedList();
        desc="'exit()': Finaliza la ejecución del programa.";
        comandos.add(new Comando("exit", desc, params, e));
        
        //Información general
        e=(Object... o)->{
            infoGeneral();
            return true;
        };
        params=new PointedList();
        desc="'info()': Muestra la información general del sistema.";
        comandos.add(new Comando("info", desc, params, e));
        
        //Sobreescritura
        e=(Object... o)->{
            override((Integer)o[0], (Integer)o[1], (Integer)o[2], String.valueOf(o[3]), (Integer)o[4]);
            return true;
        };
        params=new PointedList();
        params.add(0);
        params.add(0);
        params.add(0);
        params.add("");
        params.add(0);
        desc="'ovrd(archivo, reg, c1, c2, c3)': Sobreescribe el registro "
                + "\nnúmero 'reg' del archivo número 'archivo' con los "
                + "\ncampos 'c1', 'c2' y 'c3', en este orden.";
        comandos.add(new Comando("ovrd", desc, params, e));
        
        //Ayuda
        e=(Object... o)->{
            help();
            return true;
        };
        params=new PointedList();
        desc="'help()': Muestra el actual cuadro de ayuda.";
        comandos.add(new Comando("help", desc, params, e));
        
    }
    
    private void recibirComando() {
        recibirComando(fieldComandos.getText().trim());
    }
    
    private void recibirComando(String txt) {
        fieldComandos.setText("");
        //areaComandos.append("\n>> " + (Métodos.compareTo(areaComandos.getText(),"")!=0 ? txt : ""));
        areaComandos.append(">> " + txt + "\n");
        
        historyIndex = -1;
        if(Métodos.compareTo(txt, "") != 0) {
            history.add(txt);
            Object result = procesarComando(txt);
            if(result == null) areaComandos.append("Comando \""+txt+"\" no válido.\n");
        }
        
        JScrollBar vertScroll = scrollComandos.getVerticalScrollBar();
        vertScroll.setValue(vertScroll.getMaximum());
    }
    
    private Object procesarComando(String txt){
        Object informe=formatoComando(txt);
        int f, n;
        String cmd;
        
        try{
            Object[] o=(Object[])informe;
            f=(int)o[0];
            n=(int)o[1];
            cmd=(String)o[2];
            //procesarComando(txt.substring(f+1, n-1));
        }catch(Exception e){
            return null;
        }
        
        String[] params=Métodos.splitParameters(txt.substring(f+1, n-1), ",");
        if(params==null) return null;
        for(int i=0; i<params.length; i++) params[i]=Métodos.trim(params[i]);
        
        //for(String s: params) System.out.println("param:"+s);
        if(params.length==1 && Métodos.compareTo(params[0], "")==0) params=new String[0];
        
        for(int i=0; i<params.length; i++){
            Object o=procesarComando(params[i]);
            System.out.println("Param "+params[i]+" converted: "+o);
            if(o instanceof Integer) if((Integer)o!=-1) params[i]=String.valueOf(o);
            else if(o instanceof Double) if((Double)o!=-1) params[i]=String.valueOf(o);
            else if(o instanceof String || o instanceof Object)
                if(Métodos.compareTo(String.valueOf(o), "")!=0) params[i]=String.valueOf(o);
        }
        for(Object o: comandos){
            Comando c=(Comando)o;
            if(Métodos.compareTo(c.nombre, cmd)==0){
                System.out.println("Comando "+c.nombre+" reconocido...");
                
                //System.out.println(params.length+" and "+c.params.tam);
                if(params.length!=c.params.tam){
                    System.out.println("Número de parámetros no válido."); return null;
                }
                Object[] paramsNew=new Object[params.length];
                int i=0;
                
                for(Object o2: c.params){
                    //System.out.println("trim: "+params[i]);
                    if(o2 instanceof Integer){
                        try{
                            paramsNew[i]=Integer.parseInt(params[i]);
                        }catch(NumberFormatException e){
                            System.out.println("Error: \'"+params[i]+"\' no es de tipo entero.");
                            return null;
                        }
                    }else if(o2 instanceof Double){
                        try{
                            paramsNew[i]=Double.parseDouble(params[i]);
                        }catch(NumberFormatException e){
                            System.out.println("Error: \'"+params[i]+"\' no es de tipo real.");
                            return null;
                        }
                    }
                    else if(o2 instanceof String) paramsNew[i]=params[i];
                    else if(o2 instanceof Object) paramsNew[i]=(Object)params[i];
                    else{
                        System.out.println("Error: Parámetro irreconocible.");
                        return null;
                    }
                    i++;
                }
                //System.out.println("Parámetros finales:");
                //for(Object on: paramsNew) System.out.println(on);
                System.out.println("Comando válido! Ejecutando...");
                return c.iniciar(paramsNew);
            }
        }
        return null;
    }
    
    private Object formatoComando(String txt){
        int n=txt.length();
        if(n<3 || Métodos.compareTo(txt.substring(n-1, n), ")")!=0) return false;
        
        int f=Métodos.find("(", txt);
        if(f==-1) return false;
        
        String cmd=Métodos.trim(Métodos.lower(txt.substring(0, f)));
        if(!Métodos.isAlpha(cmd)) return false;
        
        Object[] informe={f, n, cmd};
        return informe;
    }

    private void override(int fileNum, int reg, int cod, String nombre, double d){
        if(sied==null) return;
        
        //labelCalculos.setText("Sobreescritura");
        String campos[]=new String[3], file;
        if(fileNum==1){
            campos[0]="DNI"; campos[1]="NOMBRE"; campos[2]="SALARIO(€)";
            file="directores_tecnicos.txt";
        }else if(fileNum==2){
            campos[0]="NIF"; campos[1]="NOMBRE"; campos[2]="RETORNO(€)";
            file="patrocinadores.txt";
        }else if(fileNum==3){
            campos[0]="CÓDIGO"; campos[1]="NOMBRE"; campos[2]="PRESUPUESTO(€)";
            file="secciones.txt";
        }else{ areaCalculos.setText("Error: número de archivo no válido."); return; }
        
        areaCalculos.setText("Archivo: "+fileNum+", Registro: "+reg+"."
                + "\n"+campos[0]+"\t"+campos[1]+"\t"+campos[2]
                + "\n"+cod+"\t"+nombre+"\t"+d);
        
        if(reg<0){ areaCalculos.append("\n\nError: número de registro no positivo."); return;}
        if(cod<0){ areaCalculos.append("\n\nError: registro numérico no positivo."); return;}
        if(d<0){ areaCalculos.append("\n\nError: registro numérico no positivo."); return;}
        
        int n=0;
        try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
            br.readLine();
            while(br.readLine()!=null) n++;
        }catch(IOException e){}
        
        if(n<reg){areaCalculos.append("\n\nError: registro inexistente."); return;}
        String[] lines=new String[n];
        
        try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
            String l;
            br.readLine();
            int i=-1;
            while((l=br.readLine())!=null) lines[++i]=l;
        }catch(IOException e){}
        
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(siedDirPath+file))){//ovr(1,1,100,j,20)
            
            bw.write(campos[0]+";"+campos[1]+";"+campos[2]);
            int i;
            for(i=0; i<reg-1; i++) bw.write("\n"+lines[i]);
            String[] c=Métodos.split(lines[i], ";");
            bw.write("\n"+String.valueOf(cod)+";"+nombre+";"+String.valueOf(d));
            
            for(i=i+1; i<lines.length; i++) bw.write("\n"+lines[i]);
            
            bw.close();
            areaCalculos.append("\n\nRegistro sobreescrito satisfactoriamente:"
                    + "\n"+campos[0]+"\t"+campos[1]+"\t"+campos[2]
                    + "\n"+c[0]+"\t"+c[1]+"\t"+c[2]);
            
            
            if(archivoActual==fileNum) actualizar(fileNum);
        }catch(IOException e){
            areaCalculos.append("\n\nError: sobreescritura fallida.");
        }
    }

    private int actualizar(int i){
        //if(sied==null) return;
        String c[]=new String[3], file;
        if(i==1){
            c[0]="DNI"; c[1]="NOMBRE"; c[2]="SALARIO(€)";
            file="directores_tecnicos.txt";
        }else if(i==2){
            c[0]="NIF"; c[1]="NOMBRE"; c[2]="RETORNO(€)";
            file="patrocinadores.txt";
        }else if(i==3){
            c[0]="CÓDIGO"; c[1]="NOMBRE"; c[2]="PRESUPUESTO(€)";
            file="secciones.txt";
        }else return -1;
        
        labelArchivo.setText("Archivo: \'"+file+"\'.");
        tm=new DefaultTableModel(null, c);
        tablaArchivos.setModel(tm);
        try(BufferedReader br=new BufferedReader(new FileReader(siedDirPath+file))){
            br.readLine();
            String l;
            while((l=br.readLine())!=null) tm.addRow(Métodos.split(l, ";"));
            
        }catch(IOException e){
            
        }
        return archivoActual=i;
    }
    
    private void limpiarConsola(){
        areaComandos.setText("");
    }
    
    private void help(){
        //labelCalculos.setText("Ayuda");
        areaCalculos.setText("Comandos:");
        for(Object o: comandos){
            Comando c=(Comando)o;
            areaCalculos.append("\n\n"+c.descripción);
        }
        areaCalculos.append("\n");
    }
    
    private void exit(){
        System.exit(0);
    }
    
    private void infoGeneral(){
        //labelCalculos.setText("Información General");
        areaCalculos.setText("");
        
        if(sied==null){
            areaCalculos.append("ERROR: La información del sistema no ha podido ser obtenida.");
            return;
        }
        String nombre, ubicación;
        double capital, deuda;
        float proyIngresos, infrServ, recHumanos, recSecciones, recDTs, ingEnt, ingPatr, ingVentas;
        java.time.LocalDate fundación;
        
        try{
            nombre=sied.getNombre();
            ubicación=sied.getUbicación();
            capital=sied.getCapitalGeneral();
            deuda=sied.getDeudaGeneral();
            proyIngresos=sied.getProyecciónIngresos();
            infrServ=sied.getGastosInfrServicios();
            recHumanos=sied.getGastosRecHum();
            recSecciones=sied.getGastosSecciones();
            recDTs=sied.getGastosDTsRecHum();
            ingEnt=sied.getIngresosEntradas();
            ingPatr=sied.getIngresosPatrocinios();
            ingVentas=sied.getIngresosVentas();
            fundación=sied.getFundación();
            
        }catch(Exception e){
            areaCalculos.append("ERROR: La información del sistema no ha podido ser obtenida.");
            return;
        }
        areaCalculos.append("Nombre: "+nombre+".");
        areaCalculos.append("\n\nUbicación: "+ubicación+".");
        areaCalculos.append("\n\nFundación: "+fundación+".");
        areaCalculos.append("\n\nCapital: "+capital+"€.");
        areaCalculos.append("\n\nProyección Ingresos: "+proyIngresos+"%.");
        areaCalculos.append("\n\nPorcentaje Infraestructura y Servicios: "+infrServ*100+"%.");
        areaCalculos.append("\n\nPorcentaje Recursos Humanos: "+recHumanos*100+"%.");
        areaCalculos.append("\n\nPorcentaje Secciones: "+recSecciones*100+"%.");
        areaCalculos.append("\n\nPorcentaje Rec. Humanos a DTs: "+recDTs*100+"%.");
        areaCalculos.append("\n\nIngresos por Entradas: "+ingEnt*100+"%.");
        areaCalculos.append("\n\nIngresos por Patrocinios: "+ingPatr*100+"%.");
        areaCalculos.append("\n\nIngresos por Ventas: "+ingVentas*100+"%.");
        areaCalculos.append("\n\nDeuda: "+deuda+"€.");
    }
    
    private void updatePanel(java.awt.Graphics g){
        g.drawImage(BACKGROUND, 0, 0, panelMain.getSize().width, panelMain.getSize().height, null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMain = new javax.swing.JPanel(){
            @Override
            public void paintComponent(java.awt.Graphics g){

                super.paintComponent(g);
                updatePanel(g);
            }
        };
        labelTitle = new javax.swing.JLabel();
        labelArchivo = new javax.swing.JLabel();
        labelComandos = new javax.swing.JLabel();
        scrollComandos = new javax.swing.JScrollPane();
        areaComandos = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaArchivos = new javax.swing.JTable();
        fieldComandos = new javax.swing.JTextField();
        scrollCalculos = new javax.swing.JScrollPane();
        areaCalculos = new javax.swing.JTextArea();
        labelCalculos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelTitle.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 24)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle.setText("SISTEMA DE INFORMACIÓN ENTIDAD DEPORTIVA");
        panelMain.add(labelTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, -1, -1));

        labelArchivo.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        labelArchivo.setForeground(new java.awt.Color(255, 255, 255));
        labelArchivo.setText("Archivo:");
        panelMain.add(labelArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        labelComandos.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        labelComandos.setForeground(new java.awt.Color(255, 255, 255));
        labelComandos.setText("Línea de Comandos");
        panelMain.add(labelComandos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, -1, -1));

        areaComandos.setEditable(false);
        areaComandos.setColumns(20);
        areaComandos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        areaComandos.setRows(5);
        scrollComandos.setViewportView(areaComandos);

        panelMain.add(scrollComandos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 510, 140));

        tablaArchivos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaArchivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaArchivos.setEnabled(false);
        jScrollPane2.setViewportView(tablaArchivos);

        panelMain.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 510, 150));

        fieldComandos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        panelMain.add(fieldComandos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 510, -1));

        areaCalculos.setEditable(false);
        areaCalculos.setColumns(20);
        areaCalculos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        areaCalculos.setRows(5);
        scrollCalculos.setViewportView(areaCalculos);

        panelMain.add(scrollCalculos, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 350, 370));

        labelCalculos.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        labelCalculos.setForeground(new java.awt.Color(255, 255, 255));
        labelCalculos.setText("Output");
        panelMain.add(labelCalculos, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HCI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HCI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HCI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HCI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HCI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaCalculos;
    private javax.swing.JTextArea areaComandos;
    private javax.swing.JTextField fieldComandos;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelArchivo;
    private javax.swing.JLabel labelCalculos;
    private javax.swing.JLabel labelComandos;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JPanel panelMain;
    private javax.swing.JScrollPane scrollCalculos;
    private javax.swing.JScrollPane scrollComandos;
    private javax.swing.JTable tablaArchivos;
    // End of variables declaration//GEN-END:variables
    
    @FunctionalInterface
    public interface Executing{
        Object start(Object ...o);
    }
    
    class Comando{
        
        Executing exe;
        final String nombre, descripción;
        final PointedList<Object> params;
        
        Comando(String nombre, String descripción, PointedList<Object> params){
            this.nombre=nombre;
            this.params=params;
            this.descripción=descripción;
        }
        
        Comando(String nombre, String descripción, PointedList<Object> params, Executing exe){
            this(nombre, descripción, params);
            this.exe=exe;
        }
        
        Object iniciar(Object ...objects){
            return exe.start(objects);
        }
        
    }
}