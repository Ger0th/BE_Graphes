package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.*;
public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    @Override
    protected void initTabLabel() {
     	final ShortestPathData data = getInputData();
         Node nodeA;
         double estimation; //Estimation pour aller à la destination
         for (int i=0; i<tabLabel.length;i++) {
 			nodeA=data.getGraph().getNodes().get(i);
 			double firstMethodCost, secondMethodCost;
 			
 			//Pour le shortest path
 			if (data.getMode()==Mode.LENGTH) estimation = nodeA.getPoint().distanceTo(data.getDestination().getPoint());
 			//Pour le fastest path
 			else {
 				if (data.getGraph().getGraphInformation().hasMaximumSpeed()) {
 					if (data.getMaximumSpeed()!=-1) {
 						if((firstMethodCost=nodeA.getPoint().distanceTo(data.getDestination().getPoint())*3.6/data.getGraph().getGraphInformation().getMaximumSpeed())<(secondMethodCost=nodeA.getPoint().distanceTo(data.getDestination().getPoint())*3.6/130)) {
 							estimation=firstMethodCost;
 						}
 						else estimation=secondMethodCost;						
 					}
 					else estimation=nodeA.getPoint().distanceTo(data.getDestination().getPoint())*3.6/data.getGraph().getGraphInformation().getMaximumSpeed();
 				}
 				else {
 					if (data.getMaximumSpeed()!=-1) estimation=nodeA.getPoint().distanceTo(data.getDestination().getPoint())*3.6/data.getMaximumSpeed();
 					else estimation=nodeA.getPoint().distanceTo(data.getDestination().getPoint())*3.6/130;
 				}
 				
 			}
 			
 			if (nodeA==data.getOrigin()) {
 				tabLabel[i]=new LabelStar(nodeA, false, 0, null, estimation);
 			}
 			else tabLabel[i]=new LabelStar(nodeA, false, Double.POSITIVE_INFINITY, null, estimation);
 		}
 	   
    }
}
