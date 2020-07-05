package student;


import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

import game.GetOutState;
import game.Tile;

import game.FindState;
import game.SewerDiver;
import game.Node;
import game.NodeStatus;
import game.Edge;

public class DiverMax extends SewerDiver {
	
	

    /** Get to the ring in as few steps as possible. Once you get there, 
     * you must return from this function in order to pick
     * it up. If you continue to move after finding the ring rather 
     * than returning, it will not count.
     * If you return from this function while not standing on top of the ring, 
     * it will count as a failure.
     * 
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the ring in fewer steps.
     * 
     * At every step, you know only your current tile's ID and the ID of all 
     * open neighbor tiles, as well as the distance to the ring at each of these tiles
     * (ignoring walls and obstacles). 
     * 
     * In order to get information about the current state, use functions
     * currentLocation(), neighbors(), and distanceToRing() in FindState.
     * You know you are standing on the ring when distanceToRing() is 0.
     * 
     * Use function moveTo(long id) in FindState to move to a neighboring 
     * tile by its ID. Doing this will change state to reflect your new position.
     * 
     * A suggested first implementation that will always find the ring, but likely won't
     * receive a large bonus multiplier, is a depth-first walk. Some
     * modification is necessary to make the search better, in general.*/
    @Override public void findRing(FindState state) {
        //TODO : Find the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method elsewhere, with a good specification,
        // and call it from this one.
    
    	dfs(state);
    	
    }
    
    protected HashSet<Long> map= new HashSet<Long>();
    
    public void dfs(FindState s) {
    	//terminate when ring is found
    	if(s.distanceToRing() == 0) {
    		return;
    	}
    	//sort the neighbors by distance to ring 
    	List<NodeStatus> neigh = (List) s.neighbors();
    	
    	Comparator<NodeStatus> byDist = new Comparator<NodeStatus>() {
    		@Override
    		public int compare(NodeStatus ns1, NodeStatus ns2) {
    			return ns1.compareTo(ns2);
    		}
    	};
    	
    	Collections.sort(neigh, byDist);
    	
    	//save current location
    	long curr = s.currentLocation();
    	
    	//loop through sorted neighbors
    	for(NodeStatus w: neigh) {
    		//check if the neighbor has visited
    		if(!map.contains(w.getId())) {
    			s.moveTo(w.getId());
    			//visit neighbor
    			map.add(w.getId());
    			dfs(s);
    			//if nothing was found return to the start 
    			if(s.distanceToRing() == 0)
    				break;
    			s.moveTo(curr);
    		
    		}
    	}
    }
    
    
    

    
    /** Get out of the sewer system before the steps are all used, trying to collect
     * as many coins as possible along the way. Your solution must ALWAYS get out
     * before the steps are all used, and this should be prioritized above
     * collecting coins.
     * 
     * You now have access to the entire underlying graph, which can be accessed
     * through GetOutState. currentNode() and getExit() will return Node objects
     * of interest, and getNodes() will return a collection of all nodes on the graph. 
     * 
     * You have to get out of the sewer system in the number of steps given by
     * getStepsRemaining(); for each move along an edge, this number is decremented
     * by the weight of the edge taken.
     * 
     * Use moveTo(n) to move to a node n that is adjacent to the current node.
     * When n is moved-to, coins on node n are automatically picked up.
     * 
     * You must return from this function while standing at the exit. Failing to
     * do so before steps run out or returning from the wrong node will be
     * considered a failed run.
     * 
     * Initially, there are enough steps to get from the starting point to the
     * exit using the shortest path, although this will not collect many coins.
     * For this reason, a good starting solution is to use the shortest path to
     * the exit. */
    @Override public void getOut(GetOutState state) {
        //TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        //with a good specification, and call it from this one.
    		
    	Exit3(state);
    	
    }
    
    public void Exit(GetOutState state) {
    	Paths p = new Paths();
    	List<Node> out = p.shortestPath(state.currentNode(), state.getExit());
    	
    	for(int i = 1; i < out.size(); i++) {
    		//System.out.println(out.get(i).getId());
    		state.moveTo(out.get(i));
    	}
    }
    
    public void Exit2(GetOutState state) {
    	int steps = state.stepsLeft();
    	
    	List<Node> all  = new LinkedList<Node>(state.allNodes());
    	
    	Comparator<Node> byCoins = new Comparator<Node>() {
    		@Override
    		public int compare(Node n1, Node n2) {
    			return Integer.compare(n2.getTile().getOriginalCoinValue(), 
    					n1.getTile().getOriginalCoinValue());
    		}
    	};
    	
    	Collections.sort(all, byCoins);
    	
    	Paths p = new Paths();
    	
    	for(Node n: all) {
    		List<Node> toCoin = p.shortestPath(state.currentNode(), n);
    		List<Node> toExit = p.shortestPath(n, state.getExit());
    		
    		if(distOf(toCoin) + distOf(toExit) < steps) {
    			walkPath(state, toCoin);
    			steps = state.stepsLeft();
    		}
  
    	}
    	
    	List<Node> toExit = p.shortestPath(state.currentNode(), state.getExit());
    	walkPath(state, toExit);
    	
    }
    
    public void Exit3(GetOutState state) {
    	int steps = state.stepsLeft();
    	
    	List<Node> all = new LinkedList<Node>(state.allNodes());
    	
    	Paths p = new Paths();
    	
    	HashMap<Node, PathInfo> shortest = p.allPath(state.currentNode());
    	
    	List<Entry<Node, PathInfo>> list = new LinkedList<Map.Entry<Node, PathInfo>>(shortest.entrySet());
    	
    	Comparator<Entry<Node, PathInfo>> byPathCoinAverage = new Comparator<Entry<Node, PathInfo>>() {
    		@Override
    		public int compare(Entry<Node, PathInfo> n1, Entry<Node, PathInfo> n2) {
    			if(n2.getValue().getCoinValue() != n1.getValue().getCoinValue()) {
    				return Integer.compare(
        					n2.getValue().getCoinValue(), 
        					n1.getValue().getCoinValue()
        					);
    			}
    			return Integer.compare(n2.getValue().getDistance(), 
    					n1.getValue().getDistance());
    		}
    	};
    	
    	Collections.sort(list, byPathCoinAverage);
    	
    	System.out.println("start "+ state.currentNode().getId());
    	
    	boolean hasNewPath = true;
    	while(hasNewPath){
    		
    		hasNewPath = loop(list, state, shortest);
    		shortest = p.allPath(state.currentNode());
    		list = new LinkedList<Map.Entry<Node, PathInfo>>(shortest.entrySet());
    		Collections.sort(list, byPathCoinAverage);
    		if(state.currentNode() == state.getExit())
    			return;
    	}
    	
    	List<Node> toExit = p.shortestPath(state.currentNode(), state.getExit());
    	walkPath(state, toExit);
    	
    	
    }
    
    public boolean loop(List<Map.Entry<Node, PathInfo>> list,
    		 GetOutState state, HashMap<Node, PathInfo> shortest){
    	
    	int steps = state.stepsLeft();
    	Paths p = new Paths();
    	for(Map.Entry<Node, PathInfo> n: list) {
    		List<Node> toExit = p.shortestPath(n.getKey(), state.getExit());
    		
    		if(n.getValue().getDistance() + distOf(toExit) < steps) {
    		
    			walkPath(state, shortest, n.getKey(), state.currentNode());
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public int distOf(List<Node> p) {
    	int sum = 0; 
    	for(int i = 0; i < p.size() -1; i++) {
    		sum += p.get(i).getEdge(p.get(i+1)).length();
    	}
    	return sum;
    }
    
    public void walkPath(GetOutState s, List<Node> p){
    	for(int i = 1; i < p.size() ; i++) {
    		s.moveTo(p.get(i));
    	}
    }
    
    public void walkPath(GetOutState s, HashMap<Node, PathInfo> map , Node curr, Node end){
    	if(curr != end) {
    		
    		walkPath(s, map, map.get(curr).getBackPointer(), end);
    	}
    	else
    		return;
    
    	s.moveTo(curr);
    }
    
}
