package org.weloveiran.image;

public class Resizer {
	
	private int originalHeight;
	private int originalWidth;
	private double percentual;
	private double newWidth;
	private double newHeight;
	
	public Resizer(int originalWidth, int originalHeight) {
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;
	}
	
	public Resizer newRecipient(int recipientWidth, int recipientHeight) {

		double dh = originalHeight - recipientHeight;
		double dw = originalWidth - recipientWidth;
		
		if (dh > dw) {
			percentual = (double) recipientHeight/originalHeight;
		} else {
			percentual = (double) recipientWidth/originalWidth;
		}
		
 		newWidth = originalWidth * percentual;
		newHeight = originalHeight * percentual;
		
		return this;
	}

	public double getPercentual() {
		return percentual;
	}

	public double getNewWidth() {
		return newWidth;
	}

	public double getNewHeight() {
		return newHeight;
	}
}
