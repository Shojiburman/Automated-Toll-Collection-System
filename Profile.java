import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile extends JFrame implements ActionListener {
	private Home home;
	private Frame parent;
	
	public Profile(Home f) {
		super("Profile");
		
		setLayout(new FlowLayout());
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;
		String salutation = home.getUsername();
		Label l = new Label("Account Balance");
		Label salutationLabel = new Label(salutation);

		Button addVehicleButton = new Button("Add Vehicle");
		Button paymentButton = new Button("Pay Toll");
		Button vehicleListButton = new Button("Vehicle List");
		Button tollListButton = new Button("Toll List");
		Button reportButton = new Button("Transaction List");
		Button editprofileButton = new Button("Change Password");
		Button logoutButton = new Button("Log out");
		
		addVehicleButton.setBackground(Color.DARK_GRAY);
		paymentButton.setBackground(Color.DARK_GRAY);
		vehicleListButton.setBackground(Color.DARK_GRAY);
		tollListButton.setBackground(Color.DARK_GRAY);
		reportButton.setBackground(Color.DARK_GRAY);
		editprofileButton.setBackground(Color.DARK_GRAY);
		logoutButton.setBackground(Color.DARK_GRAY);
		addVehicleButton.setForeground(Color.WHITE);
		paymentButton.setForeground(Color.WHITE);
		vehicleListButton.setForeground(Color.WHITE);
		tollListButton.setForeground(Color.WHITE);
		reportButton.setForeground(Color.WHITE);
		editprofileButton.setForeground(Color.WHITE);
		logoutButton.setForeground(Color.WHITE);
		
		add(l);
		add(salutationLabel);
		add(addVehicleButton);
		add(paymentButton);
		add(vehicleListButton);
		add(tollListButton);
		add(reportButton);
		add(editprofileButton);
		add(logoutButton);
		
		//l.setBounds(0,150,480,40);
		salutationLabel.setBounds(10,10,100,40);
		addVehicleButton.setBounds(60,60,160,40);
		vehicleListButton.setBounds(280,60,160,40);
		paymentButton.setBounds(60,120,160,40);
		tollListButton.setBounds(280,120,160,40);
		reportButton.setBounds(60,180,160,40);
		editprofileButton.setBounds(280,180,160,40);
		logoutButton.setBounds(170,240,160,40);
		
		Font myFont = new Font("Serif",Font.BOLD,24);
		Font myFont2 = new Font("Serif",Font.BOLD,12);

		l.setFont(myFont);
		salutationLabel.setFont(myFont);
		l.setAlignment(Label.CENTER);
		
		addVehicleButton.addActionListener(this);
		paymentButton.addActionListener(this);
		vehicleListButton.addActionListener(this);
		tollListButton.addActionListener(this);
		reportButton.addActionListener(this);
		editprofileButton.addActionListener(this);
		logoutButton.addActionListener(this);

		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
	}

	// listens to the button clicks for page navigations
	public void actionPerformed(ActionEvent ae) {
		String clickedButton = ae.getActionCommand();
		if(clickedButton.equals("Add Vehicle")) {
			this.setVisible(false);
			home.vehicle.setParent(this);
			home.vehicle.setVisible(true);
		} 
		else if(clickedButton.equals("Pay Toll")) {
			this.setVisible(false);
			home.payment.setParent(this);
			home.payment.setVisible(true);	
			home.payment.vehicleList(home.getUsername());	
		}
		else if(clickedButton.equals("Vehicle List")) {
			this.setVisible(false);
			home.vehicleList.setParent(this);
			home.vehicleList.setVisible(true);
			home.vehicleList.vehicleList(home.getUsername());
			home.vehicleList.deleteVehicleList(home.getUsername());
		}
		else if(clickedButton.equals("Toll List")) {
			this.setVisible(false);
			home.tollList.setParent(this);
			home.tollList.setVisible(true);		
		}
		else if(clickedButton.equals("Change Password")) {
			this.setVisible(false);
			home.editProfile.setParent(this);
			home.editProfile.setVisible(true);
		}
		else if(clickedButton.equals("Transaction List")) {
			this.setVisible(false);
			home.userReport.setParent(this);
			home.userReport.setVisible(true);
			home.userReport.transactionList(home.getUsername());
		}
		else if(clickedButton.equals("Log out")) {
			this.setVisible(false);
			home.setVisible(true);
		}
	}

	public void setParent(Frame f) {parent=f;}
}