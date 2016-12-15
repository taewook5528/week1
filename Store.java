/*
 *filename : Store.java
 *author : team Tic Toc
 *since : 2016.10.04
 *purpose/function :  �ҽ��ڵ�� Store client�� �ش� WMS�� �����Ͽ��� ��, User interface�� �ش� ����� �����ϱ� ���� 
 *					StoreGUI class�� Store class�� �����ϰ� �ִ�.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/*This class defines Store's GUI */
class storeGUI extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel stockModel, transModel, orderModel;
	private JTable stockTable, transTable, orderTable;
	private JScrollPane stockScroll, transScroll, orderScroll;
	private JPanel stockPanel, transPanel;
	private JLabel timeLabel;
	private String id;
	private Store form;
	private ResultSet rs;
	private final String[] stockColumnNames = { "Product_ID", "Product_Name", "Quantity", "Maximum capacity",
			"Maintaining minimum quantity" };
	private final String[] transColumnNames = { "Order_No", "Order_Products", "Order_Date" };
	private final String[] orderColumnNames = { "Departure_ID", "Product_ID", "Product_Name", "Amount", "Cost",
			"Shipped" };
	private Object[][] stockData, transData, orderData;

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */

	/*constructor of storeGUI*/
	public storeGUI(Store form, String id) throws SQLException {
		this.form = form;
		this.id = id;
		setTitle("Store Management");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 655, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*show current time in Store window frame using TimeLable */
		timeLabel = new JLabel("Current time : " + new Date().toString());

		timeLabel.setBounds(386, 10, 251, 15);
		contentPane.add(timeLabel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 10, 625, 359);
		contentPane.add(tabbedPane);

		// manage inventory tab panel
		stockPanel = new JPanel();
		tabbedPane.addTab("Manage inventory", null, stockPanel, null);
		stockPanel.setLayout(null);

		stockData = getInventoryData();
		stockModel = new DefaultTableModel(stockData, stockColumnNames);
		stockTable = new JTable(stockModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		stockTable.setFocusable(false);
		stockTable.setRowSelectionAllowed(true);
		stockScroll = new JScrollPane(stockTable);
		stockScroll.setBounds(0, 0, 620, 265);

		stockPanel.add(stockScroll);
		
		/*set button to add new product in Store window frame using JButton*/
		JButton btnAddProduct = new JButton("Add product");
		btnAddProduct.setBounds(7, 275, 116, 23);
		btnAddProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new AddProduct() {
						@Override
						void makeCommand() {
							String command = "P;";
							command += id + ";";
							command += this.getProductBox().getSelectedItem() + ";";
							command += this.getTextField().getText() + ";";
							command += this.getTextField_1().getText() + ";";
							command += this.getTextField_2().getText() + ";";
							form.getOut().println(command);
						}
					};
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		stockPanel.add(btnAddProduct);

		/*set button to edit inventory in Store window frame using JButton*/
		JButton btnModifyStock = new JButton("Edit inventory");
		btnModifyStock.setBounds(157, 275, 116, 23);
		btnModifyStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Add_popup("Edit Inventory", "Product ID", "Quantity") {
					private static final long serialVersionUID = 1L;

					@Override
					public void makeCommand() {
						String command = "E;";
						command += id + ";";
						command += this.textField.getText() + ";";
						command += this.textField_1.getText() + ";";
						form.getOut().println(command);
					}
				};
			}
		});
		stockPanel.add(btnModifyStock);

		/*set button to edit max inventory in Store window frame using JButton*/
		JButton btnModifyMax = new JButton("Edit Max Capacity");
		btnModifyMax.setBounds(280, 275, 153, 23);
		btnModifyMax.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Add_popup("Edit Max Capacity", "Product ID", "Max Capacity") {
					private static final long serialVersionUID = 1L;

					@Override
					public void makeCommand() {
						String command = "MX;";
						command += id + ";";
						command += this.textField.getText() + ";";
						command += this.textField_1.getText() + ";";
						form.getOut().println(command);
					}
				};
			}
		});
		stockPanel.add(btnModifyMax);

		/*set button to edit min inventory in Store window frame using JButton*/
		JButton btnModifyMin = new JButton("Edit Min Stock Amount");
		btnModifyMin.setBounds(440, 275, 173, 23);
		btnModifyMin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				new Add_popup("Edit Min Quantity", "Product ID", "Min Quantity") {
					private static final long serialVersionUID = 1L;

					@Override
					public void makeCommand() {
						String command = "MN;";
						command += id + ";";
						command += this.textField.getText() + ";";
						command += this.textField_1.getText() + ";";
						form.getOut().println(command);
					}

				};
			}
		});
		stockPanel.add(btnModifyMin);

		// order managing tab panel
		transPanel = new JPanel();
		tabbedPane.addTab("Order Management", null, transPanel, null);
		transPanel.setLayout(null);

		// table of orders
		transData = getOrderingData();
		transModel = new DefaultTableModel(transData, transColumnNames);
		transTable = new JTable(transModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		transScroll = new JScrollPane(transTable);
		transScroll.setBounds(12, 46, 596, 119);
		transPanel.add(transScroll);

		// table of shippings inform
		orderData = getShippingData();
		orderModel = new DefaultTableModel(orderData, orderColumnNames);
		orderTable = new JTable(orderModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		orderScroll = new JScrollPane(orderTable);
		orderScroll.setBounds(12, 170, 596, 119);
		transPanel.add(orderScroll);

		/*set button to inform receive to WMS*/
		JButton btnReceived = new JButton("Received");
		btnReceived.setBounds(486, 294, 122, 23);
		btnReceived.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rows[] = orderTable.getSelectedRows();
				for (int i = 0; i < rows.length; i++) {
					// if the products are shipped
					if (orderTable.getValueAt(rows[i], 5).equals("Yes")) {
						String command = "R;";
						command += id + ";"; // warehouse_id
						command += orderTable.getValueAt(rows[i], 1) + ";"; // product_id
						command += orderTable.getValueAt(rows[i], 3) + ";"; // amount
						form.getOut().println(command);
					}
				}
			}

		});
		transPanel.add(btnReceived);

		/*set button to order goods*/
		JButton btnNew = new JButton("New Order");
		btnNew.setBounds(12, 10, 140, 23);
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new NewOrder(id, true) {
						private static final long serialVersionUID = 1L;

						@Override
						void makeCommand() {
							String command = "O;";
							command += id + ";";
							command += this.getCommandData().size() + ";";
							for (int i = 0; i < this.getCommandData().size(); i++) {
								command += this.getCommandData().get(i)[0] + ";";
								command += this.getCommandData().get(i)[1] + ";";
							}
							form.getOut().println(command);
						}
					};
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		transPanel.add(btnNew);

		JButton btnDetail = new JButton("Show Detail");
		btnDetail.setBounds(494, 10, 114, 23);
		btnDetail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = transTable.getSelectedRow();
				String orderNO = (String) transTable.getValueAt(selectedRow, 0);
				try {
					new OrderDetail(orderNO);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		});
		transPanel.add(btnDetail);

		/*set button to cancel the order*/
		JButton btnCancle = new JButton("Cancel Order");
		btnCancle.setBounds(164, 10, 114, 23);
		btnCancle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rows[] = transTable.getSelectedRows();
				for (int i = 0; i < rows.length; i++) {
					String orderNO = (String) transTable.getValueAt(rows[i], 0);
					form.getOut().println("CO;" + orderNO + ";");
				}
			}

		});
		transPanel.add(btnCancle);
	}

	@Override
	public void run() {
		setVisible(true);
		while (true) {
			timeLabel.setText("Current time : " + new Date().toString());
		}
	}

	public Object[][] getInventoryData() throws SQLException {
		int rows = 0;
		rs = DataBaseConnect.execute("select count(*) from store_inventory where store_id='" + id + "'");
		if (rs.next())
			rows = rs.getInt(1);

		Object[][] stockData = new Object[rows][];
		rs = DataBaseConnect.execute("select * from store_inventory where store_id='" + id + "'");
		for (int i = 0; i < rows; i++) {
			if (rs.next()) {
				String strProduct_Name = null;
				ResultSet pdNameSet = DataBaseConnect
						.execute("select * from product where product_id='" + rs.getString("product_id") + "'");
				if (pdNameSet.next())
					strProduct_Name = pdNameSet.getString("product_name");
				Object[] tmpdata = { rs.getString("product_id"), strProduct_Name, rs.getInt("amount"),
						rs.getInt("product_max"), rs.getInt("product_min") };
				stockData[i] = tmpdata;
			}
		}
		return stockData;
	}

	public Object[][] getOrderingData() throws SQLException {
		ArrayList<String> orderIDs = new ArrayList<String>();
		// get this id's orders
		rs = DataBaseConnect.execute("select * from ordering where store_id='" + id + "'");
		while (rs.next()) {
			orderIDs.add(rs.getString("order_no"));
		}

		Object[][] orderData = new Object[orderIDs.size()][];
		rs = DataBaseConnect.execute("select * from ordering where store_id=" + id);
		for (int i = 0; i < orderIDs.size(); i++) {
			if (rs.next()) {
				int products = 0;
				ResultSet tmpRS = DataBaseConnect
						.execute("select count(*) from ordering_list where order_no=" + orderIDs.get(i));
				if (tmpRS.next())
					products = tmpRS.getInt(1);
				// make row data {Order_number, Order_products, Order_Date}
				Object[] tmpdata = { orderIDs.get(i), products, rs.getDate("order_date") };
				orderData[i] = tmpdata; // add to data set
			}
		}
		return orderData;
	}

	public Object[][] getShippingData() throws SQLException {
		rs = DataBaseConnect.execute("select count(*) from shipping where arrival_=" + id);
		Object[][] shipData = null;
		if (rs.next()) {
			shipData = new Object[rs.getInt(1)][];
			rs = DataBaseConnect.execute("select * from shipping where arrival_=" + id);
			for (int i = 0; i < shipData.length; i++) {
				if (rs.next()) {
					String strProduct_Name = null;
					ResultSet pdNameSet = DataBaseConnect
							.execute("select * from product where product_id='" + rs.getString("product_id") + "'");
					// get name of product using product_id
					if (pdNameSet.next())
						strProduct_Name = pdNameSet.getString("product_name");

					// make row data
					String shipped = rs.getBoolean("shipped") ? "Yes" : "No";
					Object[] tmpdata = { rs.getString("starting_"), rs.getString("product_id"), strProduct_Name,
							rs.getInt("amount"), rs.getDouble("cost"), shipped };
					shipData[i] = tmpdata;
				}
			}
		}
		return shipData;
	}

	// getter and setter for field
	public String[] getStockColumnNames() {
		return stockColumnNames;
	}

	public String[] getTransColumnNames() {
		return transColumnNames;
	}

	public Object[][] getStockData() {
		return stockData;
	}

	public void setStockData(Object[][] stockData) {
		this.stockData = stockData;
	}

	public void setTransData(Object[][] transData) {
		this.transData = transData;
	}

	public Object[][] getTransData() {
		return transData;
	}

	public DefaultTableModel getStockModel() {
		return stockModel;
	}

	public DefaultTableModel getTransModel() {
		return transModel;
	}

	public Object[][] getOrderData() {
		return orderData;
	}

	public DefaultTableModel getOrderModel() {
		return orderModel;
	}

	public String[] getOrderColumnNames() {
		return orderColumnNames;
	}

	public void setOrderData(Object[][] orderData) {
		this.orderData = orderData;
	}
}

public class Store extends Thread { // super class for warehouse and store

	/* field starts */
	private String id; // inherent number
	private String password;
	private storeGUI frame;
	private Socket socket; // socket for connecting server
	private BufferedReader in; // in stream for communicating with server
	private PrintWriter out; // out stream
	private storeGUI storeForm;
	private warehouseGUI warehouseForm;
	private int kind;
	/* field ends */

	/* Store constructor */
	public Store(String id, String password, int kind) throws Exception {
		this.kind = kind;
		this.id = id;
		this.password = password;
		socket = new Socket("localhost", 9001); // set socket(localhost, port
												// 9001)
		// create stream at set socket
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		if (kind == 2) {
			storeForm = new storeGUI(this, id);
			Thread gui = new Thread(storeForm);
			this.start();
			gui.start();
		} else {
			warehouseForm = new warehouseGUI((Warehouse) this, id);
			Thread gui = new Thread(warehouseForm);
			this.start();
			gui.start();
		}
	}

	public PrintWriter getOut() {
		return out;
	}

	@Override
	public void run() {
		String command;
		while (true) {
			try {
				command = in.readLine(); // ream command from server
				System.out.println(command);
				if (command.startsWith("E") || command.startsWith("MX") || command.startsWith("MN")
						|| command.startsWith("P")) {
					if (kind == 2) {
						storeForm.setStockData(storeForm.getInventoryData());
						storeForm.getStockModel().setDataVector(storeForm.getStockData(),
								storeForm.getStockColumnNames());
					} else {
						warehouseForm.setStockData(warehouseForm.getInventoryData());
						warehouseForm.getStockModel().setDataVector(warehouseForm.getStockData(),
								warehouseForm.getStockColumnNames());
					}
				} else if (command.startsWith("B")) {
					if (kind == 2) {
						storeForm.setTransData(storeForm.getOrderingData());
						storeForm.getTransModel().setDataVector(storeForm.getTransData(),
								storeForm.getTransColumnNames());
						storeForm.setOrderData(storeForm.getShippingData());
						storeForm.getOrderModel().setDataVector(storeForm.getOrderData(),
								storeForm.getOrderColumnNames());
					} else {
						warehouseForm.setSendData(warehouseForm.getSendingData());
						warehouseForm.getSendModel().setDataVector(warehouseForm.getSendData(),
								warehouseForm.getSendColumnNames());
					}
				} else if (command.startsWith("O") || command.startsWith("CO")) {
					if (kind == 2) {
						storeForm.setTransData(storeForm.getOrderingData());
						storeForm.getTransModel().setDataVector(storeForm.getTransData(),
								storeForm.getTransColumnNames());
					}
				} else if (command.startsWith("S")) {
					if (kind == 2) {
						storeForm.setOrderData(storeForm.getShippingData());
						storeForm.getOrderModel().setDataVector(storeForm.getOrderData(),
								storeForm.getOrderColumnNames());
					} else {
						warehouseForm.setStockData(warehouseForm.getInventoryData());
						warehouseForm.getStockModel().setDataVector(warehouseForm.getStockData(),
								warehouseForm.getStockColumnNames());
						warehouseForm.setSendData(warehouseForm.getSendingData());
						warehouseForm.getSendModel().setDataVector(warehouseForm.getSendData(),
								warehouseForm.getSendColumnNames());
					}
				} else if (command.startsWith("R")) {
					if (kind == 2) {
						storeForm.setStockData(storeForm.getInventoryData());
						storeForm.getStockModel().setDataVector(storeForm.getStockData(),
								storeForm.getStockColumnNames());
						storeForm.setOrderData(storeForm.getShippingData());
						storeForm.getOrderModel().setDataVector(storeForm.getOrderData(),
								storeForm.getOrderColumnNames());
					}
				} else if (command.startsWith("Verifying"))
					out.println(this.id);
				else if (command.startsWith("Accepted")) {
					// popup for login success
					JOptionPane.showMessageDialog(frame, "You are connected to server.");
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}