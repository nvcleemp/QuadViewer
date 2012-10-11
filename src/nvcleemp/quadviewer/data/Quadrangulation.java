package nvcleemp.quadviewer.data;

/**
 *
 * @author nvcleemp
 */
public class Quadrangulation {
    
    private int order; //the number of vertices
    
    private Edge firstEdge[];
    
    private int degrees[];

    public Quadrangulation(int order) {
        this.order = order;
        firstEdge = new Edge[order];
        degrees = new int[order];
    }
    
    public void setDegree(int vertex, int degree){
        degrees[vertex] = degree;
    }
    
    public void setFirstEdge(int vertex, Edge edge){
        firstEdge[vertex] = edge;
    }
    
    public int getDegree(int vertex){
        return degrees[vertex];
    }
    
    public Edge getFirstEdge(int vertex){
        return firstEdge[vertex];
    }
    
    public int getOrder(){
        return order;
    }
    
}
