package mp1v2;

// created by Christine Anne Catubig

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class GradientDescent extends JFrame{
    static Random rand = new Random();
    
    static void load_data(String filename, ArrayList<Double> y, ArrayList<ArrayList<Double>> X) throws Exception {
        BufferedReader br;
        try{
            // counts the "n" in the 'n input features', in other words, this thing counts the columns
            br = new BufferedReader(new FileReader(filename));
            int num_inputs = 0, num_rows = 0;
            String line = br.readLine();
            if(line != null){
                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) == ','){
                        num_inputs++;
                    }
                }
            }else{
                return;
            }
            br.close();
            
            // loads data into respective places(X and y)
            br = new BufferedReader(new FileReader(filename));
            while((line = br.readLine()) != null){
                Double input;
                StringTokenizer string = new StringTokenizer(line, ",");
                int counter = 1;
                ArrayList<Double> features = new ArrayList<>();
                while(string.hasMoreElements()){
                    input = Double.parseDouble((String) string.nextElement());
                    if(counter <= num_inputs){
                       features.add(input); 
                    }else{
                       y.add(input);
                    }
                    counter++;
                }
                X.add(features);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }   
    }

    static Double random(){
        return rand.nextDouble();
    }
    
    // computes h of x function given x and theta
    static Double computeHofX(ArrayList<Double> x, int theta){
        Double h = 0.0;
        for(int i = 0; i < x.size(); i++){
            h += (theta * x.get(i));
        }
        return h;
    }
    
    // computes the mean of the contents of x
    static Double getMean(ArrayList<Double> x){
        Double mean = 0.0;
        for(int i = 0; i < x.size(); i++){
            mean += x.get(i);
        }
        return mean / x.size();
    }
    
    // computes linear regression
    static Double cost(ArrayList<ArrayList<Double>> X, ArrayList<Double> y, int theta){ 
        Double[] to_be_summed = new Double[X.size()];
        for(int i = 0; i < X.size(); i++){
            to_be_summed[i] = Math.pow((computeHofX(X.get(i), theta)) - y.get(i), 2);
        }
        Double cost_ = 0.0;
        for(Double sum : to_be_summed){
            cost_ += sum;
        }
        
        cost_ /= (2 * X.size());
        return cost_;
    }
    
    // computes gradient descent values
    // the graph is not really required, ganahan lang ko i.illustrate
    static void gradientDescent(ArrayList<ArrayList<Double>> X, ArrayList<Double> y, Double alpha, int iters){
        ArrayList<Double> cost_history = new ArrayList<>();
        int theta = rand.nextInt(5);
        
        for(int j = 0; j < iters; j++){
            Double[] to_be_summed = new Double[X.size()];
            for(int i = 0; i < X.size(); i++){
                to_be_summed[i] = ((computeHofX(X.get(i), theta) - y.get(i))) * getMean(X.get(i));
            }
            
            Double summation = 0.0;
            for (Double sum : to_be_summed) {
                summation += sum;
            }
            
            Double temp = (theta - (alpha * (summation / X.size())));
            theta = temp.intValue();
            cost_history.add(cost(X, y, theta));
            System.out.println("theta: " + theta);
        }
        graph(cost_history);
        System.out.println("iters: " + iters);
    }
    
    static void graph(ArrayList<Double> cost_history){
        System.out.println(cost_history);
        new GradientDescent(cost_history).setVisible(true);
    }
    
    public GradientDescent(ArrayList<Double> cost_history){
        super("Gradient Descent");
 
        JPanel chartPanel = createChartPanel(cost_history);
        add(chartPanel, BorderLayout.CENTER);
        
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private JPanel createChartPanel(ArrayList<Double> cost_history) {
        String chartTitle = "Gradient Descent";
        String xAxisLabel = "Iteration";
        String yAxisLabel = "Cost";

        XYDataset dataset = createDataset(cost_history);

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
        
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        plot.setRenderer(renderer);
        
        return new ChartPanel(chart);
    }
    
    private XYDataset createDataset(ArrayList<Double> cost_history) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries cost = new XYSeries("Cost");
        for(int i = 0, j = 1; i < cost_history.size(); i++, j++){
            Double iter = Double.parseDouble(Integer.toString(j));
            cost.add(iter, cost_history.get(i));
            DecimalFormat df = new DecimalFormat("#.000");
            System.out.println("Cost: " + df.format(cost_history.get(i)));
        }
        dataset.addSeries(cost);
        return dataset;
    }   
    
    static Double getAlpha(){
        return (0.001 + (10 - 0.001) * rand.nextDouble());
    }
    
    public static void main(String[] args) throws Exception{
        String file = "C:\\Users\\apple\\Documents\\CaH2O\\komsaiPaMore\\Komsai 176\\Lab\\MP 1.1\\HousePricingRelationship.in";
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<ArrayList<Double>> X = new ArrayList<>();
        load_data(file, y, X);
        int iter = 0;
        while(iter <= 5){
            iter = rand.nextInt(20);
        }
        gradientDescent(X, y, getAlpha(), iter);
    }
}