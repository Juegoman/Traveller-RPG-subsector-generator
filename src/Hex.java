/**
 *
 * @author Ruben
 */
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hex {
    
    private String coords;
    private boolean isOccupied;
    private static Random rand = new Random();
    public Planet planet;
    
    public Hex(int r, int c) {
        if(c+1<10) this.coords = "0" + Integer.toString(r+1) + "0" + Integer.toString(c+1); 
        else this.coords = "0" + Integer.toString(r+1) + Integer.toString(c+1);
        this.isOccupied = rand.nextBoolean();
        if (isOccupied == false) {
            this.planet = null;
        }
        else {
            this.planet = new Planet(coords);
        }
    }
    @Override
    public String toString() {
        if (this.planet == null) return null;
        else return this.planet.toString();
    }
}
class Planet {
    public int sPort, size, atmos, temp, hydro, pop, govt, law, tl;
    public String bases, trade, status, name, coords, gas;
    private static Random rand = new Random();
    private NameGenerator gen;
    
    public Planet(String loc) {
        //coords
        this.coords = loc;
        //make the NameGen
        try {
            gen = new NameGenerator("syll.txt");
        } catch (IOException ex) {
            Logger.getLogger(Planet.class.getName()).log(Level.SEVERE, null, ex);
        }
        //gas
        if (roll("2") > 9) this.gas = " "; else this.gas = "G";
        //size
        this.size = roll("2") - 2;
        if (this.size < 0) this.size = 0;
        //atmos
        this.atmos = roll("2") - 7 + this.size;
        if (this.atmos < 0) this.atmos = 0;
        //hydro
        setTemp();
        setHydro();
        //pop
        this.pop = roll("2") - 2;
        if (this.pop < 0) this.pop = 0;
        //govt
        if (this.pop == 0) this.govt = 0;
        else this.govt = roll("2") - 7 + this.pop;
        if (this.govt < 0) this.govt = 0;
        if (this.govt > 13) this.govt = 13;
        //law
        if (this.pop == 0) this.law = 0;
        else this.law = roll("2") - 7 + this.govt;
        if (this.law < 0) this.law = 0;
        if (this.law > 9) this.law = 9;
        //starport
        int tempRoll = roll("2");
        if (tempRoll <= 2) this.sPort = 0;       //X
        else if (tempRoll <= 4) this.sPort = 1;  //E
        else if (tempRoll <= 6) this.sPort = 2;  //D
        else if (tempRoll <= 8) this.sPort = 3;  //C
        else if (tempRoll <= 10) this.sPort = 4; //B
        else this.sPort = 5;  //A
        //Tech Level
        setTL();
        //bases
        setBases();
        //trade
        setTrade();
        //status
        setStatus();
        //name
        setName();
    }
    
    @Override
    public String toString() {
        String UWP = this.name;
        if (this.name.length() < 8) UWP = UWP + "\t\t" + this.coords + "\t";
        else UWP = UWP + "\t" + this.coords + "\t";
        switch (this.sPort) { //starport hexcode
            case 0: UWP = UWP + "X"; break; //X
            case 1: UWP = UWP + "E"; break; //E
            case 2: UWP = UWP + "D"; break; //D
            case 3: UWP = UWP + "C"; break; //C
            case 4: UWP = UWP + "B"; break; //B
            case 5: UWP = UWP + "A"; break; //A
        }
        UWP = UWP + Integer.toHexString(this.size).toUpperCase() 
                + Integer.toHexString(this.atmos).toUpperCase() 
                + Integer.toHexString(this.hydro).toUpperCase() 
                + Integer.toHexString(this.pop).toUpperCase() 
                + Integer.toHexString(this.govt).toUpperCase() 
                + Integer.toHexString(this.law).toUpperCase()
                + "-" + Integer.toHexString(this.tl).toUpperCase() 
                + "\t" + this.bases + "\t" + this.trade;
        if (this.trade.length() < 8) UWP = UWP + "\t\t" + this.status + " " + this.gas;
        else UWP = UWP + "\t" + this.status + " " + this.gas;
        return UWP;
        
    }
    
    private void setName() {
        this.name = gen.compose(rand.nextInt(1) + 2);
    }
    
    private void setStatus() {
        this.status = " ";
        if (this.atmos >= 10 || this.govt == 0 || this.govt == 7 || this.govt == 10 || this.law == 0 || this.law == 9) this.status = "A";
        if (this.bases.contains("P")) this.status = "R";
    }
    
    private void setTrade() {
        this.trade = "";
        if ((this.atmos >= 4 && this.atmos <= 9) && (this.hydro >= 4 && this.hydro <= 8) && (this.pop >= 5 && this.pop <= 7)) this.trade = this.trade + "Ag ";
        if (this.size == 0 && this.atmos == 0 && this.hydro == 0) this.trade = this.trade + "As ";
        if (this.pop == 0 && this.govt == 0 && this.law == 0) this.trade = this.trade + "Ba ";
        if (this.atmos >= 2 && this.hydro == 0) this.trade = this.trade + "De ";
        if (this.atmos >= 10 && this.hydro >= 1) this.trade = this.trade + "Fl ";
        if ((this.atmos >= 4 && this.atmos <= 9) && (this.hydro >= 4 && this.hydro <= 8) && this.size >= 5) this.trade = this.trade + "Ga ";
        if (this.pop >= 9) this.trade = this.trade + "Hi ";
        if (this.tl >= 12) this.trade = this.trade + "Ht ";
        if ((this.atmos == 0 || this.atmos == 1) && this.hydro >= 1) this.trade = this.trade + "IC ";
        if (((this.atmos >= 0 && this.atmos <= 2) || this.atmos == 4 || this.atmos == 7 || this.atmos == 9) && this.pop >= 9) this.trade = this.trade + "In ";
        if (this.pop >= 1 && this.pop <= 3) this.trade = this.trade + "Lo ";
        if (this.tl <= 5) this.trade = this.trade + "Lt ";
        if ((this.atmos >= 0 && this.atmos <= 3) && (this.hydro >= 0 && this.hydro <= 3) && this.pop >= 6) this.trade = this.trade + "Na ";
        if (this.pop >= 4 && this.pop <= 6) this.trade = this.trade + "NI ";
        if ((this.atmos >= 2 && this.atmos <= 5) && (this.hydro >= 0 && this.hydro <= 3)) this.trade = this.trade + "Po ";
        if ((this.atmos == 6 || this.atmos == 8) && (this.pop >= 6 && this.pop <= 8)) this.trade = this.trade + "Ri ";
        if (this.atmos == 0) this.trade = this.trade + "Va ";
        if (this.hydro == 10) this.trade = this.trade + "Wa ";
    }
    
    private void setBases() {
        this.bases = "";
        if (this.sPort == 5) {
            if(roll("2") >= 8) this.bases = this.bases + "N";
            if(roll("2") >= 10) this.bases = this.bases + "S";
            if(roll("2") >= 8) this.bases = this.bases + "R";
            if(roll("2") >= 4) this.bases = this.bases + "T";
            if(roll("2") >= 6) this.bases = this.bases + "I";
        }else if (this.sPort == 4) {
            if(roll("2") >= 8) this.bases = this.bases + "N";
            if(roll("2") >= 8) this.bases = this.bases + "S";
            if(roll("2") >= 10) this.bases = this.bases + "R";
            if(roll("2") >= 6) this.bases = this.bases + "T";
            if(roll("2") >= 8) this.bases = this.bases + "I";
            if(roll("2") == 12) this.bases = this.bases + "P";
        }else if (this.sPort == 3) {
            if(roll("2") >= 8) this.bases = this.bases + "S";
            if(roll("2") >= 10) this.bases = this.bases + "R";
            if(roll("2") >= 10) this.bases = this.bases + "T";
            if(roll("2") >= 10) this.bases = this.bases + "I";
            if(roll("2") >= 10) this.bases = this.bases + "P";
        }else if (this.sPort == 2) {
            if(roll("2") >= 7) this.bases = this.bases + "S";
            if(roll("2") == 12) this.bases = this.bases + "P";
        }else if (this.sPort == 1) {
            if(roll("2") == 12) this.bases = this.bases + "P";
        }
    }
    
    private void setTL() {
        if (this.pop == 0) this.tl = 0;
        else {
            this.tl = roll("1");
            switch (this.sPort) { //starport bonus
                case 0: this.tl -= 4; break; //X
                case 3: this.tl += 2; break; //C
                case 4: this.tl += 4; break; //B
                case 5: this.tl += 6; break; //A
                default: break;
            }
            switch (this.size) { //size bonus
                case 0: case 1: this.tl += 2; break;
                case 2: case 3: case 4: this.tl += 1; break;
                default: break;
            }
            switch (this.atmos) { //atmos bonus
                case 0: case 1: case 2: case 3: case 10: 
                case 11: case 12: case 13: case 14: case 15: this.tl += 1; break;
                default: break;
            }
            switch (this.hydro) { //hydro bonus
                case 0: case 9: this.tl += 1; break;
                case 10: this.tl += 2; break;
                default: break;
            }
            switch (this.pop) { //pop bonus
                case 1: case 2: case 3: case 4: case 5: case 9: this.tl += 1; break;
                case 10: this.tl += 2; break;
                case 11: this.tl += 3; break;
                case 12: this.tl += 4; break;
                default: break;
            }
            switch (this.govt) { //govt bonus
                case 0: case 5: this.tl += 1; break;
                case 7: this.tl += 2; break;
                case 13: case 14: this.tl -= 2; break;
                default: break;
            }
            if (this.tl < 0) this.tl = 0;
            if (this.tl > 15) this.tl = 15;
        }
    }
    
    private void setTemp() {
        int tempRoll = roll("2");
        switch (this.atmos) {
            case 0: case 1: tempRoll = 100; break;
            case 2: case 3: tempRoll -= 2; break;
            case 4: case 5: case 14: tempRoll -= 1; break;
            case 6: case 7: break;
            case 8: case 9: tempRoll += 1; break;
            case 10: case 12: case 15: tempRoll += 2; break;
            case 11: case 13: tempRoll += 6; break;
        }
        if (tempRoll <= 2) this.temp = 0;       //Frozen
        else if (tempRoll <= 4) this.temp = 1;  //Cold
        else if (tempRoll <= 9) this.temp = 2;  //Temperate
        else if (tempRoll <= 11) this.temp = 3; //Hot
        else if (tempRoll > 11)this.temp = 4;   //Roasting
    }
    
    private void setHydro() {
        this.hydro = roll("2") - 7 + this.size;
        if (this.size < 2) {
            this.hydro = 0;
        }else {
            if (this.atmos < 2 || (this.atmos > 9 && this.atmos < 13)) {
                this.hydro -= 4;
            }
            if (this.atmos != 13){
                if (this.temp == 3) this.hydro -= 2;
                if (this.temp == 4) this.hydro -= 6;
            }
            if (this.hydro < 0) this.hydro = 0;
            if (this.hydro > 10) this.hydro = 10;
        }
    }
    
    private static int roll(String type) {
        switch (type){
            case "1": return (rand.nextInt(6)+1);
            case "2": return (rand.nextInt(6)+1)+(rand.nextInt(6)+1);
            case "66": return (rand.nextInt(6)+1)*10+(rand.nextInt(6)+1);
            default: return -1;
        }
    }
}
