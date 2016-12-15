/*
 *filename : Add_popup.java
 *author : team Tic Toc
 *since : 2016.11.07
 *purpose/function : 이 소스코드는 재고량 수정, 최대 수용 재고수량 수정, 최소 유지 재고수량  수정을 용이하게 하기 위한 목적으로 만들어졌다.
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public abstract class Add_popup extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7105759650250017612L;// 객체직렬화, 송신측과 수신측에서 같은 객체인지 확인하는 객체의 아이디
	private JPanel contentPane;
	public JTextField textField;
	public JTextField textField_1;
	private JButton btnNewButton, btnNewButton_1;

	/**
	 * Create the frame.
	 */
	/*constructor of Add_popup*/
	public Add_popup(String title, String name, String amount) {
		/*set window frame*/
		setVisible(true);
		setSize(300, 200);
		setTitle(title);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel(name);
		lblNewLabel_1.setBounds(22, 22, 100, 19);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel(amount);
		lblNewLabel_2.setBounds(22, 50, 100, 19);
		contentPane.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(130, 22, 116, 21);
		contentPane.add(textField);
		textField.setColumns(15);
		
		textField_1 = new JTextField();
		textField_1.setColumns(15);
		textField_1.setBounds(130, 50, 116, 21);
		contentPane.add(textField_1);
		
		btnNewButton = new JButton("OK");
		btnNewButton.setBounds(30, 100, 70, 23);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(this);
		
		btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(140, 100, 90, 23);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(this);
	}

	public Add_popup() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnNewButton_1) //if the pressed button is "Cancel"
			this.dispose(); //close the frame
		else if(e.getSource()==btnNewButton) { //if the pressed button is "OK"
			this.makeCommand(); //make a command string
			this.dispose();
		}
	}

	public abstract void makeCommand();
}