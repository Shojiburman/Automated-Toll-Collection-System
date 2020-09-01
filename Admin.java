import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends JFrame implements ActionListener {
	private Home home;
	private Frame parent;
	
	public Admin(Home f) {
		super("Admin Panel");
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
		
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;
		String salutation = home.getUsername();
		Label salutationLabel = new Label(salutation);

		Button addTollButton = new Button("Add Toll");
		Button updateTollButton = new Button("Manage Toll");
		Button transactionListButton = new Button("Transaction List");
		Button usersListButton = new Button("Users List");
		Button tollListButton = new Button("Toll List");
		Button logoutButton = new Button("Logout");
		
		addTollButton.setBackground(Color.DARK_GRAY);
		updateTollButton.setBackground(Color.DARK_GRAY);
		transactionListButton.setBackground(Color.DARK_GRAY);
		usersListButton.setBackground(Color.DARK_GRAY);
		tollListButton.setBackground(Color.DARK_GRAY);
		logoutButton.setBackground(Color.DARK_GRAY);
		addTollButton.setForeground(Color.WHITE);
		updateTollButton.setForeground(Color.WHITE);
		transactionListButton.setForeground(Color.WHITE);
		usersListButton.setForeground(Color.WHITE);
		tollListButton.setForeground(Color.WHITE);
		logoutButton.setForeground(Color.WHITE);
		
		add(salutationLabel);add(addTollButton);add(updateTollButton);
		add(transactionListButton);add(usersListButton);
		add(tollListButton);add(logoutButton);
		
		salutationLabel.setBounds(10,10,100,40);
		addTollButton.setBounds(140,60,200,40);
		updateTollButton.setBounds(140,120,200,40);
		tollListButton.setBounds(140,180,200,40);
		usersListButton.setBounds(140,240,200,40);
		transactionListButton.setBounds(140,300,200,40);
		logoutButton.setBounds(140,360,200,40);
		
		Font myFont = new Font("Serif",Font.BOLD,24);
		Font myFont2 = new Font("Serif",Font.BOLD,12);

		salutationLabel.setFont(myFont);
		
		addTollButton.addActionListener(this);
		updateTollButton.addActionListener(this);
		transactionListButton.addActionListener(this);
		usersListButton.addActionListener(this);
		tollListButton.addActionListener(this);
		logoutButton.addActionListener(this);
	}

	// listens to the button clicks for page navigations
	public void actionPerformed(ActionEvent ae) {
		String clickedButton=ae.getActionCommand();
		if(clickedButton.equals("Add Toll")) {
			this.setVisible(false);
			home.addToll.setParent(this);
			home.addToll.setVisible(true);
		} 
		else if(clickedButton.equals("Manage Toll")) {
			this.setVisible(false);
			home.updateToll.setParent(this);
			home.updateToll.setVisible(true);
		}
		else if(clickedButton.equals("Transaction List")) {
			this.setVisible(false);
			home.report.setParent(this);
			home.report.setVisible(true);		
		}
		else if(clickedButton.equals("Toll List")) {
			this.setVisible(false);
			home.tollList.setParent(this);
			home.tollList.setVisible(true);		
		}
		else if(clickedButton.equals("Users List")) {
			this.setVisible(false);
			home.users.setParent(this);
			home.users.deleteVehicleList(home.getUsername());
			home.users.setVisible(true);		
		}
		else if(clickedButton.equals("Logout")) {	
			this.setVisible(false);
			home.setVisible(true);	
		}
	}
	
	public void setParent(Frame f) {parent=f;}
}