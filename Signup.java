import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.*;   

public class Signup extends JFrame implements ActionListener,ItemListener{
	private TextField tf;
	private TextField tf2;
	private TextField tf3;
	private TextField tf4;
	private TextField tf5;
	private Home home;
	private Frame parent;
	private String sDay;
	private String sMonth;
	private String sYear;
	Choice monthChoice = new Choice();
	Choice yearChoice = new Choice();
	Choice dayChoice = new Choice();
	
	
	public Signup(Home f){
		super("Sign Up");
		
		setLayout(new FlowLayout());
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;
		Label l = new Label("User Name");
		Label l2 = new Label("Password");
		Label l3 = new Label("Full Name");
		Label l4 = new Label("Phone Number");
		Label l5 = new Label("Date of Birth");
		Label l6 = new Label("Already have an account?");
		tf = new TextField(12);
		tf2 = new TextField(12);
		tf2.setEchoChar('*');
		tf3 = new TextField(12);
		tf4 = new TextField(12);
		tf5 = new TextField(12);
		Button b=new Button("Sign up");
		Button b2=new Button("Log in");
		Button b3=new Button("Home");
		
		Font myFont = new Font("Serif",Font.BOLD,14);
		l.setFont(myFont);
		l2.setFont(myFont);
		l3.setFont(myFont);
		l4.setFont(myFont);
		l5.setFont(myFont);
		l6.setFont(myFont);
		
		tf.setBackground(Color.LIGHT_GRAY);
		tf.setForeground(Color.BLACK);
		tf2.setBackground(Color.LIGHT_GRAY);
		tf2.setForeground(Color.BLACK);
		tf3.setBackground(Color.LIGHT_GRAY);
		tf3.setForeground(Color.BLACK);
		tf4.setBackground(Color.LIGHT_GRAY);
		tf4.setForeground(Color.BLACK);
		tf5.setBackground(Color.LIGHT_GRAY);
		tf5.setForeground(Color.BLACK);
		
		
		b.setBackground(Color.DARK_GRAY);
		b2.setBackground(Color.DARK_GRAY);
		b3.setBackground(Color.DARK_GRAY);
		b.setForeground(Color.WHITE);
		b2.setForeground(Color.WHITE);
		b3.setForeground(Color.WHITE);
		
		add(l);add(tf);
		add(l2);add(tf2);
		add(l3);add(tf3);
		add(l4);add(tf4);
		add(l5);add(tf5);
		add(b);add(b3);
		add(l6);add(b2);
		l.setBounds(80,42,100,15);
		tf.setBounds(180,40,215,24);
		l2.setBounds(80,84,100,15);
		tf2.setBounds(180,80,215,24);
		l3.setBounds(80,124,100,15);
		tf3.setBounds(180,120,215,24);
		l4.setBounds(80,164,100,15);
		tf4.setBounds(180,160,215,24);
		l5.setBounds(80,204,100,15);
		
		b.setBounds(295,260,100,40);
		b3.setBounds(180,260,100,40);
		l6.setBounds(200,300,170,40);
		b2.setBounds(235,340,100,40);
		
		b.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);

		dob();
		
		add(yearChoice);
		add(monthChoice);
		add(dayChoice);
		yearChoice.setBounds(180,200,55,24);
		monthChoice.setBounds(245,200,90,24);
		dayChoice.setBounds(345,200,50,24);
		
		yearChoice.setBackground(Color.LIGHT_GRAY);
		yearChoice.setForeground(Color.BLACK);
		monthChoice.setBackground(Color.LIGHT_GRAY);
		monthChoice.setForeground(Color.BLACK);
		dayChoice.setBackground(Color.LIGHT_GRAY);
		dayChoice.setForeground(Color.BLACK);
		
		yearChoice.addItemListener(this);
		monthChoice.addItemListener(this);
		dayChoice.addItemListener(this);
		
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
	}

	// prepares DOB dropdowns
	public void dob(){
		yearChoice.add("Year");
		for(int i = 1900; i < 2020; i++){
			String numberAsString = String.valueOf(i);
			yearChoice.add(numberAsString);
		}
		dayChoice.add("Day");
		for(int i = 1; i < 32; i++){
			String numberAsString = String.valueOf(i);
			dayChoice.add(numberAsString);
		}
		monthChoice.add("Month");
		monthChoice.add("January");
		monthChoice.add("February");
		monthChoice.add("March");
		monthChoice.add("April");
		monthChoice.add("May");
		monthChoice.add("June");
		monthChoice.add("July");
		monthChoice.add("August");
		monthChoice.add("September");
		monthChoice.add("October");
		monthChoice.add("November");
		monthChoice.add("December");
	}

	// listens for the button clicks
	public void actionPerformed(ActionEvent ae){
		String clickedButton=ae.getActionCommand();
		if(clickedButton.equals("Sign up")){
			// vlidation: for empty fields
			if(tf.getText().trim().length() == 0 || tf2.getText().trim().length() == 0 || tf3.getText().trim().length() == 0 || tf4.getText().trim().length() == 0){
				JOptionPane.showMessageDialog(this,"All the fields except DOB are required");
			} else if (tf2.getText().trim().length() < 6) { // validation: password should at least 6 characters
				JOptionPane.showMessageDialog(this,"Please, enter a password with 6 or more characters");
			} else if (!Pattern.matches("^(?:\\+88|01)?\\d{11}$", tf4.getText())) { // regular expression to check BD numbers
				JOptionPane.showMessageDialog(this,"Invalid Bangladeshi Phone number");
			} else {
				DbConnection da = new DbConnection();
				// checks for duplicate username entry
				String q1 = "SELECT username from users WHERE username = '" + tf.getText().trim() + "'";
				ResultSet rs = null;		
				try{
					rs = da.getData(q1);
					if(rs.next()){						
						JOptionPane.showMessageDialog(this,"Please, try a new username");
					} else {
						// validation: done
						/*String uid = "SELECT max(userid) from users";
						System.out.println(uid);
						int userid=Integer.parseInt(uid); 
						userid++;*/
						//String q5 = "INSERT INTO users (userid, username, password, fullname, phone, dob, admin) VALUES ('seq_users.nextval', '" + tf.getText().trim() + "','" + tf2.getText() + "','" + tf3.getText().trim() + "','" + tf4.getText().trim() + "','" + sDay + " " + sMonth + ", " + sYear + "', 0)";
						String q5 = "INSERT INTO users (userid, username, password, fullname, phone, dob, admin) VALUES (seq_users.nextval, '" + tf.getText().trim() + "', '" + tf2.getText() + "', '" + tf3.getText().trim() + "', '" + tf4.getText().trim() + "', '" + sDay + " " + sMonth + ", " + sYear + "', 0)";
						da.updateDB(q5);
						JOptionPane.showMessageDialog(this,"Sign up successful");
						home.setUsername(tf.getText().trim()); // sets username to use as a pseudo session
						this.setVisible(false);
						home.vehicle.setParent(this);
						home.vehicle.setVisible(true);
						// resets all the fields to default
						tf.setText("");
						tf2.setText("");
						tf3.setText("");
						tf4.setText("");
						dayChoice.removeAll();
						monthChoice.removeAll();
						yearChoice.removeAll();
						dob();
					}
				}
				catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}
			}
		}
		else if(clickedButton.equals("Log in")){
			this.setVisible(false);
			home.login.setVisible(true);
			home.login.setParent(home);
		}
		else if(clickedButton.equals("Home")){
			// landing page
			this.setVisible(false);
			home.setVisible(true);
		}
	}

	// listens for the DOB dropdown changes
	// it allocates the dates dynamically to have a valid date, i.e. 31st Jan, leap year date etc
	public void itemStateChanged(ItemEvent ie){
		boolean leap = false;
		String v = (String)ie.getItem();
		if(ie.getSource() == dayChoice){
			try{
				sDay = v;
			} catch(Exception ex) {
				sDay = "1";
			}
		}
		else if(ie.getSource() == monthChoice){
			try {
				sMonth = v;
				dayChoice.removeAll();
				dayChoice.add("Day");
				if(sMonth == "February"){
					if(leap) {
						for(int i = 1; i < 30; i++){
							String numberAsString = String.valueOf(i);
							dayChoice.add(numberAsString);
						}
					} else {
						for(int i = 1; i < 29; i++){
							String numberAsString = String.valueOf(i);
							dayChoice.add(numberAsString);
						}
					}
				} else if (sMonth == "April" || sMonth == "June" || sMonth == "September" || sMonth == "November") {
					for(int i = 1; i < 31; i++){
						String numberAsString = String.valueOf(i);
						dayChoice.add(numberAsString);
					}
				} else {
					for(int i = 1; i < 32; i++){
						String numberAsString = String.valueOf(i);
						dayChoice.add(numberAsString);
					}
				}
				sDay = "1";
			} catch(Exception ex) {
				sMonth = "January";
				sDay = "1";
			}
		} else {
			try {
				sYear = v;
				int sy = Integer.valueOf(sYear);
				if(sy % 4 == 0) {
					if(sy % 100 == 0) {
						if (sy % 400 == 0) {
							leap = true;
						} else {
							leap = false;
						}
					} else {
						leap = true;
					}
				} else {
					leap = false;
				}
				dayChoice.removeAll();
				dayChoice.add("Day");
				if(sMonth == "February"){
					if(leap) {
						for(int i = 1; i < 30; i++){
							String numberAsString = String.valueOf(i);
							dayChoice.add(numberAsString);
						}
					} else {
						for(int i = 1; i < 29; i++){
							String numberAsString = String.valueOf(i);
							dayChoice.add(numberAsString);
						}
					}
				} else if (sMonth == "April" || sMonth == "June" || sMonth == "September" || sMonth == "November") {
					for(int i = 1; i < 31; i++){
						String numberAsString = String.valueOf(i);
						dayChoice.add(numberAsString);
					}
				} else {
					for(int i = 1; i < 32; i++){
						String numberAsString = String.valueOf(i);
						dayChoice.add(numberAsString);
					}
				}
				sMonth = "January";
				sDay = "1";
			} catch(Exception ex) {
				sYear = "1900";
				sMonth = "January";
				sDay = "1";
			}	
		}
	}
	
	public void setParent(Frame f) {parent=f;}
}