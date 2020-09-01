import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;


public class Login extends JFrame implements ActionListener {
	TextField  username, pass;
	private Home home;
	private Frame parent;
	
	public Login(Home f) {
		super("Log in");
		
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
		
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		home = f;
		Label usernameLabel = new Label("User Name");
		Label passLabel = new Label("Password");
		Label askForSignupLabel = new Label("Don't have an account?");
		username = new TextField (24);
		pass = new TextField (24);
		pass.setEchoChar('*');
		
		username.setBackground(Color.LIGHT_GRAY);
		username.setForeground(Color.BLACK);
		pass.setBackground(Color.LIGHT_GRAY);
		pass.setForeground(Color.BLACK);

		Button loginButton = new Button("Log in");
		Button homeButton = new Button("Home");
		Button signupButton = new Button("Sign up");
		
		loginButton.setBackground(Color.DARK_GRAY);
		homeButton.setBackground(Color.DARK_GRAY);
		signupButton.setBackground(Color.DARK_GRAY);
		loginButton.setForeground(Color.WHITE);
		homeButton.setForeground(Color.WHITE);
		signupButton.setForeground(Color.WHITE);
		
		add(usernameLabel);add(username);
		add(passLabel);add(pass);
		add(loginButton);add(homeButton);
		add(askForSignupLabel);add(signupButton);
		
		usernameLabel.setBounds(60,110,80,15);
		username.setBounds(150,110,260,24);
		passLabel.setBounds(60,155,80,40);
		pass.setBounds(150,165,260,24);
		loginButton.setBounds(285,230,100,40);
		homeButton.setBounds(165,230,100,40);
		askForSignupLabel.setBounds(200,270,150,40);
		signupButton.setBounds(225,310,100,40);
		
		
		Font myFont = new Font("Serif",Font.BOLD,14);
		Font myFont2 = new Font("Serif",Font.BOLD,12);
		pass.setFont(myFont2);
		usernameLabel.setFont(myFont);
		passLabel.setFont(myFont);
		askForSignupLabel.setFont(myFont);
		askForSignupLabel.setAlignment(Label.CENTER);
		
		loginButton.addActionListener(this);
		homeButton.addActionListener(this);
		signupButton.addActionListener(this);
	}

	// listens to button clicks
	public void actionPerformed(ActionEvent ae){
		String clickedButton = ae.getActionCommand();
		if(clickedButton.equals("Log in")){
			// validation: empty inputs
			if(username.getText().trim().length() == 0 || pass.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this,"Please, enter username & password");
			} else {
				DbConnection da = new DbConnection();
				String typedName = username.getText();
				String typedPass = pass.getText();
				// retirving password for the given username
				String q = "SELECT password from users WHERE username = '" + typedName + "'";
				ResultSet rs = null;		
				try {
					int adm = 0;	
					rs = da.getData(q);
					if(rs.next()){
						String p = rs.getString("password"); // password from DB
						if(p.equals(typedPass)){ // matching the given password with the password from DB
							home.setUsername(typedName); // successful login and so setting the username to use this as a pseudo session
							// query for checking whther the user is an "Admin" or a "user"
							//String getUserType = "SELECT admin from users WHERE username = '" + typedName + "'";
							//ResultSet rs2 = null;
							try (CallableStatement stmt = da.conn.prepareCall("{call p_isadmin(?, ?)}")) {
								stmt.setString(1, typedName);
							    stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
							    stmt.execute();
							    adm = stmt.getInt(2);
								//rs2 = da.getData(getUserType);
								//if(rs2.next()){
									//String userType = rs2.getString("admin");
									//JOptionPane.showMessageDialog(this,userType);
									if(adm == 0) { // user type user
										// redirecting to user home
										home.profile.setVisible(true);
										this.setVisible(false);
										home.profile.setParent(this);
										// clearing login fields to default
										username.setText("");
										pass.setText("");
									} else if (adm == 1) { // admin type user
										// redirecting to admin home
										home.admin.setVisible(true);
										home.admin.setParent(this);
										this.setVisible(false);
										// clearing login fields to default
										username.setText("");
										pass.setText("");
									}
								//}
							} catch(Exception ex){
								JOptionPane.showMessageDialog(this,"DB Error");
							}
						} else {
							JOptionPane.showMessageDialog(this,"Wrong credentials");
						}
					} else {
						JOptionPane.showMessageDialog(this,"Wrong credentials");
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(this,"DB Error");
				}
			}
		}
		else if(clickedButton.equals("Back")){
			home.setVisible(true);
			this.setVisible(false);
		}
		else if(clickedButton.equals("Sign up")){
			this.setVisible(false);
			home.signup.setVisible(true);
			home.signup.setParent(home);
			
		}
		else if(clickedButton.equals("Home")){
			// landing page
			this.setVisible(false);
			home.setVisible(true);
		}
	}

	public void setParent(Frame f){parent=f;}
}