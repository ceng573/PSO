/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author y
 */
public class Writer {

    public Writer() {
        
    }
    
    /*
    private void convertDouble(){
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        for (Number n : Arrays.asList(12, 123.12345, 0.23, 0.1, 2341234.212431324)) {
            Double d = n.doubleValue();
            System.out.println(df.format(d));
        }
    }
*/
    public void myWriteFile(double d) {
        File f = new File("./test.txt");
        FileWriter fr;
        try {
            fr = new FileWriter(f);
            fr.write(String.valueOf(d));
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void writePositions(String dosyaAdı, ArrayList<Particle> particles){
        try {
            File f = new File("./output/"+dosyaAdı+".txt");
            FileWriter fr;
            fr = new FileWriter(f);
            for(int i=0;i<particles.size();i++){
                fr.write(particles.get(i).getCurrentX()+","+particles.get(i).getCurrentY());
            }
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}



