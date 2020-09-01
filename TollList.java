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



public class TollList extends JFrame implements ActionListener {
	private JTextField search;
	private Home home;
	private Frame parent;
	private JTable j;
	private DefaultTableModel tableModel;

	public TollList(Home f) {
		super("Toll List");
		
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;

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

		tollList();

		//int deleteConfirm = JOptionPane.showConfirmDialog(null, "Are you sure?");
	}

    
	// prepares list of places with fares where there's a toll applicable
	public void tollList(){
		String[] columnNames = { "Place Name", "Toll Fee"};
        tableModel = new DefaultTableModel(columnNames, 0);
		DbConnection da = new DbConnection();
		String q = "SELECT place, price from toll";
		ResultSet rs = null;		
		try {
			rs = da.getData(q);
			while(rs.next()){
				String a = rs.getString("place");
				String b = rs.getString("price");
				String[] data = {a, b} ; // preparing each row
				tableModel.addRow(data); // pushing the roe to table
			}
			if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
				String[] data = {"No data yet", "No data yet"} ;
				tableModel.addRow(data);
			}
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this,"DB Error");
		}
        j = new JTable(tableModel); 
        j.setDefaultEditor(Object.class, null);
        j.setAutoCreateRowSorter(true);
        JScrollPane sp = new JScrollPane(j); 
        sp.setBounds(0, 60, 485, 440); 
        add(sp);
	}

	// listens for a button click
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
		if(clickedButton.equals("Search")){
			//String srh = search.getText().trim();
			if(search.getText().equals("")) {
				JOptionPane.showMessageDialog(this,"Type First");
			}
			else{
				int rowCount = tableModel.getRowCount();
				// clearing the current table for new data
				for (int i = rowCount - 1; i >= 0; i--) {
				    tableModel.removeRow(i);
				}
				DbConnection da = new DbConnection();
				// search query 
				String q = "SELECT * from toll WHERE place LIKE '%" + search.getText().trim() + "%' OR price LIKE '%" + search.getText().trim() + "%'";
				ResultSet rs = null;		
				try {
					rs = da.getData(q);
					while(rs.next()){
						String a = rs.getString("place");
						String b = rs.getString("price");
						String[] data = {a, b} ; // preparing each row
						tableModel.addRow(data); // pushing the roe to table
					}
					if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
						String[] data = {"No data found", "No data found"} ;
						tableModel.addRow(data);
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}
			}
		}
		else if(clickedButton.equals("Home")) {
			DbConnection da = new DbConnection();
			String salutation = home.getUsername();
			String getUserType = "SELECT admin from users WHERE username = '" + salutation + "'";
			ResultSet rs = null;
			try {
				rs = da.getData(getUserType);
				if(rs.next()) {
					String userType = rs.getString("admin");
					if(userType.equals("0")) { // user type user
						// this is a common page for both admin and user and so controlling home page redirection dynamically
						home.profile.setVisible(true);
						this.setVisible(false);
						home.profile.setParent(this);
					} else if (userType.equals("1")) { // admin type user
						home.admin.setVisible(true);
						home.admin.setParent(this);
						this.setVisible(false);
					}
				}
			} catch(Exception ex){
				JOptionPane.showMessageDialog(this,"DB Error");
			}
			
			
		} 
		else if(clickedButton.equals("Refresh")) {
			tollList();
		}
	}
	
	public void setParent(Frame f) {parent=f;}
}