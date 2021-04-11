package com.gyq.saolei;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Saolei implements ActionListener {
    JFrame frame = new JFrame("MineSweeper");//建立GUI对象
    ImageIcon bannerIcon = new ImageIcon("smile.png");//新建图标对象
    JButton bannerBtn = new JButton(bannerIcon);//创建以图标作为按钮
    ImageIcon bombIcon = new ImageIcon("bomb.png");
    ImageIcon winIcon = new ImageIcon("cool.png");
    ImageIcon failIcon = new ImageIcon("sad.png");
    ImageIcon buttonIcon = new ImageIcon("button.png");
    ImageIcon win_flagIcon = new ImageIcon("win_flag.png");

    //数据结构
    int ROW = 20;//20
    int COL = 20;//20
    int[][] data = new int[ROW][COL];
    JButton[][] btns = new JButton[ROW][COL];
    ImageIcon[] cter = new ImageIcon[8];
    float seconds = 0;//时钟计数
    int LEICOUNT = 30;//雷的个数
    int LEICODE = -1;//表示雷
    int unopenned = ROW * COL;
    int openned = 0;

    Timer timer = new Timer(10,this);//1s 触发一次actionPerformed()方法

    //初始输出
    JLabel label1 = new JLabel("                        待开："+ unopenned);
    JLabel label2 = new JLabel("                        已开："+ openned);
    JLabel label3 = new JLabel("                        用时："+ seconds + "s");


    public Saolei(){
        frame.setBounds(300,200,650,770);//窗口尺寸(650*750)
        frame.setResizable(true);//窗口大小不可改变
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//按x可关闭
        frame.setLayout(new BorderLayout());//建立布局

        setHeader();
        addMine();
        setButtons();

        frame.setVisible(true);//可看见
    }

    private void setHeader(){
        JPanel panel = new JPanel(new GridBagLayout());//建立画布
        //将顶部按钮和3个标签加入画布中
        GridBagConstraints c1 = new GridBagConstraints(0,0,3,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(bannerBtn, c1);//将按钮bannerBtn加入到布局c1的画布panel上

        bannerBtn.addActionListener(this);

        //设定标签和按钮的显示属性
        label1.setOpaque(true);
        label1.setBackground(Color.white);
        label1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label2.setOpaque(true);
        label2.setBackground(Color.white);
        label2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label3.setOpaque(true);
        label3.setBackground(Color.white);
        label3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        GridBagConstraints c2 = new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(label1, c2);
        GridBagConstraints c3 = new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(label2, c3);
        GridBagConstraints c4 = new GridBagConstraints(2,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(label3, c4);

        frame.add(panel,BorderLayout.NORTH);//添加按钮位置设定在NORTH方，即上方
    }

    private void addMine(){
        Random rand = new Random();
        int i = 0;
        //对没雷的位置随机放雷
        while (i < LEICOUNT){
            int r = rand.nextInt(ROW); //0~ROW-1
            int c = rand.nextInt(COL); //0~COL-1
            if (data[r][c] != LEICODE){//当前位置没有雷
                data[r][c] = LEICODE;
                i++;
            }
        }

        //计算周边的雷的数量
        for ( i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (data[i][j] == LEICODE) continue;
                int tempCount = 0;
                //统计当前(不是雷的)格子周围八个格子的雷的总数
                if(i>0 && j>0 && data[i-1][j-1]==LEICODE) tempCount++;
                if(i>0 && data[i-1][j]==LEICODE) tempCount++;
                if(i>0 && j<COL-1 && data[i-1][j+1]==LEICODE) tempCount++;
                if(j>0 && data[i][j-1]==LEICODE) tempCount++;
                if(j<COL-1 && data[i][j+1]==LEICODE) tempCount++;
                if(i<ROW-1 && j>0 && data[i+1][j-1]==LEICODE) tempCount++;
                if(i<ROW-1 && data[i+1][j]==LEICODE) tempCount++;
                if(i<ROW-1 && j<COL-1 && data[i+1][j+1]==LEICODE) tempCount++;
                data[i][j]=tempCount;
            }
        }
    }

    private void setButtons(){
        Container con = new Container();
        con.setLayout(new GridLayout(ROW,COL));

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                JButton btn = new JButton(buttonIcon);
                btn.addActionListener(this);//按钮加入监听事件
                //btn.setMargin(new Insets(0,0,0,0));
                con.add(btn);
                btns[i][j] = btn;//加入按钮数组中
            }
        }

        for (int i = 0; i < 8; i++) {
            cter[i] = new ImageIcon(String.valueOf(i+1) + ".png");
        }

        frame.add(con,BorderLayout.CENTER);//容器添加到中央
    }

    private void openCell(int i,int j){
        JButton btn = btns[i][j];

        //递归出口
        if (!btn.isEnabled()) return;

        //操作
        btn.setIcon(null);//删除当前按钮图标
        btn.setEnabled(false);//使按钮不可用
        if (data[i][j] > 0){
            btn.setIcon(cter[data[i][j]-1]);
            btn.setDisabledIcon(cter[data[i][j]-1]);
        }
        addOpenCount();
        //递归体
        if (data[i][j] == 0){
            if (i>0 && j>0) openCell(i-1,j-1);
            if (i>0) openCell(i-1,j);
            if (i>0 && j<COL-1) openCell(i-1,j+1);
            if (j>0 ) openCell(i,j-1);
            if (j<COL-1) openCell(i,j+1);
            if (i<ROW-1 &&j>0 ) openCell(i+1,j-1);
            if (i<ROW-1 ) openCell(i+1,j);
            if (i<ROW-1 &&j<COL-1) openCell(i+1,j+1);
        }
    }

    private void addOpenCount(){
        openned++;
        unopenned--;
        label1.setText("                        待开：" + unopenned);
        label2.setText("                        已开：" + openned);
    }

    @Override
    //事件发生时的操作,这个方法很重要
    public void actionPerformed(ActionEvent e) {
        //1.点击按钮触发
        if (e.getSource() instanceof JButton){
            JButton btn = (JButton)e.getSource();
            //1.1.点击banner触发
            if (btn.equals(bannerBtn)){
                restart(); //重新开始游戏
                return;
            }else{
            //1.2.点击格子触发
                if (openned == 0) timer.start();
                for (int i = 0; i < ROW; i++) {
                    for (int j = 0; j < COL; j++) {
                        if (btn.equals(btns[i][j])){ //找到被触发的按钮
                            if (data[i][j] == LEICODE){
                                //踩到雷，游戏失败
                                //被点击的雷背景设为红色
                                btn.setBackground(Color.red);
                                lose(i,j);
                            } else {
                                //若不是雷，打开
                                openCell(i,j);
                                checkWin();
                            }
                            return;
                        }
                    }
                }
            }
        //2.时钟触发
        }else if (e.getSource() instanceof Timer){
            seconds = (float) (seconds + 0.01);
            label3.setText("                        用时：" + String.format("%.2f",seconds) +"s");
            timer.start();//再次启动时钟计时器
            return;
        }
    }

    /**
     * 1.数据清零。2.按钮恢复状态。3.重新启动时钟
     */
    private void restart() {

        bannerBtn.setIcon(bannerIcon);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                data[i][j] = 0;
                btns[i][j].setIcon(buttonIcon);
                btns[i][j].setBackground(null);
                btns[i][j].setEnabled(true);
            }
        }

        unopenned = ROW * COL;
        openned = 0;
        seconds = 0;
        label1.setText("                        待开："+ unopenned);
        label2.setText("                        已开："+ openned);
        label3.setText("                        用时："+ seconds + "s");

        addMine();

    }

    private void checkWin() {
        int count = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btns[i][j].isEnabled()) count++; //统计当前按钮未打开的按钮数
            }
        }
        //只剩雷没有打开,将图标设为旗帜图案,游戏成功
        if (count == LEICOUNT){
            timer.stop();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (btns[i][j].isEnabled()){
                        btns[i][j].setEnabled(false);
                        btns[i][j].setIcon(win_flagIcon);
                        btns[i][j].setDisabledIcon(win_flagIcon);
                    }
                }
            }
            bannerBtn.setIcon(winIcon);
        }
    }

    private void lose(int m,int n) {
        bannerBtn.setIcon(failIcon);
        timer.stop();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                JButton btn = btns[i][j];
                if (btn.isEnabled()){ //只处理未打开的
                    if (data[i][j]==LEICODE){//显示所有雷
                        btn.setEnabled(false);
                        btn.setIcon(bombIcon);
                        btn.setDisabledIcon(bombIcon);
                    }else{
                        //显示所有数字
                        if (data[i][j] > 0){
                            btn.setEnabled(false);
                            btn.setIcon(cter[data[i][j]-1]);
                            btn.setDisabledIcon(cter[data[i][j]-1]);
                        }else {
                            btn.setIcon(null);
                            btn.setEnabled(false);
                        }
                    }
                }
            }
        }
        //JOptionPane.showMessageDialog(frame,"你失败了!\n点击\"你爆雷了\"重新开始！");



    }

    public static void main(String[] args) {
        new Saolei();
    }

}
