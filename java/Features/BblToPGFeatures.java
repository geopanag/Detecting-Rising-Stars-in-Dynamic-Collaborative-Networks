/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giwrgos
 */
class PowerEdge {// extends GraphEvent.Edge {

    double weight;
    String id;

    public PowerEdge(String id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //public String toString() {
    //     return id;
    // }
    public String toString() {
        return id + "\t" + weight;
    }
}

public class BblToPGFeatures {

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
       /*
        Computes the powergraph features. 
        */
        try {
            for (int now = 1998;now<=2005;now++){
            BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream("\\data\\bbls\\weightAggregated" + now + ".edg_converted.bbl")));
                Graph<String, PowerEdge> g = new UndirectedSparseGraph<String, PowerEdge>();
                String strLine;
                HashMap<String, Double> Sweis = new HashMap<String, Double>();//set and its weight
                HashMap<String, String> sets = new HashMap<String, String>();//node and the set it belongs to
                HashMap<String, Double> Slen = new HashMap<String, Double>();//set and its number of nodes

                while ((strLine = br.readLine()) != null) {
                    String[] s = strLine.split("\t");
                    if (s[0].contains("NODE")) {
                        g.addVertex(s[1]);
                    } else if (s[0].contains("SET")) {
                        Sweis.put(s[1], Double.parseDouble(s[2]));
                    } else if (s[0].contains("IN")) {
                        sets.put(s[1], s[2]);
                        if (Slen.containsKey(s[2])) {
                            double a = Slen.get(s[2]);
                            Slen.put(s[2], a + 1.0);
                        } else {
                            Slen.put(s[2], 1.0);
                        }

                    } else if (s[0].contains("EDGE")) {
                        g.addEdge(new PowerEdge(s[1] + "\t" + s[2], Double.parseDouble(s[3])), s[1], s[2], EdgeType.UNDIRECTED);
                    }
                }

                ArrayList<String> IndependantSets = new ArrayList<String>();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("\\data\\yearly graphs and datasets\\" + now + "\\authPGS" + now + ".csv"), "utf-8"));

                for (Map.Entry entry : Slen.entrySet()) {
                    String set = entry.getKey().toString();
                    if (Sweis.containsKey(set)) {
                        double a = Sweis.get(set);
                        Sweis.put(set, a * (double) entry.getValue());
                    }
                }
                for (String node : g.getVertices()) {
                    if (isNumeric(node)) {
                        String set = sets.get(node);
                        double weight = 0.0;
                        double clique = 0.0;
                        if (set == null) {
                            System.out.println("node " + node + " belongs to no powernode");
                        } else {
                            //the powernode's weight
                            weight = Sweis.get(set);
                            String tempSet = set;
                            //find the sets a node belongs to in a recursive manner
                            while (tempSet != null) {
                                if (IndependantSets.contains(set)) {
                                    break;
                                }
                                //take the set one level higher than the one we are in
                                String temp = sets.get(tempSet);
                                if (temp != null) {
                                    //add this set's weight
                                    clique += Sweis.get(tempSet);
                                    //start from the next level
                                    tempSet = temp;
                                } else {
                                    IndependantSets.add(tempSet);
                                    break;
                                }
                            }
                        //the clique's weight is the sum of the set's neighborhs (node or powernode) weight 
                            //multiplied by their edges with the set
                            Object[] neigs = g.getNeighbors(set).toArray();
                            for (int j = 0; j < neigs.length; j++) {
                                double w = 1.0;
                                if (Sweis.containsKey(neigs[j].toString())) {
                                    w = Sweis.get(neigs[j].toString());
                                }
                                PowerEdge p = g.findEdge(set, neigs[j].toString());
                                clique += w * p.getWeight();
                            }
                        }
                        writer.flush();
                        writer.write(node + "," + weight + "," + clique);
                        writer.newLine();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
