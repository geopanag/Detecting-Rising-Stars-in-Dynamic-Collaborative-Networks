/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giwrgos
 */
public class Compute1998_1999DnInd {

    public static void main(String[] args) {
        /*
        Compute the dynamic indices of the changes between 1998 and 1999
        */
        try {
            int now = 1999;
            String strLine;

            BufferedReader br = new BufferedReader(new InputStreamReader(
                     new FileInputStream("\\data\\yearly graphs and datasets\\"+now+"\\change"+now+".csv")));
            BufferedReader brFeat = new BufferedReader(new InputStreamReader(
                    new FileInputStream("\\data\\yearly graphs and datasets\\"+now+"\\features"+now+".csv")));
            BufferedReader brFeatOld = new BufferedReader(new InputStreamReader(
                    new FileInputStream("\\data\\yearly graphs and datasets\\"+now+"\\features"+(now-1)+".csv")));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\"+now+"\\dynamic_indices"+now+".csv"), "utf-8"));
             writer.write("id,minCPSum,maxCPSum,lastCPSum,sumCPSum,avgPSum,minCPNorm,maxCPNorm,lastCPNorm,sumCPNorm,avgPNorm,minCPNow,maxCPNow,lastCPNow,sumCPNow,avgPNow,minCSum,maxCCSum,lastCCSum,sumCCSum,avgCSum,minCCNorm,maxCCNorm,lastCCNorm,sumCCNorm,avgCNorm,"
                    + "minCCNow,maxCCNow,lastCCNow,sumCCNow,avgCNow,minCEigen,maxCEigen,lastCEigen,sumCEigen,avgEigen,minCDegNorm,maxCDegNorm,lastCDegNorm,sumCDegNorm,avgDegNorm,minCEdgWei,maxCEdgWei,lastCEdgWei,sumCEdgWei,avgEdgWei,minCWpnW,maxCWpnW,lastCWpnW,sumCWpnW,avgWpnW,"
                    + "minCWpnC,maxCWpnC,lastCWpnC,sumCWpnC,avgWpnC,minCSpnW,maxCSpnW,lastCSpnW,sumCSpnW,avgSpnW,minCSpnC,maxCSpnC,lastCSpnC,sumCSpnC,avgSpnC");
            writer.newLine();
            writer.flush();
            HashMap<String, String> features = new HashMap<String, String>();
            HashMap<String, String> oldFeat = new HashMap<String, String>();
            brFeat.readLine();
            brFeatOld.readLine();
            br.readLine();
            
            while ((strLine = brFeat.readLine()) != null) {
                features.put(strLine.split(",")[0], strLine);
            }

            while ((strLine = brFeatOld.readLine()) != null) {
                oldFeat.put(strLine.split(",")[0], strLine);
            }
            //derive changes from the feature
            while ((strLine = br.readLine()) != null) {
                String[] st = strLine.split(",");
                String row = st[0];
                String[] feat = features.get(st[0]).split(",");
                String[] old = null;
                if (oldFeat.containsKey(st[0])) {
                    old = oldFeat.get(st[0]).split(",");
                }
                for (int k = 1; k < st.length; k++) {
                    row = row + "," + st[k]; //min
                    row = row + "," + st[k]; //max
                    row = row + "," + st[k];//last
                    row = row + "," + st[k];//sum
                    //the temp characteristics 3,6
                    if (k == 3 || k == 6) {
                        if (old != null) {
                            row = row + "," + (Double.parseDouble(feat[k]) + Double.parseDouble(old[k])) + ":" + 2;//krata posa xronia exei emfanistei o author
                        } else {
                             row = row + "," + (Double.parseDouble(feat[k]))+":"+1;
                        }
                        //the sum characteristics
                    } else if (k == 1 || k == 4) {
                        row = row + "," + 2;
                    } else {

                        row = row + "," + feat[k];
                    }
                }

                writer.write(row);
                writer.newLine();
                writer.flush();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compute1998_1999DnInd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Compute1998_1999DnInd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Compute1998_1999DnInd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
