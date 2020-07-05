package student;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import graph.Graph;
import graph.Node;
import gui.TextIO;

public class PathDataGenerator {

    //@Test
    /** Create and print data for map seed16.txt  */
    public void testPrintMapSeed16() {
        Graph g= getGraph("data/Maps/Seed16.txt");
        printInfo(g);
    }

    //@Test
    /** Create and print data for map seed16.txt  */
    public void testPrintMapTestBoard1() {
        Graph g= getGraph("data/Maps/TestBoard1.txt");
        printInfo(g);
    }
    
    //@Test
    /** Create and print data for map TestBoard2.txt  */
    public void testPrintMapTestBoard2() {
        Graph g= getGraph("data/Maps/TestBoard2.txt");
        printInfo(g);
    }
    
    //@Test
    /** Create and print data for map Board3.txt  */
    public void testPrintMapBoard3() {
        Graph g= getGraph("data/Maps/Board3.txt");
        printInfo(g);
    }
    
    @Test
    /** Create and print data for seeded map
     * 5842236091853851505L  */
    public void testPrintMapSeed1() {
        Graph g= Graph.randomBoard(5842236091853851505L);
        printInfo(g);
    }



    /** Print (1) number of nodes, (2) a list of node names, and (3)
     * the array of shortest paths for graph s.*/
    public void printInfo(Graph g) {
        HashSet<Node> nodeSet= g.getNodes();
        int size= nodeSet.size();
        String names= "";
        Node[] nodes= new Node[size];
        int k= 0;
        //inv: k-1 nodes have been enumerated and placed in nodeArray.
        //     their names have been placed in names with a space after each
        for (Node node : nodeSet) {
            nodes[k]= node;
            names= names + "\"" + nodes[k] + "\" ";
            k= k+1;
        }

        System.out.println(size);
        System.out.println(names);
        int[][] distance= new int[size][size];
        for (int r= 0; r < distance.length; r= r+1) {
            String row= "";
            for (int c= 0; c < distance.length; c= c+1) {
                List<Node> list= Paths.minPath(nodes[r], nodes[c]);
                distance[r][c]= Paths.sumPath(list);
                row= row + distance[r][c] + " ";
            }
            System.out.println(row);
        }
    }

    /** Return a graph for file named s in the data. */
    public Graph getGraph(String s) {
        try {
            return Graph.getJsonGraph(new JSONObject(TextIO.read(new File(s))));
        } catch (IOException e) {
            throw new RuntimeException("IO Exception reading in graph " + s);
        }
    }

}
