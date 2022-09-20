import java.util.Arrays;

public class QuadNode {
    /**
     * children: the children of some QuadNode. label from bottom left: 2 3
     *                                                                  0 1
     * Note that each region contains the left and bottom border.
     * min: minimum value of the QuadNode's children, or just the QuadNode itself
     * region: what is the region which the space covers
     * We will assume that each "QuadNode" encompasses the left and bottom border.
     */
    private QuadNode[] children;
    private int min;
    private double[][] region;

    // Constructors
    public QuadNode(){
        this.children = new QuadNode[4];
        this.min = 0;
        this.region = new double[][]{{0, 0}, {1024, 1024}};
    }

    public QuadNode(int min, double[][] region) {
        this.children = new QuadNode[4];
        this.min = min;
        this.region = region;
    }

    public QuadNode(double[][] region) {
        this.children = new QuadNode[4];
        this.region = region;
    }

    public QuadNode(double x1, double x2, double y1, double y2) {
        this(new double[][] {{Math.min(x1, x2), Math.min(y1, y2)},
                            {Math.max(x1, x2), Math.max(y1, y2)}});
    }

    // Generates the children of specified indexes
    public void genChildren(int[] toGen){
        for (int index: toGen){
            if (index < 4 && index >= 0){
                // gen child
                double dx = (region[1][0] - region[0][0])/2;
                double dy = (region[1][1] - region[0][1])/2;
                double midx = (region[1][0] + region[0][0])/2;
                double midy = (region[1][1] + region[0][1])/2;
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

    public double[][] getRegion() {
        return region;
    }

    public void setRegion(double[][] region) {
        this.region = region;
    }

    // HELPER METHODS
    // Specifies if the QuadNode has any children
    public boolean isChild() {
        for (int i = 0; i < 4 ; ++i){
            if (children[i] != null) return false;
        }
        return true;
    }

    public static boolean isChild(QuadNode n){
        return n.isChild();
    }

    // Determine which quadrant the point is in wrt the QuadNode
    public int getIndexFromPosition(double x, double y){
        double midx = (region[1][0] + region[0][0])/2;
        double midy = (region[1][1] + region[0][1])/2;
        int index = 0;
        if (x >= midx) index += 1;
        if (y > midy) index += 2;
        return index;
    }


    // Update Functions

    // Gets the QuadNode which is specified in the region of a child.
    // Recall that each "QuadNode" encompasses the left and bottom border.
    public QuadNode getQuadNode(double x, double y){
        if (this.isChild()) return this;
        int index = getIndexFromPosition(x, y);
        return children[index];
    }

    public void updateMin(){
        min = -1023456789;
        for (QuadNode child:children){
            if (child != null){
                if (min == -1023456789){
                    min = child.getMin();
                    continue;
                }
                min = Math.min(child.getMin(), min);
            }
        }
    }

    // Update the Node at some position
    public void updateQuadNode(double x, double y, int toAdd, boolean override){
        if (this.isChild()){
            this.min = override ? toAdd : toAdd + this.min;
            return;
        }
        // Find the child where the QuadNode is in
        int index = getIndexFromPosition(x, y);
        children[index].updateQuadNode(x, y, toAdd, override);

        // update min count
        this.updateMin();
    }

    // For some rectangle, find out which quadrants it is in
    public int[] findRegions(double x1, double x2, double y1, double y2){
        int[] arr = new int[4];
        double[][] corners = new double[][]{ {x1, y1}, {x1, y2}, {x2, y1}, {x2, y2} };
        for (int i = 0; i < 4; ++i){
            int index = getIndexFromPosition(corners[i][0], corners[i][1]);
            arr[i] = index;
            for (int j = 0; j < i; ++j){
                if (arr[i] == arr[j]) {
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
        double dx = (region[1][0] - region[0][0])/2;
        double dy = (region[1][1] - region[0][1])/2;
        double midx = (region[1][0] + region[0][0])/2;
        double midy = (region[1][1] + region[0][1])/2;
        int x = (index%2)*2-1;
        int y = (index/2)*2-1;
        double x1 = midx+x*dx; double x2 = midx;
        double y1 = midy+y*dy; double y2 = midy;
        //System.out.println(Arrays.toString(region[0]) + " " + Arrays.toString(region[1]));
        //System.out.println(String.format("Rect pos is: %f %f %f %f with index %d", x1, y1, x2, y2, index));
        return new double[] {Math.min(x1, x2), Math.max(x1, x2), Math.min(y1, y2), Math.max(y1, y2)};
    }

    // returns set of min, min pos. Every queried rectangle includes every quadrant on the boundary of the queried rectangle.
    public Object[] getRectMin(double x1, double x2, double y1, double y2){
        //System.out.println("\n" + Arrays.toString(region[0]) + " " + Arrays.toString(region[1]));
        //System.out.println(String.format("%f %f %f %f", x1, x2, y1, y2));
        int[] indexes = findRegions(x1, x2, y1, y2);
        Arrays.sort(indexes);

        // check that all cells are filled
        double midx1 = (region[1][0] + 3*region[0][0])/4;
        double midy1 = (region[1][1] + 3*region[0][1])/4;
        double midx2 = (3*region[1][0] + region[0][0])/4;
        double midy2 = (3*region[1][1] + region[0][1])/4;
        if (this.isChild()){
            return new Object[] {min, getRegion()[0]};
        }
        else if ((x1 < midx1 && y1 < midy1 && x2 >= midx2 && y2 >= midy2)){
            for (int i = 0; i < 4; ++i){
                if (children[i].min == this.min){
                    double[] positions = getIndexPosition(i);
                    return getRectMin(positions[0], positions[1], positions[2], positions[3]);
                }
            }
            return new Object[] {min, getRegion()[0]};
        }
        else {
            int runningMin = 1023456789;
            double[] pos = new double[2];
            for (int i: indexes){
                if (i == -1) continue;
                double[] rectPos = getIndexPosition(i);
                switch (i){
                    case 0:
                        rectPos[0] = x1;
                        rectPos[2] = y1;
                        break;
                    case 1:
                        rectPos[1] = x2;
                        rectPos[2] = y1;
                        break;
                    case 2:
                        rectPos[0] = x1;
                        rectPos[3] = y2;
                        break;
                    case 3:
                        rectPos[1] = x2;
                        rectPos[3] = y2;
                        break;
                }

                Object[] rect = children[i].getRectMin(rectPos[0], rectPos[1], rectPos[2], rectPos[3]);
                if (runningMin > (int)rect[0]){
                    runningMin = (int)rect[0];
                    pos = (double[]) rect[1];
                }
            }
            return new Object[] {runningMin, pos};
        }

        //backtracking: where does the vertex actually exist?

    }











    @Override
    public String toString() {
        return "QuadNode{\n" +
                "children=" + Arrays.toString(children) +
                "\n, min=" + min +
                ", region=" + Arrays.toString(region[0]) + " " + Arrays.toString(region[1]) +
                '}';
    }
}
