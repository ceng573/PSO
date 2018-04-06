package pso;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author y */
public class PSO {
    private  double w = 1.0;
    private final double C1 = 2.0;
    private final double C2 = 2.0;
    private final double k = 1.0;
    
    private final int PARTICLE_SIZE = 20;
    private final long SEED = 654321;
    private final int MAX_ITERATION = 200;
    private final int PROBLEM = 1;
    private static final double P = Math.PI;
    private static final double E = Math.E;
    
    private ArrayList<Particle> particles;
    private Random rand;
    private int globalBest;
    
    public PSO() {
   
    }    
    public static void main(String[] args) {               
        PSO pso = new PSO();
        pso.run();
    }
    
    private void run(){
        initialize();
      
        globalBest = 0;
        
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("./output/fitness.txt", true)));
            
            int z = 0;
            while(z < MAX_ITERATION){
                if(z<50 && (z%5 == 0 )) 
                    writePositions();
                
                for(int i=0;i<PARTICLE_SIZE;i++){
                    if(particles.get(i).getpBestFitness() < particles.get(globalBest).getpBestFitness()){    
                        System.out.println("değişti : "+particles.get(globalBest).getpBestFitness()+"->"+particles.get(i).getpBestFitness());// finding new GLOBAL BEST
                        globalBest = i;                    
                    }
                }

                for(int i=0;i<PARTICLE_SIZE;i++){                                                                                                                   // move operator
                    particles.get(i).setVx( calculateVx(particles.get(i)));                                                                                     //calculate new Vx
                    particles.get(i).setVy( calculateVy(particles.get(i)));

                    particles.get(i).setCurrentX( particles.get(i).getCurrentX() + particles.get(i).getVx() );                                    //currentX = currentX + Vx
                    particles.get(i).setCurrentY( particles.get(i).getCurrentY() + particles.get(i).getVy() );                                    //currentY = currentY + Vy

                    if(PROBLEM == 1){
                        if(particles.get(i).getCurrentX() > 3 || particles.get(i).getCurrentX() < -3){                  //x degeri aralıgın dısına cıkarsa
                            particles.get(i).setCurrentX(randomInterval(-3,3));
                            System.out.println("düzeltX");
                        }
                        if(particles.get(i).getCurrentY() > 2 || particles.get(i).getCurrentY() < -2){
                            particles.get(i).setCurrentY(randomInterval(-2,2));
                                System.out.println("düzeltY");
                        }             
                    } else if(PROBLEM == 2){
                        if(particles.get(i).getCurrentX() > 2*P || particles.get(i).getCurrentX() < -2*P){                  //x degeri aralıgın dısına cıkarsa
                            particles.get(i).setCurrentX(randomInterval(-2*P,2*P));
                            System.out.println("düzeltX");
                        }
                        if(particles.get(i).getCurrentY() > 2*P || particles.get(i).getCurrentY() < -2*P){
                            particles.get(i).setCurrentY(randomInterval(-2*P,2*P));
                                System.out.println("düzeltY");
                        }  
                    }

                    if(PROBLEM == 1){
                        particles.get(i).setCurrentFitness(fitness1(particles.get(i).getCurrentX(), particles.get(i).getCurrentY()));       //current_fitness = F1(currentX, currentY)
                    }else if(PROBLEM == 2){
                        particles.get(i).setCurrentFitness(fitness2(particles.get(i).getCurrentX(), particles.get(i).getCurrentY()));       //current_fitness = F2(currentX, currentY)
                    }

                    if(particles.get(i).getCurrentFitness() < particles.get(i).getpBestFitness()){                                                  //finding personal best
                        particles.get(i).setpBestFitness(particles.get(i).getCurrentFitness());
                        particles.get(i).setpBestX(particles.get(i).getCurrentX());
                        particles.get(i).setpBestY(particles.get(i).getCurrentY());
                    }
                }
                w = 1.0 - (((double) z) / MAX_ITERATION) ;                                                                                                      //reduce w for control V

                out.println(z+"\t"+Math.round(particles.get(globalBest).getpBestFitness()*10000.0)/10000.0);

                z++;
            }
        }catch (IOException e) {           System.err.println(e);            }
         finally{            if(out != null){            out.close();            }        } 
            
        writePositions();
       
    }
        
    private void writeFitness(int _index, double _fitness ){
        try {
            File f = new File("./output/fitnes.txt");
            FileWriter fr;
            fr = new FileWriter(f);
            fr.write("\n"+_index+"\t"+Math.round(_fitness*10000.0)/10000.0);
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    private  void writePositions(){
        
   PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("./output/positions.txt", true)));
            out.println("döngü");
            for(int i=0;i<PARTICLE_SIZE;i++){
                out.println(Math.round(particles.get(i).getCurrentX() *1000.0)/1000.0+"\t"
                        +    (Math.round(particles.get(i).getCurrentY()*1000.0)/1000.0));
            }
        }catch (IOException e) {           System.err.println(e);            }
         finally{            if(out != null){            out.close();            }        }      
    }
    
    private void ekranaYazdir1(int i){
        System.out.println(
                    particles.get(i).getpBestFitness()+"\t"
                    +particles.get(i).getCurrentFitness()+" \t"
                    +particles.get(i).getCurrentX() + "\t"
                    +particles.get(i).getCurrentY());
    }

    private void initialize(){
        particles = new ArrayList<>();
        for(int i=0;i<PARTICLE_SIZE;i++){
            Particle p = new Particle();
            if(PROBLEM == 1){
                p.setCurrentX(randomInterval(-3, 3));
                p.setCurrentY(randomInterval(-2, 2));
                p.setCurrentFitness(fitness1(p.getCurrentX(),p.getCurrentY()));                
            } else if(PROBLEM == 2){
                p.setCurrentX(randomInterval(-2*P, 2*P));
                p.setCurrentY(randomInterval(-2*P, 2*P));
                p.setCurrentFitness(fitness2(p.getCurrentX(),p.getCurrentY()));
            }
            p.setpBestX(p.getCurrentX());
            p.setpBestY(p.getCurrentY());
            
            p.setpBestFitness(p.getCurrentFitness());
            particles.add(p);
        }
    }
    
    private double calculateVx(Particle p){
        rand = new Random();
        rand.setSeed(SEED);
        return k*(
                p.getVx()*w
                +C1*rand.nextDouble()*(p.getpBestX()-p.getCurrentX())
                +C2*rand.nextDouble()*(particles.get(globalBest).getpBestX() - p.getCurrentX())
                );
    }
    
    private double calculateVy(Particle p){
        rand = new Random();
        rand.setSeed(SEED);
        return k*(
                p.getVy()*w
                +C1*rand.nextDouble()*(p.getpBestY()-p.getCurrentY())
                +C2*rand.nextDouble()*(particles.get(globalBest).getpBestY() - p.getCurrentY())
                );
    }

    private double fitness1(double x,double y){
        return (4-(2.1*x*x)+Math.pow(x,4)/3)*x*x + (x*y) + (-4+4*y*y)*y*y;
    }
    
    private double fitness2( double x, double y){
        return Math.abs(x) +Math.abs(y)
                * Math.pow(E, -(Math.sin(x*x)+Math.sin(y*y)) );
    }
    
    private double randomInterval(double low, double high){
        rand = new Random();
        return (high-low)*rand.nextDouble()+low;
    }
    
}
