package Project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Station extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(), set(0, 40));
	JPanel p2 = get(new JPanel(new BorderLayout(0, 5)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.CENTER)));
	
	JScrollPane scl = get(new JScrollPane(p3, 22, 31));
	
	JComboBox com1 = get(new JComboBox());
	
	JLabel lab1 = get(new JLabel(""), setp(15));
	JLabel lab2 = get(new JLabel(""), setf(blue), setb(15));
	JLabel lab3 = get(new JLabel(""), setp(15));
	JLabel lab4 = get(new JLabel("열차 시간표"), setb(20));
	
	ArrayList<ArrayList<String>> metro = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	ArrayList<LocalTime> time = new ArrayList<>();
	
	int st;
	int index;
	
	public Station(int n) {
		
		st = n + 1;
		
		SetFrame(this, "역정보", DISPOSE_ON_CLOSE, 500, 600);
		design();
		action();
		setVisible(true);
		
		com1.setSelectedIndex(0);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Map();
			}
		});
		
	}

	@Override
	public void design() {
		
		Route.rt = new Route();
		
		add(p1, "North");
		add(p2);
		
		p2.add(com1, "North");
		p2.add(scl);
		
		p1.add(lab1);
		p1.add(lab2);
		p1.add(lab3);
		
		com();
		
	}	
	
	public void com() {
		
		com1.removeAllItems();
		Query("select * from metro m, route r where m.serial = r.metro and r.station = ?", metro, st + "");
		
		for (ArrayList<String> list : metro) {
			com1.addItem(list.get(1));
		}
		
		com1.setSelectedIndex(0);
		
	}

	public void table() {
		
		if (com1.getSelectedIndex() == -1) {
			return;
		}
		
		Query("select * from station s, route r where s.serial = r.station and r.metro = ?;", list, metro.get(com1.getSelectedIndex()).get(0));
		
		String now = "", before = "", next = "";
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get(0).contentEquals(st + "")) {
				index = i;
			}
		}
		
		Route.rt.start(intnum(list.get(0).get(0)) -1, intnum(list.get(list.size() - 1).get(0)) - 1);
		
		now = list.get(index).get(1);
		before = index == 0 ? "기점 ←" : station.get(index - 1).get(1) + " ←";
		next = index == list.size() - 1 ? "→ 종점" : "→ " + station.get(index + 1).get(1);
		
		lab1.setText(before);
		lab2.setText(now);
		lab3.setText(next);
		
		times();
		
	}
	
	public void times() {
		
		p3.removeAll();
		time.clear();
		
		p3.add(lab4);
		
		int row = com1.getSelectedIndex();
		
		LocalTime start = LocalTime.parse(metro.get(row).get(2));
		LocalTime end = LocalTime.parse(metro.get(row).get(3));
		
		while (start.isBefore(end)) {
			time.add(start);
			start = start.plusMinutes(LocalTime.parse(metro.get(row).get(4)).getMinute());
		}
		
		int h = time.get(0).getHour();
		int r = end.getHour() - h;
		int height = 0;
		
		int cost = 0;
		for (int i = 0; i < list.size(); i++) {
			cost += Route.rt.maps[intnum(list.get(i == 0 ? 0 : i -1).get(0)) - 1][intnum(list.get(i).get(0)) - 1];
			if (list.get(i).get(0).contentEquals(list.get(index).get(0))) {
				break;
			}
		}
		
		for (int i = 0; i <= r; i++) {
			
			JPanel pn1 = get(new JPanel(new BorderLayout()));
			JPanel pn2 = get(new JPanel(new GridLayout(0, 1)));
			JLabel lb = get(new JLabel(h + "시", 0), set(BorderFactory.createMatteBorder(0, 0, 0, 1, blue)), set(50, 0), setb(15));
			
			pn1.add(lb, "West");
			pn1.add(pn2);
			
			int n = 0;
			for (int j = 0; j < time.size(); j++) {
				if (time.get(j).getHour() == h) {
					n++;
					pn2.add(new JLabel(time.get(j).plusSeconds(cost * 5).format(df2), 0));
				}
			}
			
			h++;
			
			pn1.setPreferredSize(new Dimension(450, n * 20));
			height += n * 20;
			
			allfont(pn2);
			p3.add(pn1);
			
		}
		
		p3.setPreferredSize(new Dimension(0, height + 150));
		
	}
	
	@Override
	public void action() {
		
		lab1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (lab1.getText().contentEquals("기점 ←")) {
					err("해당 노선의 마지막 위치입니다.");
				}else {
					
					index--;
					st = intnum(list.get(index).get(0));
					com();
					
				}
				
			}
		});
		
		lab3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (lab1.getText().contentEquals("→ 종점")) {
					err("해당 노선의 마지막 위치입니다.");
				}else {
					
					index++;
					st = intnum(list.get(index).get(0));
					com();
					
				}
				
			}
		});
		
		com1.addActionListener(e->{
			table();
		});
		
	}

}
