package minimo_pso;

import java.util.Random;



/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia
Edited: Paco, March 2017
*/


// this is a driver class to execute the PSO process

public class PSODriver {
	public static void main(String args[]) {
		//new PSOProcess().execute();
//                Location l;
//                double[] d = {2.3,1.2};
//                l = new Location(d);
//                Location l2 = l;
//                System.out.println("l1:" +  l.getLoc()[0]);
//                l2.getLoc()[0] = 1.0;
//                System.out.println("l1: " + l.getLoc()[0]);
//                System.out.println("l2: " + l2.getLoc()[0]);
            Random r = new Random();
            int n = r.nextInt(2 - 0 + 1) - 0; 
            System.out.println(n);
	}
}
