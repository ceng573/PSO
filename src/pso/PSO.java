/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;
import java.util.ArrayList;
import java.util.Random;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author y
 */
public class PSO {
    private  double w = 1.0;
    private final double C1 = 2.0;
    private final double C2 = 2.0;
    private final double k = 1.0;
    
    private final int PARTICLE_SIZE = 20;
    private final long SEED = 654321;
    private final int DEPTH = 1;
    private final int MAX_ITERATION = 80;
    
    private ArrayList<Particle> particles;
    private Random rand;
    private int globalBest;
    
    private static DefaultCategoryDataset dataset;
    private static XYSeriesCollection xyDataset;
    
    public PSO() {
   
    }
    
        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

       
        PSO pso = new PSO();
        pso.run();
        /*
        LineChart_AWT chart = new LineChart_AWT("PSO", "Değer","Döngü", dataset);
        chart.pack( );
        chart.setVisible( true );
        
        */
        
        
        XYLineChart_AWT chart = new XYLineChart_AWT("Browser Usage Statistics",
         "Which Browser are you using?", xyDataset);
      chart.pack( );          
      RefineryUtilities.centerFrameOnScreen( chart );          
      chart.setVisible( true ); 
             
    }
    
    private void run(){
        initialize();
            
        globalBest = 0;
        
        dataset = new DefaultCategoryDataset( ); 
        
        int z = 0;
        while(z < MAX_ITERATION){
            for(int i=0;i<PARTICLE_SIZE;i++){
                if(particles.get(i).getpBestFitness() < particles.get(globalBest).getpBestFitness()){                                         // finding new GLOBAL BEST
                    globalBest = i;
                }
            }
            
            for(int i=0;i<PARTICLE_SIZE;i++){                                                                                                                   // move operator
                particles.get(i).setVx( calculateVx(particles.get(i)));                                                                                     //calculate new Vx
                particles.get(i).setVy( calculateVy(particles.get(i)));
                
                particles.get(i).setCurrentX( particles.get(i).getCurrentX() + particles.get(i).getVx() );                                    //currentX = currentX + Vx
                particles.get(i).setCurrentY( particles.get(i).getCurrentY() + particles.get(i).getVy() );                                    //currentY = currentY + Vy
                
                if(particles.get(i).getCurrentX() > 3 || particles.get(i).getCurrentX() < -3){
                    particles.get(i).setCurrentX(randomInterval(-3,3));
                    System.out.println("düzeltX");
                }
                if(particles.get(i).getCurrentY() > 2 || particles.get(i).getCurrentY() < -2){
                    particles.get(i).setCurrentY(randomInterval(-2,2));
                        System.out.println("düzeltY");
                }                   
                
                particles.get(i).setCurrentFitness(fitness1(particles.get(i).getCurrentX(), particles.get(i).getCurrentY()));       //current_fitness = F1(currentX, currentY)
                //particles.get(i).setCurrentFitness(fitness2(particles.get(i).getCurrentX(), DEPTH));                                        //current_fitness = F2
                
                if(particles.get(i).getCurrentFitness() < particles.get(i).getpBestFitness()){                                                  //finding personal best
                    particles.get(i).setpBestFitness(particles.get(i).getCurrentFitness());
                    particles.get(i).setpBestX(particles.get(i).getCurrentX());
                    particles.get(i).setpBestY(particles.get(i).getCurrentY());
                }
            }
            dataset.addValue( particles.get(globalBest).getpBestFitness() , "fitness value" , Integer.toString(z));
             w = 1.0 - (((double) z) / MAX_ITERATION) ;
            z++;
            System.out.println("best fitness "+z+" : "+globalBest+" "+particles.get(globalBest).getpBestFitness()+" "+w);
        }
        
        XYSeries xySeries = new XYSeries("fitness value");
        
        for(int i=0;i<PARTICLE_SIZE;i++){
            xySeries.add(particles.get(i).getCurrentX(), particles.get(i).getCurrentY());
            System.out.println(
                    particles.get(i).getpBestFitness()+"\t"
                    +particles.get(i).getCurrentFitness()+" \t"
                    +particles.get(i).getCurrentX() + "\t"
                    +particles.get(i).getCurrentY());
        }      
        xyDataset = new XYSeriesCollection();
        xyDataset.addSeries(xySeries);
        
    }

    private void initialize(){
        particles = new ArrayList<>();
        for(int i=0;i<PARTICLE_SIZE;i++){
            Particle p = new Particle();
            p.setCurrentX(randomInterval(-3, 3));
            p.setCurrentY(randomInterval(-2, 2));
            p.setpBestX(p.getCurrentX());
            p.setpBestY(p.getCurrentY());
            p.setCurrentFitness(fitness1(p.getCurrentX(),p.getCurrentY()));
            p.setpBestFitness(p.getCurrentFitness());
            particles.add(p);
        }
        //System.out.println("random "+randomInterval(-3,3));
        //System.out.println("fitness: "+fitness1(-0.4381681163920629,	1.9661631855522934));
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
    /*
    private double calculateVy(Particle p){
        rand = new Random();
        return k*(
                p.getVy()*w
                +C1*rand.nextDouble()*( p.getpBestY()-p.getCurrentY() )
                +C2*rand.nextDouble()*( particles.get(globalBest).getpBestY() - p.getCurrentY() )
                );
    }
    */
    private double fitness1(double x,double y){
        return (4-(2.1*x*x)+Math.pow(x,4)/3)*x*x + (x*y) + (-4+4*y*y)*y*y;
    }
    
    private double fitness2(double x, double d){
        double value = 0.0;
        double part1 = 0.0;             //problem2  iki parçaya ayrılıp döngüler(toplamlar) 
        double part2 = 0.0;             //sonrası birleştirilecek(çarpılacak)
        for(int i=0;i<d;i++){
            part1 = part1 + x;
        }
        for(int i=0;i<d;i++){
            part2 = part2 + Math.sin(x*x);
        }
        value = part1*Math.pow(Math.E, -part2); //problem2 son hali
        return value;
    }
    
    private double randomInterval(int low, int high){
        rand = new Random();
        return (high-low)*rand.nextDouble()+low;
    }
    
    


    
}
