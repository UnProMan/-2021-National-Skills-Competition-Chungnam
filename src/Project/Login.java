package Project;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Login extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new GridLayout(2, 1, 0, 5)), set(30, 0));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1, 0, 5)));
	
	JButton btn1= get(new JButton("로그인"));
	
	JTextField txt1 = new JTextField();
	JPasswordField txt2 = new JPasswordField();
	
	public Login() {
		
		SetFrame(this, "로그인", DISPOSE_ON_CLOSE, 300, 150);
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
		
		add(p1);
		
		p1.add(p2, "West");
		p1.add(p3);
		p1.add(btn1, "South");
		
		p2.add(new JLabel("ID"));
		p2.add(new JLabel("PW"));
		
		p3.add(txt1);
		p3.add(txt2);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				err("아이디와 비밀번호를 모두 입력해야 합니다.");
			}else if (txt1.getText().contentEquals("admin") && txt2.getText().contentEquals("1234")) {
				jop("관리자님 환영합니다.");
				dispose();
				new Admin();
			}else {
				
				Query("select * from user where id = ? and pw = ?;", member, txt1.getText(), txt2.getText());
				
				if (member.isEmpty()) {
					err("일치하는 회원정보가 없습니다.");
				}else {
					
					jop(member.get(0).get(3) + "님 안녕하세요.");
					
					dispose();
					new Home();
					
				}
				
			}
			
		});
		
	}

}
