package Project;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Base.Base;

public class WayPoint extends JDialog implements Base{

	JPanel p1 = get(new JPanel(new FlowLayout(0)));
	JScrollPane scl = get(new JScrollPane(p1));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	ArrayList<Integer> data;
	
	public WayPoint(ArrayList<Integer> data) {

		this.data = data;
		
		SetDial(this, "경유지", DISPOSE_ON_CLOSE, 550, 500);
		design();
		action();
		setModal(true);
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(scl);
		
		for (int i = 0; i < data.size() - 1; i++) {
			
			ArrayList row = new ArrayList<>();
			Query("select * from metro m, route r1, route r2 where m.serial = r1.metro and m.serial = r2.metro and r1.station = ? and r2.station = ?;", list, station.get(data.get(i)).get(0), station.get(data.get(i + 1)).get(0));
			
			int orient = 0;
			if (intnum(list.get(0).get(5)) < intnum(list.get(0).get(8))) {
				orient  =0;
			}else {
				orient = 1;
			}
			
			if (!list.isEmpty()) {
				if (temp.size() == 0 || !temp.get(temp.size() -1).get(0).contentEquals(list.get(orient).get(0))) {
					
					row.add(list.get(orient).get(0));
					row.add(list.get(orient).get(1));
					row.add(station.get(data.get(i)).get(0));
					temp.add(row);
					
				}
			}
			
		}
		
		String txt = "<html>";
		int num = 1;
		
		for (int i = 0; i < data.size(); i++) {
			
			if (i == 0) {
				txt += "<b>" + num + ". " + station.get(data.get(i)).get(1) + " - 출발</b> <br><br>"; 
			}else if (i == data.size() - 1) {
				txt += "<b>" + num + ". " + station.get(data.get(i)).get(1) + " - 도착</b> <br><br>";
			}else {
				
				if (temp.size() == 1) {
					txt += num + ". " + station.get(data.get(i)).get(1) + "<br><br>";
				}else {
					
					String change = "";
					
					for (int j = 0; j < temp.size(); j++) {
						if (station.get(data.get(i)).get(0).contentEquals(temp.get(j).get(2))) {
							txt += "<b>" + num + ". " + station.get(data.get(i)).get(1) + " - 하차 <br><br>";
							num++;
							txt += num + ". " + station.get(data.get(i)).get(1) + " - " + "환승 " + temp.get(j).get(1).substring(0, 3) + " - " + temp.get(j).get(1).substring(5) + "으(로) 환승 </b><br><br>";
							change = station.get(data.get(i)).get(1);
						}
					}
					
					txt += station.get(data.get(i)).get(1).contentEquals(change) ? "" : num + ". " + station.get(data.get(i)).get(1) + "<br><br>";
					
				}
				
			}
			
			num++;
			
		}
		
		p1.add(new JLabel(txt));
		allfont(p1);
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
