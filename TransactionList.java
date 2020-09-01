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



public class TransactionList extends JFrame implements ActionListener {
	private JTextField search;
	private Home home;
	private Frame parent;
	private JTable j;
	private DefaultTableModel tableModel;

	public TransactionList(Home f) {
		super("Transaction List");
		
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

		transactionList();

		//int deleteConfirm = JOptionPane.showConfirmDialog(null, "Are you sure?");
	}

    
	// preparing data table for all the transactions 
	public void transactionList(){
		String[] columnNames = {"User ID","Place Name", "Vehicle Number", "Vehicle ID", "Price", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
		DbConnection da = new DbConnection();
		String q = "SELECT * from TRANSACTION";
		ResultSet rs = null;		
		try {
			rs = da.getData(q);
			while(rs.next()){
				String a = rs.getString("USERID");
				String b = rs.getString("placeName");
				String c = rs.getString("VEHICLENUMBER");
				String d = rs.getString("VEHICLEID");
				String e = rs.getString("AMOUNT");
				String f = rs.getString("TIME");
				String[] data = {a, b, c, d, e, f}; // preparing each row
				tableModel.addRow(data); // pushing the roe to table
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
        sp.setBounds(0, 60, 485, 440); 
        add(sp);
	}

	// listents for button clicks
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
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
				ResultSet rs = null;
				try {
					String q = "SELECT * from transaction WHERE userid = '" + search.getText().trim() + "' OR vehicleid = '" + search.getText().trim() + "' OR placeName LIKE '%" + search.getText().trim() + "%' OR vehiclenumber LIKE '%" + search.getText().trim() + "%' OR amount LIKE '%" + search.getText().trim() + "%' OR time LIKE '%" + search.getText().trim() + "%'";
					rs = da.getData(q);
					while(rs.next()){
						String a = rs.getString("USERID");
						String b = rs.getString("placeName");
						String c = rs.getString("VEHICLENUMBER");
						String d = rs.getString("VEHICLEID");
						String e = rs.getString("AMOUNT");
						String f = rs.getString("TIME");
						String[] data = {a, b, c, d, e, f}; // preparing each row
						tableModel.addRow(data); // pushing the roe to table
					}
					if (tableModel.getRowCount() == 0) { // checking for empty table and showing no data labels
						String[] data = {"No data found", "No data found", "No data found", "No data found"} ;
						tableModel.addRow(data);
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}
			}
			
		}
		else if(clickedButton.equals("Home")) {
			home.admin.setVisible(true);
			home.admin.setParent(this);
			this.setVisible(false);
		} else if(clickedButton.equals("Refresh")) {
			transactionList();
		}
	}
	
	public void setParent(Frame f) {parent=f;}
}