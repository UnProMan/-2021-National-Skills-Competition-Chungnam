package Project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import Base.Base;

public class Map extends JFrame implements Base{
	
	int row[][] = { {618,41},{662,41},{705,40},{756,42},{804,39},{840,42},{872,41},{911,41},{948,39},{988,44},{1026,42},{1053,64},{1070,80},{1090,98},{1091,116},{1091,128},{1088,147},{1088,168},{1089,179},{1089,195},{1089,215},{1089,242},{1089,258},{1088,278},{1048,279},{1014,279},{981,281},{935,278},{868,278},{825,278},{793,280},{733,279},{731,327},{729,420},{729,460},{730,550},{679,548},{659,551},{633,549},{587,550},{550,549},{463,551},{431,551},{397,551},{361,551},{325,551},{297,551},{262,548},{243,535},{224,518},{208,505},{188,489},{177,477},{152,480},{123,481},{95,479},{63,482},{64,503},{64,524},{63,544},{66,560},{66,585},{458,590},{461,636},{459,658},{428,656},{459,681},{462,712},{469,725},{485,739},{503,756},{505,779},{505,812},{505,842},{506,877},{506,909},{505,935},{548,938},{596,938},{628,907},{640,934},{681,939},{725,936},{774,938},{815,938},{859,941},{898,939},{939,938},{977,939},{1018,938},{1060,938},{1100,941},{1138,937},{1197,938},{1257,941},{1338,937},{1397,933},{1395,879}
	, {377,488},{382,514},{449,516},{511,517},{550,595},{552,628},{550,672},{601,670},{636,669},{679,672},{728,674},{780,672},{809,673},{838,676},{852,672},{920,671},{943,670},{967,671},{1007,638},{1040,606},{1065,588},{1067,544},{1066,512},{1066,480},{1068,462},{1066,440},{1064,419},{1049,402},{1035,391},{1014,369},{963,326},{939,322},{868,324},{844,320},{796,329},{764,323},{681,325},{652,348},{628,370},{603,395},{580,415},{549,436},{548,460},{551,490},{550,516},{1067,395},{1065,354},{1012,303}
	,{169,187},{201,187},{236,188},{274,187},{304,188},{345,188},{374,188},{397,187},{431,188},{453,189},{481,191},{513,189},{570,189},{615,188},{665,187},{698,187},{736,187},{794,189},{795,215},{799,244},{824,371},{857,398},{882,419},{900,441},{902,478},{901,536},{884,551},{855,580},{856,631},{858,710},{924,710},{943,710},{969,709},{992,692},{1009,672},{1054,670},{1077,673},{1099,672},{1097,616},{1170,617},{1234,617}
	,{149,849},{184,846},{215,850},{256,848},{289,849},{327,849},{351,829},{366,810},{382,799},{405,780},{413,771},{425,758},{467,758},{548,757},{570,757},{599,757},{643,760},{690,759},{721,758},{780,755},{779,729},{778,704},{780,627},{780,593},{782,552},{782,510},{782,471},{760,449},{754,398},{786,372},{866,243},{868,214},{870,187},{868,147},{927,149},{976,146},{1010,148},{1044,147},{1154,148},{1208,148},{1252,148}
	,{294,284},{320,307},{349,334},{376,361},{378,391},{378,414},{379,435},{379,461},{428,488},{462,491},{490,487},{524,489},{593,520},{637,499},{634,469},{634,443},{681,397},{681,357},{683,246},{760,246},{896,369},{928,371},{969,367},{1099,372},{1115,383},{1137,403},{1156,423},{1153,447},{1154,478},{1156,514},{1237,516},{1234,496},{1234,477},{1234,453},{1234,435},{1233,415},{1236,392},{1237,375},{1295,373},{1296,393},{1300,411},{1236,537},{1240,552},{1234,586},{1234,656},{1245,666},{1264,685}};
	
	JPanel p1;
	JScrollPane scl;
	
	JButton btn1 = get(new JButton("결정"));
	
	int start = -1, fin = -1, index;
	
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item1 = new JMenuItem("출발으로 설정");
	JMenuItem item2 = new JMenuItem("도착으로 설정");
	JMenuItem item3 = new JMenuItem("해당 역 정보 보기");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Map() {
		
		SetFrame(this, "지도", DISPOSE_ON_CLOSE, 1000, 500);
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
		
		Route.rt = new Route();
		
		pop.add(item1);
		pop.add(item2);
		pop.add(item3);
		
		p1 = get(new JPanel(null) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(new ImageIcon(file("metro.png")).getImage(), 0, 0, 1500, 1000, null);
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.white);
				
				for (int i = 0; i < Route.rt.data.size() - 1; i++) {
					g2.drawLine(row[Route.rt.data.get(i)][0], row[Route.rt.data.get(i)][1], row[Route.rt.data.get(i + 1)][0], row[Route.rt.data.get(i + 1)][1]);
				}
				
			}
		}, set(1500, 1000));
		add(scl = new JScrollPane(p1));
		add(btn1, "South");
		
		Query("select * from path;", list);
		
		for (int i = 0; i < list.size(); i++) {
			
			int sx = row[intnum(list.get(i).get(1)) - 1][0];
			int sy = row[intnum(list.get(i).get(1)) - 1][1];
			int fx = row[intnum(list.get(i).get(2)) - 1][0];
			int fy = row[intnum(list.get(i).get(2)) - 1][1];
			
			int x = fx - sx;
			int y = fy - sy;
			
			JLabel lab = get(new JLabel(list.get(i).get(3)), setf(Color.red), setp(10));
			lab.setBounds((sx + x/2) - 5, (sy + y/2) - 15, 25, 25);
			
			p1.add(lab);
			
		}
		
		for (int i = 0; i < station.size(); i++) {
			
			JLabel lab = get(new JLabel(""), set(30, 30));
			lab.setBounds(row[intnum(station.get(i).get(0)) - 1][0], row[intnum(station.get(i).get(0)) - 1][1], 30, 30);
			
			p1.add(lab);
			
			int j = i;
			
			lab.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (e.getButton() ==3) {
						pop.show(lab, e.getX(), e.getY());
					}
					
					index = j;
					
				}
			});
			
		}
		
	}
	
	public void load() {
		
		if (start == -1 || fin == -1) {
			return;
		}
		
		Route.rt.start(start, fin);
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (start == -1 || fin == -1) {
				err("출발역과 도착역을 모두 선택해야 합니다.");
			}else {
				
				dispose();
				new Ticketing(Route.rt.data, Route.rt.distance[fin], LocalDateTime.now().plusMinutes(30));
				
			}
			
		});
		
		item1.addActionListener(e->{
			
			jop("출발역을 " + station.get(index).get(1) + "으로 설정하였습니다." );
			fin = -1;
			start = index;
			load();
			
		});
		
		item2.addActionListener(e->{
			
			jop("도착역을 " + station.get(index).get(1) + "으로 설정하였습니다." );
			fin = index;
			load();
			
		});
		
		item3.addActionListener(e->{
			dispose();
			new Station(index);
		});
		
	}
}
