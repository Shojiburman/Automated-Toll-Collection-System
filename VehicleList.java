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
import java.sql.CallableStatement;



public class VehicleList extends JFrame implements ActionListener, ItemListener {
	private JTextField search;
	private Home home;
	private Frame parent;
	private JTable j;
	private DefaultTableModel tableModel;
	private String mNumber = "Select";
	Choice vehicleNumber = new Choice();
	

	public VehicleList(Home f) {
		super("Vehicle List");
		
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;

		Label deleteLabel = new Label("To delete, select a vehicle number first");
		
		vehicleNumber.addItemListener(this);
		vehicleNumber.setForeground(Color.BLACK);
		vehicleNumber.setBackground(Color.LIGHT_GRAY);
		vehicleNumber.setBounds(90,390,150,24);
		add(vehicleNumber);

		deleteLabel.setBounds(10,350,500,20);
		Font myFont = new Font("Serif",Font.BOLD,14);
		deleteLabel.setFont(myFont);
		add(deleteLabel);

		Button deleteButton = new Button("Delete Vehicle");
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setBackground(Color.DARK_GRAY);
		deleteButton.setBounds(260,380,150,40);
		add(deleteButton);
		deleteButton.addActionListener(this);
		deleteLabel.setAlignment(Label.CENTER);
		


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
		search.setBounds(230, 10, 145, 40);
		searchButton.setBounds(390, 10, 90, 40);
		
		searchButton.addActionListener(this);
		homeButton.addActionListener(this);
		logoutButton.addActionListener(this);
	}

    

	public void vehicleList(String username){
		String[] columnNames = {"Brand Name","Model", "Number", "Class", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
		DbConnection da = new DbConnection();
		String userid = "SELECT userid from users WHERE username = '" + username + "'";
		ResultSet rs = null;
		ResultSet rs2 = null;	
		try {
			rs2 = da.getData(userid);
			while(rs2.next()){
				userid = rs2.getString("userid");
			}
			String q = "SELECT * from vehicle WHERE userid = '" + Integer.parseInt(userid.trim()) + "'";
			rs = da.getData(q);
			while(rs.next()){
				String a = rs.getString("brandname");
				String b = rs.getString("vehiclemodel");
				String c = rs.getString("vehiclenumber");
				String d = rs.getString("vehicleclass");
				String e = rs.getString("vehicletype");
				String[] data = {a, b, c, d, e}; // preparing each row
				tableModel.addRow(data); // pushing the row to table
			}
			if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
				String[] data = {"No data yet", "No data yet", "No data yet", "No data yet", "No data yet"} ;
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

	// clearing the current table for new data
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
		String salutation = home.getUsername();
		if(clickedButton.equals("Search")){
			if(search.getText().equals("")) {
				JOptionPane.showMessageDialog(this,"Type First");
			}
			else {
				int rowCount = tableModel.getRowCount();
				// clearing the current table for new data
				for (int i = rowCount - 1; i >= 0; i--) {
				    tableModel.removeRow(i);
				}
				DbConnection da = new DbConnection();
				// search query
				ResultSet rs2 = null;
				ResultSet rs = null;		
				try {
					String userid = "select userid from users where username = '" + salutation + "'";
					rs2 = da.getData(userid);
					while(rs2.next()){
						userid = rs2.getString("userid");
					}
					String q = "SELECT * from vehicle WHERE userid = '" + Integer.parseInt(userid.trim()) + "' AND (brandname LIKE '%" + search.getText().trim() + "%' OR vehiclemodel LIKE '%" + search.getText().trim() + "%' OR vehiclenumber LIKE '%" + search.getText().trim() + "%' OR vehicleclass LIKE '%" + search.getText().trim() + "%' OR vehicletype LIKE '%" + search.getText().trim() + "%')";
					rs = da.getData(q);
					while(rs.next()){
						String a = rs.getString("brandname");
						String b = rs.getString("vehiclemodel");
						String c = rs.getString("vehiclenumber");
						String d = rs.getString("vehicleclass");
						String e = rs.getString("vehicletype");
						String[] data = {a, b, c, d, e} ; // preparing each row
						tableModel.addRow(data); // pushing the row to table
					}
					if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
						String[] data = {"No data found", "No data found", "No data found", "No data found", "No data found"} ;
						tableModel.addRow(data);
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}
			}
			
		}
		else if(clickedButton.equals("Home")) {
			home.profile.setVisible(true);
			home.profile.setParent(this);
			this.setVisible(false);
		} else if(clickedButton.equals("Delete Vehicle")){
            if(mNumber != "Select"){ // validation: checking empty input
                DbConnection da = new DbConnection();
                // validation: done
				//String q5 = "DELETE FROM vehicle WHERE vehiclenumber = '" + mNumber + "'";	
				//da.updateDB(q5);
				try (CallableStatement stmt = da.conn.prepareCall("{call p_delete_vehicle(?, ?)}")) {
						    stmt.setString(1, mNumber);
						    stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
						    stmt.execute();
						    //userid = stmt.getInt(2);
						} catch(Exception ex) {
							JOptionPane.showMessageDialog(this, ex);
						}
				JOptionPane.showMessageDialog(this,"Vehicle Deleted");
            } else{
                JOptionPane.showMessageDialog(this,"Please, select a vehicle number to delete");
            }
		} else if(clickedButton.equals("Refresh")) {
			vehicleList(salutation);
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
			String userid = "SELECT userid from users WHERE username = '" + username + "'";
			rs2 = da.getData(userid);
			while(rs2.next()){
				userid = rs2.getString("userid");
			}
			String q = "SELECT * from vehicle WHERE userid = '" + Integer.parseInt(userid.trim()) + "'";
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