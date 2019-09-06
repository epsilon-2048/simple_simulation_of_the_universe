package epsilon.my;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class MyPanel extends JPanel implements Runnable {//

    ArrayList<Body> stars = new ArrayList();
    Random random = new Random(new Date().getTime());

    static int      frame       = 100; //帧长  单位毫秒
    static double   n           = 0.001;  //帧长的倍数
    int             starNum     = 2000;    //调星体数量
    double          max         = 0;     //最大星星的质量
    int             showNum     = 150;  //当星体数量小于showNum，显示编号
    int             size        = 10;   //调星星标号的字体大小
    int             maxMass     = 100;  //调初始最大质量
    int             pathSize    = 100;  //调轨道长度

    public void init(){

        for (int i = 0; i < starNum; i++) {
            stars.add(new Body(i,
                    random.nextDouble() * maxMass,  //调初始最大质量
                    random.nextInt(1500),  //初始位置x //窗体大小：1500 800
                    random.nextInt(800),  //初始位置y
                    0,                         //初始x轴速度
                    0,                          //初始y轴速度
                    pathSize));                     //轨迹长度
            if(i>starNum-5)
                stars.add(new Body(i,
                        random.nextDouble() * 2000 + 1000,
                        random.nextInt(1500),
                        random.nextInt(800),
                        0,
                        0,
                        200));
        }

       /* //等边三角形及其重心、坐标、
        stars.add(new Body(0, 2000, 300, 400, 0,1,200));
        stars.add(new Body(1, 2000, 500, 400, -1,0,200));
        stars.add(new Body(2, 2000, 400, 573, 1,0,200));
        stars.add(new Body(2, 20000, 400, 458, 0,0,200));
*/
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);//抹去上次画的小球、轨道
        g.setFont(new Font("",Font.BOLD,12));
        g.drawString("当前星星数："+stars.size(), 5, 15);
/*        if (max > 400000) //调帧长的倍数
            n = 1;
        else if(max > 200000)
            n *= 0.1;
        else if(max > 80000)
            n *= 0.05;
        else if(max > 30000)
            n *= 0.01;*/
        for (int i = 0; i < stars.size(); i++) {
            if (max < stars.get(i).getMass())
                max = stars.get(i).getMass();
            stars.get(i).updateAndMove();
            stars.get(i).attract(stars);
        }
        for (int i = 0; i < stars.size(); i++) {
            //画圆
            g.setColor(stars.get(i).getColor());
            g.fillOval((int) (stars.get(i).getPosition().getX()+0.5) - (int) (stars.get(i).getR()+0.5),
                    (int) (stars.get(i).getPosition().getY()+0.5) - (int) (stars.get(i).getR()+0.5),
                    (int) (stars.get(i).getR()+0.5)*2,
                    (int) (stars.get(i).getR()+0.5)*2);
            //画编号
           // System.out.println(stars.size());
            if(stars.size()<showNum){
                g.setColor(Color.black);
                g.setFont(new Font("",Font.BOLD,size));
                g.drawString(String.valueOf(stars.get(i).getId()),
                        (int) (stars.get(i).getPosition().getX()+0.5),
                        (int) (stars.get(i).getPosition().getY()+0.5));
            }

            //画轨道
            for (int j = 0; j < stars.get(i).getStarPath().size(); j++) {
                g.setColor(Color.BLACK);
                g.fillOval((int) (stars.get(i).getStarPath().get(j).getX()+0.5),
                        (int) (stars.get(i).getStarPath().get(j).getY()+0.5),
                        1,
                        1);
            }
        }
    }

    public void run() {//实现Runable接口里run方法,当线程启动的时候会自动调用run方法。
        init();

        while (true) {
            try {
                Thread.sleep(frame);
            }
            catch (Exception e) {
            }
            repaint();//再次调用paint方法
        }
    }
}

public class Main
{
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JFrame w = new JFrame();//生成窗口容器对象
        MyPanel pw = new MyPanel();//创建扩展画布对象，这时画布已经有小球
        Thread thread = new Thread(pw);
        thread.start();
        w.add(pw);//把画布加到窗口容器上
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        w.setSize(1500,800);//设置窗口大小
        w.setLocationRelativeTo(null);
        w.setVisible(true);//上面的窗口是在内存中，并未显示
    }
}
