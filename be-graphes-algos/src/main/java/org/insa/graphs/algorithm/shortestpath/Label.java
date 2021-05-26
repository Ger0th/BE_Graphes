package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
	
	private Node sommetCourant;
	private boolean marque;
	private double cout;
	private Arc pere;
	
	public Label(Node sommetCourant, boolean marque, double cout, Arc pere) {
		this.sommetCourant=sommetCourant;
		this.marque=marque;
		this.cout=cout;
		this.pere=pere;
	}
	
	public double getCost() {
		return cout;
	}
	public Node getCurrentNode() {
		return sommetCourant;
	}
	public boolean getMarque() {
		return marque;
	}
	public Arc getPere() {
		return pere;
	}
	
	
	public void setCurrentNode(Node currentNode) {
		this.sommetCourant=currentNode;
	}
	public void setMarque(boolean nouvMarque) {
		marque=nouvMarque;
	}
	
	public void setCost(double newCost) {
		cout=newCost;
	}
	
	public void setPere(Arc nouvPere) {
		pere=nouvPere;
	}
	
	public double getTotalCost() {
		return this.cout;
	}
	@Override
	public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Label) {
            return this.getCurrentNode() ==((Label)other).getCurrentNode();
        }
        return false;
    }
	
	@Override
    public int compareTo(Label other) {
        return Double.compare(this.getTotalCost(), other.getTotalCost());
    }
}