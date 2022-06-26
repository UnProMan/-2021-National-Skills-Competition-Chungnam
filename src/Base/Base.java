package Base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public interface Base {
	
	public void design();
	public void action();
	
	ArrayList<ArrayList<String>> member = new ArrayList<>();
	ArrayList<ArrayList<String>> station = new ArrayList<>();
	
	DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm:ss");
		
	Color blue = new Color(051, 102, 255);
	
	default void SetFrame(JFrame f, String title, int ex, int x ,int y) {
		f.setTitle("서울메트로 - " + title);
		f.setDefaultCloseOperation(ex);
		f.setSize(x, y);
		f.setLocationRelativeTo(null);
	}
	
	default void SetDial(JDialog d, String title, int ex, int x, int y) {
		d.setTitle(title);
		d.setDefaultCloseOperation(ex);
		d.setSize(x, y);
		d.setLocationRelativeTo(null);
	}
	
	default <Any> Any get(JComponent comp, Set...sets) {
		
		comp.setFont(new Font("맑은 고딕", 0, comp.getFont().getSize()));
		
		if (comp instanceof JButton) {
			comp.setBackground(blue);
			comp.setForeground(Color.white);
		}
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return (Any) comp;
		
	}
	
	default void allfont(JComponent comp) {
		for (Component c : comp.getComponents()) {
			c.setFont(new Font("맑은 고딕", 0, c.getFont().getSize()));
		}
		comp.revalidate();
		comp.repaint();
	}
	
	default JTextField gettxt(String title, Set...sets ) {
		
		JTextField txt = new JTextField() {
			public void paint(Graphics g) {
				super.paint(g);
				
				if (!this.getText().isBlank()) {
					return;
				}
				
				g.setColor(Color.gray);
				g.drawString(title, this.getInsets().left, this.getInsets().top + g.getFontMetrics().getMaxAscent());
				
			}
		};
		
		for (Set set : sets) {
			set.set(txt);
		}
		
		return txt;
		
	}
	
	default void jop(String txt) {
		JOptionPane.showMessageDialog(null, txt, "메시지", JOptionPane.INFORMATION_MESSAGE);
	}
	
	default void err(String txt) {
		JOptionPane.showMessageDialog(null, txt, "메시지", JOptionPane.ERROR_MESSAGE);
	}
	
	default Set set(boolean tf) {
		return c->c.setEnabled(tf);
	}
	
	default Set set(Border border) {
		return c->c.setBorder(border);
	}
	
	default Set set(int x, int y) {
		return c->c.setPreferredSize(new Dimension(x, y));
	}
	
	default Set setb(int font) {
		return c->c.setFont(new Font("맑은 고딕", 1, font));
	}
	
	default Set setp(int font) {
		return c->c.setFont(new Font("맑은 고딕", 0, font));
	}
	
	default Set setb(Color color) {
		return c->c.setBackground(color);
	}
	
	default Set setf(Color color) {
		return c->c.setForeground(color);
	}
	
	default String file(String txt) {
		return "지급자료/images/" + txt;
	}
	
	default boolean isnum(String txt) {
		
		try {
			
			Integer.parseInt(txt);
			return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	default boolean isnumbers(String txt, int len) {
		
		try {
			
			Integer.parseInt(txt);
			
			if (txt.length() == len) {
				return true;
			}else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	default boolean isdate(int y, int m, int d) {
		
		try {
			
			LocalDate.of(y, m, d);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	default Integer intnum(String txt) {
		return Integer.parseInt(txt);
	}
	
	default JLabel getimg(String txt, int x, int y, Set...sets) {
		
		JLabel comp = new JLabel(new ImageIcon(new ImageIcon(file(txt)).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default void Query(String sql, ArrayList<ArrayList<String>> list, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/metro?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			list.clear();
			ResultSet rs = s.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			
			while (rs.next()) {
				ArrayList row = new ArrayList<>();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				list.add(row);
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void Updat(String sql, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/metro?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			s.executeUpdate();
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void SetData(String sql, ArrayList<ArrayList<String>> list, DefaultTableModel model, int start, int fin, String...v) {
		
		Query(sql, list, v);
		
		model.setNumRows(0);
		
		for (int i = 0; i < list.size(); i++) {
			Vector row = new Vector<>();
			for (int j = start; j < fin; j++) {
				row.add(list.get(i).get(j));
			}
			model.addRow(row);
		}
		
	}
	
}
