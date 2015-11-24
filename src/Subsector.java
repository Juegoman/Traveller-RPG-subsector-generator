/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruben
 */
public class Subsector {
    private Hex[][] subsect;
    
    public Subsector(String[] args) {
        subsect = new Hex[8][10];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 10; j++) {
                subsect[i][j] = new Hex(i,j);
            }
        }
        int regens = 0;
        boolean allArgsFound = false;
        while (!allArgsFound) {
            boolean notFound = true;
            for(String arg : args) {
                while(notFound) {
                    for (Hex[] a : subsect) {
                        for (Hex b : a) {
                            if (b.planet != null) {
                                if (b.planet.trade.contains(arg)) {
                                    notFound = false;
                                    break;
                                }
                            }
                            if (!notFound) break;
                        }
                        if (!notFound) break;
                    }
                    if (notFound) break;
                }
                if (notFound) break;
            }
            if (notFound) {
                subsect = new Hex[8][10];
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 10; j++) {
                        subsect[i][j] = new Hex(i,j);
                    }
                }
                regens++;
            }
            if (!notFound) {
                allArgsFound = true;
            }
        }
        System.out.println("Regenerations: " + regens);
    }
    @Override
    public String toString() {
        String str = "";
        for (Hex[] a : subsect) {
            for (Hex b : a) {
                if (b.toString() != null) {
                    str = str + b.toString() + "\n";
                }
            }
        }
        return str;
    }
}
