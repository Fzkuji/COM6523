package dependenceAnalysis.util;

import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Graph<T> {

	/**
	 * A facade class to store graphs as DirectedMultiGraphs using the JGraphT framework.
	 */

	protected DirectedMultigraph<T, DefaultEdge> graph;
	
	public Graph(){
		graph = new DirectedMultigraph<T, DefaultEdge>(new ClassBasedEdgeFactory<T, DefaultEdge>(DefaultEdge.class));

	}
	
	public void addNode(T n){
		graph.addVertex(n);
	}

	public void addEdge(T a, T b) {
		if(a.equals(b))
			return;
		if(graph.containsVertex(a) && graph.containsVertex(b)) {
			graph.addEdge(a,b);
		}
	}

	/**
	 * Returns the immediate predecessors of a node.
	 * @param a
	 * @return
	 */
	public Set<T> getPredecessors(T a){
		Set<T> preds = new HashSet<T>();
		for(DefaultEdge de : graph.incomingEdgesOf(a)){
			preds.add(graph.getEdgeSource(de));
		}
		return preds;
	}

	/**
	 * Returns the immediate successors of a node.
	 * @param a
	 * @return
	 */
	public Set<T> getSuccessors(T a){

		Set<T> succs = new HashSet<T>();
		for(DefaultEdge de : graph.outgoingEdgesOf(a)){
			succs.add(graph.getEdgeTarget(de));
		}
		return succs;
	}

	/**
	 * Returns all of the nodes in the graph.
	 * @return
	 */
	public Set<T> getNodes(){
		return graph.vertexSet();
	}

	/**
	 * Returns the entry node - the node with no predecessors.
	 * Assumes that there is only one such node in the graph.
	 * @return
	 */
	public T getEntry(){
		for(T n : getNodes()){
			if(graph.incomingEdgesOf(n).isEmpty())
				return n;
		}
		return null;
	}

	/**
	 * Returns the exit node - the node with no successors.
	 * Assumes that there is only one such node in the graph.
	 * @return
	 */
	public T getExit(){
		for(T n : getNodes()){
			if(graph.outgoingEdgesOf(n).isEmpty())
				return n;
		}
		return null;
	}

	/**
	 * Returns a representation of the graph in the GraphViz dot format. This can be written to a file and visualised using GraphViz.
	 * @return
	 */
	public String toString(){
		String dotString = "digraph gr{\n";
		for (T node : getNodes()) {
			String label = node.toString();
			label = label.replaceAll("[^a-zA-Z0-9]","_");
			if(getSuccessors(node).isEmpty()){
				dotString+="\""+label+"\"\n";
			}
			else for (T succ: getSuccessors(node)) {
				String succLabel = succ.toString();
				succLabel = succLabel.replaceAll("[^a-zA-Z0-9]","_");
				dotString+="\""+label+"\""+"->"+"\""+succLabel+"\""+"\n";
			}
		}
		dotString+="}";
		return dotString;
	}


	/**
	 * Return all transitive successors of m - i.e. any instructions
	 * that could eventually be reached from m.
	 * @param m
	 * @return
     */
	public Collection<T> getTransitiveSuccessors(T m){
		return transitiveSuccessors(m, new HashSet<T>());
	}

	private Collection<T> transitiveSuccessors(T m, Set<T> done){
		Collection<T> successors = new HashSet<T>();
		for(T n : getSuccessors(m)){
			if(!done.contains(n)) {
				successors.add(n);
				done.add(n);
				successors.addAll(transitiveSuccessors(n, done));
			}
		}
		return successors;
	}

	/**
	 * For a given pair of nodes in a DAG, return the ancestor that is common to both nodes.
	 *
	 * Important: This operation presumes that the graph contains no cycles.
	 * @param x
	 * @param y
	 * @return
	 */
	public T getLeastCommonAncestor(T x, T y) {
        T current = x;
        while(!containsTransitiveSuccessors(current,x,y)){
            current = getPredecessors(current).iterator().next();
        }
        return current;
    }

	private boolean containsTransitiveSuccessors(T x, T x2, T y) {
		Collection<T> transitiveSuccessors = getTransitiveSuccessors(x);
        if(transitiveSuccessors.contains(x2) && transitiveSuccessors.contains(y))
        	return true;
        else
        	return false;
	}

}
