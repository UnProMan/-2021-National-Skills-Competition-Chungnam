package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Pay extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new GridLayout(0, 1)), set(new EmptyBorder(10, 10, 10, 10)), set(200, 0), setb(blue));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)), setb(Color.white));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1)), setb(Color.white));
	JPanel p4 = get(new JPanel(new FlowLayout(0, 5, 5)), setb(Color.white));
	JPanel p5 = get(new JPanel(new FlowLayout(0)), setb(Color.white));
	
	JLabel lab1 = get(new JLabel("결제 정보"), setf(Color.white), setp(20));
	JLabel lab2 = get(new JLabel(""), setf(Color.white), setp(14));
	JLabel lab3 = get(new JLabel(""), setf(Color.white), setp(14));
	JLabel lab4 = get(new JLabel(""), setf(Color.white), setp(14));
	JLabel lab5 = get(new JLabel("Seoul Metro Ticket"), setb(20));
	
	JLabel img = getimg("logo.png", 180, 40);
	JLabel lb1 = get(new JLabel("안녕하세요, " + member.get(0).get(3) + "님."),set(480, 30), setp(15));
	JLabel lb2 = get(new JLabel("탑승권자 이름은"), set(120, 30), setp(15));
	JLabel lb3 = get(new JLabel("카드번호는"), set(90, 30), setp(15));
	JLabel lb4 = get(new JLabel("CVC는"), setp(15), set(70, 30));
	JLabel lb5 = get(new JLabel("카드 비밀번호는"), setp(15), set(120, 30));
	
	JLabel n1 = get(new JLabel("이고,"), setp(15), set(200, 30));
	JLabel n2 = get(new JLabel("이고,"), setp(15), set(100, 30));
	JLabel n3 = get(new JLabel("입니다."), setp(15), set(80, 30));
	
	JButton btn1 = get(new JButton("결제하기"));
	
	JTextField txt1 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(120, 30));
	JTextField txt2 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(60, 30));
	JTextField txt3 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(60, 30));
	JTextField txt4 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(60, 30));
	JTextField txt5 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(60, 30));
	JTextField txt6 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(60, 30));
	JTextField txt7 = get(new JTextField(), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)), set(60, 30));
	
	int pay = 0;
	
	ArrayList<Integer> data;
	int cost;
	LocalDateTime now;
	
	DecimalFormat df = new DecimalFormat("#,##0");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Pay(ArrayList<Integer> data, int cost, LocalDateTime now) {
		
		this.data = data;
		this.cost = cost;
		this.now = now;
		
		SetFrame(this, "결제", DISPOSE_ON_CLOSE, 700, 400);
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
		
		lab2.setText("<html>예매구간:<br><b>" + station.get(data.get(0)).get(1) + "<br>" + station.get(data.get(data.size() - 1)).get(1) + "<br>구간");
		lab3.setText("<html>탑승 시간:<br><b>" + now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "<br>" + now.format(df2) + " 열차");
		
		int age = LocalDate.now().getYear() - LocalDate.parse(member.get(0).get(4)).getYear();
		
		pay = cost * 5 > 1200 ? cost * 5 : 1200;
		
		if (age <= 13) {
			pay = (int) (pay * 0.9);
		}else if (age >= 65) {
			pay = (int) (pay * 0.5);
		}
		lab4.setText("<html>총 결제 금액:<br><b>" + df.format(pay) + "원");
		
		add(p1, "West");
		add(p2);
		
		p1.add(lab1);
		p1.add(lab2);
		p1.add(lab3);
		p1.add(lab4);
		
		p2.add(p3, "North");
		p2.add(p4);
		p2.add(btn1, "South");
		
		p3.add(p5);
		p3.add(lab5);
		
		p5.add(img);
		
		p4.add(lb1);
		p4.add(lb2);
		p4.add(txt1);
		p4.add(n1);
		
		p4.add(lb3);
		p4.add(txt2);
		p4.add(txt3);
		p4.add(txt4);
		p4.add(txt5);
		p4.add(n2);
		
		p4.add(lb4);
		p4.add(txt6);
		p4.add(lb5);
		p4.add(txt7);
		p4.add(n3);
		
		for (Component comp : p4.getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setHorizontalAlignment(JLabel.CENTER);
			}
		}
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			int a = JOptionPane.showConfirmDialog(null, "구매하시겠습니까?", "메시지", JOptionPane.YES_NO_OPTION);
			
			if (a == 0) {
				
				for (Component comp : p4.getComponents()) {
					if (comp instanceof JTextField && ((JTextField) comp).getText().isBlank()) {
						err("모든 항목을 입력해야 합니다.");
						return;
					}
				}
				
				if (!isnumbers(txt2.getText(), 4) || !isnumbers(txt3.getText(), 4) || !isnumbers(txt4.getText(), 4) || !isnumbers(txt5.getText(), 4)) {
					err("카드 번호는 각 4자리 숫자로 구성해야합니다.");
				}else if (!txt6.getText().contentEquals(txt2.getText().substring(0, 1) + txt3.getText().substring(0, 1) + txt4.getText().substring(0, 1))) {
					err("CVC 코드가 일치하지 않습니다.");
				}else if (!txt7.getText().contentEquals(member.get(0).get(4).substring(0, 4))) {
					err("카드 비밀번호가 일치하지 않습니다.");
				}else {
					
					int random = random();
					
					Updat("insert into purchase values(?,?,?,?,?,?,?,?);", random + "", member.get(0).get(0), station.get(data.get(0)).get(0), station.get(data.get(data.size() - 1)).get(0), pay + "", now.format(df2), now.format(df1), cost + "");
					
					jop("<html>결제가 완료되었습니다!<br>예매번호 : " + random);
					
					dispose();
					new Home();
					
				}
				
			}
			
		});
		
	}
	
	public Integer random() {
		
		int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
		
		Query("select * from purchase where serial = ?;", list, random + "");
		
		if (!list.isEmpty()) {
			random();
		}else {
			return random;
		}
		
		return null;
		
	}
	
}
