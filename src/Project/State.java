package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class State extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new BorderLayout()), setb(Color.white), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p3 = get(new JPanel(new GridLayout(25, 8, 5, 5)));
	JPanel p4 = get(new JPanel(new GridLayout(1, 7, 10, 0)));
	JPanel p5 = get(new JPanel(new BorderLayout()));
	JPanel p6;
	JPanel p7 = get(new JPanel(new GridLayout(8, 1)));
	
	JLabel lab1 = get(new JLabel("자주 이용하는 시간대"), setp(15));
	JLabel lab2 = get(new JLabel("시간 별 이용자 수"), setp(12));
	JLabel lab3 = get(new JLabel("자주 이용하는 회원"), setp(15));
	
	JLabel time[][] = new JLabel[24][7];
	String h[] = "12오전,,2 오전,,4 오전,,6 오전,,8 오전,,10 오전,,12 오후,,2 오후,,4 오후,,6 오후,,8 오후,,10오후, ".split(",");
	String v[] = "일, 월, 화, 수, 목, 금, 토".split(", ");
	Color color[] = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.pink, Color.black, Color.cyan};
	Color blue[] = new Color[7];
	int count[] = new int[8];
	
	int sum;
	
	JComboBox com1 = new JComboBox("최근 7일, 최근 30일, 최근 90일".split(", "));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public State() {
		
		setTitle("서울메트로 - 통계");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		design();
		action();
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		
		com1.setSelectedIndex(0);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Admin();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1);
		p1.add(lab1, "North");
		p1.add(p2);
		
		p2.add(lab2, "North");
		p2.add(p3);
		
		for (int i = 0; i < time.length; i++) {
			for (int j = 0; j < time[i].length; j++) {
				p3.add(time[i][j] = get(new JLabel("", 0), setf(Color.red), setb(Color.LIGHT_GRAY), set(60, 25)));
				time[i][j].setOpaque(true);
				
				if (j == 6) {
					p3.add(new JLabel(h[i]));
				}
				
			}
		}
		
		for (int i = 0; i < v.length; i++) {
			p3.add(new JLabel(v[i], 0));
		}
		
		p3.setOpaque(false);
		p2.add(p3 = new JPanel(new BorderLayout()), "South");
		p3.setOpaque(false);
		p3.add(p4);
		p4.setOpaque(false);
		
		for (int i = 0; i < blue.length; i++) {
			blue[i] = new Color(187 - (i *30), 187 - (i * 30), 255);
			JLabel lab = get(new JLabel(i * 5 + "+"), setb(blue[i]), setf(Color.red));
			lab.setOpaque(true);
			p4.add(lab);
		}
		p3.add(com1, "South");
		
		Query("select u.name, count(*) from purchase pu, user u where u.serial = pu.user group by u.serial order by count(*) desc limit 8;", list);
		sum = 0;
		int a = list.size() - 1;
		count = new int[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			
			sum += intnum(list.get(i).get(1));
			count[a] = intnum(list.get(i).get(1));
			a--;
			
		}
		
		add(p5, "East");
		p5.add(p6 = get(new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				
				int c= 0;
				for (int i = 0; i < count.length; i++) {
					
					int sarc = (c * 360) / sum + 90;
					int arc = (count[i] * 360) / sum + 1;
					
					g.setColor(color[count.length - i - 1]);
					g.fillArc(60, 20, 500, 500, sarc, arc);
					c+= count[i];
					
				}
				
			}
		}, set(600, 550)));
		p5.add(p7, "South");
		
		for (int i = 0; i < list.size(); i++) {
			
			JPanel pn1 = get(new JPanel());
			JLabel lb1 = get(new JLabel("■"), setf(color[i]), setb(15));
			JLabel lb2= get(new JLabel((i + 1) + "등 " + list.get(i).get(0) + " (" + list.get(i).get(1) + "회)"), setp(15));
			
			pn1.add(lb1);
			pn1.add(lb2);
			
			p7.add(pn1);
			
		}
		
	}

	@Override
	public void action() {
		
		com1.addActionListener(e->{
			
			for (int i = 0; i < time.length; i++) {
				for (int j = 0; j < time[i].length; j++) {
					time[i][j].setBackground(Color.LIGHT_GRAY);
					time[i][j].setText("");
				}
			}
			
			Query("select hour(time), if(weekday(date) = 6, 0, weekday(date) + 1), count(*) from purchase where date <= now() and date >= date_add(now(), interval - " + com1.getSelectedItem().toString().replaceAll("[^0-9]", "") + " day) group by time order by date;", temp);
			
			for (int i = 0; i < temp.size(); i++) {
				
				int index = intnum(temp.get(i).get(2)) / 5 > 5 ? 5 : intnum(temp.get(i).get(2)) / 5;
				
				time[intnum(temp.get(i).get(0))][intnum(temp.get(i).get(1))].setText(temp.get(i).get(2));
				time[intnum(temp.get(i).get(0))][intnum(temp.get(i).get(1))].setBackground(blue[index]);
				
			}
			
			revalidate();
			repaint();
			
		});
		
	}

}
