package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	
	private Map<Integer,Airport> idMap;
	
	
	
	public Model() {
		dao= new ExtFlightDelaysDAO();
		idMap= new HashMap<Integer,Airport>();
		dao.loadAllAirports(idMap);  //qui non devo passare come parametro anche int x, dato che sono TUTTI I VERTICI
		
	
	}
	
	
	public void creaGrafo(int x) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	
		//AGGIUNGO VERTICI (TUTTI) 
		Graphs.addAllVertices(grafo, idMap.values());
		
	    //AGGIUNGO GLI ARCHI 
		
		for(Rotta r: dao.getRotte(x,idMap)) {
			
			//se il grafo contiene gia questi vertici 
			if(this.grafo.containsVertex(r.getA1()) && this.grafo.containsVertex(r.getA2())) {
				DefaultWeightedEdge e= this.grafo.getEdge(r.getA1(), r.getA2());
				
				//se l'arco non esiste, aggiungilo con il relativo peso
				if(e==null) {
					Graphs.addEdgeWithVertices(this.grafo, r.getA1(), r.getA2(), r.getPeso());
				} else {
					//ho gia un arco TRA GLI STESSI VERTICI, MA SONO SCAMBIATI--> incremento il peso 
					double pesoV= this.grafo.getEdgeWeight(e);
					double pesoN= pesoV+ r.getPeso();
					double pesoMedia= pesoN/2;
					
					this.grafo.setEdgeWeight(e, pesoMedia);
				}
		}	
	  }
	}
	
	
	//PER AVERE IL NUMERO VERTICI E ARCHI DEL GRAFP
	
	public int getNumeroVertici() {
		
		if(this.grafo!=null) {
			return this.grafo.vertexSet().size();
			}
		return 0;
		}
	
	
	public int getNumeroArchi() {
		if(this.grafo!=null) {
			return this.grafo.edgeSet().size();
		}
		return 0;
	}
	
	
	
	//creo metodo per stampare l'elenco di tutti gli archi con le relative distanze
// il metodo è getRotte senza nulla tra parentesi
	
	public List<Rotta> getRotte(){
		
		List<Rotta> rotte = new LinkedList<Rotta>();
	
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			Rotta ro= new Rotta(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e));
		      rotte.add(ro);
			
			}
	
	return rotte;

}
	
}
