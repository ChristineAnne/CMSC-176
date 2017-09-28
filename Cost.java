package mp1v1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;

// created by Christine Anne Catubig

public class Cost {
    
    static SimpleMatrix X, y;
    static int num_rows = 0, num_inputs = 0;
    
    // This function reads input from a file (HousePricingRelationship.in) and loads the data into its respective 
    // containers(X and y; preferably Matrix objects)
    static void load_data(String filename) throws Exception {
        BufferedReader br;
        try{
            // counts the "n" in the 'n input features'
            br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            if(line != null){
                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) == ','){
                        num_inputs++;
                    }
                }
            }else{
                System.out.println("File is empty");
                return;
            }
            br.close();
            
            // counts how many rows there are
            br = new BufferedReader(new FileReader(filename));
            while((line = br.readLine()) != null){
                num_rows++;
            }
            br.close();
            
            System.out.println("Rows: " + num_rows);
            System.out.println("Columns/Number of inputs: " + num_inputs);
            
            X = new SimpleMatrix(num_rows, num_inputs);
            y = new SimpleMatrix(num_rows, 1);
            
            // puts data into their rightful places(matrices)
            br = new BufferedReader(new FileReader(filename));
            int row = 0, column = 0, y_Row = 0;
            while((line = br.readLine()) != null){
                Double input;
                StringTokenizer string = new StringTokenizer(line, ",");
                while(string.hasMoreElements()){
                    input = Double.parseDouble((String) string.nextElement());
                    if(column != 2){
                        X.set(row, column, input);
                        column++;
                    }else{
                        column = 0;
                        row++;
                        y.set(y_Row, 0, input);
                        y_Row++;
                    }
                }
            }	
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // This function accepts:
    //      1. X - the whole input data
    //      2. y - the whole output data,
    //      3. theta - theta values representing one hypothesis
    // This function should compute the cost of the given hypothesis
    static void cost(SimpleMatrix X, SimpleMatrix y, SimpleMatrix theta){ 
        SimpleMatrix h_of_x = X.mult(theta);
        SimpleMatrix h_minus_y  = h_of_x.minus(y).elementPower(2);
        
        Double summation = h_minus_y.elementSum();
        Double final_ = summation / (2 * X.numRows());
        DecimalFormat df = new DecimalFormat("#.000");
        System.out.println("Cost: " + df.format(final_));
    }

    static int random(){
        Random rand = new Random();
        return rand.nextInt(10);
    }
    
    // checks if variable num is already in array arr
    static Boolean duplicate(int num, int[] arr){
        for(int i = 0; i < arr.length; i++){
            if(num == arr[i]){
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) throws Exception {
        String file = "C:\\Users\\apple\\Documents\\CaH2O\\komsaiPaMore\\Komsai 176\\Lab\\MP 1.1\\HousePricingRelationship.in";
        SimpleMatrix theta = new SimpleMatrix(2, 1);
        
        load_data(file);	
        
        // getting random numbers for theta
        int[] random_numbers = new int[2];
        for(int i = 0; i < 2; i++){
            int num = random();
            while(duplicate(num, random_numbers)){
                num = random();
            }
            random_numbers[i] = num;
            theta.set(i, 0, num);
        }
        
        cost(X, y, theta);
    }
}