package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class TrainTime extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new GridLayout(14, 1, 0, 5)), set(new EmptyBorder(5, 5, 5, 5)), set(300, 0));
	JPanel p2 = get(new JPanel(new FlowLayout(0)));
	JPanel p3 = get(new JPanel(new FlowLayout(0)), set(350, 0));
	
	JScrollPane scl = get(new JScrollPane(p2, 22, 31), set(new EmptyBorder(5, 5, 5, 5)));
	
	JComboBox com1 =get(new JComboBox());
	JComboBox com2 = get(new JComboBox());
	
	JLabel lab1 = get(new JLabel(""), setb(13));
	JLabel lab2 = get(new JLabel(""), setp(12));
	JLabel lab3 = get(new JLabel(""), setp(15), set(330, 50));
	
	ArrayList<ArrayList<String>> metro = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public TrainTime() {
		
		SetFrame(this, "열차 시간표", DISPOSE_ON_CLOSE, 1000, 500);
		design();
		action();
		setVisible(true);
		
		com1.setSelectedIndex(0);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Home();
			}
		});
		
	}

	@Override
	public void design() {
		
		Route.rt = new Route();
		
		Query("SELECT * FROM metro.metro;", metro);
		for (ArrayList<String> list : metro) {
			com1.addItem(list.get(1));
		}
		
		add(p1, "West");
		add(scl);
		add(p3, "East");
		
		p1.add(com1);
		p1.add(com2);
		p1.add(lab1);
		p1.add(lab2);
		
	}
	
	public void com2() {
		
		com2.removeAllItems();
		
		LocalTime start= LocalTime.parse(metro.get(com1.getSelectedIndex()).get(2));
		LocalTime fin = LocalTime.parse(metro.get(com1.getSelectedIndex()).get(3));
		
		while (start.isBefore(fin)) {
			com2.addItem(start.format(df2));
			start = start.plusMinutes(LocalTime.parse(metro.get(com1.getSelectedIndex()).get(4)).getMinute());
		}
		
		com2.setSelectedIndex(0);
		
	}
	
	public void table() {
		
		p2.removeAll();
		
		LocalTime time = LocalTime.parse(com2.getSelectedItem() + "");
		
		Query("select * from station s, route r where s.serial = r.station and r.metro = ?;", list, metro.get(com1.getSelectedIndex()).get(0));
		
		Route.rt.start(intnum(list.get(0).get(0)) - 1, intnum(list.get(list.size() - 1).get(0)) - 1);
		
		int cost = 0;
		int height = 0;
		for (int i = 0; i < list.size(); i++) {
			
			cost += Route.rt.maps[intnum(list.get(i == 0 ? 0 : i - 1).get(0)) - 1][intnum(list.get(i).get(0)) - 1];
			
			JPanel pn1 = get(new JPanel(new BorderLayout()), setb(Color.white), set(new EmptyBorder(5, 5, 5, 5)), set(280, 40));
			JLabel lb1 = get(new JLabel((i + 1) + ". " + list.get(i).get(1)));
			JLabel lb2 = get(new JLabel(time.plusSeconds(cost * 5).format(df2) + "도착"));
			
			pn1.add(lb1);
			pn1.add(lb2, "East");
			
			int j = i;
			
			pn1.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					for (Component comp : p2.getComponents()) {
						comp.setBackground(Color.white);
					}
					
					pn1.setBackground(Color.orange);
					lab3.setText("<html>" + list.get(j).get(1) + "에서<br>환승 가능한 노선");
					
					change(j);
					
				}
			});
			
			height += pn1.getPreferredSize().height;
			p2.add(pn1);
			
		}
		
		lab1.setText(com1.getSelectedItem().toString());
		lab2.setText("소요시간 : " + LocalTime.of(0, 0, 0).plusSeconds(cost * 5).format(df2));
		
		p2.setPreferredSize(new Dimension(0, height + 150));
		
		revalidate();
		repaint();
		
	}
	
	public void change(int j) {
		
		p3.removeAll();
		
		p3.add(lab3);
		int height = 0;
		Query("select * from metro m, route r where m.serial = r.metro and r.station = ?", temp, list.get(j).get(0));
		
		for (int i = 0; i < temp.size(); i++) {
			if (!temp.get(i).get(0).contentEquals((com1.getSelectedIndex() + 1) + "")) {
				
				JPanel pn1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)), set(330, 40), setb(Color.white));
				
				pn1.add(new JLabel(temp.get(i).get(1)));
				allfont(pn1);
				
				height += pn1.getPreferredSize().height;
				p3.add(pn1);
				
			}
		}
		
		p3.setPreferredSize(new Dimension(350, height + 100));
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		com1.addActionListener(e->{
			com2();
		});
		
		com2.addActionListener(e->{
			if (com2.getSelectedIndex() != -1) {
				table();
			}
		});
		
	}

}
