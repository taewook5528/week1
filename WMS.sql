create table Product
(
   Product_ID varchar(10) Not null,
   Product_Name varchar(10) Not null,
   Unit_Price double Not null,
   Description varchar(100),
   primary key (Product_ID)
);

create table Ordering
(
   Order_NO varchar(10) Not null,
   Store_ID varchar(10) not null,
   foreign key(Store_ID) references Store(Store_ID),
   Order_date date Not null,
   primary key (Order_NO)
);

create table Ordering_list
(
   Product_ID varchar(10) not null,
   Order_NO varchar(10) not null,
   foreign key(Product_ID) references Product(Product_ID),
   foreign key(Order_NO) references Ordering(Order_NO),
   amount int Not null
);

create table Store(
   Store_ID varchar(10) Not null,
   latitude double Not null,
   longitude double Not null,
   address varchar(100) Not null,
   Owner varchar(20) not null,
   Contact varchar(15) not null,
   cash double Not null,
   primary key (Store_ID)
);

create table Store_inventory
(
   Store_ID varchar(10) not null,
   Product_ID varchar(10) not null,
   foreign key(Store_ID) references Store(Store_ID),
   foreign key(Product_ID) references Product(Product_ID),
   amount int Not null,
   product_min int not null,
   product_max int not null
);

create table Warehouse(
   Warehouse_ID varchar(10) Not null,
   latitude double Not null,
   longitude double Not null,
   address varchar(100) Not null,
   Owner varchar(20) not null,
   Contact varchar(15) not null,
   primary key (Warehouse_ID)
);

create table Warehouse_inventory
(
   Warehouse_ID varchar(10) not null,
   Product_ID varchar(10) not null,
   foreign key(Warehouse_ID) references Warehouse(Warehouse_ID),
   foreign key(Product_ID) references Product(Product_ID),
   amount int Not null,
   product_min int not null,
   product_max int not null
);

create table identification(
   ID varchar(13) Not null,
   Password varchar(10) Not null,
   isStore boolean not null,
   primary key (ID)
);

create table log(
   log_no varchar(10) not null,
   Product_ID varchar(10) not null,
   Member_ID varchar(10) not null,
   Changed_result int not null,
   isStore boolean not null,
   change_date date not null,
   foreign key(Product_ID) references Product(Product_ID)
);

create table shipping(
	starting_ varchar(10) not null,
	arrival_ varchar(10) not null,
	Product_ID varchar(10) not null,
	foreign key(starting_) references Warehouse(Warehouse_ID),
	foreign key(arrival_) references Store(Store_ID),
	foreign key(Product_ID) references Product(Product_ID),
	amount int not null,
	cost double not null,
	shipped boolean not null
);

create table distance(
   store_id varchar(10) not null,
   warehouse_id varchar(10) not null,
   distance double not null,
   foreign key(store_id) references Store(Store_ID),
   foreign key(warehouse_id) references Warehouse(Warehouse_ID)
);

/*Product attribute*/
INSERT INTO Product VALUES ('0001', 'gold ax', 1000,'ax of gold');
INSERT INTO Product VALUES ('0002', 'silver ax', 100,'ax of silver');
INSERT INTO Product VALUES ('0003', 'bronze ax', 10, 'ax of bronze');
INSERT INTO Product VALUES ('0004', 'steel ax', 1,'ax of steel');

/*Identification attribute*/
INSERT INTO identification VALUES ('2001', '1111',1);
INSERT INTO identification VALUES ('2002', '2222',1);
INSERT INTO identification VALUES ('2003', '3333',1);
INSERT INTO identification VALUES ('2004', '4444',1);
INSERT INTO identification VALUES ('2005', '5555',1);
INSERT INTO identification VALUES ('2006', '6666',1);
INSERT INTO identification VALUES ('2007', '7777',1);
INSERT INTO identification VALUES ('2008', '8888',1);
INSERT INTO identification VALUES ('2009', '9999',1);
INSERT INTO identification VALUES ('2010', '1010',1);
INSERT INTO identification VALUES ('2011', '1111',1);
INSERT INTO identification VALUES ('2012', '1212',1);
INSERT INTO identification VALUES ('1001', '1111',0);
INSERT INTO identification VALUES ('1002', '2222',0);
INSERT INTO identification VALUES ('1003', '3333',0);
INSERT INTO identification VALUES ('1004', '4444',0);
INSERT INTO identification VALUES ('1005', '5555',0);
INSERT INTO identification VALUES ('1006', '6666',0);
INSERT INTO identification VALUES ('1007', '7777',0);
INSERT INTO identification VALUES ('1008', '8888',0);
INSERT INTO identification VALUES ('1009', '9999',0);
INSERT INTO identification VALUES ('admin','0000',0);
INSERT INTO identification VALUES ('server', '0000',0);
/*Warehouse attribute*/
INSERT INTO Warehouse VALUES ('1001', 37.2626155, -121.7254909, '1701 Airport Blvd, San Jose, CA 95110','Kim','010-1111-2222');
INSERT INTO Warehouse VALUES ('1002', 37.1325447, -122.0849749, 'San Martin Airport, San Martin, CA','Lim','010-1231-6752');
INSERT INTO Warehouse VALUES ('1003', 35.0404632, -120.6662746, '2000 S Union Ave, Bakersfield, CA 93307','Lim','010-1861-2321');
INSERT INTO Warehouse VALUES ('1004', 35.2812894, -120.9818909, '500 James Fowler Rd, Santa Barbara, CA 93117','Lee','010-7611-8822');
INSERT INTO Warehouse VALUES ('1005', 34.5108931, -119.3612722, '1 World Way, Los Angeles, CA 90045','Jung','010-3561-9872');
INSERT INTO Warehouse VALUES ('1006', 35.3402769, -121.1396926, '3701 Wings Way, Bakersfield, CA 93308','Kim','010-1371-2182');
INSERT INTO Warehouse VALUES ('1007', 35.7435418, -121.05459, '561 Air Park Dr, Oceano, CA 93445','Park','010-4041-2382');
INSERT INTO Warehouse VALUES ('1008', 35.9394727, -121.087549, '9501 W Airport Dr, Visalia, CA 93277','Joe','011-211-6317');
INSERT INTO Warehouse VALUES ('1009', 36.3723706, -122.0664811, 'Durian Coalinga, Coalinga, CA 93210','Hong','010-1875-0252');

/*Store attribute*/
INSERT INTO Store VALUES ('2001', 36.3240833, -121.2612616, '1351 Oak Ave, Greenfield, CA 93927','Kim','010-1111-2222',1000000);
INSERT INTO Store VALUES ('2002', 36.7453908, -119.8240287, '894 W Belmont Ave, Fresno, CA 93728','Chung','017-193-1202', 500000);
INSERT INTO Store VALUES ('2003', 34.5827633, -118.1472724, '1233 Rancho Vista Blvd, Palmdale, CA 93551','Yoon','016-115-2222', 400000);
INSERT INTO Store VALUES ('2004', 34.1801346, -118.8882653, '350 W Hillcrest Dr, Thousand Oaks, CA 91360','Jun','011-631-1030', 300000);
INSERT INTO Store VALUES ('2005', 36.9680227, -122.0374471, '400 Beach St, Santa Cruz, CA 95060', 'Kim','011-812-1106',700000);
INSERT INTO Store VALUES ('2006', 36.5944317, -121.9026045, '1 Old Fishermans Wharf, Monterey, CA 93940','Sung','010-1124-9712', 800000);
INSERT INTO Store VALUES ('2007', 35.6352092, -120.65914, '2970 Union Rd, Paso Robles, CA 93446','Sim','010-4321-9283', 600000);
INSERT INTO Store VALUES ('2008', 35.4831096, -120.7056184, '9100 Morro Rd, Atascadero, CA 93422','Lim','010-1231-1943', 200000);
INSERT INTO Store VALUES ('2009', 35.1260491, -119.4690389, '29 Emmons Park Dr, Taft, CA 93268','Gang','010-1331-2932', 400000);
INSERT INTO Store VALUES ('2010', 37.6389002, -121.020682, '435 College Ave, Modesto, CA 95350','Goe','010-1001-1214', 600000);
INSERT INTO Store VALUES ('2011', 37.0656258, -120.8750696, '947 W Pacheco Blvd, Los Banos, CA 93635','Kim','017-1421-0182', 700000);
INSERT INTO Store VALUES ('2012', 35.0564827, -118.1804715, '16660 Sierra Hwy, Mojave, CA 93501','Ki','010-4166-1029', 800000);

/*Store_inventory*/
INSERT INTO Store_inventory VALUES ('2001', '0001', 13,6,29);
INSERT INTO Store_inventory VALUES ('2001', '0002', 23,11,48);
INSERT INTO Store_inventory VALUES ('2001', '0003', 33,16,66);
INSERT INTO Store_inventory VALUES ('2001', '0004', 43,21,86);
INSERT INTO Store_inventory VALUES ('2002', '0001', 11,5,25);
INSERT INTO Store_inventory VALUES ('2002', '0002', 21,10,46);
INSERT INTO Store_inventory VALUES ('2002', '0003', 31,15,63);
INSERT INTO Store_inventory VALUES ('2002', '0004', 41,20,80);
INSERT INTO Store_inventory VALUES ('2003', '0001', 19,10,40);
INSERT INTO Store_inventory VALUES ('2003', '0002', 29,14,60);
INSERT INTO Store_inventory VALUES ('2003', '0003', 39,15,80);
INSERT INTO Store_inventory VALUES ('2003', '0004', 49,19,90);
INSERT INTO Store_inventory VALUES ('2004', '0001', 12,9,30);
INSERT INTO Store_inventory VALUES ('2004', '0002', 22,12,40);
INSERT INTO Store_inventory VALUES ('2004', '0003', 32,13,50);
INSERT INTO Store_inventory VALUES ('2004', '0004', 42,15,60);
INSERT INTO Store_inventory VALUES ('2005', '0001', 16,11,80);
INSERT INTO Store_inventory VALUES ('2005', '0002', 26,12,90);
INSERT INTO Store_inventory VALUES ('2005', '0003', 36,15,60);
INSERT INTO Store_inventory VALUES ('2005', '0004', 46,14,90);
INSERT INTO Store_inventory VALUES ('2006', '0001', 10,5,20);
INSERT INTO Store_inventory VALUES ('2006', '0002', 20,10,40);
INSERT INTO Store_inventory VALUES ('2006', '0003', 30,15,60);
INSERT INTO Store_inventory VALUES ('2006', '0004', 40,20,80);
INSERT INTO Store_inventory VALUES ('2007', '0001', 13,6,28);
INSERT INTO Store_inventory VALUES ('2007', '0002', 23,11,45);
INSERT INTO Store_inventory VALUES ('2007', '0003', 33,16,66);
INSERT INTO Store_inventory VALUES ('2007', '0004', 43,21,88);
INSERT INTO Store_inventory VALUES ('2008', '0001', 15,6,35);
INSERT INTO Store_inventory VALUES ('2008', '0002', 25,12,50);
INSERT INTO Store_inventory VALUES ('2008', '0003', 35,15,70);
INSERT INTO Store_inventory VALUES ('2008', '0004', 45,21,90);
INSERT INTO Store_inventory VALUES ('2009', '0001', 23,11,48);
INSERT INTO Store_inventory VALUES ('2009', '0002', 33,15,66);
INSERT INTO Store_inventory VALUES ('2009', '0003', 43,20,80);
INSERT INTO Store_inventory VALUES ('2009', '0004', 53,24,100);
INSERT INTO Store_inventory VALUES ('2010', '0001', 13,6,20);
INSERT INTO Store_inventory VALUES ('2010', '0002', 23,10,40);
INSERT INTO Store_inventory VALUES ('2010', '0003', 33,14,60);
INSERT INTO Store_inventory VALUES ('2010', '0004', 43,20,70);
INSERT INTO Store_inventory VALUES ('2011', '0001', 14,7,29);
INSERT INTO Store_inventory VALUES ('2011', '0002', 24,12,50);
INSERT INTO Store_inventory VALUES ('2011', '0003', 34,17,68);
INSERT INTO Store_inventory VALUES ('2011', '0004', 44,22,88);
INSERT INTO Store_inventory VALUES ('2012', '0001', 11,5,20);
INSERT INTO Store_inventory VALUES ('2012', '0002', 21,10,45);
INSERT INTO Store_inventory VALUES ('2012', '0003', 31,15,60);
INSERT INTO Store_inventory VALUES ('2012', '0004', 41,20,80);

/*Warehouse_inventory*/
INSERT INTO Warehouse_inventory VALUES ('1001', '0001', 100,50,200);
INSERT INTO Warehouse_inventory VALUES ('1001', '0002', 130,65,250);
INSERT INTO Warehouse_inventory VALUES ('1001', '0003', 150,75,250);
INSERT INTO Warehouse_inventory VALUES ('1001', '0004', 170,70,300);
INSERT INTO Warehouse_inventory VALUES ('1002', '0001', 110,55,220);
INSERT INTO Warehouse_inventory VALUES ('1002', '0002', 140,70,280);
INSERT INTO Warehouse_inventory VALUES ('1002', '0003', 160,80,320);
INSERT INTO Warehouse_inventory VALUES ('1002', '0004', 130,60,260);
INSERT INTO Warehouse_inventory VALUES ('1003', '0001', 120,60,200);
INSERT INTO Warehouse_inventory VALUES ('1003', '0002', 160,80,320);
INSERT INTO Warehouse_inventory VALUES ('1003', '0003', 150,75,300);
INSERT INTO Warehouse_inventory VALUES ('1003', '0004', 170,85,340);
INSERT INTO Warehouse_inventory VALUES ('1004', '0001', 90,45,180);
INSERT INTO Warehouse_inventory VALUES ('1004', '0002', 110,55,220);
INSERT INTO Warehouse_inventory VALUES ('1004', '0003', 120,60,210);
INSERT INTO Warehouse_inventory VALUES ('1004', '0004', 130,65,270);
INSERT INTO Warehouse_inventory VALUES ('1005', '0001', 70,35,140);
INSERT INTO Warehouse_inventory VALUES ('1005', '0002', 90,45,180);
INSERT INTO Warehouse_inventory VALUES ('1005', '0003', 100,50,200);
INSERT INTO Warehouse_inventory VALUES ('1005', '0004', 110,55,220);
INSERT INTO Warehouse_inventory VALUES ('1006', '0001', 180,80,300);
INSERT INTO Warehouse_inventory VALUES ('1006', '0002', 190,89,380);
INSERT INTO Warehouse_inventory VALUES ('1006', '0003', 210,105,430);
INSERT INTO Warehouse_inventory VALUES ('1006', '0004', 230,110,450);
INSERT INTO Warehouse_inventory VALUES ('1007', '0001', 150,75,300);
INSERT INTO Warehouse_inventory VALUES ('1007', '0002', 130,65,250);
INSERT INTO Warehouse_inventory VALUES ('1007', '0003', 110,55,220);
INSERT INTO Warehouse_inventory VALUES ('1007', '0004', 90,45,180);
INSERT INTO Warehouse_inventory VALUES ('1008', '0001', 50,20,100);
INSERT INTO Warehouse_inventory VALUES ('1008', '0002', 70,35,140);
INSERT INTO Warehouse_inventory VALUES ('1008', '0003', 130,65,260);
INSERT INTO Warehouse_inventory VALUES ('1008', '0004', 170,75,340);
INSERT INTO Warehouse_inventory VALUES ('1009', '0001', 180,90,360);
INSERT INTO Warehouse_inventory VALUES ('1009', '0002', 180,90,320);
INSERT INTO Warehouse_inventory VALUES ('1009', '0003', 180,90,360);
INSERT INTO Warehouse_inventory VALUES ('1009', '0004', 210,100,400);

INSERT INTO Ordering VALUES ('10001', '2001','2016-11-22 13:00:00');
INSERT INTO Ordering VALUES ('10002', '2002','2016-11-22 13:00:00');
INSERT INTO Ordering VALUES ('10003', '2003','2016-11-23 14:00:00');
INSERT INTO Ordering VALUES ('10004', '2004','2016-11-23 14:00:00');
INSERT INTO Ordering VALUES ('10005', '2005','2016-11-24 15:00:00');

INSERT INTO Ordering_list VALUES ('0001','10001', 10);
INSERT INTO Ordering_list VALUES ('0002','10001', 30);
INSERT INTO Ordering_list VALUES ('0003','10001', 40);
INSERT INTO Ordering_list VALUES ('0001','10002', 5);
INSERT INTO Ordering_list VALUES ('0002','10002', 30);
INSERT INTO Ordering_list VALUES ('0003','10002', 50);
INSERT INTO Ordering_list VALUES ('0001','10003', 40);
INSERT INTO Ordering_list VALUES ('0003','10004', 50);
INSERT INTO Ordering_list VALUES ('0004','10004', 70);
INSERT INTO Ordering_list VALUES ('0004','10005', 80);

/*shipping*/
INSERT INTO shipping VALUES ('1001', '2001', '0001', 10, 2000, 1);
INSERT INTO shipping VALUES ('1008', '2003', '0003', 27, 1000, 1);
INSERT INTO shipping VALUES ('1005', '2007', '0002', 84, 4000, 1);
INSERT INTO shipping VALUES ('1007', '2009', '0002', 35, 3000, 1);
INSERT INTO shipping VALUES ('1003', '2006', '0001', 68, 2000, 1);
INSERT INTO shipping VALUES ('1004', '2002', '0004', 71, 1000, 1);
INSERT INTO shipping VALUES ('1009', '2004', '0001', 53, 5000, 1);
INSERT INTO shipping VALUES ('1002', '2012', '0003', 41, 1100, 1);
INSERT INTO shipping VALUES ('1008', '2010', '0004', 3, 3000, 1);
INSERT INTO shipping VALUES ('1002', '2011', '0002', 32, 4000, 1);
INSERT INTO shipping VALUES ('1006', '2009', '0001', 59, 5000, 1);
INSERT INTO shipping VALUES ('1005', '2005', '0004', 26, 1000, 1);
INSERT INTO shipping VALUES ('1004', '2004', '0003', 17, 1000, 1);
INSERT INTO shipping VALUES ('1003', '2005', '0002', 65, 4000, 1);
INSERT INTO shipping VALUES ('1006', '2012', '0001', 15, 3000, 1);
INSERT INTO shipping VALUES ('1005', '2011', '0004', 84, 2000, 1);
INSERT INTO shipping VALUES ('1008', '2007', '0003', 37, 6000, 1);
INSERT INTO shipping VALUES ('1002', '2003', '0002', 41, 2000, 1);
INSERT INTO shipping VALUES ('1001', '2009', '0004', 40, 4000, 1);
INSERT INTO shipping VALUES ('1005', '2004', '0001', 28, 3000, 1);
INSERT INTO shipping VALUES ('1007', '2001', '0003', 38, 1000, 1);
INSERT INTO shipping VALUES ('1009', '2003', '0002', 31, 2000, 1);
INSERT INTO shipping VALUES ('1004', '2004', '0004', 63, 3000, 1);
INSERT INTO shipping VALUES ('1005', '2007', '0001', 61, 1200, 1);
INSERT INTO shipping VALUES ('1003', '2005', '0003', 38, 1500, 1);