import java.io.*;
import java.util.*;
import java.text.*;

public class AvgPriceFinder{

	public static int itemCount;

	public static void main (String[]args) throws Exception {

		//BlankTextFile.txt is a placeholder name of any .txt file where original text is uploaded
		BufferedReader txtFile = new BufferedReader(new FileReader("ShoppingPageStream.txt") );

		ArrayList<Double> sumOfPrices = new ArrayList<Double>();

		//keeps track of previous price in the case that two lines in a row represent the smae article
		double prevPrice = 0;

		//Runs through full txt file 
		while(txtFile.ready()){
			String readLine = txtFile.readLine();
			double price = findMinPrice(readLine);
			if(price > 0 && price != prevPrice) sumOfPrices.add(price);
			prevPrice = price;
		}
		txtFile.close();

		double avgPrice = findAvgPrice(sumOfPrices);

		DecimalFormat formatter = new DecimalFormat("0.00");
		System.out.println("The average price of these " + itemCount + " items is $" + formatter.format(avgPrice));

	} 

	/**
	 * Finds the minimum price from an individual line within the file
	 * Useful for when there's an old price and a sale price on the same line (sale price is to be used)
	 * @param String initLine contains the current line 
	 * @return minimum price in the current line 
	 */ 
	public static double findMinPrice(String initLine){
		//String of the line we're currently on
		StringBuilder line = new StringBuilder(initLine);

		//List of possible prices
		ArrayList<String> prices = new ArrayList<String>();

		//Captures the number after a $ and puts it into prices list
		for(int i = 0; i < line.length(); i++){
			if (line.charAt(i) == '$' && !Character.isLetter(line.charAt(i+1))){
				int increment = 0; //incraments forward in the line 

				//sets incrament to how many spaces ahead the price numbers begin
				for(int j=0; !Character.isDigit(line.charAt(i+j)); j++){
					increment++;
				}

				char curr = line.charAt(i+increment);
				StringBuilder price = new StringBuilder();

				while(Character.isDigit(curr) || curr == '.' || curr == ','){ //while current char is not a letter, add it to price
					if(curr != ',') price.append(String.valueOf(curr)); //add curr to price string
					increment++;
					if(i+increment < line.length()) curr = line.charAt(i+increment);
					else curr = ' ';
				}

				prices.add(price.toString().trim()); //adds price to prices list
			}
		}

		double minPrice = Integer.MAX_VALUE; //infinity 

		//Sets minPrice as the lowest price in the prices list 
		for(String price : prices){
			double priceNum = Double.parseDouble(price);
			if(priceNum < minPrice) minPrice = priceNum;
		}

		if(minPrice == Integer.MAX_VALUE) return 0;
		return minPrice;
	}

	/**
	 * Find the average price from array list of multiple prices 
	 * @param ArrayList<Double> sumOfPrices contains all min prices from the page
	 * @return the average price found
	 */
	public static double findAvgPrice(ArrayList<Double> sumOfPrices){
		int count = 0;
		double sum = 0;

		for(double price : sumOfPrices){
			sum += price;
			count++;
		}

		itemCount = count;

		return sum/count;
	}


}