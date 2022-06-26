package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class TIcketPanel extends JPanel implements Base{

	JPanel p1 = get(new JPanel(new GridLayout(1, 3)));
	JPanel p2 = get(new JPanel(new GridLayout(2, 1)), setb(Color.white), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1)), setb(Color.white));
	JPanel p4 = get(new JPanel(new GridLayout(2, 1)), setb(Color.white), set(new EmptyBorder(10, 10, 10, 10)));
	
	JButton btn1 = get(new JButton("선택"));
	
	JLabel lab1 = get(new JLabel(""), setp(20));
	JLabel lab2 = get(new JLabel("", JLabel.RIGHT), setp(20));
	JLabel lab3 = get(new JLabel("", 0), setf(blue));
	JLabel lab;
	
	JLabel dep = get(new JLabel(""), setf(Color.gray));
	JLabel arr = get(new JLabel("",JLabel.RIGHT), setf(Color.gray));
	
	JFrame f;
	String timelist1, timelist2;
	int cost;
	ArrayList<Integer> data;
	LocalDateTime now;
	
	LocalTime start, end, time;
	
	DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
	
	public TIcketPanel(JFrame f, String timelist1, String timelist2, int cost, ArrayList<Integer> data, LocalDateTime now) {
		
		this.f = f;
		this.timelist1 = timelist1;
		this.timelist2 = timelist2;
		this.cost = cost;
		this.data = data;
		this.now = now;
		
		setLayout(new BorderLayout(5, 0));
		setPreferredSize(new Dimension(430, 80));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		time = LocalTime.of(0, 0, 0).plusSeconds(cost * 5);
		
		if (!timelist2.isBlank()) {
			time = LocalTime.parse(timelist2).plusSeconds(cost * 5);
		}
		
		start = LocalTime.parse(timelist1);
		end = start.plusSeconds(time.getSecond()).plusMinutes(time.getMinute());
		
		lab1.setText(start.format(df));
		lab2.setText(end.format(df));
		lab3.setText(time.format(DateTimeFormatter.ofPattern("m분 s초 소요")));
		
		dep.setText(station.get(data.get(0)).get(1));
		arr.setText(station.get(data.get(data.size() - 1)).get(1));
		
		add(p1);
		add(btn1, "East");
		
		p1.add(p2);
		p1.add(p3);
		p1.add(p4);
		
		p2.add(lab1);
		p2.add(dep);
		
		p4.add(lab2);
		p4.add(arr);
		
		p3.add(lab = new JLabel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(Color.black);
				g.drawLine(0, 20, 1000, 20);
				
			}
		});
		p3.add(lab3);
		
	}

	@Override
	public void action() {

		btn1.addActionListener(e->{
			
			if (member.isEmpty()) {
				err("로그인 후 예매 가능합니다.");
			}else {
				
				int a = JOptionPane.showConfirmDialog(null, lab1.getText() + "시간 지하철을 예매할까요?", "메시지" ,JOptionPane.YES_NO_OPTION);
				
				if (a == 0) {
					f.dispose();
					new Pay(data, cost, now);
				}
				
			}
			
		});
		
	}

}
