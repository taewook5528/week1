/*
 *filename : Server.java
 *author : team Tic Toc
 *since : 2016.10.30
 *purpose/function : �����ͺ��̽� ������ ����, ������ �����Ͽ���.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;

public class Server extends Thread {
	// set of each client's printwriters
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	// set of client's ids
	private static HashSet<String> ids = new HashSet<String>();
	private static ResultSet rs;

	// Server Constructor
	public Server() throws Exception {
		ServerSocket ss = new ServerSocket(9001); // port number is 9001
		System.out.println("The server has been hosted.");
		try {
			while (true) {
				// make handler object when client access.
				new Handler(ss.accept()).start();
			}
		} finally {
			ss.close();
		}
	}
	
	private static class Handler extends Thread { // Handler class
		private String id;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket) { // handler constructor
			this.socket = socket;
		}

		public void run() {
			try {
				// add client's id and printwriter
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("Verifying id..");

					id = in.readLine(); // id reading
					if (id == null)
						return;

					synchronized (ids) { // add id when it's ok
						if (!ids.contains(id)) {
							ids.add(id);
							break;
						}
					}
				}

				out.println("Accepted");
				System.out.println(id + " has logged in");
				writers.add(out); // add printwriter

				while (true) {
					String input = in.readLine(); // when client send message to
													// server
					if (input == null) {
						return;
					}
					System.out.println(id + " : " + input);
					String[] commands = input.split(";");
					boolean isStore = false;
					switch (commands[0]) {
					case "E": // edit inventory
						// check if this member is store
						rs = DataBaseConnect
								.execute("select isStore from identification where id='" + commands[1] + "'");
						if (rs.next()) {
							isStore = rs.getBoolean(1);
						}

						// update inventory
						if (isStore) {
							DataBaseConnect.update("update store_inventory set amount=" + commands[3]
									+ " where store_id=" + commands[1] + " and product_id=" + commands[2]);
						} else {
							DataBaseConnect.update("update warehouse_inventory set amount=" + commands[3]
									+ " where warehouse_id=" + commands[1] + " and product_id=" + commands[2]);
						}

						// add to log
						DataBaseConnect.update(makeLog(commands[2], commands[1], commands[3], isStore));

						break;

					case "MX": // edit inventory capacity
						// check if this member is store
						rs = DataBaseConnect
								.execute("select isStore from identification where id='" + commands[1] + "'");
						if (rs.next()) {
							isStore = rs.getBoolean(1);
						}
						if (isStore) {
							DataBaseConnect.update("update store_inventory set product_max=" + commands[3]
									+ " where store_id=" + commands[1] + " and product_id=" + commands[2]);
						} else {
							DataBaseConnect.update("update warehouse_inventory set product_max=" + commands[3]
									+ " where warehouse_id=" + commands[1] + " and product_id=" + commands[2]);
						}
						break;

					case "MN": // edit inventory quantity
						// check if this member is store
						rs = DataBaseConnect
								.execute("select isStore from identification where id='" + commands[1] + "'");
						if (rs.next()) {
							isStore = rs.getBoolean(1);
						}
						if (isStore) {
							DataBaseConnect.update("update store_inventory set product_min=" + commands[3]
									+ " where store_id=" + commands[1] + " and product_id=" + commands[2]);
						} else {
							DataBaseConnect.update("update warehouse_inventory set product_min=" + commands[3]
									+ " where warehouse_id=" + commands[1] + " and product_id=" + commands[2]);
						}
						break;

					case "B": // batch process
						calculate(commands[1]); // calculate using order_no
						break;

					case "O": // make new order
						int orderNo = 0;
						rs = DataBaseConnect.execute("select * from ordering");
						while (rs.next())
							orderNo = rs.getInt(1); // get max value of
													// order_number
						++orderNo; // generate new order_No

						// insert into ordering
						DataBaseConnect.update("insert into ordering values ('" + orderNo + "','" + commands[1] + "','"
								+ new Date(System.currentTimeMillis()) + "')");

						// insert into ordering_list
						for (int i = 1; i <= Integer.parseInt(commands[2]); i++) {
							DataBaseConnect.update("insert into ordering_list values ('" + commands[i * 2 + 1] + "','"
									+ (orderNo) + "','" + commands[i * 2 + 2] + "')");
						}
						break;

					case "CO": // cancel order
						DataBaseConnect.update("delete from ordering_list where order_no=" + commands[1]);
						DataBaseConnect.update("delete from ordering where order_no=" + commands[1]);
						break;

					case "R": // received
						isStore = true;
						// delete the shipping info
						DataBaseConnect.update("delete from shipping where arrival_=" + commands[1] + " and product_id="
								+ commands[2] + " and amount=" + commands[3]);

						// edit store's inventory
						DataBaseConnect.update("update store_inventory set amount=amount+" + commands[3]
								+ " where store_id=" + commands[1] + " and product_id=" + commands[2]);

						// add to log
						ResultSet qtySet = DataBaseConnect.execute("select * from store_inventory where store_id="
								+ commands[1] + " and product_id=" + commands[2]);
						if (qtySet.next()) {
							int amount = qtySet.getInt("amount");
							DataBaseConnect.update(makeLog(commands[2], commands[1], amount + "", isStore));
						}

						break;

					case "S": // shipped
						isStore = false;
						// update the order 'shipped = true'
						DataBaseConnect.update("update shipping set shipped=" + 1 + " where starting_=" + commands[1]
								+ " and arrival_=" + commands[2] + " and product_id=" + commands[3] + " and amount="
								+ commands[4]);

						// update warehouse's inventory
						DataBaseConnect.update("update warehouse_inventory set amount=amount-" + commands[4]
								+ " where warehouse_id=" + commands[1] + " and product_id=" + commands[3]);

						// add to log
						qtySet = DataBaseConnect.execute("select * from warehouse_inventory where warehouse_id="
								+ commands[1] + " and product_id=" + commands[3]);
						if (qtySet.next()) {
							int amount = qtySet.getInt("amount");
							DataBaseConnect.update(makeLog(commands[3], commands[1], amount + "", isStore));
						}

						break;

					case "AS": // add a store
						// add a new store
						DataBaseConnect.update("insert into store values ('" + commands[1] + "','" + commands[4] + "','"
								+ commands[5] + "','" + commands[3] + "','" + commands[6] + "','" + commands[7] + "','"
								+ 0 + "')");
						// add a new identification
						DataBaseConnect.update("insert into identification values ('" + commands[1] + "','"
								+ commands[2] + "','" + "1')");
						Head.calculateNewStore(commands[1]);
						break;

					case "AW": // add a warehouse
						// add a new warehouse
						DataBaseConnect.update("insert into warehouse values ('" + commands[1] + "','" + commands[4]
								+ "','" + commands[5] + "','" + commands[3] + "','" + commands[6] + "','" + commands[7]
								+ "')");
						// add a new identification
						DataBaseConnect.update("insert into identification values ('" + commands[1] + "','"
								+ commands[2] + "','" + "0')");
						Head.calculateNewWarehouse(commands[1]);
						break;

					case "DS": // delete a store
						rs = DataBaseConnect
								.execute("select count(*) from ordering where store_id='" + commands[1] + "'");
						if (rs.next()) {
							if (rs.getInt(1) != 0) // if there was order from
													// the store
								// show error message and break
								JOptionPane.showMessageDialog(null,
										"Cannot delete the store: There are unprocessed orders");
							break;
						}
						rs = DataBaseConnect
								.execute("select count(*) from shipping where arrival_='" + commands[1] + "'");
						if (rs.next()) {
							if (rs.getInt(1) != 0) // if there was order from
													// the store
								// show error message and break
								JOptionPane.showMessageDialog(null,
										"Cannot delete the warehouse: There are unprocessed shipping orders");
							break;
						}
						// delete inventory
						DataBaseConnect.update("delete from store_inventory where store_id='"+commands[1]+"'");
						// delete distances
						DataBaseConnect.update("delete from distance where store_id='" + commands[1] + "'");
						// delete store
						DataBaseConnect.update("delete from store where store_id='" + commands[1] + "'");
						// delete account
						DataBaseConnect.update("delete from identification where id='" + commands[1] + "'");
						// delete log
						DataBaseConnect.update("delete from log where Member_ID='"+commands[1]+"'");
						break;

					case "DW": // delete a warehouse
						rs = DataBaseConnect
								.execute("select count(*) from shipping where starting_='" + commands[1] + "'");
						if (rs.next()) {
							// if there was order from the store
							if (rs.getInt(1) != 0) {
								// show error message and break
								JOptionPane.showMessageDialog(null,
										"Cannot delete the warehouse: There are unprocessed shipping orders");
								break;
							}
						}
						// delete inventory
						DataBaseConnect.update("delete from warehouse_inventory where warehouse_id='"+commands[1]+"'");
						// delete distances
						DataBaseConnect.update("delete from distance where warehouse_id='" + commands[1] + "'");
						// delete store
						DataBaseConnect.update("delete from warehouse where warehouse_id='" + commands[1] + "'");
						// delete account
						DataBaseConnect.update("delete from identification where id='" + commands[1] + "'");
						// delete log
						DataBaseConnect.update("delete from log where Member_ID='"+commands[1]+"'");
						break;
						
					case "P": // add a product
						if(commands[1].startsWith("1")) //if the member is warehouse
							DataBaseConnect.update("insert into warehouse_inventory values ('"+commands[1]+"','"+commands[2]+"','"+commands[3]+"','"+commands[5]+"','"+commands[4]+"')");
						else
							DataBaseConnect.update("insert into store_inventory values ('"+commands[1]+"','"+commands[2]+"','"+commands[3]+"','"+commands[5]+"','"+commands[4]+"')");
						break;
					}
					for (PrintWriter writer : writers)
						writer.println(input + "has completed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (id != null)
					ids.remove(id);
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(id + " has logged off");
				}
			}
		}

		private String makeLog(String product_id, String id, String changeResult, boolean isStore) throws SQLException {
			String logQuery = null;
			int logNo = 0;
			int boolNum = isStore ? 1 : 0;
			rs = DataBaseConnect.execute("select * from log");

			// get max value of log_number
			while (rs.next())
				logNo = rs.getInt(1);
			++logNo; // generate new log_No

			// make log insert query
			logQuery = "insert into log values ('" + logNo + "','" + product_id + "','" + id + "','" + changeResult
					+ "','" + boolNum + "','" + new Date(System.currentTimeMillis()) + "')";

			return logQuery;
		}

		private void calculate(String order_no) throws SQLException {
			String store_id = null;
			ArrayList<Double> distances = new ArrayList<Double>();
			ArrayList<Object[]> ordering_list = new ArrayList<Object[]>();

			rs = DataBaseConnect.execute("select * from ordering where order_no=" + order_no);
			if (rs.next())
				store_id = rs.getString("store_id"); // get store's id

			rs = DataBaseConnect.execute("select * from distance where store_id=" + store_id + " order by distance");
			while (rs.next())
				distances.add(rs.getDouble("distance")); // get distances in
															// sorted order

			rs = DataBaseConnect.execute("select * from ordering_list where order_no=" + order_no);
			while (rs.next()) {
				Object[] tmpOrder = { rs.getString("product_id"), rs.getInt("amount") };
				ordering_list.add(tmpOrder); // get order info and add to list
			}

			// process with each product
			while (ordering_list.size() > 0) {
				for (int j = 0; j < distances.size(); j++) {
					String warehouse_id = null;
					int product_qty = 0;
					rs = DataBaseConnect.execute("select * from distance where distance=" + distances.get(j));
					if (rs.next()) {
						// get warehouse's id
						warehouse_id = rs.getString("warehouse_id");
					}

					rs = DataBaseConnect.execute("select * from warehouse_inventory where warehouse_id=" + warehouse_id
							+ " and product_id=" + ordering_list.get(0)[0]);
					if (rs.next()) {
						// get product's quantity in this warehouse
						product_qty = rs.getInt("amount");
					}

					// if the warehouse can ship this product
					if (product_qty >= (Integer) ordering_list.get(0)[1]) {
						double cost = 0;
						double unit_price = 0;
						rs = DataBaseConnect
								.execute("select * from product where product_id=" + ordering_list.get(0)[0]);
						if (rs.next())
							unit_price = rs.getDouble("unit_price");
						// cost = (unit_price * quantity of product) + (distance
						// * cost per distance)
						cost = unit_price * ((Integer) ordering_list.get(0)[1]) + distances.get(j) * 1;
						try {
							// insert into shipping table
							DataBaseConnect.update("insert into shipping values ('" + warehouse_id + "','" + store_id
									+ "','" + ordering_list.get(0)[0] + "','" + ordering_list.get(0)[1] + "','" + cost
									+ "','" + 0 + "')");
							// delete row in ordering_list table
							DataBaseConnect.update("delete from ordering_list where product_id="
									+ ordering_list.get(0)[0] + " and order_no=" + order_no);
							// delete order in list and jump to next loop
							ordering_list.remove(0);
							break;
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			}
			// when all orders has processed, delete order_no
			DataBaseConnect.update("delete from ordering where order_no=" + order_no);
		}
	}
}