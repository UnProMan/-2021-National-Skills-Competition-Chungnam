
package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Ticketing extends JFrame implements Base{

	JPanel p1 = get(new JPanel(),set(0, 80), set(new EmptyBorder(30, 0, 0, 0)));
	JPanel p2 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p3 = get(new JPanel(new FlowLayout(0)), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
	JPanel p4 = get(new JPanel(new BorderLayout()));
	JPanel p5 = get(new JPanel(new BorderLayout()));
	JPanel p6 = get(new JPanel(new FlowLayout(0, 5, 5)));
	
	JScrollPane scl = get(new JScrollPane(p4, 22, 31));
	
	JLabel lab1 = get(new JLabel("", 0), setf(blue), setp(20));
	JLabel lab2 = get(new JLabel(""), setp(15), setf(blue));
	JLabel lab3 = get(new JLabel("자세히보기"), setf(Color.red), setp(15), set(new EmptyBorder(0, 0, 0, 25)));
	
	JLabel date1, date2;
	
	JButton btn1 = get(new JButton("시간 변경"));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> list2 = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	ArrayList<String> timelist1 = new ArrayList<>();
	ArrayList<String> timelist2 = new ArrayList<>();
	
	ArrayList<Integer> data;
	int cost;
	LocalDateTime date;
	
	public Ticketing(ArrayList<Integer> data, int cost, LocalDateTime date) {

		this.data = data;
		this.cost = cost;
		this.date = date;
		
		SetFrame(this, "예매", DISPOSE_ON_CLOSE, 500, 600);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Map();
			}
		});
		
	}

	@Override
	public void design() {
		
		lab1.setText(station.get(data.get(0)).get(1) + " → " + station.get(data.get(data.size() - 1)).get(1));
		date1 = get(new JLabel(date.format(df1)));
		date2 = get(new JLabel(date.format(df2)));
		
		p1.add(lab1);
		
		add(p1, "North");
		add(p2);
		
		p2.add(p3, "North");
		p2.add(scl);
		
		p3.add(btn1);
		p3.add(date1);
		p3.add(date2);
		
		p4.add(p5, "North");
		p4.add(p6);
		
		p5.add(lab2);
		p5.add(lab3, "East");
		
		table();
		
	}
	
	public void table() {
		
		temp.clear();
		int cnt = 0;
		int wait[] = new int[10], last = 0, lcost = 0;
		
		for (int i = 0; i < data.size() - 1; i++) {
			
			ArrayList row = new ArrayList<>();
			
			Query("select * from metro m, route r1, route r2 where m.serial = r1.metro and m.serial = r2.metro and r1.station = ? and r2.station = ?;", list, station.get(data.get(i)).get(0), station.get(data.get(i + 1)).get(0));
			Query("select cost from path where departure = ? and arrive = ?;", list2, station.get(data.get(i)).get(0), station.get(data.get(i + 1)).get(0));
			
			last = intnum(list2.get(0).get(0)) * 5;
			lcost += intnum(list2.get(0).get(0)) * 5;
			
			int orient = 0;
			if (intnum(list.get(0).get(5)) < intnum(list.get(0).get(8))) {
				orient  =0;
			}else {
				orient = 1;
			}
			
			if (!list.isEmpty()) {
				if (temp.size() == 0 || !temp.get(temp.size() - 1).get(0).contentEquals(list.get(orient).get(0))) {
					
					if (i != 0) {
						lcost -= last;
					}
					
					row.add(list.get(orient).get(0));
					row.add(list.get(orient).get(2));
					row.add(list.get(orient).get(3));
					row.add(list.get(orient).get(4));
					row.add(station.get(data.get(i)).get(0));
					temp.add(row);
					
					Query("select * from route where metro = ?;", list, temp.get(cnt).get(0));
					
					int a = 0;
					while (!list.get(a).get(1).contentEquals(temp.get(cnt).get(4))) {
						try {
							Query("select cost from path where departure = ? and arrive = ?;", list2, list.get(a).get(1), list.get(a + 1).get(1));
							wait[cnt] += intnum(list2.get(0).get(0)) * 5;
						} catch (Exception e) {
						}
						a++;
					}
					
					LocalTime today = LocalTime.parse(date2.getText());
					LocalTime start = LocalTime.parse(temp.get(cnt).get(1));
					LocalTime end = LocalTime.parse(temp.get(cnt).get(2));
					int interval = LocalTime.parse(temp.get(cnt).get(3)).getMinute();
					
					if (today.isAfter(end)) {
						return;
					}
					
					int c = 0;
					while (start.isBefore(today)) {
						start = LocalTime.parse(temp.get(cnt).get(1));
						start = start.plusMinutes(interval * c);
						start = start.plusSeconds(wait[cnt]);
						c++;
					}
					
					if (cnt == 0) {
						while (start.isBefore(end)) {
							timelist1.add(start + "");
							start = start.plusMinutes(interval);
						}
					}else {
						
						LocalTime start1 = LocalTime.of(0, 0, 0), start2 = LocalTime.of(0, 0, 0), times;
						
						start1 = LocalTime.parse(timelist1.get(0));
						start1 = start1.plusSeconds(lcost);
						start2 = start;
						
						while (start1.isBefore(end)) {
							times = LocalTime.of(0, 0, 0);
							while (start2.isBefore(start1)) {
								start2 = start2.plusMinutes(interval);
							}
							
							times = times.plusSeconds(start2.toSecondOfDay() - start1.toSecondOfDay());
							timelist2.add(times + "");
							start1 = start1.plusMinutes(LocalTime.parse(temp.get(0).get(3)).getMinute());
							
						}
						
					}
					
					cnt++;
					
				}
			}
			
		}
		
		lab2.setText(data.size() + "개역 정차 / " + (temp.size() - 1) + "회 환승");
		
		try {
			for (int i = 0; i < timelist1.size(); i++) {
				p6.add(new TIcketPanel(this, timelist1.get(i), timelist2.size() == 0 ? "" : timelist2.get(i), cost, data, date));
			}
		} catch (Exception e) {
		}
		
		p4.setPreferredSize(new Dimension(480, timelist1.size() * 80 + 85));
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		lab3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new WayPoint(data);
			}
		});
		
		btn1.addActionListener(e->{
			dispose();
			new TimeSelect(data, cost, date);
		});
		
	}

}
