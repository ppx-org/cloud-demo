
/** ----------------- core ----------------- **/

CREATE TABLE test 
(
  TEST_ID 		int(11) not null auto_increment,
  TEST_NAME 	varchar(32) not null,
  TEST_TIME 	timestamp not null default cURRENT_TIMESTAMP,
  TEST_DATE 	date default NULL,
  primary key (TEST_ID)
);
create unique index idx_test_name on test(TEST_NAME);

/** ----------------- merchant ----------------- */

create table merchant_account 
(
  ACCOUNT_ID 		int(11) not null auto_increment,
  MERCHANT_ID 		int(11) not null,
  LOGIN_ACCOUNT 	varchar(32) not null,
  LOGIN_PASSWORD 	varchar(32) not null,
  RECORD_STATUS 	tinyint(1) not null default 1,
  CREATED 			timestamp not null default CURRENT_TIMESTAMP,
  primary key (ACCOUNT_ID)
);

/** MERCHANT_ID继承merchant_account.ACCOUNT_ID */
create table merchant 
(
  MERCHANT_ID 		int(11) not null,
  MERCHANT_NAME 	varchar(32) not null,
  RECORD_STATUS 	tinyint(1) not null default 1,
  CREATED 			timestamp not null default CURRENT_TIMESTAMP,
  primary key (MERCHANT_ID)
);

/** index */
create index idx_merchant_account_mer_id on merchant_account(MERCHANT_ID);
create unique index idx_merchant_account_acc on merchant_account(LOGIN_ACCOUNT);
create unique index idx_merchant_name on merchant(MERCHANT_NAME);

create table repository
(
   REPO_ID              int not null auto_increment,
   MERCHANT_ID          int not null,
   REPO_NAME            varchar(32) not null,
   REPO_ADDRESS         varchar(128) not null,
   RECORD_STATUS        smallint not null default 1,
   CREATED              timestamp not null default CURRENT_TIMESTAMP,
   primary key (REPO_ID)
);

/** STORE_ID继承repository.REPO_ID */
create table store
(
   STORE_ID             int not null,
   MERCHANT_ID          int not null,
   STORE_NAME           varchar(32) not null,
   STORE_NO             varchar(32) not null,
   STORE_LNG			varchar(32) not null,
   STORE_LAT			varchar(32) not null,
   STORE_PHONE			varchar(32),
   STORE_IMG			varchar(128),
   RECORD_STATUS        smallint not null default 1,
   CREATED              timestamp not null default CURRENT_TIMESTAMP,
   primary key (STORE_ID)
);

create table store_map_repo
(
   REPO_ID              int not null,
   STORE_ID             int not null,
   primary key (REPO_ID, STORE_ID)
);

create table category
(
   CAT_ID               int not null auto_increment,
   MERCHANT_ID          int not null,
   PARENT_ID            int not null,
   CAT_NAME             varchar(32) not null,
   CAT_PRIO             int not null,
   CAT_IMG_X			smallint not null default 0,
   CAT_IMG_Y			smallint not null default 0,
   RECORD_STATUS        smallint not null default 1,
   CREATED              timestamp not null default CURRENT_TIMESTAMP,
   primary key (CAT_ID)
);

create table brand
(
   BRAND_ID             int not null auto_increment,
   MERCHANT_ID          int not null,
   BRAND_NAME           varchar(32) not null,
   BRAND_PRIO           int not null,
   BRAND_IMG_X			smallint not null default 0,
   BRAND_IMG_Y			smallint not null default 0,
   RECORD_STATUS        smallint not null default 1,
   CREATED              timestamp not null default CURRENT_TIMESTAMP,
   primary key (BRAND_ID)
);

create table theme
(
   THEME_ID           int not null auto_increment,
   MERCHANT_ID        int,
   THEME_NAME         varchar(32) not null,
   THEME_PRIO         int not null,
   THEME_IMG_X		  smallint not null default 0,
   THEME_IMG_Y	      smallint not null default 0,
   RECORD_STATUS      smallint not null default 1,
   CREATED            timestamp not null default CURRENT_TIMESTAMP,
   primary key (THEME_ID)
);

create table theme_map_prod
(
   THEME_ID			int not null,
   PROD_ID			int not null,
   primary key (THEME_ID, PROD_ID)
);

/** ----------------- release ----------------- */

create table sku
(
   SKU_ID               int not null auto_increment,
   PROD_ID              int not null,
   STOCK_NUM            int not null not null,
   PRICE                decimal(7,2) not null,
   SKU_PRIO             int not null,
   SKU_NAME				varchar(32),
   SKU_IMG_SRC          varchar(128),
   primary key (SKU_ID)
);

create table product
(
   PROD_ID              int not null,
   MERCHANT_ID          int not null,
   CAT_ID               int not null,
   MAIN_CAT_ID			int not null,	
   BRAND_ID             int,
   REPO_ID              int not null,
   PROD_TITLE           varchar(32) int not null,
   SKU_DESC				varchar(32),
   PROD_PRIO			int not null default 10000,
   PROD_STATUS 			tinyint(1) NOT NULL DEFAULT 1,   
   primary key (PROD_ID)
);

create table product_detail
(
	PROD_ID 		int not null,
	PROD_POSITION	varchar(32),
	BAR_CODE		varchar(32),
	PROD_DESC		varchar(128),
	PROD_ARGS		varchar(128),
	CREATED 		timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	primary key (PROD_ID)
);

create table product_img
(
   PROD_IMG_ID           int not null auto_increment,
   PROD_ID               int not null,
   PROD_IMG_PRIO         int not null,
   PROD_IMG_SRC          varchar(128) not null,
   primary key (PROD_IMG_ID)
);

/** ----------------- user ----------------- */

create table user_order
(
   ORDER_ID             int not null auto_increment,
   OPENID				varchar(32) NOT NULL,
   STORE_ID             int not null,
   ORDER_TIME           timestamp not null,
   ORDER_STATUS         smallint not null,
   ORDER_PRICE          decimal(7,2) not null,
   PAY_PRICE            decimal(7,2) not null,
   primary key (ORDER_ID)
);

create table order_item
(
   ITEM_ID              int not null auto_increment,
   ORDER_ID             int not null,
   SKU_ID               int not null,
   PROD_ID				int not null,
   ITEM_UNIT_PRICE      decimal(7,2) not null,
   ITEM_PRICE           decimal(7,2) not null,
   ITEM_NUM             int not null,
   ITEM_TITLE           varchar(32) not null,
   ITEM_SKU             varchar(32),
   ITEM_IMG             varchar(128),
   ITEM_PROMO           varchar(32),
   primary key (ITEM_ID)
);

create table user_cart 
(
	OPENID		varchar(32) NOT NULL,
	SKU_ID		int not null,
	STORE_ID	int not null,
	SKU_NUM		int not null,
	CREATED 	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	primary key (OPENID, SKU_ID)
);

create table user_favorite
(
	OPENID		varchar(32) NOT NULL,
	PROD_ID		int not null,
	STORE_ID	int not null,
	CREATED 	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	primary key (OPENID, PROD_ID)
);


