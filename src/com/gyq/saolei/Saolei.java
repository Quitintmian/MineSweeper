package com.gyq.saolei;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Saolei implements ActionListener {
    JFrame frame = new JFrame("MineSweeper");//����GUI����
    ImageIcon bannerIcon = new ImageIcon("smile.png");//�½�ͼ�����
    JButton bannerBtn = new JButton(bannerIcon);//������ͼ����Ϊ��ť
    ImageIcon bombIcon = new ImageIcon("bomb.png");
    ImageIcon winIcon = new ImageIcon("cool.png");
    ImageIcon failIcon = new ImageIcon("sad.png");
    ImageIcon buttonIcon = new ImageIcon("button.png");
    ImageIcon win_flagIcon = new ImageIcon("win_flag.png");

    //���ݽṹ
    int ROW = 20;//20
    int COL = 20;//20
    int[][] data = new int[ROW][COL];
    JButton[][] btns = new JButton[ROW][COL];
    ImageIcon[] cter = new ImageIcon[8];
    float seconds = 0;//ʱ�Ӽ���
    int LEICOUNT = 30;//�׵ĸ���
    int LEICODE = -1;//��ʾ��
    int unopenned = ROW * COL;
    int openned = 0;

    Timer timer = new Timer(10,this);//1s ����һ��actionPerformed()����

    //��ʼ���
    JLabel label1 = new JLabel("                        ������"+ unopenned);
    JLabel label2 = new JLabel("                        �ѿ���"+ openned);
    JLabel label3 = new JLabel("                        ��ʱ��"+ seconds + "s");


    public Saolei(){
        frame.setBounds(300,200,650,770);//���ڳߴ�(650*750)
        frame.setResizable(true);//���ڴ�С���ɸı�
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//��x�ɹر�
        frame.setLayout(new BorderLayout());//��������

        setHeader();
        addMine();
        setButtons();

        frame.setVisible(true);//�ɿ���
    }

    private void setHeader(){
        JPanel panel = new JPanel(new GridBagLayout());//��������
        //��������ť��3����ǩ���뻭����
        GridBagConstraints c1 = new GridBagConstraints(0,0,3,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(bannerBtn, c1);//����ťbannerBtn���뵽����c1�Ļ���panel��

        bannerBtn.addActionListener(this);

        //�趨��ǩ�Ͱ�ť����ʾ����
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

        frame.add(panel,BorderLayout.NORTH);//��Ӱ�ťλ���趨��NORTH�������Ϸ�
    }

    private void addMine(){
        Random rand = new Random();
        int i = 0;
        //��û�׵�λ���������
        while (i < LEICOUNT){
            int r = rand.nextInt(ROW); //0~ROW-1
            int c = rand.nextInt(COL); //0~COL-1
            if (data[r][c] != LEICODE){//��ǰλ��û����
                data[r][c] = LEICODE;
                i++;
            }
        }

        //�����ܱߵ��׵�����
        for ( i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (data[i][j] == LEICODE) continue;
                int tempCount = 0;
                //ͳ�Ƶ�ǰ(�����׵�)������Χ�˸����ӵ��׵�����
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
                btn.addActionListener(this);//��ť��������¼�
                //btn.setMargin(new Insets(0,0,0,0));
                con.add(btn);
                btns[i][j] = btn;//���밴ť������
            }
        }

        for (int i = 0; i < 8; i++) {
            cter[i] = new ImageIcon(String.valueOf(i+1) + ".png");
        }

        frame.add(con,BorderLayout.CENTER);//������ӵ�����
    }

    private void openCell(int i,int j){
        JButton btn = btns[i][j];

        //�ݹ����
        if (!btn.isEnabled()) return;

        //����
        btn.setIcon(null);//ɾ����ǰ��ťͼ��
        btn.setEnabled(false);//ʹ��ť������
        if (data[i][j] > 0){
            btn.setIcon(cter[data[i][j]-1]);
            btn.setDisabledIcon(cter[data[i][j]-1]);
        }
        addOpenCount();
        //�ݹ���
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
        label1.setText("                        ������" + unopenned);
        label2.setText("                        �ѿ���" + openned);
    }

    @Override
    //�¼�����ʱ�Ĳ���,�����������Ҫ
    public void actionPerformed(ActionEvent e) {
        //1.�����ť����
        if (e.getSource() instanceof JButton){
            JButton btn = (JButton)e.getSource();
            //1.1.���banner����
            if (btn.equals(bannerBtn)){
                restart(); //���¿�ʼ��Ϸ
                return;
            }else{
            //1.2.������Ӵ���
                if (openned == 0) timer.start();
                for (int i = 0; i < ROW; i++) {
                    for (int j = 0; j < COL; j++) {
                        if (btn.equals(btns[i][j])){ //�ҵ��������İ�ť
                            if (data[i][j] == LEICODE){
                                //�ȵ��ף���Ϸʧ��
                                //��������ױ�����Ϊ��ɫ
                                btn.setBackground(Color.red);
                                lose(i,j);
                            } else {
                                //�������ף���
                                openCell(i,j);
                                checkWin();
                            }
                            return;
                        }
                    }
                }
            }
        //2.ʱ�Ӵ���
        }else if (e.getSource() instanceof Timer){
            seconds = (float) (seconds + 0.01);
            label3.setText("                        ��ʱ��" + String.format("%.2f",seconds) +"s");
            timer.start();//�ٴ�����ʱ�Ӽ�ʱ��
            return;
        }
    }

    /**
     * 1.�������㡣2.��ť�ָ�״̬��3.��������ʱ��
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
        label1.setText("                        ������"+ unopenned);
        label2.setText("                        �ѿ���"+ openned);
        label3.setText("                        ��ʱ��"+ seconds + "s");

        addMine();

    }

    private void checkWin() {
        int count = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btns[i][j].isEnabled()) count++; //ͳ�Ƶ�ǰ��ťδ�򿪵İ�ť��
            }
        }
        //ֻʣ��û�д�,��ͼ����Ϊ����ͼ��,��Ϸ�ɹ�
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
                if (btn.isEnabled()){ //ֻ����δ�򿪵�
                    if (data[i][j]==LEICODE){//��ʾ������
                        btn.setEnabled(false);
                        btn.setIcon(bombIcon);
                        btn.setDisabledIcon(bombIcon);
                    }else{
                        //��ʾ��������
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
        //JOptionPane.showMessageDialog(frame,"��ʧ����!\n���\"�㱬����\"���¿�ʼ��");



    }

    public static void main(String[] args) {
        new Saolei();
    }

}
