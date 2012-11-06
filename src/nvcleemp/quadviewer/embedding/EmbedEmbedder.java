package nvcleemp.quadviewer.embedding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import nvcleemp.quadviewer.data.Embedding;
import nvcleemp.quadviewer.data.Quadrangulation;
import nvcleemp.quadviewer.io.WriteGraphEmbeddingReader;
import nvcleemp.quadviewer.io.WriteGraphWriter;

/**
 * Implementation of interface Embedder which uses the C-program embed
 * to embed the quadrangulation. This program needs to be in the class
 * path.
 * 
 * @author nvcleemp
 */
public class EmbedEmbedder implements Embedder{

    @Override
    public void embed(Quadrangulation quadrangulation) {
        Embedding e = quadrangulation.createEmbedding();
        try {
            Process process = new ProcessBuilder("./embed").start();
            
            OutputStream stdin = process.getOutputStream();
            InputStream stdout = process.getInputStream();
            
            final OutputStreamWriter ow = new OutputStreamWriter(stdin);
            WriteGraphWriter.writeQuadrangulation(quadrangulation, ow);
            ow.flush();
            stdin.close();
            final BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            double[][] embedding = 
                    WriteGraphEmbeddingReader.readEmbedding(
                                    quadrangulation, br);
            br.close();
            
            for (int i = 0; i < embedding.length; i++) {
                e.setX(i, embedding[i][0]);
                e.setY(i, embedding[i][1]);
            }
        } catch (IOException ex) {
            Logger.getLogger(EmbedEmbedder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
