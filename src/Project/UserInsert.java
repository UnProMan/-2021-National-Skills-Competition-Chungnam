package Project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class UserInsert extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(5,5,5,5)));
	JPanel p2 = get(new JPanel(new FlowLayout(0, 5, 10)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		
	JButton btn1= get(new JButton("회원가입"));
	
	JLabel lab1 = get(new JLabel("아이디"), set(50, 30));
	JLabel lab2 = get(new JLabel("비밀번호"), set(50, 30));
	JLabel lab3 = get(new JLabel("이름"), set(50, 30));
	JLabel lab4 = get(new JLabel("생년월일"), set(50, 30));
	JLabel lab5 = get(new JLabel("휴대전화"), set(50, 30));
	
	JTextField txt1 = gettxt("", set(280, 30));
	JTextField txt2 = gettxt("", set(280, 30));
	JTextField txt3 = gettxt("", set(280, 30));
	JTextField txt4 = gettxt("yyyy", set(90, 30));
	JTextField txt5 = gettxt("mm", set(70, 30));
	JTextField txt6 = gettxt("dd", set(70, 30));
	JTextField txt7 = gettxt("000", set(70, 30));
	JTextField txt8 = gettxt("0000", set(85, 30));
	JTextField txt9 = gettxt("0000", set(85, 30));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public UserInsert() {
		
		SetFrame(this, "회원가입", DISPOSE_ON_CLOSE, 380, 300);
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
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(btn1);
		
		p2.add(lab1);
		p2.add(txt1);
		
		p2.add(lab2);
		p2.add(txt2);
		
		p2.add(lab3);
		p2.add(txt3);
		
		p2.add(lab4);
		p2.add(txt4);
		p2.add(txt5);
		p2.add(new JLabel("월"));
		p2.add(txt6);
		p2.add(new JLabel("일"));
		
		p2.add(lab5);
		p2.add(txt7);
		p2.add(new JLabel("-"));
		p2.add(txt8);
		p2.add(new JLabel("-"));
		p2.add(txt9);
		
		allfont(p2);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			for (Component comp : p2.getComponents()) {
				if (comp instanceof JTextField && ((JTextField) comp).getText().isBlank()) {
					err("빈칸을 모두 입력해야 합니다.");
					return;
				}
			}
			
			Query("select * from user where id = ?;", list, txt1.getText());
			
			if (!list.isEmpty()) {
				err("이미 사용중인 아이디입니다.");
			}else if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$]).{3,}$", txt2.getText()) == false) {
				err("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
			}else if (!isnumbers(txt4.getText(), 4) || !isnumbers(txt5.getText(), 2) || !isnumbers(txt6.getText(), 2) || !isdate(intnum(txt4.getText()), intnum(txt5.getText()), intnum(txt6.getText()))) {
				err("생년월일은 올바른 형식으로 입력해야 합니다.");
			}else if (LocalDate.of(intnum(txt4.getText()), intnum(txt5.getText()), intnum(txt6.getText())).isAfter(LocalDate.now())) {
				err("생년월일은 미래가 아니어야 합니다.");
			}else if (!isnumbers(txt7.getText(), 3) || !isnumbers(txt8.getText(), 4) || !isnumbers(txt9.getText(), 4)) {
				err("전화번호를 올바른 형식으로 입력해야 합니다.");
			}else {
				
				jop("회원가입이 완료되었습니다.");
				
				Updat("insert into user values(null, ?,?,?,?,?);", txt1.getText(), txt2.getText(), txt3.getText(), txt4.getText() + "-" + txt5.getText() + "-" + txt6.getText(), txt7.getText() + "-" + txt8.getText() + "-" + txt9.getText());
				
				dispose();
				new Home();
				
			}
			
		});
		
	}

}
