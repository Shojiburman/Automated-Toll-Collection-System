import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateToll extends JFrame implements ActionListener, ItemListener{
	private TextField  newPlaceName, newTollFee;
	private Home home;
	private Frame parent;
	private String place = "Select";
	private String currentPlace = "Select";
	Choice placeName = new Choice();
	Choice currentPlaceName = new Choice();

	public UpdateToll(Home f) {
		super("Manage Toll");
		home = f;

		setLayout(null);
		setSize(500,500);
		setLocation(400,100);

        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		
		String salutation = home.getUsername();
		Label salutationLabel = new Label(salutation);
		
		Label newPlaceNameLabel = new Label("New Place Name");
		newPlaceName = new TextField (24);
		newPlaceName.setBackground(Color.LIGHT_GRAY);
		newPlaceName.setForeground(Color.BLACK);

		Label currentPlaceNameLabel = new Label("Current Place Name");
		currentPlaceName.setBackground(Color.LIGHT_GRAY);
		currentPlaceName.setForeground(Color.BLACK);

		Label newTollFeeLabel = new Label("New Toll Fee");
		newTollFee = new TextField (24);
		newTollFee.setBackground(Color.LIGHT_GRAY);
		newTollFee.setForeground(Color.BLACK);

		//placeName = new JTextField (40);
		
		placeName.setBackground(Color.LIGHT_GRAY);
		placeName.setForeground(Color.BLACK);

		Button homeButton = new Button("Home");
		Button submitButton = new Button("Update");
		Button deleteButton = new Button("Delete Place");
		
		homeButton.setBackground(Color.DARK_GRAY);
		submitButton.setBackground(Color.DARK_GRAY);
		deleteButton.setBackground(Color.DARK_GRAY);
		homeButton.setForeground(Color.WHITE);
		submitButton.setForeground(Color.WHITE);
		deleteButton.setForeground(Color.WHITE);
		
		add(newPlaceNameLabel);
		add(currentPlaceNameLabel);
		add(newTollFeeLabel);
		add(newPlaceName);
		add(currentPlaceName);
		add(newTollFee);
		add(homeButton);
		add(submitButton);
		add(salutationLabel);
		add(placeName);
		add(deleteButton);
		
		newPlaceNameLabel.setBounds(60,140,130,24);
		newPlaceName.setBounds(200,140,220,24);
		currentPlaceNameLabel.setBounds(60,170,130,24);
		currentPlaceName.setBounds(200,170,220,24);

		newTollFeeLabel.setBounds(60,200,130,24);
		newTollFee.setBounds(200,200,220,24);

		placeName.setBounds(60,68,220,40);

		homeButton.setBounds(200,260,100,40);
		submitButton.setBounds(320,260,100,40);
		deleteButton.setBounds(300,60,120,40);
		salutationLabel.setBounds(10,10,150,20);
		
		
		Font myFont = new Font("Serif",Font.BOLD,14);
		Font myFont2 = new Font("Serif",Font.BOLD,12);
		newPlaceNameLabel.setFont(myFont);
		newTollFeeLabel.setFont(myFont);
		newPlaceName.setFont(myFont2);
		newTollFee.setFont(myFont2);
		currentPlaceNameLabel.setFont(myFont);
		salutationLabel.setFont(myFont);
		
		homeButton.addActionListener(this);
		submitButton.addActionListener(this);	
		deleteButton.addActionListener(this);
		placeName.addItemListener(this);
		currentPlaceName.addItemListener(this);
		placeList();
	}

	// listens for button clicks
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
		if(clickedButton.equals("Update")) {
			try {
				int pass = Integer.parseInt(newTollFee.getText().trim());
				// validation: empty fields
				if(newPlaceName.getText().trim().length() == 0 || newTollFee.getText().trim().length() == 0 || currentPlace == "Select") {
					JOptionPane.showMessageDialog(this,"Please, fill up all fields");
				} 
				else if(pass < 0){
					JOptionPane.showMessageDialog(this,"Please, enter positive valid price");
				}
				else {
					DbConnection da = new DbConnection();
					// checking for duplicate entries toll place is unique
					String q1 = "SELECT place from toll WHERE place = '" + newPlaceName.getText().trim() + "'";
					ResultSet rs = null;
					try {
						rs = da.getData(q1);
						if(rs.next()){						
							JOptionPane.showMessageDialog(this,"Name already exists, please, try something new");
						} else{
							//validation: all done
							String q5 = "UPDATE toll SET place = '" + newPlaceName.getText().trim() + "', price = '" + newTollFee.getText().trim() + "' WHERE place = '" + currentPlace + "'";
							int updatedRows = da.updateDB(q5);
							if(updatedRows > 0) {
								JOptionPane.showMessageDialog(this,"Place Updated");
								// resetting everything to default
								placeList();
								newPlaceName.setText("");
								newTollFee.setText("");
							} else {
								JOptionPane.showMessageDialog(this,"Invalid request.");
							}
						}
					}
					catch(Exception ex) {
						JOptionPane.showMessageDialog(this,"DB Error");
					}
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,"Invalid input");
			}
		}
		else if(clickedButton.equals("Home")) {
			// admin home page
			this.setVisible(false);
			home.admin.setParent(this);
			home.admin.setVisible(true);
		}
		else if(clickedButton.equals("Delete Place")) {
			// validation: checking for non-selection on the data table
			if(place == "Select" || place.trim() == "") {
				JOptionPane.showMessageDialog(this,"Please, select a place");
			} else {
				DbConnection da = new DbConnection();
				String q5 = "DELETE FROM toll WHERE place = '" + place + "'";	
				int updatedRows = da.updateDB(q5);
				if (updatedRows > 0) {
					JOptionPane.showMessageDialog(this,"Place Deleted");
					// updating placelist
					placeList();
				} else {
					JOptionPane.showMessageDialog(this,"Invalid request.");
				}
			}
		}
	}

	// populating placelist dropdown
	public void placeList(){
		placeName.removeAll();
		placeName.add("Select");
		// resetting for every reload
		currentPlaceName.removeAll();
		currentPlaceName.add("Select");
		DbConnection da = new DbConnection();
		String q = "SELECT place from toll";
		ResultSet rs = null;		
		try {
			rs = da.getData(q);
			while(rs.next()){
				placeName.add(rs.getString("place"));
				currentPlaceName.add(rs.getString("place"));
			}
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this,"DB Error");
		}
	}

	// listens for dropdown selection change 
	public void itemStateChanged(ItemEvent ie){
		String v = (String)ie.getItem();
		if (ie.getSource() == placeName){
			try{
				place = v;
			} catch(Exception ex) {
				place = "Select"; // setting fallback values(exception handling)
			}
		} else if (ie.getSource() == currentPlaceName){
			try{
				currentPlace = v;
			} catch(Exception ex) {
				currentPlace = "Select"; // setting fallback values(exception handling)
			}
		}
	}

	public void setParent(Frame f) {parent=f;}
}