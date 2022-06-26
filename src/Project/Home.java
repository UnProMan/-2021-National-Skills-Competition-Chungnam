package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Home extends JFrame implements Base{

	JPanel p1;
	JPanel p2 = get(new JPanel(new GridLayout(1, 3, 10, 0)));
	JPanel p3= get(new JPanel(new BorderLayout()));
	JPanel p4 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p5 = get(new JPanel(new GridLayout(1, 2, 5, 5)));
	JPanel p6 = get(new JPanel(new BorderLayout()));
	JPanel p[] = new JPanel[3];
	
	JLabel lab1 = get(new JLabel("안전한 도시철도, 편리한 교통 서비스"), setp(20), setf(blue));
	JLabel lab2 = get(new JLabel("서울교통공사가 도시교통의 미래를 만들어 갑니다."), setp(15), setf(Color.white));
	JLabel lab3 = get(new JLabel("", 0), setp(35), setf(Color.white));
	JLabel lab4 = get(new JLabel(""), setf(Color.white), setb(20));
	JLabel lab5 = get(new JLabel(""), setp(15),setf(Color.white));
	JLabel img = getimg("logo.png", 200, 50);
	
	JButton btn1=  get(new JButton("경로 검색"));
	JButton btn2=  get(new JButton("노선도"));
	JButton btn3=  get(new JButton("열차 시간표"));
	JButton btn4=  get(new JButton("마이페이지"));
	JButton btn5=  get(new JButton("로그인"));
	JButton btn6=  get(new JButton("회원가입"));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Home() {
		
		SetFrame(this, "홈", EXIT_ON_CLOSE, 1000, 500);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		Query("SELECT * FROM metro.station;", station);
		
		add(p1 = get(new JPanel(new BorderLayout()) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(new ImageIcon(file("background.png")).getImage(), 0, 0, 1000, 500, null);
				
			}
		}, set(new EmptyBorder(30, 30, 30, 30))));
		
		p1.add(p3, "North");
		p1.add(p2);
		
		p3.add(p4);
		p3.add(img, "East");
		
		p4.add(lab1);
		p4.add(lab2);
		
		for (int i = 0; i < p.length; i++) {
			p2.add(p[i] = get(new JPanel(new GridLayout(2, 1, 10, 10)), setb(new Color(0, 0, 0, 120)), set(new EmptyBorder(10, 10, 10, 10))));
		}
		
		p[0].add(btn1);
		p[0].add(p5);
		
		p5.add(btn2);
		p5.add(btn3);
		
		p[1].add(lab3);
		p[1].add(btn4);
		
		logo();
		
		p2.setOpaque(false);
		p3.setOpaque(false);
		p4.setOpaque(false);
		p5.setOpaque(false);
		p6.setOpaque(false);
		
		new Thread(()->{
			try {
				
				while (true) {
					
					lab3.setText(LocalTime.now().format(df2));
					revalidate();
					repaint();
					Thread.sleep(1000);
					
				}
				
			} catch (Exception e) {
			}
		}).start();
		
	}
	
	void logo() {
		
		p[2].removeAll();
		
		if (member.isEmpty()) {
			p[2].add(btn5);
			p[2].add(btn6);
			btn6.setText("회원가입");
		}else {
			
			Query("select sum(distance) * 0.1 from purchase where user = ?;", list, member.get(0).get(0));
			
			lab4.setText("<html>안녕하세요? " + member.get(0).get(3) + "님");
			lab5.setText("<html>서울메트로와 총 " + list.get(0).get(0) + "km를 함께했습니다.");
			btn6.setText("로그아웃");
			lab5.setVerticalAlignment(JLabel.TOP);
			
			p6.add(lab4, "North");
			p6.add(lab5);
			
			p[2].add(p6);
			p[2].add(btn6);
			
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			dispose();
			new Map();
		});
		
		btn2.addActionListener(e->{
			dispose();
			new RouteMap();
		});
		
		btn3.addActionListener(e->{
			dispose();
			new TrainTime();
		});
		
		btn4.addActionListener(e->{
			
			if (member.isEmpty()) {
				err("로그인 후 이용 가능합니다.");
			}else {
				
				dispose();
				new MyPage();
				
			}
			
		});
		
		btn5.addActionListener(e->{
			dispose();
			new Login();
		});
		
		btn6.addActionListener(e->{
			
			if (btn6.getText().contentEquals("회원가입")) {
				dispose();
				new UserInsert();
			}else {
				member.clear();
				logo();
			}
			
		});
		
	}
	
	public static void main(String[] args) {
		new Home();
	}
	
}
