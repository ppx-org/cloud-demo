
/** ----------------- promo ----------------- */

create table program (
  PROG_ID 			int(11) not NULL auto_increment,
  MERCHANT_ID 		int(11) not NULL,
  PROG_NAME 		varchar(32) not NULL,
  PROG_PRIO 		int(11) not NULL,
  PROG_BEGIN 		date not NULL,
  PROG_END 			date not NULL,
  POLICY_TYPE 		varchar(32) NOT NULL,
  POLICY_ARGS 		varchar(32),
  PROG_IMG_X		smallint not null default 0,
  PROG_IMG_Y		smallint not null default 0,
  PROG_STATUS 		smallint not null default 1,
  CREATED 			timestamp not null default CURRENT_TIMESTAMP,
  primary key (PROG_ID)
) comment='促销方案';

create table program_status_history
(	
  HISTORY_PROG_STATUS_ID 	int not null auto_increment,
  PROG_ID        			int not null,
  HISTORY_PROG_STATUS		smallint not null,
  CREATED         			timestamp not null default CURRENT_TIMESTAMP,
  CREATOR					int,
  primary key(HISTORY_PROG_STATUS_ID)
) comment='促销状态变更历史';

create table program_category
(
   PROG_ID              int not null,
   CAT_ID               int not null,
   primary key (PROG_ID, CAT_ID)
) comment='分类促销';

create table program_brand
(
   PROG_ID              int not null,
   BRAND_ID             int not null,
   primary key (PROG_ID, BRAND_ID)
) comment='品牌促销';

create table program_product
(
   PROG_ID              int not null,
   PROD_ID           	int not null,
   primary key (PROG_ID, PROD_ID)
) comment='产品促销';

create table program_change
(
   PROG_ID              int not null,
   PROD_ID              int not null,
   CHANGE_PRICE         decimal(7,2) not null,
   primary key (PROG_ID, PROD_ID)
) comment='换购促销';

create table program_special
(
   PROG_ID              int not null,
   PROD_ID              int not null,
   SPECIAL_PRICE        decimal(7,2) not null,
   primary key (PROG_ID, PROD_ID)
) comment='特价促销';

create table program_dependence
(
   PROG_ID              int not null,
   PROD_ID              int not null,
   DEPEND_RPOD_ID       int not null,
   DEPEND_PRICE         decimal(7,2) not null,
   primary key (PROD_ID, PROG_ID)
) comment='组合促销';

create table program_index
(
   PROD_ID              int not null,
   PROG_ID              int not null,
   MERCHANT_ID 			int not null,
   INDEX_BEGIN          date not null,
   INDEX_END            date not null,
   INDEX_PRIO           int not null,
   INDEX_POLICY         varchar(32) not null,
   INDEX_GROUP          varchar(32),
   CAT_ID 				int,
   BRAND_ID 			int,
   primary key (PROD_ID, PROG_ID)
) comment='价格索引';
create index idx_program_index_prog on program_index(PROG_ID);
create index idx_program_index_merchant on program_index(MERCHANT_ID);
create index idx_program_index_date on program_index(PROD_ID, INDEX_BEGIN, INDEX_END);






