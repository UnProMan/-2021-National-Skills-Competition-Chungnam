package Project;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import Base.Base;

public class Admin extends JFrame implements Base{

	JButton btn1 = get(new JButton("회원 관리"));
	JButton btn2 = get(new JButton("지하철 이용 통계"));
	JButton btn3 = get(new JButton("로그아웃"));
	
	public Admin() {
		
		SetFrame(this, "관리자", DISPOSE_ON_CLOSE, 300, 200);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Home();
			}
		});
		
	}

	@Override
	public void design() {
		
		setLayout(new GridLayout(0, 1));
		
		add(btn1);
		add(btn2);
		add(btn3);
		
	}

	@Override
	public void action() {

		btn1.addActionListener(e->{
			dispose();
			new UserDB();
		});
		
		btn2.addActionListener(e->{
			dispose();
			new State();
		});
		
		btn3.addActionListener(e->{
			dispose();
			new Home();
		});
		
	}

}
