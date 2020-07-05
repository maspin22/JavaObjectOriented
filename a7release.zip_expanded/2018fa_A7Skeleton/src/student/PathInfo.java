package student;

import java.util.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game.Edge;
import game.Node;

public class PathInfo {
	
	private int distance; 
	private int coinValue;
	private Node backPointer; // backpointer on path from start node to this one

	
	
	public PathInfo(int c, int d, Node p) {
		coinValue = c;
		distance= d;     // Distance from start node to this one.
		backPointer= p;  // Backpointer on the path (null if start node)
	}
	

	public Node getBackPointer() {
		return backPointer;
	} 
	public int getDistance() {
		return distance;
	}

	public int getCoinValue() {
		return coinValue;
	}

	public void setBackPointer(Node n) {
		backPointer = n;
	} 
	public void setDistance(int d) {
		distance = d;
	}

	public void setCoinValue(int c) {
		coinValue = c;
	}
	
}
