package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Paynum extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout(10, 10)));
	JPanel p3 = get(new JPanel(new GridLayout(0, 3, 10, 10)));
	
	JButton btn1 = get(new JButton("확인"));
	JButton btn2 = get(new JButton("←"), setb(Color.white), set(new LineBorder(Color.gray)), setf(Color.black));
	JButton btn3 = get(new JButton("재배치"), setb(Color.white), set(new LineBorder(Color.gray)), setf(Color.black));
	
	JTextField txt1 = get(new JTextField());
	
	int n;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Paynum() {
		
		SetFrame(this, "예매번호 확인", DISPOSE_ON_CLOSE, 400, 400);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new MyPage();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(txt1, "North");
		p1.add(p2);
		
		p2.add(btn1, "North");
		p2.add(p3);
		
		random();
		
	}
	
	public void random () {
		
		p3.removeAll();
		
		n = 0;
		ThreadLocalRandom.current().ints(0, 10).distinct().limit(10).forEach(T->{
			
			JButton btn = get(new JButton(T + ""), setb(Color.white) ,set(new LineBorder(Color.gray)), setf(Color.black));
			
			if (n == 9) {
				p3.add(btn2);
			}
			
			btn.addActionListener(e->{
				txt1.setText(txt1.getText() + btn.getText());
			});
			
			n++;
			p3.add(btn);
			
		});
		
		p3.add(btn3);
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank()) {
				err("예매번호를 입력해야 합니다.");
			}else {
				
				Query("select * from purchase where serial = ? and user = ?;", list, txt1.getText(), member.get(0).get(0));
				
				if (list.isEmpty()) {
					err("예매번호가 일치하지 않습니다.");
				}else {
					dispose();
					new Ticket(txt1.getText());
				}
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			if (!txt1.getText().isBlank()) {
				
				txt1.setText(txt1.getText().substring(0, txt1.getText().length() -1));
				
			}
			
		});
		
		btn3.addActionListener(e->{
			random();
		});
		
		txt1.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				e.consume();
			}
		});
		
	}

}
