package sample;

import java.util.Arrays;
import java.util.Objects;

public class QuadNode {
    /**
     * children: the children of some sample.QuadNode. label from bottom left: 2 3
     *                                                                  0 1
     * Note that each region contains the left and bottom border.
     * min: minimum value of the sample.QuadNode's children, or just the sample.QuadNode itself
     * region: what is the region which the space covers [x1,y1,x2,y2]
     * We will assume that each "sample.QuadNode" encompasses the left and bottom border.
     */
    private QuadNode[] children;
    QuadNode minNode;
    private int min;
    private BoundingBox boundingBox;

    // Constructors
    public QuadNode(int min, BoundingBox nodeRegion){
        this.min = min;
        this.children = new QuadNode[4];
        this.boundingBox = nodeRegion;
    }

    public QuadNode(int min, Double[][] nodeRegion){
        this(min, new BoundingBox(nodeRegion));
    }

    public QuadNode(BoundingBox region) {
        this(0,region);
    }

    public QuadNode(Double[][] region) {
        this(new BoundingBox(region));
    }

    public QuadNode(){
        this(new Double[][]{{0.0, 0.0}, {1024.0, 1024.0}}); //Default region
    }


    public QuadNode(Double x1, Double x2, Double y1, Double y2) { //Yuck
        this(new Double[][] {{Math.min(x1, x2), Math.min(y1, y2)},
                            {Math.max(x1, x2), Math.max(y1, y2)}});
    }

    // Generates the children of specified indexes
    public void genChildren(int[] toGen){
        for (int index: toGen){
            if (index < 4 && index >= 0){
                // gen child
                double dx = boundingBox.width()/2;
                double dy = boundingBox.height()/2;
                double midx = boundingBox.midX();
                double midy = boundingBox.midY();
                int x = (index%2)*2-1;
                int y = (index/2)*2-1;
                this.children[index] = new QuadNode(midx+x*dx, midx, midy+y*dy, midy);
            }
        }
    }

    // Generates all children
    public void genChildren(){
        genChildren(new int[]{0,1,2,3});
    }


    // Getter and Setter Methods
    public QuadNode[] getChildren() {
        return children;
    }

    public void setChildren(QuadNode[] children) {
        this.children = children;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }


    // HELPER METHODS
    // Specifies if the sample.QuadNode has any children
    public boolean isLeaf() {
        for (int i = 0; i < 4 ; ++i){
            if (children[i] != null) return false;
        }
        return true;
    }

    public static boolean isLeaf(QuadNode n){
        return n.isLeaf();
    }

    // Determine which quadrant the point is in wrt the sample.QuadNode, excluding boundaries
    public Integer getIndexFromPosition(double x, double y){

        double midx = boundingBox.midX();
        double midy = boundingBox.midY();

        if(x==midx){return null;}
        if(y==midy){return null;}

        int index = 0;
        if (x > midx) index += 1;
        if (y > midy) index += 2;
        return index;
    }


    // Update Functions

    // Gets the sample.QuadNode which is specified in the region of a child.
    // Recall that each "sample.QuadNode" encompasses the left and bottom border.
    public QuadNode getQuadNode(double x, double y){
        if (this.isLeaf()) return this;
        int index = getIndexFromPosition(x, y);
        return children[index];
    }

    //Assumes all child nodes are updated
    public void updateMin(){
        min = -1023456789;
        for (QuadNode child:children){
            if (child != null){
                if (min == -1023456789){
                    min = child.getMin();
                    minNode = child;
                    continue;
                }
                int childMin = child.getMin();
                if(childMin<min){
                    min = child.getMin();
                    minNode = child;
                }
            }
        }
    }

    // Update the Node at some position
    public void updateQuadNode(double x, double y, int toAdd, boolean override){
        if (this.isLeaf()){
            this.min = override ? toAdd : toAdd + this.min;
            return;
        }
        // Find the child where the sample.QuadNode is in
        int index = getIndexFromPosition(x, y);
        children[index].updateQuadNode(x, y, toAdd, override);

        // update min count
        this.updateMin();
    }

    // For some rectangle, find out which quadrants it is in
    public Integer[] findRegions(double x1, double y1, double x2, double y2){
        Integer[] arr = new Integer[4];
        double[][] corners = new double[][]{ {x1, y1}, {x1, y2}, {x2, y1}, {x2, y2} };
        for (int i = 0; i < 4; ++i){
            int index = getIndexFromPosition(corners[i][0], corners[i][1]);
            arr[i] = index;
            for (int j = 0; j < i; ++j){
                if (Objects.equals(arr[i], arr[j])) {
                    arr[i] = -1;
                    break;
                }
            }
        }
        //System.out.println(Arrays.toString(arr));
        return arr;
    }

    // For some index, find the region's boundaries.
    public double[] getIndexPosition(int index){
        double dx = boundingBox.width()/2;
        double dy = boundingBox.height()/2;
        double midx = boundingBox.midX();
        double midy = boundingBox.midY();
        int x = (index%2)*2-1;
        int y = (index/2)*2-1;
        double x1 = midx+x*dx; double x2 = midx;
        double y1 = midy+y*dy; double y2 = midy;
        //System.out.println(Arrays.toString(region[0]) + " " + Arrays.toString(region[1]));
        //System.out.println(String.format("Rect pos is: %f %f %f %f with index %d", x1, y1, x2, y2, index));
        return new double[] {Math.min(x1, x2), Math.max(x1, x2), Math.min(y1, y2), Math.max(y1, y2)};
    }

    public int getMinIndex(){
        return getIndexFromPosition(minNode.boundingBox.midX(),minNode.boundingBox.midY());
    }

    // returns set of min, min pos. Every queried rectangle includes every quadrant on the boundary of the queried rectangle.
    public QuadNode getRectMin(BoundingBox searchArea){
        //Check for equality
        if (searchArea.isEqualsTo(this.boundingBox)) return this.minNode;
        if (!searchArea.hasPositiveArea()) return new QuadNode(Integer.MAX_VALUE, new BoundingBox(new Double[][]{{0.0,0.0},{0.0,0.0}}));
        QuadNode retNode = null;
        for (QuadNode child : children){
            if (child == null) continue;
            QuadNode newRetNode = child.getRectMin(searchArea.intersect(child.boundingBox));
            if (retNode == null || newRetNode.min < retNode.min){
                retNode = newRetNode;
            }
        }
        return retNode;
    }



    @Override
    public String toString() {
        if(this.isLeaf()){
            return "\nsample.QuadNode Leaf{" +
                    "min=" + min +
                    ", region=" + Arrays.toString(boundingBox.getRegion()[0]) + " " + Arrays.toString(boundingBox.getRegion()[1]) +
                    '}';
        }
        return "\nsample.QuadNode{\n" +
                "children=" + Arrays.toString(children) +
                "\n, min=" + min + ", minregion="+ minNode.boundingBox+
                ", region=" + Arrays.toString(boundingBox.getRegion()[0]) + " " + Arrays.toString(boundingBox.getRegion()[1]) + "\n"+
                '}';
    }
}
