/**
 * 
 */
package com.gmail.br45entei.test;

/** @author br45e */
public class TestDirection {
	
	/** @param args Program command line arguments */
	public static final void main(String[] args) {
		int x = 0;
		int z = 2;
		int lastZ = 2;
		int dir = 0;
		while(true) {
			String l = x + "," + z + ",";
			System.out.println(l + (l.length() >= 8 ? "" : "\t") + "\t\t\tlastZ=" + lastZ + " " + (dir == 0 ? "right" : (dir == 1 ? "down" : (dir == 2 ? "left" : (dir == 3 ? "up" : "right_1")))));
			switch(dir) {
			default:
			case 0:
				x++;
				if(x == lastZ) {
					dir++;
				}
				break;
			case 1:
				z--;
				if(-z == lastZ) {
					dir++;
				}
				break;
			case 2:
				x--;
				if(-x == lastZ) {
					dir++;
				}
				break;
			case 3:
				z++;
				if(z == lastZ) {
					dir++;
				}
				break;
			case 4:
				x++;
				if(x == 0) {
					lastZ++;
					z = lastZ;
					dir = 0;
				}
				break;
			}
			try {
				Thread.sleep(1L);
			} catch(InterruptedException ignored) {
			}
		}
	}
	
}
