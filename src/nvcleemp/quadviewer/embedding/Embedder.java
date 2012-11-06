package nvcleemp.quadviewer.embedding;

import nvcleemp.quadviewer.data.Quadrangulation;

/**
 * Interface for an embedder for quadrangulations.
 * 
 * @author nvcleemp
 */
public interface Embedder {
    public void embed(Quadrangulation quadrangulation);
}
