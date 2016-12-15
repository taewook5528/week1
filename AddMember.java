/*
 *filename : AddMember.java
 *author : team Tic Toc
 *since : 2016.11.30
 *purpose/function : Pop-up window when pressing button. Warehouse 혹은 store를 WMS 시스템에 추가할 때 사용되는 기능을 갖춘 클래스이다.
 *					새로운 warehouse 혹은 store를 WMS에 입력할 때, ID와 password, 위도, 경도, 주소, 관리자, 연락처를 입력해야한다.
 *					하지만, 위도와 경도는 주소를 입력하면 google Map API를 활용해서 자동으로 입력되도록 개발하였다. 
 *					이로써 사용자의 편의성을 높이고자 하였다.
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/*새로운 warehouse 혹은 store를 추가하는 클래스 */
public abstract class AddMember extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7710271073006436030L;
	private JPanel contentPane;
	private JTextField idField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_Re;
	private JTextField addressField;
	private JTextField latitudeField;
	private JTextField longitudeField;
	private JButton btnOK, btnCancel, btnConvert;
	private int memberKind;
	private JLabel lblOwner;
	private JTextField ownerField;
	private JLabel lblContactNumber;
	private JTextField contactField;

	/**
	 * Create the frame.
	 */
	/*constructor of AddMember*/
	public AddMember(boolean isStore) {
		memberKind = isStore ? 2 : 1; // if the adding member is a store,
										// memberKind is 2. else it is 1.
		setBounds(100, 100, 315, 350);
		setVisible(true);

		if (isStore) // set frame's title by isStore value.
			setTitle("Add a store");
		else
			setTitle("Add a warehouse");

		/*set AddMember window frame*/
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/*set JTextField to input store's ID or Warehouse's ID*/
		JLabel lbl_ID = new JLabel("ID");
		lbl_ID.setBounds(36, 10, 21, 15);
		contentPane.add(lbl_ID);

		idField = new JTextField();
		idField.setBounds(128, 7, 159, 21);
		contentPane.add(idField);
		idField.setColumns(10);

		/*set JTextField to input password when adding new member*/
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(12, 56, 109, 15);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(128, 53, 159, 21);
		contentPane.add(passwordField);

		/*set JTextField to check password again when adding new member*/
		JLabel lblRewritePassword = new JLabel("Re-write Password");
		lblRewritePassword.setBounds(12, 81, 116, 15);
		contentPane.add(lblRewritePassword);

		passwordField_Re = new JPasswordField();
		passwordField_Re.setBounds(128, 78, 159, 21);
		contentPane.add(passwordField_Re);

		JLabel lblRed = new JLabel("*First Number of ID should be '" + memberKind + "'.");
		lblRed.setForeground(Color.RED);
		lblRed.setFont(new Font("Arial", Font.PLAIN, 11));
		lblRed.setBounds(22, 31, 163, 15);
		contentPane.add(lblRed);

		/*set JTextField to input address when adding new member*/
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(12, 106, 57, 15);
		contentPane.add(lblAddress);

		addressField = new JTextField();
		addressField.setBounds(12, 127, 283, 21);
		contentPane.add(addressField);
		addressField.setColumns(10);

		/*set JTextField to input latitude when adding new member*/
		JLabel lblLatitude = new JLabel("Latitude");
		lblLatitude.setBounds(12, 158, 57, 15);
		contentPane.add(lblLatitude);

		latitudeField = new JTextField();
		latitudeField.setBounds(81, 155, 116, 21);
		contentPane.add(latitudeField);
		latitudeField.setColumns(10);

		/*set JTextField to input Longitude when adding new member*/
		JLabel lblLongitude = new JLabel("Longitude");
		lblLongitude.setBounds(12, 183, 57, 15);
		contentPane.add(lblLongitude);

		longitudeField = new JTextField();
		longitudeField.setColumns(10);
		longitudeField.setBounds(81, 180, 116, 21);
		contentPane.add(longitudeField);

		/*set JButton to register the new member on the WMS*/
		btnOK = new JButton("OK");
		btnOK.setBounds(12, 278, 97, 23);
		btnOK.addActionListener(this);
		contentPane.add(btnOK);

		/*set JButton to cancel the new member on the WMS*/
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(125, 278, 97, 23);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel);

		/*set JTextField to input owner's name or nickname when adding new member*/
		lblOwner = new JLabel("Owner");
		lblOwner.setBounds(12, 208, 57, 15);
		contentPane.add(lblOwner);

		ownerField = new JTextField();
		ownerField.setColumns(10);
		ownerField.setBounds(81, 205, 116, 21);
		contentPane.add(ownerField);

		/*set JTextField to input member's contact number when adding new member*/
		lblContactNumber = new JLabel("Contact");
		lblContactNumber.setBounds(12, 233, 57, 15);
		contentPane.add(lblContactNumber);

		contactField = new JTextField();
		contactField.setColumns(10);
		contactField.setBounds(81, 230, 116, 21);
		contentPane.add(contactField);

		/*set JButton to convert the address to latitude and longitude automatically*/
		btnConvert = new JButton("Convert");
		btnConvert.setBounds(209, 154, 86, 23);
		btnConvert.addActionListener(this);
		contentPane.add(btnConvert);
	}

	/*This method calculates latitude and longitude based on address using GoogleMatrix*/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == btnConvert) { //convert button
			try {
				String resultStr = new GoogMatrixRequest(addressField.getText()).calculate();
				String[] results = resultStr.split("\n|:|\\{|\\}|,");
				latitudeField.setText(results[144]);
				longitudeField.setText(results[147]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource() == btnCancel) //cancel button
			this.dispose();
		else { //ok button
			if (passwordField.getText().equals(passwordField_Re.getText())) {
				makeCommand();
				this.dispose();
			} else
				JOptionPane.showMessageDialog(null, "Two Passwords are not correct.");
		}
	}

	public JTextField getIdField() {
		return idField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JTextField getLatitudeField() {
		return latitudeField;
	}

	public JTextField getLongitudeField() {
		return longitudeField;
	}

	public JTextField getAddressField() {
		return addressField;
	}

	public JTextField getOwnerField() {
		return ownerField;
	}

	public JTextField getContactField() {
		return contactField;
	}

	abstract void makeCommand();
}