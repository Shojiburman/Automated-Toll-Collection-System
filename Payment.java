import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.sql.Date;
import java.sql.CallableStatement;


public class Payment extends JFrame implements ActionListener, ItemListener {
	private Home home;
	private Frame parent;
	private String place = "Select";
	private String vehicleno = "Select";
	Choice placeName = new Choice();
	Choice vehicleNumber = new Choice();

	public Payment(Home f) {
		super("Payment");
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);

        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;
		Label placeNameLabel = new Label("Place Name");
		Label vehiclenumberLabel = new Label("Vehicle Number");
		
		Button paymentButton = new Button("Pay");
		Button homeButton = new Button("Home");
		
		Font myFont = new Font("Serif",Font.BOLD,14);
		placeNameLabel.setFont(myFont);
		vehiclenumberLabel.setFont(myFont);
		
		placeName.setBackground(Color.LIGHT_GRAY);
		placeName.setForeground(Color.BLACK);
		vehicleNumber.setBackground(Color.LIGHT_GRAY);
		vehicleNumber.setForeground(Color.BLACK);
		paymentButton.setBackground(Color.DARK_GRAY);
		homeButton.setBackground(Color.DARK_GRAY);
		paymentButton.setForeground(Color.WHITE);
		homeButton.setForeground(Color.WHITE);
		
		add(placeNameLabel);add(vehiclenumberLabel);
		add(placeName);add(vehicleNumber);
		add(paymentButton);add(homeButton);
		
		placeNameLabel.setBounds(75,100,100,15);
		placeName.setBounds(185,100,215,24);
		vehiclenumberLabel.setBounds(75,150,100,15);
		vehicleNumber.setBounds(185,150,215,24);
		paymentButton.setBounds(300,224,100,40);
		homeButton.setBounds(185,224,100,40);
		
		paymentButton.addActionListener(this);
		homeButton.addActionListener(this);
		placeName.addItemListener(this);
		vehicleNumber.addItemListener(this);

		placeList();
	}

	// listens for button click
	public void actionPerformed(ActionEvent ae) {
		String clickedButton = ae.getActionCommand();
		String salutation = home.getUsername();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
   		LocalDateTime now = LocalDateTime.now(); 
		if(clickedButton.equals("Pay")) {
			// validation: checking for empty inputs
			if(place == "Select" || vehicleno == "Select") {
				JOptionPane.showMessageDialog(this, "All the fields are required");
			} else {
				DbConnection da = new DbConnection();
				// collecting price to put in the transaction table
				String q = "SELECT price from toll WHERE place = '" + place + "'";
				String userid = "Select userid from users where username = '"+ salutation +"'";
				//String vehicleID = "SELECT VEHICLEID from VEHICLE WHERE VEHICLENUMBER = '" + vehicleno + "'";
				int vehicleID = 0;
				ResultSet rs = null;
				//ResultSet rs2 = null;
				ResultSet rs3 = null;		
				try (CallableStatement stmt = da.conn.prepareCall("{call p_getvehicleid(?, ?)}")) {
					stmt.setString(1, vehicleno);
				    stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
				    stmt.execute();
				    vehicleID = stmt.getInt(2);
					// rs2 = da.getData(vehicleID);
					// while(rs2.next()){
					// 	vehicleID = rs2.getString("VEHICLEID");
					// }
					rs3 = da.getData(userid);
					while(rs3.next()){
						userid = rs3.getString("userid");
					}
					rs = da.getData(q);
					if(rs.next()){
						int amount = Integer.parseInt(rs.getString("price"));
						//System.out.println("INSERT INTO TRANSACTION(TID, PLACENAME, AMOUNT, TIME, USERID, VEHICLENUMBER, VEHICLEID) VALUES (seq_transaction.nextval, '" + place + "', '" + amount + "', '" + Date.valueOf(dtf.format(now)) + "', '" + userid + "','" + vehicleno + "', '" + vehicleID + "')");
						String q3 = "INSERT INTO TRANSACTION(TID, PLACENAME, AMOUNT, TIME, USERID, VEHICLENUMBER, VEHICLEID) VALUES (seq_transaction.nextval, '" + place + "', '" + amount + "', SYSDATE, '" + userid + "','" + vehicleno + "', '" + Integer.toString(vehicleID) + "')";
						int updatedRows = da.updateDB(q3);
						if(updatedRows > 0) {
							JOptionPane.showMessageDialog(this, "Payment Successful");
							// setting dropsown fields to default values
							vehicleList(home.getUsername());
							placeList();
						} else {
							JOptionPane.showMessageDialog(this,"Invalid request.");
						}
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this, "DB Error");
				}				
			}
		}
		else if(clickedButton.equals("Home")) {
			// user home
			this.setVisible(false);
			home.profile.setVisible(true);
			home.profile.setParent(home);
		}
	}

	// showing dynamic place lists from DB as a dropdown
	public void placeList(){
		placeName.removeAll();
		placeName.add("Select");
		DbConnection da = new DbConnection();
		String q = "SELECT place from toll";
		ResultSet rs = null;		
		try {
			rs = da.getData(q);
			while(rs.next()){
				placeName.add(rs.getString("place"));
			}
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this,"DB Error");
		}
	}

	// showing list of vehicles as dropdown
	public void vehicleList(String username){
		vehicleNumber.removeAll();
		vehicleNumber.add("Select");
		DbConnection da = new DbConnection();
		ResultSet rs = null;	
		ResultSet rs2 = null;		
		try {
			String userid = "Select userid from users where username = '"+ username +"'";
			rs2 = da.getData(userid);
			while(rs2.next()){
				userid = rs2.getString("userid");
			}
			String q = "SELECT * from vehicle WHERE userid = '" + userid + "'";
			rs = da.getData(q);
			while(rs.next()){
				vehicleNumber.add(rs.getString("vehiclenumber"));
			}
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this,"DB Error");
		}
	} 

	// listens to dropdown value changes and handles exception by putting a default value
	public void itemStateChanged(ItemEvent ie){
		String v = (String)ie.getItem();
		if (ie.getSource() == placeName){
			try{
				place = v;
			} catch(Exception ex) {
				place = "Select";
			}
		} else if (ie.getSource() == vehicleNumber){
			try{
				vehicleno = v;
			} catch(Exception ex) {
				vehicleno = "Select";
			}
		}
	}
	
	public void setParent(Frame f) {parent=f;}
}