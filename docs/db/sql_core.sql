
/** ----------------- core ----------------- **/

CREATE TABLE test 
(
  TEST_ID 		int(11) not null auto_increment,
  TEST_NAME 	varchar(32) not null,
  TEST_TIME 	timestamp not null default CURRENT_TIMESTAMP,
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
) comment='商户帐号';

/** MERCHANT_ID继承merchant_account.ACCOUNT_ID */
create table merchant 
(
  MERCHANT_ID 		int(11) not null,
  MERCHANT_NAME 	varchar(32) not null,
  RECORD_STATUS 	tinyint(1) not null default 1,
  CREATED 			timestamp not null default CURRENT_TIMESTAMP,
  primary key (MERCHANT_ID)
) comment='商户';

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
) comment='仓库';
create index idx_repository_merchant_id on repository(MERCHANT_ID);

/** STORE_ID继承repository.REPO_ID */
create table store
(
   STORE_ID             int not null,
   MERCHANT_ID          int not null,
   STORE_NAME           varchar(32) not null,
   STORE_NO             varchar(32) not null,
   STORE_LNG			varchar(32) not null,
   STORE_LAT			varchar(32) not null,
   BUSINESS_BEGIN		time not null,
   BUSINESS_END			time not null,
   STORE_PHONE			varchar(32),
   STORE_IMG			varchar(128),
   RECORD_STATUS        smallint not null default 1,
   CREATED              timestamp not null default CURRENT_TIMESTAMP,
   primary key (STORE_ID)
) comment='店铺';
create index idx_store_merchant_id on store(MERCHANT_ID);

create table store_map_repo
(
   REPO_ID              int not null,
   STORE_ID             int not null,
   primary key (REPO_ID, STORE_ID)
) comment='店铺对应仓库';

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
) comment='分类';
create index idx_category_merchant_id on category(MERCHANT_ID);
create index idx_category_parent_id on category(PARENT_ID);

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
) comment='品牌';
create index idx_brand_merchant_id on brand(MERCHANT_ID);

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
) comment='主题';
create index idx_theme_merchant_id on theme(MERCHANT_ID);

create table theme_map_prod
(
   THEME_ID			int not null,
   PROD_ID			int not null,
   primary key (THEME_ID, PROD_ID)
) comment='主题产品';

/** ----------------- release ----------------- */

create table change_stock
(
	CHANGE_STOCK_ID int not null auto_increment,
	SKU_ID          int not null,
	CHANGE_NUM 		int not null,
	CHANGE_TYPE	 	smallint not null,
	CHANGE_COMMENT	varchar(128),
	CREATED         timestamp not null default CURRENT_TIMESTAMP,
	CREATOR			int,
	OPENID			varchar(32),
	ORDER_ID        int,
	primary key (CHANGE_STOCK_ID)
) comment='产品库存变更历史';
create index idx_change_stock_prod_id on change_stock(SKU_ID);

create table change_price
(
	CHANGE_PRICE_ID	int not null auto_increment,
	SKU_ID			int not null,
	CHANGE_PRICE	decimal(7,2) not null,
	CHANGE_COMMENT	varchar(128),
	CREATED         timestamp not null default CURRENT_TIMESTAMP,
	CREATOR			int not null,
	primary key (CHANGE_PRICE_ID)
) comment='产品价格变更历史';
create index idx_change_price_prod_id on change_price(SKU_ID);

create table change_status 
(
	CHANGE_STATUS_ID 	int not null auto_increment,
	PROD_ID 			int not null,
	CHANGE_STATUS 		tinyint(1) not null,
	CREATED         	timestamp not null default CURRENT_TIMESTAMP,
	CREATOR				int not null,
	primary key (CHANGE_STATUS_ID)
) comment='产品状态变更历史';
create index idx_change_status_prod_id on change_status(PROD_ID);

create table sku
(
   SKU_ID               int not null auto_increment,
   PROD_ID              int not null,
   STOCK_NUM            int not null,
   SOLD_NUM				int not null default 0,
   PRICE                decimal(7,2) not null,
   SKU_PRIO             int not null,
   SKU_NAME				varchar(32),
   SKU_IMG_SRC          varchar(128),
   primary key (SKU_ID)
) comment='产品SKU';
create index idx_sku_prod_id on sku(PROD_ID);

create table product
(
   PROD_ID              int not null,
   MERCHANT_ID          int not null,
   CAT_ID               int not null,
   MAIN_CAT_ID			int not null,	
   BRAND_ID             int,
   REPO_ID              int not null,
   PROD_TITLE           varchar(32) not null,
   SKU_DESC				varchar(32),
   PROD_PRIO			int not null default 10000,
   PROD_STATUS 			tinyint(1) not null DEFAULT 1,   
   primary key (PROD_ID)
) comment='产品';
create index idx_product_merchant_id on product(MERCHANT_ID);
create index idx_product_cat_id on product(CAT_ID);
create index idx_product_repo_id on product(REPO_ID);
create index idx_product_prod_status on product(PROD_STATUS);

create table product_detail
(
	PROD_ID 		int not null,
	CREATED 		timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CREATOR			int not null,
	PROD_POSITION	varchar(32),
	BAR_CODE		varchar(32),
	PROD_DESC		varchar(512),
	PROD_ARGS		varchar(512),
	primary key (PROD_ID)
) comment='产品明细';

create table product_img
(
   PROD_IMG_ID           int not null auto_increment,
   PROD_ID               int not null,
   PROD_IMG_PRIO         int not null,
   PROD_IMG_SRC          varchar(128) not null,
   primary key (PROD_IMG_ID)
) comment='产品图片';
create index idx_product_img_prod_id on product_img(PROD_ID);


/** ----------------- user ----------------- */

create table user_info
(
	OPENID				varchar(32) not null,
	LAST_LOGIN_UPDATED	timestamp not null default CURRENT_TIMESTAMP,
	LAST_LOGIN_TYPE		tinyint(1) not null,
	NICK_NAME			varchar(64),
	GENDER				tinyint(1),
	LANGUAGE			varchar(32),
	CITY				varchar(32),
	PROVINCE			varchar(32),
	COUNTRY				varchar(32),
	AVATAR_URL			varchar(128),
	primary key(OPENID)
) comment='用户信息';



create table user_order
(
   ORDER_ID             int not null auto_increment,
   OPENID				varchar(32) NOT NULL,
   STORE_ID             int not null,
   ORDER_TIME           timestamp not null,
   ORDER_STATUS         smallint not null,
   ORDER_PRICE          decimal(7,2) not null,
   PAY_PRICE            decimal(7,2) not null,
   DELIVER_CODE			varchar(32),
   primary key (ORDER_ID)
) comment='订单';
create index idx_user_order_openid on user_order(OPENID);
create index idx_user_order_store_id on user_order(STORE_ID);

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
) comment='订单项';
create index idx_order_item_order_id on order_item(ORDER_ID);
create index idx_order_item_prod_id on order_item(PROD_ID);

create table order_item_status
(
	ITEM_ID 			int not null,
    ITEM_STATUS 		tinyint(1) not null default 1,
    LOCK_TIME 			timestamp not null,
    LOCK_OPERATOR		int not null,
	CONFIG_TIME			timestamp,
    CONFIG_OPERATOR		int,
    DELIVER_TIME		timestamp,
    DELIVER_OPERATOR	int,
	primary key(ITEM_ID)
) comment='订单项状态变更历史';

create table order_status_history
(	
	HISTORY_ID 		int not null auto_increment,
	ORDER_ID        int not null,
	HISTORY_STATUS  smallint not null,
	CREATED         timestamp not null default CURRENT_TIMESTAMP,
	CREATOR			int,
	OPENID			varchar(32),
	HISTORY_COMMENT	varchar(128),
	primary key(HISTORY_ID)
) comment='订单状态变更历史';
create index idx_order_his_order_id on order_status_history(ORDER_ID);

create table user_cart 
(
	OPENID		varchar(32) NOT NULL,
	SKU_ID		int not null,
	STORE_ID	int not null,
	SKU_NUM		int not null,
	CREATED 	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	primary key (OPENID, SKU_ID)
) comment='购物车';

create table user_favorite
(
	OPENID		varchar(32) NOT NULL,
	PROD_ID		int not null,
	STORE_ID	int not null,
	CREATED 	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	primary key (OPENID, PROD_ID)
) comment='收藏';

create table import_data (
  MERCHANT_ID 	int(11) NOT NULL,
  ROWNUM 		int(11) NOT NULL,
  RESULT 		int(11) NOT NULL,
  INT_1 		int(11) DEFAULT NULL,
  INT_2 		int(11) DEFAULT NULL,
  NUM_1 		decimal(7,2) DEFAULT NULL,
  NUM_2 		decimal(7,2) DEFAULT NULL,
  CHAR_1 		varchar(32) DEFAULT NULL,
  CHAR_2 		varchar(32) DEFAULT NULL,
  primary key (MERCHANT_ID, ROWNUM)
) comment='导入数据中间表';





