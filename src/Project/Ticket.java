package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Ticket extends JFrame implements Base{
	
	JLabel lab1 = get(new JLabel("열차 승차권 - TRAIN TICKET", 0), setf(Color.white), setb(40), set(0, 100), setb(blue));
	JLabel lab2 = get(new JLabel(""), setp(15));
	JLabel lab3 = get(new JLabel("", 0), setb(30));
	JLabel lab4 = get(new JLabel("", JLabel.RIGHT), setp(15));
	
	JPanel p1 = get(new JPanel(new GridLayout(1, 3)), set(new EmptyBorder(20, 20, 20, 20)));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	String serial = "";
	
	public Ticket(String serial) {
		
		this.serial = serial;
		
		SetFrame(this, "티켓", DISPOSE_ON_CLOSE, 1000, 450);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Paynum();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("select concat(s1.name, ' ▶ ', s2.name), distance * 0.1, date, time, format(price, 0) from purchase pu, station s1, station s2 where pu.departure = s1.serial and pu.departure = s2.serial and pu.serial = ?;", list, serial);
		
		add(lab1, "North");
		add(p1);
		
		p1.add(lab2);
		p1.add(lab3);
		p1.add(lab4);
		
		lab2.setText("<html>" + list.get(0).get(1) + "km <br>" + LocalDate.parse(list.get(0).get(2)).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) + "<br>" + LocalTime.parse(list.get(0).get(3)).format(DateTimeFormatter.ofPattern("HH시 mm분 ss초")));
		lab3.setText(list.get(0).get(0));
		lab4.setText(list.get(0).get(4) + "원");
		
		lab3.setVerticalAlignment(JLabel.TOP);
		lab1.setOpaque(true);
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
