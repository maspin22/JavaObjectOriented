/* Time spent on a6: 02 hours and 30 minutes.
* When you change the above, please do it carefully. Change hh to the hours and mm
* to the minutes and leave everything else as is. If the minutes are 0, change mm
* to 0. This will help us in extracting times and giving you the average and max.
* 
* Name: Michael Aspinwall, Mathew Scullin
* Netid (s): mia27 mjs698
* What I thought about this assignment: 
* This assignment ensured understanding of Dijkstra's shortest path algorithm.
*
*/
package student;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import graph.Edge;
import graph.Graph;
import graph.Node;

/** This class contains the shortest-path algorithm and other methods */
public class Paths {

	/*S= {}; F= {v}; d[v]= 0;
	// invariant: pts (1)..(3) given above
	while (F ≠ {}) {
	   f= node in F with minimum d value;
	   Remove f from F, add it to S;    

	   for each neighbor w of f {
	       if (w is not in S or F) {
	          d[w]= d[f] + wgt(f, w);
	          add w to F; bk[w]= f;
	       } else if (d[f]+wgt (f,w) < d[w]) {
	          d[w]= d[f] + wgt(f, w);
	          bk[w]= f;
	       }
	   }
	}*/
	
    /** Return the shortest path from node v to node end ---or the empty list
     * if a path does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
    public static List<Node> minPath(Node v , Node end) {
        /* TODO Read Piazza note Assignment A6 for ALL details. */
        Heap<Node> F = new Heap<Node>(false); // As in abstract algorithm in @700.
        
/*      Replace arrays d and bk and set S of the abstract algorithm by a single HashMap<Node, DistBack>,
*       which should contain an entry for each node of Settled set S and Frontier set F. */
        HashMap<Node, DistBack> info = new HashMap<Node, DistBack>();
        
        F.insert(v, 0);
        info.put(v, new DistBack(null, 0));
        
        while(F.size > 0) {
        	Node f = F.poll();
        	if(f == end) {
        		return getPath(info, end);
        	}
        	for(Edge e : f.getExits()) {
        		Node w = e.getOther(f); 
        		int dist = info.get(f).distance + e.length;
        		if(!info.containsKey(w)) {
        			F.insert(w, dist);
        			info.put(w, new DistBack(f, dist));
        		}
        		else if (dist < info.get(w).distance) {
        			F.changePriority(w, dist);
        			info.get(w).distance = dist;
        			info.get(w).bkptr = f;
        		}
        	}
        }
        	
        // no path from v to end
        return new LinkedList<Node>();
    }


    /** Return the path from the beginning node to node end.
     *  Precondition: info contains all the necessary information about
     *  the path. */
    public static List<Node> getPath(HashMap<Node, DistBack> info, Node end) {
        List<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.add(0, p);
            p= info.get(p).bkptr;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path pa.
     * Precondition: pa contains at least 1 node. If 1, it's a path of length 0,
     * i.e. with no edges. */
    public static int sumPath(List<Node> pa) {
        synchronized(pa) {
            Iterator<Node> iter= pa.iterator();
            Node node= iter.next();  // First node on path
            int sum= 0;
            // invariant: s = sum of weights of edges from start to v
            while (iter.hasNext()) {
                Node q= iter.next();
                sum= sum + node.getEdge(q).length;
                node= q;
            }
            return sum;
        }
    }

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    private static class DistBack {
        private int distance; // distance from start node to this one
        private Node bkptr; // backpointer on path from start node to this one

        /** Constructor: an instance with backpointer p and
         * distance d from the start node.*/
        private DistBack(Node p, int d) {
            distance= d;     // Distance from start node to this one.
            bkptr= p;  // Backpointer on the path (null if start node)
        }

        /** return a representation of this instance. */
        public String toString() {
            return "dist " + distance + ", bckptr " + bkptr;
        }
    }
    
    
}
