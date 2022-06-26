package Project;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import Base.Base;

public class UserDB extends JFrame implements Base{

	JPanel p1 = get(new JPanel());
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(new EmptyBorder(10, 5, 5, 5)));
	
	JButton btn1 = get(new JButton("검색"));
	JButton btn2 = get(new JButton("저장"));
	
	JTextField txt1 = get(new JTextField(25));
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("번호, 아이디, 비밀번호, 이름, 생일, 휴대전화".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2);
	JTable tbl = new JTable(model) {
		public boolean isCellEditable(int row, int column) {
			if (column == 0 || column == 1) {
				return false;
			}else {
				return true;
			}
		};
	};
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public UserDB() {
		
		SetFrame(this, "회원 관리", DISPOSE_ON_CLOSE, 600, 400);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Admin();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1, "North");
		add(scl);
		add(p2, "South");
		
		p1.add(txt1);
		p1.add(btn1);
		
		p2.add(btn2);
		
		table();
		
	}
	
	public void table() {
		
		SetData("select * from user where name like ?", list, model, 0, 6, "%" + txt1.getText() + "%");
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2) {
					
					if (tbl.getSelectedColumn() == 0 || tbl.getSelectedColumn() == 1) {
						
						Query("select * from user where serial = ?;", member, tbl.getValueAt(tbl.getSelectedRow(), 0).toString());
						
						dispose();
						new Buylist(1);
						
					}
					
				}
				
			}
		});
		
		btn1.addActionListener(e->{
			table();
		});
		
		btn2.addActionListener(e->{
			
			int a = 0;
			
			for (int i = 0; i < tbl.getRowCount(); i++) {
				
				String st[] = new String[6];
				for (int j = 0; j < st.length; j++) {
					st[j] = tbl.getValueAt(i, j).toString();
				}
				
				Query("select * from user where serial = ? and id = ? and pw = ? and name = ? and birth = ? and phone = ?;", list, st[0],st[1],st[2],st[3],st[4], st[5]);
				
				if (list.isEmpty()) {
					a = 1;
					Updat("update user set pw = ?, name = ?, birth = ?, phone = ? where serial = ?;", st[2], st[3], st[4], st[5], st[0]);
				}
				
			}
			
			if (a == 0) {
				err("변경사항이 없습니다.");
			}else {
				jop("변경사항이 수정되었습니다.");
			}
			
			table();
			
		});
		
	}

}
