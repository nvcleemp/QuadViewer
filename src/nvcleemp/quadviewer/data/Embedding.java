package nvcleemp.quadviewer.data;

/**
 * Class that stores an embedding for a quadrangulation.
 * 
 * @author nvcleemp
 */
public class Embedding {
    private double[][] coordinates;

    public Embedding(int order) {
        coordinates = new double[order][2];
    }
    
    public void setX(int vertex, double coordinate){
        coordinates[vertex][0] = coordinate;
    }
    
    public void setY(int vertex, double coordinate){
        coordinates[vertex][1] = coordinate;
    }
    
    public double getX(int vertex){
        return coordinates[vertex][0];
    }
    
    public double getY(int vertex){
        return coordinates[vertex][1];
    }
    
}
