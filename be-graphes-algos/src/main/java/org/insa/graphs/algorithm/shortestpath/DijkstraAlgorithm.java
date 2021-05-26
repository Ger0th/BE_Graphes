package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Collections;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import java.util.concurrent.*;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	protected Label tabLabel[]; //Tableau qui contient  les labels des noeuds
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        tabLabel = new Label[data.getGraph().size()];
    }
    
    
    protected void initTabLabel() {
		// Initialisation du tableau d'étiquettes
		Node nodeA;
		final ShortestPathData data = getInputData();
		for (int i=0; i<tabLabel.length;i++) {
			nodeA=data.getGraph().getNodes().get(i);
			if (nodeA==data.getOrigin()) {
				tabLabel[i]=new Label(nodeA, false, 0, null);
			}
			else tabLabel[i]=new Label(nodeA, false, Double.POSITIVE_INFINITY, null);
		}
	}
    
    
    @Override
    protected ShortestPathSolution doRun() {
    	
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        long lStartTime = System.nanoTime();
        boolean destMarquee = false; //Booléen qui servira à indiquer lorsqu'on marquera la destination
        Node nodeA;// Variable servant à initialiser le tableau et servant pendant les itérations pour ne pas parcourir une même Node plusieurs fois
        Label labelA,labelB;// Labels qui seront utiles  pendant les itérations du programme
        int nbrIterations=0;//compteur d'itérations pour vérifier qu'on parcoure bien tout le graphe
        double newCost;//Double dans lequel on va stocker le nouveau cout calculé pendant les itérations
        
        
        initTabLabel();//on initialise le tableau d'étiquettes
        
        BinaryHeap<Label> currentHeap=new BinaryHeap<Label>();//Tas pour les sommets qu'on sera en train de traiter
        currentHeap.insert(tabLabel[data.getOrigin().getId()]);//Initialisation de ce tas
        
        
        //Partie des boucles principales
        while (!destMarquee&&nbrIterations<tabLabel.length) { // On utilise tabLabel.length pour le nombre de noeuds dans le graph car ce paramètre est plus facile d'accès que la taille du graph directement
        	//Chercher le sommet de coût min, mettre la node correspondante dans NodeA et l'enlever de currentHeap
        	if (currentHeap.isEmpty()) {
				break ; // break utilisé pour ne pas capturer l'exception d'une file vide pour ne pas gêner la capture d'un autre type d'erreurs
			}

			labelA = currentHeap.deleteMin();
			nodeA = labelA.getCurrentNode();

			labelA.setMarque(true);
			notifyNodeMarked(nodeA);			
			if (nodeA==data.getDestination()) destMarquee = true;
			if (nodeA==data.getOrigin()) notifyOriginProcessed(data.getOrigin());
			
			for (Arc arcAB: nodeA.getSuccessors()) {
				labelB=tabLabel[arcAB.getDestination().getId()];
				if (data.isAllowed(arcAB)&&!labelB.getMarque()) {
					if (data.getMode()==Mode.LENGTH) newCost = labelA.getCost()+arcAB.getLength(); //Pour le shortest path
					else newCost = labelA.getCost()+arcAB.getMinimumTravelTime();//Pour le fastest path
					
					//On met à jour la node et le tas au besoin
					if (newCost<labelB.getCost()) {
						//Insertion dans le tas des nodes en traitement si c'est la première mise à jour du sommet
						if (Double.isInfinite(labelB.getCost()) && Double.isFinite(newCost)) {
							labelB.setCost(newCost);
							labelB.setPere(arcAB);   
							currentHeap.insert(labelB); //insertion dans le tas de la nouvelle node traitée
							notifyNodeReached(arcAB.getDestination());
						}
						else {
							currentHeap.remove(labelB);//On extrait le label déjà existant
							labelB.setCost(newCost);
							labelB.setPere(arcAB); 
							currentHeap.insert(labelB); //On réinsère le label après mise à jour des données
						}
					}
				}
			}
			nbrIterations++;
        }
        
        //Partie construction de la solution
        if (!tabLabel[data.getDestination().getId()].getMarque()) {
			nodeA = null;
			solution = new ShortestPathSolution(data, Status.INFEASIBLE, new Path(data.getGraph(), nodeA));
		}
		else {
			notifyDestinationReached(data.getDestination());//On préviens que la destination a été atteinte
			ArrayList<Arc> arcs = new ArrayList<>();// On crée le chemin à partir des informations venant des labels
			Arc arc = tabLabel[data.getDestination().getId()].getPere();
			while (arc != null) {
				arcs.add(arc);
				arc = tabLabel[arc.getOrigin().getId()].getPere();
			}

			// On reverse le path
			Collections.reverse(arcs);

			// On construit la solution finale
			if (arcs.isEmpty()) solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), data.getOrigin()));
			else solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
		}
		long lEndTime = System.nanoTime();
		System.out.println("Temps de calcul (ms) : "+(lEndTime-lStartTime)/1000000);
        return solution;
    }

}
