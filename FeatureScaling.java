package mp2v1;

// created by Christine Anne Catubig

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FeatureScaling extends JFrame {
    
    public FeatureScaling(ArrayList<Double[]> zscore){
        super("Feature Scaling");
        XYDataset dataset = createDataset(zscore);
        JFreeChart chart = ChartFactory.createScatterPlot("Feature Scaling", "", "", dataset);
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
        
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private XYDataset createDataset(ArrayList<Double[]> z) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int key = 0;
        for(int i = 0; i < z.size(); i++){
            int counter = 1;
            XYSeries series = new XYSeries("");
            for(int j = 0; j < z.get(i).length; j++){
                series.add(counter, z.get(i)[j]);
                counter++;
            }
            series.setKey(key);
            key++;
            dataset.addSeries(series);
        }
        return dataset;
    }
    
    // loads data from the attached dataset (irisflowers.csv) and places it in a container
    static ArrayList<Double[]> load(String filename) throws Exception{
        BufferedReader br;
        ArrayList<Double[]> X = new ArrayList<>();
        try{           
            // counts number of rows
            br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            int num_rows = 0;
            while((line = br.readLine()) != null){
                num_rows++;
            }
            br.close();
            
            // counts number of columns
            br = new BufferedReader(new FileReader(filename));
            line = br.readLine();
            int num_columns = 0;
            if(line != null){
                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) == ','){
                        num_columns++;
                    }
                }
            }else{
                System.out.println("File is empty");
                return null;
            }
            br.close();
            
            // store all data inputs into an arraylist of Double arrays
            br = new BufferedReader(new FileReader(filename));
            line = br.readLine();
            ArrayList<Double[]> x = new ArrayList<>();
            while((line = br.readLine()) != null){
                Double[] in = new Double[num_columns];
                StringTokenizer string = new StringTokenizer(line, ",");
                for(int i = 0; i < num_columns; i++){
                    in[i] = Double.parseDouble((String) string.nextElement());
                }
                x.add(in);
            }
            br.close();
            
            // magic? magic.
            int counter = 0;
            while(counter < num_columns){
                Double[] input = new Double[num_rows];
                for(int i = 0; i < x.size(); i++){
                    input[i] = x.get(i)[counter];
                }
                X.add(input);
                counter++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return X;
    }
    
    // scales each feature in the container X using z-score
    static void scaleFeatures(ArrayList<Double[]> X){
        ArrayList<Double[]> zscores = new ArrayList<>();
        for(int c = 0; c < X.size(); c++){
            Double[] input = X.get(c);
            Double[] zc = new Double[input.length];
            Double mean = getMean(input);
            Double sv = getStandardDev(input, mean);
            for(int i = 0; i < input.length; i++){
                zc[i] = ((input[i] - mean) / sv);
            }
            zscores.add(zc);
        }
        
        for(int i = 0; i < zscores.size(); i++){
            for(int j = 0; j < zscores.get(i).length; j++){
                System.out.print(zscores.get(i)[j] + " ");
            }
            System.out.println();
        }
        
        new FeatureScaling(zscores);
    }
    
    // returns the mean of the contents passed into function
    static Double getMean(Double[] input){
        Double total = 0.0;
        for(int i = 0; i < input.length; i++){
            total += input[i];
        }
        return (total / input.length);
    }
    
    // returns the standard deviation of contents passed
    static Double getStandardDev(Double[] input, Double mean){
        Double total = 0.0;
        for(int i = 0; i < input.length; i++){
            total += Math.pow((input[i] - mean), 2);
        }
        return (total / input.length);
    }
    
    public static void main(String[] args) throws Exception{
        String filename = "C:\\Users\\apple\\Documents\\CaH2O\\komsaiPaMore\\Komsai 176\\Lab\\MP 2.1\\irisflowers.csv";
        ArrayList<Double[]> X = load(filename);
        scaleFeatures(X);
    }
}
