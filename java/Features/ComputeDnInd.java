/*
 * To ComputeDnInd this license header, choose License Headers in Project Properties.
 * To ComputeDnInd this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giwrgos
 */
public class ComputeDnInd {

    public static void main(String[] args) {
        /* 
        The dynamic indices for each year, enclose the changing behaviour of each feature throughout the years until the year examined.
        The final dataset is the dynamic indices of 2005, added with the trend of independent features in AddTrend2DnInd
        */
        try {
            for (int now = 2000; now < 2006; now++) {
                String strLine;
                int y = now - 1;
                
                //start with the dynamic indices of 1999
                BufferedReader brPreviousInd = new BufferedReader(new InputStreamReader(
                        new FileInputStream("\\data\\yearly graphs and datasets\\dynamic_indices" + y + ".csv")));

                //keep the changes of an author
                HashMap<String, String> changes = new HashMap<String, String>();
                brPreviousInd.readLine();//step header
                
                while ((strLine = brPreviousInd.readLine()) != null) {
                    changes.put(strLine.split(",")[0], strLine);
                }

                BufferedReader brFeat = new BufferedReader(new InputStreamReader(
                        new FileInputStream("\\data\\yearly graphs and datasets\\" + now + "\\features" + now + ".csv")));
                HashMap<String, String> features = new HashMap<String, String>();
                brFeat.readLine();//step header
                while ((strLine = brFeat.readLine()) != null) {
                    features.put(strLine.split(",")[0], strLine);
                }
                ///the new dynamic_indicess
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("\\data\\yearly graphs and datasets\\" + now + "\\dynamic_indices" + now + ".csv"), "utf-8"));

                writer.write("id,minCPSum,maxCPSum,lastCPSum,sumCPSum,avgPSum,minCPNorm,maxCPNorm,lastCPNorm,sumCPNorm,avgPNorm,minCPNow,maxCPNow,lastCPNow,sumCPNow,avgPNow,minCSum,maxCCSum,lastCCSum,sumCCSum,avgCSum,minCCNorm,maxCCNorm,lastCCNorm,sumCCNorm,avgCNorm,"
                        + "minCCNow,maxCCNow,lastCCNow,sumCCNow,avgCNow,minCEigen,maxCEigen,lastCEigen,sumCEigen,avgEigen,minCDegNorm,maxCDegNorm,lastCDegNorm,sumCDegNorm,avgDegNorm,minCEdgWei,maxCEdgWei,lastCEdgWei,sumCEdgWei,avgEdgWei,minCWpnW,maxCWpnW,lastCWpnW,sumCWpnW,avgWpnW,"
                        + "minCWpnC,maxCWpnC,lastCWpnC,sumCWpnC,avgWpnC,minCSpnW,maxCSpnW,lastCSpnW,sumCSpnW,avgSpnW,minCSpnC,maxCSpnC,lastCSpnC,sumCSpnC,avgSpnC");
                writer.newLine();
                writer.flush();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream("\\data\\yearly graphs and datasets\\" + now + "\\change" + now + ".csv")));
                //for every author in current change, find the correspoing dynamic_indices and change it
                //if it doesn't exist in dynamic_indices, write him
                br.readLine();//step header
                while ((strLine = br.readLine()) != null) {
                    //the current changes  (14 columns)
                    String[] ch = strLine.split(",");
                    String newRow = ch[0];
                    if (features.containsKey(newRow)) {
                        //the features (14 columns)
                        String[] fe = features.get(newRow).split(",");
                        //the totale changes dataset (66 columns)
                        //if the author is already in all changes
                        if (changes.containsKey(newRow)) {
                            String[] allC = changes.get(newRow).split(",");
                            //ch column index
                            int x = 1;
                            for (int k = 1; k < ch.length; k++) {
                                 //min change
                                if (Double.parseDouble(ch[k]) < Double.parseDouble(allC[x])) {

                                    newRow = newRow + "," + ch[k];
                                } else {
                                    newRow = newRow + "," + allC[x];
                                }
                                x++;
                                //max change
                                if (Double.parseDouble(ch[k]) > Double.parseDouble(allC[x])) {
                                    newRow = newRow + "," + ch[k];
                                } else {
                                    newRow = newRow + "," + allC[x];
                                }
                                x++;
                                newRow = newRow + "," + Double.parseDouble(ch[k]);
                                x++;
                                //sum of old changes and current ComputeDnInd
                                double sumC = Double.parseDouble(ch[k].replace("\"", "")) + Double.parseDouble(allC[x].replace("\"", ""));
                                newRow = newRow + "," + sumC;
                                x++;
                                //the temp characteristics 3,6 sum of features, to calculate avg at the end
                                if (k == 3 || k == 6) {
                                    String[] s = allC[x].replace("\"", "").split(":");//s[0]= value, s[1] = denominator
                                    //the old + the new
                                    double sumF = Double.parseDouble(fe[k].replace("\"", "")) + Double.parseDouble(s[0]);
                                    //the times the author occured=the denominator in the average
                                    int den = Integer.parseInt(s[1]) + 1;
                                    if (now == 2005) {
                                        newRow = newRow + "," + (double) (sumF / den);
                                    } else {
                                        newRow = newRow + "," + sumF + ":" + den;
                                    }
                                    //the sum characteristics
                                } else if (k == 1 || k == 4) {
                                    if (now == 2005) {
                                        //take the feature and divide it with the times that the feature has occured in the dataset
                                        int a = Integer.parseInt(allC[x].replace("\"", "")) + 1;//take the times the author has occured
                                        double b = Double.parseDouble(fe[k].replace("\"", ""));//take tha last value
                                        newRow = newRow + "," + (b / a);
                                    } else {
                                        int a = Integer.parseInt(allC[x].replace("\"", "")) + 1;
                                        newRow = newRow + "," + a;//keep times to divide with the last value (in 2005)
                                    }
                                } else {
                                    //the rest are cumulative because the rest of the independent are penalized 
                                    //and the graph feats are based on the penalized equation edge weight
                                    newRow = newRow + "," + allC[x].replace("\"", "");
                                }
                                x++;
                            }
                        } else {//if the author didn't exist at the old dynamic_indices, initialize it with current changes
                            for (int k = 1; k < ch.length; k++) {
                                newRow = newRow + "," + ch[k]; //min
                                newRow = newRow + "," + ch[k]; //max
                                newRow = newRow + "," + ch[k];//last
                                newRow = newRow + "," + ch[k];//sum
                                //the temp characteristics 3,6
                                if (k == 3 || k == 6) {
                                    if (now == 2005) {
                                    newRow = newRow + "," + (Double.parseDouble(fe[k]));                                        
                                    }else{
                                    newRow = newRow + "," + (Double.parseDouble(fe[k])) + ":" + 1;                                        
                                    }
                                    
                                    //the sum characteristics
                                } else if (k == 1 || k == 4) {
                                    if (now == 2005) {
                                     newRow = newRow + ","+fe[k];//it is the first ocurrence of it so it is not divided
                                    }else{
                                     newRow = newRow + "," + 1;   //keep times to divide with the last value (in 2005)
                                    }
                                } else {
                                    //the rest are cumulative because the  rest of the independent are penalized 
                                    //and the graph feats are based on the penalized equation edge weight
                                    newRow = newRow + "," + fe[k];
                                }
                            }
                        }
                        writer.write(newRow);
                        writer.newLine();
                        writer.flush();
                    } else {
                        System.out.println("wrong " + newRow);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ComputeDnInd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ComputeDnInd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
