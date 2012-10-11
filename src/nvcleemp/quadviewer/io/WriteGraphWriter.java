package nvcleemp.quadviewer.io;

import java.io.IOException;
import java.io.Writer;
import nvcleemp.quadviewer.data.Edge;
import nvcleemp.quadviewer.data.Quadrangulation;

/**
 * Utility class provides a method to write a quadrangulation to writegraph2d format.
 * 
 * @author nvcleemp
 */
public class WriteGraphWriter {

    private WriteGraphWriter() {
        //do not instantiate
    }
    
    public static void writeQuadrangulation(Quadrangulation q, Writer w) throws IOException{
        w.write(">>writegraph2d<<\n");
        for (int i = 0; i < q.getOrder(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%3d  0.0 0.0", i+1));
            Edge e, elast;
            e = elast = q.getFirstEdge(i);
            do{
                sb.append(String.format(" %3d", e.getEnd() + 1));
                e = e.getNext();
            } while (!e.equals(elast));
            w.write(sb.append("\n").toString());
        }
        w.write("0\n");
    }
}
