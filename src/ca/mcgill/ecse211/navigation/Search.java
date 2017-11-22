package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;

/**
 * Class that searches the opposing zone for the flag.
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class Search extends Thread {
	Driver driver;
	
	private static boolean captured = false;
	private static boolean searching = false;
	
	private static int blockColor;
	private static float redColor;
	private static float blueColor;
	private static float greenColor; 
	/**
	 * Constructor for the Search class.
	 * @param odometer Odometer created in the MainController class.
	 * @param driver Driver created in MainController.
	 * @since 1.1
	 */
	public Search(Driver driver) {
		this.driver = driver;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while(!(getCaptured()) && getSearching()) {
			redColor = MainController.getSearchLightValue(0);	//Red
			greenColor = MainController.getSearchLightValue(1);	//Green
			blueColor = MainController.getSearchLightValue(2);	//Blue
			if (redColor >= 2 && greenColor >= 2 && blueColor >= 2){
				scanBlock(redColor, greenColor, blueColor);
			}
		}
	}
	
	/**
	 * Scans the block found to check whether it is the right color block or not.
	 * @since 1.1
	 */
	private void scanBlock(float red, float green, float blue) {
		if(getBlock() == 1) { //Red 
			if( red > (green + blue)){  						// Red block or Yellow block
				if (red/2 > (green + blue)){					// Red block
					captureBlock();
				}
			}
		}
		else if(getBlock() == 2) { //Blue
			if (red < (green + blue)){					
				if ( blue > red && blue > green){		
					captureBlock();
				}
			}
		}
		else if(getBlock() == 3) { //Yellow
			if( red > (green + blue)){  		
				if(redColor/2 < (greenColor+blueColor)){
					captureBlock();
				}
			}
		}
		else if(getBlock() == 4) { //White
			if (red < (green + blue)){	
				if ( blueColor < redColor && blueColor < greenColor){
					captureBlock();
				}
			}
		}
	}
	
	/**
	 * If the robot finds the block perform this. 
	 * @since 1.1
	 */
	private void captureBlock() {
		Sound.beep();
		Sound.beep();
		Sound.beep();
		setCaptured(true);
		setSearching(false);
	}
	
	/**
	 * Checks whether or not the robot has captured the block.
	 * @return captureStatus Returns true if the block has been captured, false otherwise. 
	 * @since 1.1
	 */
	public boolean getCaptured() {
		return captured;
	}
	

	/**
	 * Sets the value of the captured boolean. 
	 * @param captured Boolean true if the block has been captured, false otherwise. 
	 * @since 1.1
	 */
	private void setCaptured(boolean found) {
		captured = found; 
	}
	
	/**
	 * Checks whether the block being scanned is the flag. 
	 * @return Returns true if the block is the flag, false otherwise.
	 * @since 1.1
	 */
	public int getBlock() {
		return blockColor;
	}
	
	/**
	 * Sets the values of the foundBlock boolean. 
	 * @param foundBlock True if the block is the flag, false otherwise. 
	 * @since 1.1
	 */
	private void setBlock(int flagColor) {
		blockColor = flagColor;
	}

	/**
	 * Checks if the robot is currently searching for the block. 
	 * @return True if it is searching, false otherwise.
	 * @since 1.1
	 */
	public boolean getSearching() {
		return searching;
	}
	
	public void setSearching(boolean bool) {
		searching = bool;
	}
}
