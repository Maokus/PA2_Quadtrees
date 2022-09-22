import java.util.Arrays;

public class BoundingBox{
    private Double[][] region;

    //Constructors
    BoundingBox(){
        this.region = new Double[][]{{0.0,0.0},{0.0,0.0}};
    }

    BoundingBox(Double[][] region){
        this.region = region;
    }

    //Getters and setters
    public Double[][] getRegion() {
        return region;
    }

    public void setRegion(Double[][] region) {
        this.region = region;
    }

    //Generative getters
    public Double midX(){
        return (region[0][0]+region[1][0])/2;
    }

    public Double midY(){
        return (region[0][1]+region[1][1])/2;
    }

    public Double width(){
        return region[1][0]-region[0][0];
    }

    public Double height(){
        return region[1][1]-region[0][1];
    }

    //Utility functions
    public boolean contains(Double[] point){
        return point[0]>region[0][0] &&
                point[0]<region[1][0] &&
                point[1]>region[0][1] &&
                point[1]>region[1][1];
    }

    public BoundingBox intersect(BoundingBox region){
        double x5 = Math.max(this.region[0][0], region.region[0][0]);
        double y5 = Math.max(this.region[0][1], region.region[0][1]);
        double x6 = Math.min(this.region[1][0], region.region[1][0]);
        double y6 = Math.min(this.region[1][1], region.region[1][1]);

        return new BoundingBox(new Double[][]{{x5,y5},{x6,y6}});
    }

    //Boolean checks

    public boolean isEncompassedBy(BoundingBox rect){
        return rect.region[0][0]<=this.region[0][0] &&
                rect.region[0][1]<=this.region[0][1] &&
                rect.region[1][0]>=this.region[1][0] &&
                rect.region[1][1]>=this.region[1][1];
    }

    public boolean isEqualsTo(BoundingBox rect){
        return rect.encompasses(this) && rect.isEncompassedBy(this);
    }

    public boolean encompasses(BoundingBox rect){
        return this.region[0][0]<=rect.region[0][0] &&
                this.region[0][1]<=rect.region[0][1] &&
                this.region[1][0]>=rect.region[1][0] &&
                this.region[1][1]>=rect.region[1][1];
    }

    public boolean hasPositiveArea(){
        return region[0][0]<region[1][0]&&region[0][1]<region[1][1];
    }

    public static boolean hasPositiveArea(BoundingBox r){
        return r.region[0][0]<r.region[1][0]&&r.region[0][1]<r.region[1][1];
    }

    @Override
    public String toString() {
        return "{{"+region[0][0]+","+region[0][1]+"},{"+region[1][0]+","+region[1][1]+"}}";
    }
}
