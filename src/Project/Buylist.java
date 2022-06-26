package Project;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import Base.Base;

public class Buylist extends JFrame implements Base{

	JPanel p1 = get(new JPanel());
	JPanel p2 = get(new JPanel(new GridLayout(0, 7)));
	
	JButton btn1 = get(new JButton("◀"));
	JButton btn2 = get(new JButton("▶"));
	
	JLabel lab1 = get(new JLabel(""));
	JLabel lab;
	
	LocalDate now = LocalDate.now();
	LocalDate date;
	
	String st[] = "일, 월, 화, 수, 목, 금, 토".split(", ");
	
	public Buylist(int n) {
		
		SetFrame(this, "결제내역", DISPOSE_ON_CLOSE, 1000, 750);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				if (n == 0) {
					new MyPage();
				}else {
					member.clear();
					new UserDB();
				}
				
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1, "North");
		add(p2);
		
		p1.add(btn1);
		p1.add(lab1);
		p1.add(btn2);
		
		now = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
		
		cal();
		
	}

	public void cal() {
		
		p2.removeAll();
		
		lab1.setText(now.format(DateTimeFormatter.ofPattern("yyyy년 MM월")));
		
		for (int i = 0; i < st.length; i++) {
			p2.add(lab = get(new JLabel(st[i], 0), setf(i == 0 ? Color.red : i == 6 ? Color.blue : Color.black)));
		}
		
		int val = now.withDayOfMonth(1).getDayOfWeek().getValue();
		
		date = now.plusDays(-val);
		
		for (int i = 0; i < 42; i++) {
			p2.add(new CalPnael(now, date.plusDays(i)));
		}
		
		repaint();
		revalidate();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			now = now.plusMonths(-1);
			cal();
		});
		
		btn2.addActionListener(e->{
			now = now.plusMonths(1);
			cal();
		});
		
	}

}
