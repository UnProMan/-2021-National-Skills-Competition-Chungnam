package Setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Setting{

	public Setting() {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			Statement s= c.createStatement();
			
			s.executeUpdate("drop database if exists metro");
			s.executeUpdate("create database if not exists metro");
			s.executeUpdate("use metro");
			s.executeUpdate("create table Station(serial int primary key auto_increment, name varchar(40))");
			s.executeUpdate("create table Metro(serial int primary key auto_increment, name varchar(60), start time, end time, `interval` time)");
			s.executeUpdate("create table route(serial int primary key auto_increment, station int, metro int, foreign key(station) references station(serial), foreign key(metro) references metro(serial))");
			s.executeUpdate("create table Path(serial int primary key auto_increment, departure int, arrive int, cost int, foreign key(departure) references station(serial), foreign key(arrive) references station(serial))");
			s.executeUpdate("create table User(serial int primary key auto_increment, id varchar(20), pw varchar(50), name varchar(30), birth date, phone varchar(14))");
			s.executeUpdate("create table Purchase(serial varchar(6), user int, departure int, arrive int, price int, time time, date date, distance int, foreign key(departure) references station(serial), foreign key(arrive) references station(serial))");
			s.executeUpdate("drop user if exists user@'localhost'");
			s.executeUpdate("create user if not exists user@'localhost' identified by '1234'");
			s.executeUpdate("grant select, update, insert, delete on metro.* to user@'localhost'");
			s.executeUpdate("set global local_infile = 1");
			
			String st[] = "station, Metro, Route, Path, User, Purchase".split(", ");
			for (int i = 0; i < st.length; i++) {
				s.executeUpdate("load data local infile '지급자료/" + st[i] + ".txt' into table " + st[i] + " lines terminated by '\r\n' ignore 1 lines");
			}
			
			JOptionPane.showMessageDialog(null, "세팅 완료", "메시지", JOptionPane.INFORMATION_MESSAGE);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	public static void main(String[] args) {
		new Setting();
	}
}
