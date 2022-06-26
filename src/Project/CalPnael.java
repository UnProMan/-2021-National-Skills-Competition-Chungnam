package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import Base.Base;

public class CalPnael extends JPanel implements Base{

	JPanel p1 = get(new JPanel(new FlowLayout(0)), setb(Color.white));
	JScrollPane scl = new JScrollPane(p1);
	
	JLabel lab;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	LocalDate date, now;
	
	Color lred = new Color(255, 102, 102);
	Color lblue = new Color(102, 102, 255);
	
	public CalPnael(LocalDate now, LocalDate date) {
		
		this.now = now;
		this.date = date;
		
		setLayout(new BorderLayout());
		setBackground(Color.white);
		setBorder(new LineBorder(Color.black));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		int c= date.getDayOfWeek().getValue();
		
		if (now.getMonthValue() == date.getMonthValue()) {
			lab = get(new JLabel(date.format(DateTimeFormatter.ofPattern("dd")), JLabel.RIGHT), setf(c == 6 ? Color.blue : c == 7 ? Color.red : Color.black));
		}else {
			lab = get(new JLabel(date.format(DateTimeFormatter.ofPattern("dd")), JLabel.RIGHT), setf(c == 6 ? lblue : c == 7 ? lred : Color.gray));
		}
		
		add(lab, "North");
		add(scl);
		
		Query("select concat(s1.name, '→', s2.name) from purchase pu, station s1, station s2 where pu.departure = s1.serial and pu.departure = s2.serial and pu.user = ? and date = ? order by pu.serial;", list, member.get(0).get(0), date.toString());
		String txt = "<html>";
		
		for (int i = 0; i < list.size(); i++) {
			txt += list.get(i).get(0) + "<br>";
		}
		
		p1.add(new JLabel(txt));
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
