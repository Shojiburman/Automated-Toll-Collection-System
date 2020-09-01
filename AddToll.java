import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddToll extends JFrame implements ActionListener{
	private TextField  placeName, tollFee;
	private Home home;
	private Frame parent;

	public AddToll(Home f) {
		super("Add Toll");
		home = f;

		setLayout(null);
		setSize(500,500);
		setLocation(400,100);

        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		
		String salutation = home.getUsername();
		Label salutationLabel = new Label(salutation);
		
		Label placeNameLabel = new Label("Place Name");
		placeName = new TextField (24);
		placeName.setBackground(Color.LIGHT_GRAY);
		placeName.setForeground(Color.BLACK);

		Label tollFeeLabel = new Label("Toll Fee");
		tollFee = new TextField (24);
		tollFee.setBackground(Color.LIGHT_GRAY);
		tollFee.setForeground(Color.BLACK);

		Button homeButton = new Button("Home");
		Button submitButton = new Button("Submit");
		
		homeButton.setBackground(Color.DARK_GRAY);
		submitButton.setBackground(Color.DARK_GRAY);
		homeButton.setForeground(Color.WHITE);
		submitButton.setForeground(Color.WHITE);
		
		add(placeNameLabel);
		add(placeName);
		add(tollFeeLabel);
		add(tollFee);
		add(homeButton);
		add(submitButton);
		add(salutationLabel);
		
		placeNameLabel.setBounds(60,100,100,15);
		placeName.setBounds(160,100,250,24);
		tollFeeLabel.setBounds(60,130,100,40);
		tollFee.setBounds(160,130,250,24);
		homeButton.setBounds(160,220,100,40);
		submitButton.setBounds(305,220,100,40);
		salutationLabel.setBounds(10,10,150,20);
		
		
		Font myFont = new Font("Serif",Font.BOLD,14);
		Font myFont2 = new Font("Serif",Font.BOLD,12);
		placeName.setFont(myFont2);
		tollFee.setFont(myFont2);
		placeNameLabel.setFont(myFont);
		tollFeeLabel.setFont(myFont);
		salutationLabel.setFont(myFont);
		
		homeButton.addActionListener(this);
		submitButton.addActionListener(this);		
	}
	// listens to the button clicks
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
		if(clickedButton.equals("Submit")) { 
			try {
				int pass = Integer.parseInt(tollFee.getText().trim());
				// validation: checks for empty values
				if(placeName.getText().trim().length() == 0 || tollFee.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(this,"Please, enter place name & price");
				} 
				else if(Integer.parseInt(tollFee.getText().trim()) < 0){
					JOptionPane.showMessageDialog(this,"Please, enter positive valid price");
				}
				else {
					DbConnection da = new DbConnection(); // DB connection
					// query for checking duplicate entry(place should be unique)
					String q1 = "SELECT place from toll WHERE place = '" + placeName.getText().trim() + "'";
					ResultSet rs = null;		
					try {
						rs = da.getData(q1);
						if(rs.next()){				
							JOptionPane.showMessageDialog(this,"Place exists, please, try a new place name");
						} else {
							// all validation done: inserts data into DB 
							String q5 = "INSERT INTO toll (TOLLID, PLACE, PRICE) VALUES (toll_seq.nextval, '" + placeName.getText().trim() + "','" + Integer.parseInt(tollFee.getText()) + "')";
							int updatedRows = da.updateDB(q5);
							if (updatedRows > 0) {
								JOptionPane.showMessageDialog(this,"Place Added");
								// clearing form fields and setting them to default values
								home.updateToll.placeList(); // after adding a new toll place updating the toll list table
								placeName.setText("");
								tollFee.setText("");
							} else {
								JOptionPane.showMessageDialog(this,"Invalid request.");
							}
							
						}
					}
					catch(Exception ex) {
						JOptionPane.showMessageDialog(this,"DB Error");
					}
				}
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this,"Invalid input");
			}
		}
		else if(clickedButton.equals("Home")) {
			// home screen for admin
			this.setVisible(false);
			home.admin.setParent(this);
			home.admin.setVisible(true);
		}
	}

	public void setParent(Frame f) {parent = f;}
}