import java.beans.DesignMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;






// class Link {
//     final String start;
//     final String destination;
//     final int departureTime;
//     final int duration;
    
//     public Link(String start, String destination, int departureTime, int duration)
//     {
//     	this.start = start;
//     	this.destination = destination;
//     	this.departureTime = departureTime;
//     	this.duration = duration;
//     }

// 	@Override
// 	public String toString() {
// 		return "Link [" + start + "," + destination
// 				+ "," + departureTime + "," + duration
// 				+ "]";
// 	}
    
    
    
// }

class Graph {

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		ArrayList<Node> keyset = (ArrayList<Node>) adjacentEdges.keySet();
		for(String n : adjacentEdges.keySet())
		{
			sb.append(n+": "+adjacentEdges.get(n)+"\n\n");
		}
		return sb.toString();
	}

	public HashMap<String, List<Link>> adjacentEdges = new HashMap<String, List<Link>>();

	public void addEgde(Link l) {
		List<Link> list;
		if (!adjacentEdges.containsKey(l.start)) {
			
			list = new ArrayList<Link>();
			adjacentEdges.put(l.start, list);
		} else {
			list = adjacentEdges.get(l.start);
			
		}
		list.add(new Link(l.start, l.destination, l.departureTime, l.duration));
	}

	public List<Link> get(Node source) {
		List<Link> toReturn = adjacentEdges.get(source);
		if (toReturn == null) {
			return Collections.emptyList();
		} else {
			return toReturn;
		}
	}

//	public Graph getReversedList() {
//		Graph list = new Graph();
//		for (List<Link> edges : adjacentEdges.values()) {
//			for (Link edge : edges) {
//				list.addEgde(edge.to, edge.from);
//			}
//		}
//		return list;
//	}

	public Set<String> getSourceNodeSet() {
		return adjacentEdges.keySet();
	}

	public Collection<Link> getAllEdges() {
		List<Link> edges = new ArrayList<Link>();
		for (List<Link> e : adjacentEdges.values()) {
			edges.addAll(e);
		}
		return edges;
	}
	
	
	
	
	public void DFS(String s, Link incoming, String dest, List<Link> links, Set<List<Link>> connections)
	{
		if(s == dest){
//			System.out.println("adding");
			connections.add(new ArrayList<Link>(links));
			System.out.println();
//			links.remove(links.size()-1);
		}else{		
			//jinak jdu na dalsi zastavku
		if(adjacentEdges.containsKey(s))
		{
//			System.out.println(adjacentEdges.get(s));
		for(Link e : adjacentEdges.get(s))
		{	
			
			if(incoming != null && incoming.departureTime+incoming.duration > e.departureTime) 
			{
//				links.remove(links.size()-1);
//				System.out.println("slepa vetec");
			}
			else
			{
				
//				System.out.println(tmplinks);
				String nextNode = e.destination;
				List<Link> tmplinks = new ArrayList<Link>(links);
				tmplinks.add(e);
//				System.out.println("pridavam");
				
//				Collections.copy(tmplinks, links);
				DFS(nextNode, e, dest, tmplinks, connections);
				
			}
			
			
			}
		}
		else
		{
//			links.remove(links.size()-1);
//			System.out.println("slepa vetec");
		}
		}
		
		
//		return null;
		
		
		
		
		
	}
	
	
	
	
	
}




class Node implements Comparable<Node> {
	public String name;
	public double lowlink = -1; // used for Tarjan's algorithm
	public double index = -1; // used for Tarjan's algorithm

	public Node(String nameToInsert) {
		this.name = nameToInsert;
	}

	public int compareTo(final Node param) {
		return param == this ? 0 : -1;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		try
		{
			Node n = (Node) obj;
			return n.name == this.name;
		}
		catch(ClassCastException ex)
		{
			return false;
		}
		
	}
	
	

	
}

public class homework4 {

	
	static Set < List<Link> >getConnections(String start, String destination, int departureTime, Set<Link> links)
	{
		Graph graph = new Graph();
		for(Link l : links)
		{
			if(l.departureTime >= departureTime ) graph.addEgde(l);
		}
		Set<List<Link>> connections = new HashSet<List<Link>>();
		for(String s : graph.adjacentEdges.keySet())
		{			
			if(s == start)
			{
				List<Link> tmpLinks = new ArrayList<Link>();	
				graph.DFS(s, null, destination, tmpLinks, connections);
			}
		}
		System.out.println(connections);
		ArrayList<List<Link>> obsolete = new ArrayList<List<Link>>();
		for(List<Link> connection : connections)
		{
			int departure = connection.get(0).departureTime;
			int arrival = connection.get(connection.size()-1).departureTime + connection.get(connection.size()-1).duration;
			
			for(List<Link> connection2 : connections)
			{
				int departure2 = connection2.get(0).departureTime;
				int arrival2 = connection2.get(connection2.size()-1).departureTime + connection2.get(connection2.size()-1).duration;
//				System.out.println((departure - departure2) <= 0 && arrival - arrival2 >= 0);
//				System.out.println(departure - departure2 != 0 || arrival - arrival2 != 0);
				if((departure - departure2) <= 0 && arrival - arrival2 >= 0)
				{
					if(departure - departure2 != 0 || arrival - arrival2 != 0)
					{
						obsolete.add(connection);
//						System.out.println(connections.size());
						break;
					} 
					//break;
				}
			}
			
		}
		for(List<Link> connection : obsolete)
		{
			connections.remove(connection);
		}
		
		return connections;
	}
	
	
	
	
	
		
		
		
		
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Set<Link> links =  new HashSet<Link>();
	       links.add(new Link("B", "C", 5, 4));
	       links.add(new Link("A", "C", 4, 20));
	       links.add(new Link("A", "C", 0, 20));
	       links.add(new Link("D", "C", 3, 6));
	       links.add(new Link("B", "C", 2, 2));
	       links.add(new Link("A", "D", 1, 2));
	       links.add(new Link("A", "B", 1, 3));
//	       links.add(new Link("l3", "l6", 200, 45));
//	       homework4 hw = new homework4();
	       System.out.println(getConnections("A", "B", 0,links));
	 
	       
	}

}
