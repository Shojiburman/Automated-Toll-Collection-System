import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.*;  



public class UsersList extends JFrame implements ActionListener, ItemListener {
	private JTextField search;
	private Home home;
	private Frame parent;
	private JTable j;
	private DefaultTableModel tableModel;
	private int count;
	private String mNumber = "Select";
	Choice vehicleNumber = new Choice();
	
	public UsersList(Home f) {
		super("Users List");
		
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;

		vehicleNumber.addItemListener(this);

		search = new JTextField (40);
		search.setHorizontalAlignment(SwingConstants.CENTER);
		search.setBackground(Color.LIGHT_GRAY);
		search.setForeground(Color.BLACK);

		Button searchButton = new Button("Search");
		Button homeButton = new Button("Home");
		Button logoutButton = new Button("Refresh");

		searchButton.setBackground(Color.DARK_GRAY);
		homeButton.setBackground(Color.DARK_GRAY);
		logoutButton.setBackground(Color.DARK_GRAY);
		searchButton.setForeground(Color.WHITE);
		homeButton.setForeground(Color.WHITE);
		logoutButton.setForeground(Color.WHITE);
		
		add(search);
		add(searchButton);
		add(homeButton);
		add(logoutButton);
		
		homeButton.setBounds(10, 10, 100, 40);
		logoutButton.setBounds(120, 10, 100, 40);
		search.setBounds(230, 10, 150, 40);
		searchButton.setBounds(390, 10, 90, 40);
		
		searchButton.addActionListener(this);
		homeButton.addActionListener(this);
		logoutButton.addActionListener(this);
		


		Label deleteLabel = new Label("To delete, select a users first");
		
		
		vehicleNumber.setForeground(Color.BLACK);
		vehicleNumber.setBackground(Color.LIGHT_GRAY);
		vehicleNumber.setBounds(90,390,150,24);
		add(vehicleNumber);

		deleteLabel.setBounds(10,350,500,20);
		Font myFont = new Font("Serif",Font.BOLD,14);
		deleteLabel.setFont(myFont);
		add(deleteLabel);

		Button deleteButton = new Button("Delete Users");
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setBackground(Color.DARK_GRAY);
		deleteButton.setBounds(260,380,150,40);
		add(deleteButton);
		deleteLabel.setAlignment(Label.CENTER);
		deleteButton.addActionListener(this);





		String[] columnNames = { "User Name", "Full name", "Phone Number"};
        tableModel = new DefaultTableModel(columnNames, 0);
		DbConnection da = new DbConnection();
		String q = "SELECT username, fullname, phone FROM users WHERE admin = 0";
		ResultSet rs = null;		
		try {
			rs = da.getData(q);
			while(rs.next()) {
				String a = rs.getString("username");
				String b = rs.getString("fullname");
				String c = rs.getString("phone");
				String[] data = {a, b, c}; // preparing each row
				tableModel.addRow(data); // pushing the row to table
			}
			if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
				String[] data = {"No data yet", "No data yet", "No data yet"} ;
				tableModel.addRow(data);
			}
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this,"DB Error");
		}
        j = new JTable(tableModel); 
        j.setDefaultEditor(Object.class, null);
        j.setAutoCreateRowSorter(true);
        JScrollPane sp = new JScrollPane(j); 
        sp.setBounds(0, 60, 485, 280); 
        add(sp);


	}

	// listens to button clicks
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
		if(clickedButton.equals("Search")){
			count = 0;
			// clearing the current table for new data
			if(search.getText().equals("")) {
				JOptionPane.showMessageDialog(this,"Type First");
			}
			else{
				int rowCount = tableModel.getRowCount();
				for (int i = rowCount - 1; i >= 0; i--) {
				    tableModel.removeRow(i);
				}
				DbConnection da = new DbConnection();
				// search query
				String q = "SELECT * from users WHERE username LIKE '%" + search.getText().trim() + "%' OR fullname LIKE '%" + search.getText().trim() + "%' OR phone LIKE '%" + search.getText().trim() + "%'";
				ResultSet rs = null;		
				try {
					rs = da.getData(q);
					while(rs.next()){
						String a = rs.getString("username");
						String b = rs.getString("fullname");
						String c = rs.getString("phone");
						String[] data = {a, b, c}; // preparing each row
						tableModel.addRow(data); // pushing the row to table
					} 
					if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
						String[] data = {"No data found", "No data found", "No data found"} ;
						tableModel.addRow(data);
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}
			}
		}
		else if(clickedButton.equals("Home")) {
			// admin home page
			count = 0;
			home.admin.setVisible(true);
			home.admin.setParent(this);
			this.setVisible(false);
		}
		else if(clickedButton.equals("Refresh")) {
			count += 1;
			if(count < 2){
				int rowCount = tableModel.getRowCount();
				for (int i = rowCount - 1; i >= 0; i--) {
				    tableModel.removeRow(i);
				}
				DbConnection da = new DbConnection();
				// search query
				String q = "SELECT * from users";
				ResultSet rs = null;		
				try {
					rs = da.getData(q);
					while(rs.next()){
						String a = rs.getString("username");
						String b = rs.getString("fullname");
						String c = rs.getString("phone");
						String[] data = {a, b, c}; // preparing each row
						tableModel.addRow(data); // pushing the row to table
					} 
					if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
						String[] data = {"No data found", "No data found", "No data found"} ;
						tableModel.addRow(data);
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}		
			}
			else {
				JOptionPane.showMessageDialog(this,"Refreshed");
			}
		}
		else if(clickedButton.equals("Delete Users")){
			if(mNumber != "Select"){ // validation: checking empty input
                DbConnection da = new DbConnection();
                // validation: done
				String q5 = "DELETE FROM users WHERE username = '" + mNumber + "'";	
				int updatedRows = da.updateDB(q5);
				if(updatedRows > 0) {
					JOptionPane.showMessageDialog(this,"Users Deleted");
				} else {
					JOptionPane.showMessageDialog(this,"Invalid request.");
				}
            } else{
            	JOptionPane.showMessageDialog(this,mNumber);
                JOptionPane.showMessageDialog(this,"Please, select a users to delete");
            }
		}
	}

	// showing list of vehicles as dropdown
	public void deleteVehicleList(String username){
		vehicleNumber.removeAll();
		vehicleNumber.add("Select");
		DbConnection da = new DbConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;		
		try {
			String q = "SELECT username from users where admin = 0";
			rs = da.getData(q);
			while(rs.next()){
				vehicleNumber.add(rs.getString("username"));
			}
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this,"DB Error");
		}
	}



	// listens to dropdown value changes and handles exception by putting a default value
	public void itemStateChanged(ItemEvent ie){
		String v = (String)ie.getItem();
		if (ie.getSource() == vehicleNumber){
			try{
				mNumber = v;
			} catch(Exception ex) {
				mNumber = "Select";
			}
		}
	}

	
	public void setParent(Frame f) {parent=f;}
}