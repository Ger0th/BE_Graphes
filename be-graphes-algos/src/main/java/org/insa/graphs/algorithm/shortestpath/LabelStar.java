package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
	
	double estimationCost;

	public LabelStar(Node sommetCourant, boolean marque, double cout, Arc pere, double estimation) {
		super(sommetCourant, marque, cout, pere);
		estimationCost = estimation;
	}
	
	public double getEstimationCost() {
		return this.estimationCost;
	}
	
	@Override
	public double getTotalCost() {
		return this.getCost()+this.estimationCost;
	}	
	

}