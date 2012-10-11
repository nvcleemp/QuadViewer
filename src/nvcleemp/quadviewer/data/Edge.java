package nvcleemp.quadviewer.data;

/**
 * An oriented edge.
 * 
 * @author nvcleemp
 */
public class Edge {
    private int start;
    private int end;
    
    private Edge prev;
    private Edge next;
    private Edge inverse;
    
    private Angle angle; /* angle between this edge and next edge;
                            0: alpha, 1: beta, 2: gamma, 3: delta */

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public Angle getAngle() {
        return angle;
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public Edge getInverse() {
        return inverse;
    }

    public void setInverse(Edge inverse) {
        this.inverse = inverse;
    }

    public Edge getNext() {
        return next;
    }

    public void setNext(Edge next) {
        this.next = next;
    }

    public Edge getPrevious() {
        return prev;
    }

    public void setPrevious(Edge prev) {
        this.prev = prev;
    }
    
    
}
