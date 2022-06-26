package Project;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class RouteMap extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(0, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JScrollPane scl;
	
	JComboBox com1 = get(new JComboBox());
	
	ArrayList<ArrayList<String>> metro = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public RouteMap() {
		
		SetFrame(this, "노선도", DISPOSE_ON_CLOSE, 900, 680);
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
		
		Query("SELECT * FROM metro.metro;", metro);
		for (ArrayList<String> m : metro) {
			com1.addItem(m.get(1));
		}
		
		add(p1);
		
	}

	@Override
	public void action() {
		
		com1.addActionListener(e->{
			
			Query("select * from station s, route r where s.serial = r.station and r.metro = ?;", list, metro.get(com1.getSelectedIndex()).get(0));
			
			p1.removeAll();
			
			p1.add(com1, "North");
			p1.add(scl = new JScrollPane(new map()));
			
			revalidate();
			repaint();
			
		});
		
	}
	
	public class map extends JPanel implements Base{

		int x[] = new int[list.size()], y[] = new int[list.size()];
		int px[] = new int[list.size() + list.size()/6 + 1], py[] = new int[list.size() + list.size()/6 + 1];
		double km[] = new double[list.size()];
		int time[] = new int[list.size()];
		
		public map() {
			
			setLayout(null);
			setBackground(Color.white);
			
			design();
			action();
			
		}
		
		public void design() {
			
			Route.rt = new Route();
			Route.rt.start(intnum(list.get(0).get(0)) - 1, intnum(list.get(list.size() - 1).get(0)) - 1);
			
			for (int i = 0; i < list.size() - 1; i++) {
				
				int cost = Route.rt.maps[intnum(list.get(i).get(0)) - 1][intnum(list.get(i + 1).get(0)) - 1];
				
				km[i] = Math.round((cost * 100) / 100.0);
				time[i] = cost * 5;
				
			}
			
			int sx = 170, offx = 100, sy = 80, offy = 100, pidx = 0;
			
			x[0] = sx;
			y[0] = sy;
			px[pidx] = x[0] - offx / 2;
			py[pidx] = y[0];
			pidx++;
			px[pidx] = x[0] + offx / 2;
			py[pidx] = y[0];
			
			for (int i = 1; i < list.size(); i++) {
				
				pidx ++;
				
				if (i%6 == 0) {
					px[pidx] = px[pidx - 1];
					py[pidx] = py[pidx - 1] + offy;
					pidx++;
				}
				
				y[i] =(i/6) * offy + sy;
				py[pidx] = y[i];
				
				if (i/6%2 == 0) {
					x[i] = (i%6) * offx + sx;
					px[pidx] = x[i] + offx / 2;
				}else {
					x[i] = 5 * offx - (i%6) * offx + sx;
					px[pidx] = x[i] - offx / 2;
				}
				
			}
			
			setPreferredSize(new Dimension(getWidth(), y[y.length - 1] + 100));
			draw();
			revalidate();
			repaint();
			
		}
		
		public void draw() {
			
			for (int i = 0; i < list.size(); i++) {
				
				JLabel lab = get(new JLabel(list.get(i).get(1)), setp(15));
				lab.setBounds(x[i] - lab.getText().length() * 5, y[i] + 5, 100, 25);
					
				int j = i;
				
				lab.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						
						String msg = "<html>" + lab.getText() + "에서 갈 수 있는 역<br>";
						
						msg += j == 0 ? "" : list.get(j - 1).get(1) + (j == list.size() - 1 ? "" : ",");
						msg += j == list.size() - 1 ? "" : list.get(j + 1).get(1);
						
						msg += "<br>" + lab.getText() + "를 지나가는 노선<br>";
						
						Query("select * from metro m, route r where m.serial = r.metro and r.station = ?;", temp, list.get(j).get(0));
						
						for (int j2 = 0; j2 < temp.size(); j2++) {
							msg += temp.get(j2).get(1) + "<br>";
						}
						
						jop(msg);
						
					}
				});
				
				add(lab);
				
			}
			
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(2));
			
			g2.setColor(Color.black);
			g2.setFont(new Font("맑은 고딕", 1, 30));
			g2.drawString("노선도 - " + com1.getSelectedItem().toString(), 120, 30);
			
			g2.setColor(blue);
			if (list.size() %6 == 0) {
				g2.drawPolyline(px, py, px.length -1);
			}else {
				g2.drawPolyline(px, py, px.length);
			}
			
			g2.setColor(Color.red);
			for (int i = 0; i < list.size(); i++) {
				g2.fillOval(x[i], y[i] - 5, 10, 10);
			}
			
			g2.setFont(new Font("맑은 고딕", 1, 12));
			g2.setColor(Color.black);
			
			int a= 0;
			for (int i = 0; i < list.size() - 1; i++) {
				
				a++;
				
				if (a > 0 && a < 6 || a > 12 && a < 18 || a > 24 && a < 30 || a > 36 && a < 42) {
					g2.drawString(km[i] + "km (" + time[i] + "초)", x[i] + 35, y[i] - 10);
				}else if (a == 6 || a == 18 || a == 30 || a == 42) {
					g2.drawString(km[i] + "km (" + time[i] + "초)", x[i] + 60, y[i] + 60);
				}else if (a > 6 && a < 12 || a > 18 && a < 24 || a > 30 && a < 36) {
					g2.drawString(km[i] + "km (" + time[i] + "초)", x[i] - 80, y[i] - 10);
				}else if (a == 12 || a == 24 || a == 36) {
					g2.drawString(km[i] + "km (" + time[i] + "초)", x[i] - 135, y[i] + 60);
				}
				
			}
			
		}
		
		@Override
		public void action() {
			
			
			
		}
		
	}
	
}
