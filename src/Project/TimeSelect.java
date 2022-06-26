package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class TimeSelect extends JDialog implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(1, 2, 10, 0)));
	JPanel p3 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(40, 30, 40, 30)));
	JPanel p4 = get(new JPanel(new GridLayout(1, 2)), set(new EmptyBorder(0, 10, 0, 10)));
	JPanel p5 = get(new JPanel(new GridLayout(1, 3)));
	JPanel p6 = get(new JPanel(new GridLayout(1, 3)), set(new EmptyBorder(0, 10, 0, 10)));
	
	JPanel pn1 = get(new JPanel(new BorderLayout()));
	JPanel pn2 = get(new JPanel(new GridLayout(1, 3)));
	JPanel pn3 = get(new JPanel(new GridLayout(0, 7)));
	
	JLabel lab1 = get(new JLabel("탑승하실 날짜와 시간을 선택해주세요."), set(BorderFactory.createMatteBorder(0, 0, 1, 0, blue)), setp(20));
	JLabel hup = get(new JLabel("▲", 0), setb(20), setf(Color.gray));
	JLabel hdown = get(new JLabel("▼", 0), setb(20), setf(Color.gray));
	JLabel mup = get(new JLabel("▲", 0), setb(20), setf(Color.gray));
	JLabel mdown = get(new JLabel("▼", 0), setb(20), setf(Color.gray));
	
	JLabel before = get(new JLabel("<"));
	JLabel next = get(new JLabel(">", JLabel.RIGHT));
	JLabel today = get(new JLabel("", 0));
	
	JLabel h = get(new JLabel("", JLabel.RIGHT), setp(13));
	JLabel m = get(new JLabel(""), setp(13));
	JLabel lab2 = get(new JLabel(":", 0), setp(13));
	
	JButton btn1 = get(new JButton("해당 시간으로 변경하기"));
	String st[] = "Sun, Mon, Tue, Wed, Thu, Fri, Sat".split(", ");
	JLabel lab;
	
	ArrayList<Integer> data;
	int cost;
	LocalDateTime now;
	
	int select = 0;
	
	public TimeSelect(ArrayList<Integer> data, int cost, LocalDateTime now) {
		
		this.data = data;
		this.cost = cost;
		this.now = now;
		
		SetDial(this, "서울메트로 - 시간 선낵", DISPOSE_ON_CLOSE, 600, 500);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Ticketing(data, cost, now);
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(p2);
		p1.add(btn1, "South");
		
		p2.add(p3);
		p2.add(pn1);
		
		p3.add(p4, "North");
		p3.add(p5);
		p3.add(p6, "South");
		
		p4.add(hup);
		p4.add(mup);
		
		p5.add(h);
		p5.add(lab2);
		p5.add(m);
		
		p6.add(hdown);
		p6.add(mdown);
		
		pn1.add(pn2, "North");
		pn1.add(pn3);
		
		pn2.add(before);
		pn2.add(today);
		pn2.add(next);
		
		cal();
		
	}
	
	public void cal() {
		
		pn3.removeAll();
		
		today.setText(now.format(DateTimeFormatter.ofPattern("MM월 yyyy")));
		h.setText(now.getHour() + "");
		m.setText(now.getMinute() + "");
		
		for (int i = 0; i < st.length; i++) {
			pn3.add(lab = get(new JLabel(st[i], 0), setf(blue)));
		}
		
		LocalDate date = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		
		int val = date.withDayOfMonth(1).getDayOfWeek().getValue();
		int start = val == 7 ? 0 : val;
		int fin = date.lengthOfMonth();
		
		for (int i = 1 - start; i < 42 - start; i++) {
			
			if (i < 1 || i > fin) {
				pn3.add(new JLabel());
			}else {
				
				JLabel lb = get(new JLabel(i + "", 0), setb(i == select ? Color.orange : Color.white));
				lb.setOpaque(true);
				
				if (select == 0 && LocalDate.now().isEqual(LocalDate.of(now.getYear(), now.getMonth(), i))) {
					lb.setBackground(Color.orange);
					select = i;
				}
				
				int j = i;
				
				lb.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {

						for (Component comp : pn3.getComponents()) {
							comp.setBackground(Color.white);
						}
						
						lb.setBackground(Color.orange);
						now = LocalDateTime.of(now.getYear(), now.getMonthValue(), j, now.getHour(), now.getMinute(), now.getSecond());
						
						select = j;
						
					}
				});
				
				pn3.add(lb);
				
			}
			
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {

		btn1.addActionListener(e->{
			
			LocalDate d = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
			LocalTime t = LocalTime.of(now.getHour(), now.getMinute(), now.getSecond());
			
			if (d.isBefore(LocalDate.now())) {
				err("이미 지난 날짜는 선택할 수 없습니다.");
			}else if (t.isBefore(LocalTime.now().plusMinutes(30))) {
				err("이미 지난 시간은 선택할 수 없습니다.");
			}else {
				dispose();
				new Ticketing(data, cost, now);
			}
			
		});
		
		hup.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (now.getHour() != 23) {
					
					now = now.plusHours(1);
					cal();
					
				}
				
			}
		});
		
		hdown.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (now.getHour() != 1) {
					
					now = now.plusHours(-1);
					cal();
					
				}
				
			}
		});
		
		mup.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (now.getMinute() != 59) {
					
					now = now.plusMinutes(1);
					cal();
					
				}
				
			}
		});
		
		mdown.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (now.getMinute() != 1) {
					
					now = now.plusMinutes(-1);
					cal();
					
				}
				
			}
		});
		
		before.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				now = now.plusMonths(-1);
				cal();
			}
		});
		
		next.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				now = now.plusMonths(1);
				cal();
			}
		});
		
	}

}
