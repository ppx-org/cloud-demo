
/** ----------------- content/home ----------------- */

create table home_swiper
(
	SWIPER_ID	int(11) NOT NULL auto_increment,
	STORE_ID 	int(11) NOT NULL,
	SWIPER_IMG 	varchar(128) NOT NULL,
	SWIPER_URL 	varchar(128) NOT NULL,
	SWIPER_PRIO	int not null,
	CREATED     timestamp not null default CURRENT_TIMESTAMP,
	primary key (SWIPER_ID)
);

create table home_level
(
	LEVEL_ID 	int(11) NOT NULL auto_increment,
	STORE_ID 	int(11) NOT NULL,
	LEVEL_NAME	varchar(32) NOT NULL,
	LEVEL_PRIO 	int not null,
	CREATED     timestamp not null default CURRENT_TIMESTAMP,
	primary key (LEVEL_ID)
);

create table home_level_product
(
	LEVEL_ID 	int(11) NOT NULL,
	PROD_ID 	int(11) NOT NULL,
	PROD_PRIO 	int not null,
	CREATED     timestamp not null default CURRENT_TIMESTAMP,
	primary key (LEVEL_ID, PROD_ID)
);

create table home_img
(
	MERCHANT_ID int(11) NOT NULL,
	IMG_TYPE 	varchar(32) NOT NULL,
	IMG_PRIO	int(11) NOT NULL,
	IMG_SRC		varchar(256),
	primary key (MERCHANT_ID, IMG_TYPE)
);




