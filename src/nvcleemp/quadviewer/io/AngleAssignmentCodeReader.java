package nvcleemp.quadviewer.io;

import java.io.IOException;
import java.io.InputStream;
import nvcleemp.quadviewer.data.Angle;
import nvcleemp.quadviewer.data.Edge;
import nvcleemp.quadviewer.data.Quadrangulation;

/**
 * Uitility class to read a quadrangulation in angle assignment format. This 
 * format is based upon planar code, but also includes the angles between two
 * edges. This format is only defined for quadrangulations.
 * 
 * @author nvcleemp
 */
public class AngleAssignmentCodeReader {

    private AngleAssignmentCodeReader() {
        //do not instantiate
    }
    
    private static Edge findEdge(Quadrangulation q, int from, int to){
        Edge e = q.getFirstEdge(from);
        if(e.getEnd()==to){
            return e;
        } else {
            e = e.getNext();
            while(e!=q.getFirstEdge(from) && e.getEnd()!=to){
                e = e.getNext();
            }
            if(e==q.getFirstEdge(from)){
                throw new RuntimeException("Looking for a non-existing edge!");
            } else {
                return e;
            }
        }
    }
    
    private static Quadrangulation decodeAngleAssignment(int[] code){
        Quadrangulation q = new Quadrangulation(code[0]);
        int pos = 1;
        int vertex = 0;
        while(pos<code.length){
            Edge e = new Edge(vertex, code[pos++]-1);
            q.setFirstEdge(vertex, e);
            //set inverse
            if(e.getEnd()<vertex){
                Edge inverse = findEdge(q, e.getEnd(), vertex);
                inverse.setInverse(e);
                e.setInverse(inverse);
            }
            int degree = 1;
            while(code[pos]!=0){
                //create edge
                Edge nextEdge = new Edge(vertex, code[pos++]-1);
                degree++;
                e.setNext(nextEdge);
                nextEdge.setPrevious(e);
                
                //set inverse
                if(nextEdge.getEnd()<vertex){
                    Edge inverse = findEdge(q, nextEdge.getEnd(), vertex);
                    inverse.setInverse(nextEdge);
                    nextEdge.setInverse(inverse);
                }
                
                //continue
                e = nextEdge;
            }
            q.getFirstEdge(vertex).setPrevious(e);
            e.setNext(q.getFirstEdge(vertex));
            
            //read angles
            pos++;
            e = q.getFirstEdge(vertex);
            for (int i = 0; i < degree; i++) {
                e.setAngle(Angle.values()[code[pos++]-1]);
                e = e.getNext();
            }
            if(code[pos]!=0){
                throw new RuntimeException("Invalid format for angle assignment file");
            }
            
            //next vertex
            vertex++;
            pos++; //last zero
        }
        
        return null;
    }
    
    private enum Endian {
        LITTLE_ENDIAN{
            @Override
            protected int combineBytes(int b1, int b2) throws IOException{
                return b1 + b2*256;
            }
        }, BIG_ENDIAN{
            @Override
            protected int combineBytes(int b1, int b2) throws IOException{
                return b1*256 + b2;
            }
        };
        
        protected abstract int combineBytes(int b1, int b2) throws IOException;
        
        public int readTwoBytes(InputStream is) throws IOException{
            int b1 = is.read();
            int b2 = is.read();
            if(b1 == -1 || b2 == -1)
                return -1;
            return combineBytes(b1, b2);
        }
    }
    
    private static int readTwoBytes(InputStream is) throws IOException {
        return readTwoBytes(is, Endian.LITTLE_ENDIAN);
    }
    
    private static int readTwoBytes(InputStream is, Endian e) throws IOException {
        return e.readTwoBytes(is);
    }
    
    public static Quadrangulation readAngleAssignment(InputStream is) throws IOException{
        int read = is.read();
        int[] buffer = new int[3];
        boolean bufferUsed = false;
        boolean twoByte = false;
        int[] code;
        int i;
        
        if(read==-1){
            return null;
        }
        
        if(read=='>'){
            //possibly dealing with a header >>angle_assignment<<
            buffer[0] = read;
            buffer[1] = is.read();
            buffer[2] = is.read();
            bufferUsed = true;
            if(buffer[1]=='>' && buffer[2]=='a'){
                //this is a header
                while(read!='<' && read!=-1) read = is.read();
                if(read==-1){
                    throw new IOException("Reached end of file while reading header.");
                }
                read = is.read();
                if(read!='<'){
                    throw new IOException("Found corrupt header.");
                }
                read = is.read();
                if(read==-1){
                    return null;
                }
                bufferUsed = false;
            }
        }
        if(bufferUsed){
            //we are sure that buffer[0] is not 0 (otherwise the buffer wouldn't have been used)
            //so buffer[0] is the number of vertices
            int edgeCount = 4*(buffer[0]-2); //number of directed edges
            code = new int[2*edgeCount+1+2*buffer[0]];
            code[0] = buffer[0];
            code[1] = buffer[1];
            code[2] = buffer[2];
            i = 3;
        } else if(read==0){
            int order = readTwoBytes(is);
            int edgeCount = 4*(order-2);
            code = new int[2*edgeCount+1+2*order];
            code[0] = order;
            i = 1;
            twoByte = true;
        } else {
            int edgeCount = 4*(read-2);
            code = new int[2*edgeCount+1+2*read];
            code[0] = read;
            i = 1;
        }
        
        for( ; i<code.length; i++){
            if(twoByte){
                code[i] = readTwoBytes(is);
            } else {
                code[i] = is.read();
            }
            if(code[i]==-1){
                throw new IOException("Reached end of file while reading quadrangulation.");
            }
        }
        
        return decodeAngleAssignment(code);
    }
}
