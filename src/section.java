public class section
{
//    private int upperLeft;
//    private int lowerLeft;
//    private int lowerRight;
//    private int upperRight;
    private int[] topLeftCell;
    private int[] topRightCell;
    private int[] bottomRightCell;
    private int[] bottomLeftCell;
    private int rank;


    public section(int[] topLeftCell, int rank)
    {
        this.topLeftCell = topLeftCell;
        this.topRightCell = new int[]{topLeftCell[0] + 4, topLeftCell[1]};
        this.bottomRightCell = new int[]{topLeftCell[0] + 4, topLeftCell[1] - 4};
        this.bottomLeftCell = new int[]{topLeftCell[0], topLeftCell[1] - 4};
        this.rank = rank;

    }

//    public void setSectionEdges() {
//        this.upperEdge[0] = this.topLeftCell[0];
//        this.upperEdge[1] = this.topLeftCell[0]+1;
//        this.upperEdge[2] = this.topLeftCell[0]+2;
//        this.upperEdge[3] = this.topLeftCell[0]+3;
//        this.upperEdge[4] = this.topLeftCell[0]+4;
//    }
//    public int[] getUpperEdge(){return this.upperEdge;}
//    public int[] getLowerEdge(){return this.lowerEdge;}
//    public int[] getLeftEdge(){return this.leftEdge;}
//    public int[] getRightEdge(){return this.rightEdge;}
//
    public int[] getTopLeftCell(){return this.topLeftCell;}
    public int[] getTopRightCell(){return this.topRightCell;}
    public int[] getBottomRightCell(){return this.bottomRightCell;}
    public int[] getBottomLeftCell(){return this.bottomLeftCell;}
    public int getRank(){return this.rank;}
}
