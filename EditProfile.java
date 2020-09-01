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
import java.sql.CallableStatement;

public class EditProfile extends JFrame implements ActionListener {
	private TextField newPass, confirmPass, currentPass;
	private Home home;
	private Frame parent;
	
	public EditProfile(Home f) {
		super("Change Password");
		
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);

        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;
		String salutation = home.getUsername();
		Label salutationLabel = new Label(salutation);
		Label newPassLabel = new Label("New Password");
		Label confirmPassLabel = new Label("Confirm Password");
		Label currentPassLabel = new Label("Current Password");

		newPass = new TextField(12);
		confirmPass = new TextField(12);
		currentPass = new TextField(12);

		Button updateButton = new Button("Update");
		Button homeButton = new Button("Home");
		
		Font myFont = new Font("Serif",Font.BOLD,14);
		salutationLabel.setFont(myFont);
		newPassLabel.setFont(myFont);
		confirmPassLabel.setFont(myFont);
		currentPassLabel.setFont(myFont);
		
		newPass.setBackground(Color.LIGHT_GRAY);
		newPass.setForeground(Color.BLACK);
		confirmPass.setBackground(Color.LIGHT_GRAY);
		confirmPass.setForeground(Color.BLACK);
		currentPass.setBackground(Color.LIGHT_GRAY);
		currentPass.setForeground(Color.BLACK);
		
		
		updateButton.setBackground(Color.DARK_GRAY);
		homeButton.setBackground(Color.DARK_GRAY);
		updateButton.setForeground(Color.WHITE);
		homeButton.setForeground(Color.WHITE);
		
		add(salutationLabel);
		add(newPassLabel);add(newPass);
		add(confirmPassLabel);add(confirmPass);
		add(currentPassLabel);add(currentPass);
		add(updateButton);add(homeButton);
		
		salutationLabel.setBounds(10,10,100,15);
		newPassLabel.setBounds(70,84,100,15);
		newPass.setBounds(205,80,200,24);
		confirmPassLabel.setBounds(70,124,115,15);
		confirmPass.setBounds(205,120,200,24);
		currentPassLabel.setBounds(70,164,115,15);
		currentPass.setBounds(205,160,200,24);
		
		updateButton.setBounds(305,260,100,40);
		homeButton.setBounds(190,260,100,40);
		
		updateButton.addActionListener(this);
		homeButton.addActionListener(this);
	}

	// listens to the button clicks
	public void actionPerformed(ActionEvent ae) {
		String clickedButton = ae.getActionCommand();
		String salutation = home.getUsername();
		if(clickedButton.equals("Update")) {
			try {
				// validation: password being at least 6 characters
				if(newPass.getText().trim().length() < 6 || confirmPass.getText().trim().length() < 6) {
					JOptionPane.showMessageDialog(this,"Please, enter a password with 6 or more characters");
				} else if(newPass.getText().trim() == confirmPass.getText().trim()) { // validation: password match
					JOptionPane.showMessageDialog(this,"New passwords don't match");
				} else {
					DbConnection da = new DbConnection();
					// query for entering the current password as new
					String q1 = "SELECT username from users WHERE username = '" + salutation + "' AND password='" + newPass.getText() + "'";
					ResultSet rs = null;		
					try{
						rs = da.getData(q1);
						if(rs.next()){						
							JOptionPane.showMessageDialog(this,"Password can't be the same as the current one, try a new password");
						} else {
							// validation done: updating password to DB
							//String q5 = "UPDATE users SET password = '" + newPass.getText().trim() + "' WHERE username = '" + salutation + "' AND password ='" + currentPass.getText().trim() + "'";
							//da.updateDB(q5);
							try (CallableStatement stmt = da.conn.prepareCall("{call p_update_password(?,?,?,?)}")) {
							    stmt.setString(1, newPass.getText().trim());
							    stmt.setString(2, salutation);
							    stmt.setString(3, currentPass.getText().trim());
							    stmt.registerOutParameter(4, java.sql.Types.NUMERIC);
							    stmt.execute();
							    //userid = stmt.getInt(2);
							    JOptionPane.showMessageDialog(this,"Updated");
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(this, ex);
							}

							this.setVisible(false);
							home.profile.setVisible(true);
							home.profile.setParent(home);

							// clearing form fields to default
							newPass.setText("");
							confirmPass.setText("");
							currentPass.setText("");
						}
					}
					catch(Exception ex){
						JOptionPane.showMessageDialog(this,"DB Error");
					}
					
				}
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(this,"DB Error");
			}
		}
		else if(clickedButton.equals("Home")) {
			// home page for users
			this.setVisible(false);
			home.profile.setVisible(true);
			home.profile.setParent(home);
		}
	}
	
	public void setParent(Frame f) {parent=f;}
}