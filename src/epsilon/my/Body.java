package epsilon.my;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Body {
    private int id;
    private double mass; //质量参数
    private double r;// 半径
    private VPoint position;
    // private Point position;  //位置
    private VPoint vel;  //速度  //横纵方向的速度
    private Color color;
    private LinkedList<VPoint> starPath;  //轨迹
    private int pathSize;  //轨迹长度

    //计算半径,顺便把max质量恒星赋值
    double calR(double mass) {
        // TODO Auto-generated method stub
    /*    if( max==null){
            max=this;
        }
        if(max.mass<=this.mass){
            max=this;
        }*/
        //球形半径
        return Math.pow(mass/Math.PI * (3f/4f),1f/3f);
    }

    void addPath(VPoint pos){
        //改变轨迹首尾位置
        if (starPath.size() < pathSize){
            starPath.addFirst(pos);
        } else {
            starPath.removeLast();
            starPath.addFirst(pos);
        }
    }

    //更新并移动
    void  updateAndMove(){
        //简单移动

/*        this.position.move(this.position.x+(int)(this.vel.getX()),
                this.position.y + (int)(this.vel.getY()));*/
        addPath(this.position);
        this.position = vAdd(this.position, this.vel);

    }
    //计算引力  误差有些大
    void attract(ArrayList<Body> stars){
        for (int i = 0; i < stars.size(); i++) {
            Body other = stars.get(i);
            if (this == other)  //如果同一个星 跳过
                continue;
            //计算两颗星的距离，碰撞或 （+。+）
            VPoint dir = new VPoint(this.position.getX() - other.position.getX(),
                    this.position.getY() - other.position.getY());

            /*VPoint dir = new VPoint(this.position.x - other.position.x,
                    this.position.y - other.position.y);*/

            if (msg(dir)<=this.r+other.r){  //碰撞
                if (this.mass >= other.mass){
                    //v(向量) = （v1（向量）*m1 + v2（向量）*m2）/（m1+m2）
                    this.vel = vDiv(vAdd(vMult(this.vel,this.mass),vMult(other.vel,other.mass)),
                            this.mass + other.mass);
                    this.mass = this.mass + other.mass;
                    if (this.mass > 400000)
                        this.color = Color.red;
                    else if (this.mass > 200000)
                        this.color = Color.PINK;
                    else if (this.mass > 80000)
                        this.color = Color.orange;
                    else if (this.mass > 30000)
                        this.color = Color.lightGray;

                    this.r = calR(this.mass);
                    stars.remove(i); //移除小的
                } else{
                    //这里不需要吗？再议
                }
            } else {
                //计算对其他星星的速度的影响
                double forcePower = Constant.G * (this.mass + other.mass) / (msg(dir) * msg(dir));
                //之前算出的方向的法向来乘以力的大小得到引力的方向
                //一般情况下，沿曲线运动的质点，其加速度可以分解为两个正交的分量，即：
                //和轨道相切的分量称为切向分量，
                //和轨道垂直的分量称为法向分量。
                VPoint force = vMult(normalize(dir),forcePower);
                // F = m*a的变形  frame为每帧的长度
                VPoint move =vMult(vDiv(force,other.mass),MyPanel.n*MyPanel.frame);
                //更新速度(加)
                other.vel = vAdd(other.vel, move);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        ArrayList<Body> stars = new ArrayList();
        Random random = new Random(47);
        for (int i = 0; i < 500; i++){
            stars.add( new Body(i,random.nextDouble() * 1500,
                    random.nextInt(1000),
                    random.nextInt(1000),
                    0,
                    0,10));
        }
        for (int step = 0; ; step++ ) {
            for (int i = 0; i < stars.size(); i++){
                stars.get(i).updateAndMove();
                stars.get(i).attract(stars);
            }
            for (Body b :
                    stars) {
                System.out.println(b);
            }
            System.out.println("---------------"+step+"--------------------------");
            Thread.sleep(100);
            if (stars.size() == 1)
                break;
        }
    }

    //向量求模
    private double msg(VPoint dir){
        return Math.pow(dir.getX() * dir.getX() + dir.getY() * dir.getY(), 0.5);
    }
    //数乘向量
    private VPoint vMult(VPoint v, double m){
        return new VPoint(v.getX() * m, v.getY() * m);
    }
    //向量相加
    private VPoint vAdd(VPoint va, VPoint vb){
        return new VPoint(va.getX() + vb.getX(), va.getY() + vb.getY());
    }
    //向量除数
    private VPoint vDiv(VPoint v, double m){
        return new VPoint(v.getX() / m, v.getY() / m);
    }
    //求单位向量
    private VPoint normalize(VPoint v){
        return vDiv(v,msg(v));
    }












    public Body(int id, double mass, int x, int y,double vx, double vy,int pathSize) {
        this.id = id;
        this.mass = mass;
        this.r = calR(mass);
        this.position = new VPoint(x, y);
        //this.position = new Point(x, y);
        this.vel = new VPoint(vx,vy);
        color = Color.MAGENTA;
        this.starPath = new LinkedList<>();
        this.pathSize = pathSize;
    }

    public int getId() {
        return id;
    }

    public double getR() {
        return r;
    }

    public VPoint getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public LinkedList<VPoint> getStarPath() {
        return starPath;
    }

    @Override
    public String toString() {
        return "Body{" +
                "id=" + id +
                ", mass=" + mass +
                ", r=" + r +
                ", position=" + position +
                ", vel=" + vel +
                ", color=" + color +
                ", StarPath=" + starPath +
                ", pathSize=" + pathSize +
                '}';
    }


    public double getMass() {
        return this.mass;
    }
}
