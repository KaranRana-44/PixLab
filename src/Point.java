public class Point {
    private int x;

    private int y;

    public Point(int a,int b){
        x=a;
        y=b;

    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
