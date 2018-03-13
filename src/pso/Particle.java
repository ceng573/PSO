/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;

import java.util.Random;

/**
 *
 * @author y
 */
public class Particle {
    
    private double currentX;
    private double currentY;
    private double currentFitness;
    private double pBestX;
    private double pBestY;
    private double pBestFitness;
    private double Vx;
    private double Vy;
    private Random generator;
    
    public Particle() {
        this.currentX = randomInterval(-3,3);
        this.currentY = randomInterval(-2,2);
        this.pBestX = currentX;
        this.pBestY = currentY;
        this.pBestFitness = 0.0;
    }
    
    private double randomInterval(int low, int high){
        generator = new Random();
        return (high-low)*generator.nextDouble()+low;
    }
    
    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }
        
    public double getCurrentFitness() {
        return currentFitness;
    }

    public void setCurrentFitness(double currentFitness) {
        this.currentFitness = currentFitness;
    }

    public double getpBestX() {
        return pBestX;
    }

    public void setpBestX(double pBestX) {
        this.pBestX = pBestX;
    }

    public double getpBestY() {
        return pBestY;
    }

    public void setpBestY(double pBestY) {
        this.pBestY = pBestY;
    }

    public double getpBestFitness() {
        return pBestFitness;
    }

    public void setpBestFitness(double pBestFitness) {
        this.pBestFitness = pBestFitness;
    }

    public double getVx() {
        return Vx;
    }

    public void setVx(double Vx) {
        this.Vx = Vx;
    }

    public double getVy() {
        return Vy;
    }

    public void setVy(double Vy) {
        this.Vy = Vy;
    }

    
    
}
