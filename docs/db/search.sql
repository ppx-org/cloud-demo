
/** ----------------- search ----------------- */

create table search_normal_v1
(
	PROD_ID  	int(11) NOT NULL,	
	INDEX_ID 	int(11) NOT NULL,
	MERCHANT_ID int(11) NOT NULL,
	WORDS 		varchar(256) NOT NULL,
	primary key (PROD_ID)
) comment='综合排序搜索v1';

create table search_normal_v2
(
	PROD_ID  	int(11) NOT NULL,	
	INDEX_ID 	int(11) NOT NULL,
	MERCHANT_ID int(11) NOT NULL,
	WORDS 		varchar(256) NOT NULL,
	primary key (PROD_ID)
) comment='综合排序搜索v2';

create table search_new_v1
(
	PROD_ID  	int(11) NOT NULL,	
	INDEX_ID 	int(11) NOT NULL,
	MERCHANT_ID int(11) NOT NULL,
	WORDS 		varchar(256) NOT NULL,
	primary key (PROD_ID)
) comment='最新排序搜索v1';

create table search_new_v2
(
	PROD_ID  	int(11) NOT NULL,	
	INDEX_ID 	int(11) NOT NULL,
	MERCHANT_ID int(11) NOT NULL,
	WORDS 		varchar(256) NOT NULL,
	primary key (PROD_ID)
) comment='最新排序搜索v2';


create table search_ext_word
(
	MERCHANT_ID int(11) NOT NULL,
	EXT_WORD 	varchar(32) NOT NULL,
	primary key (MERCHANT_ID, EXT_WORD)
);

create table search_version
(
	MERCHANT_ID 	int(11) NOT NULL,
	VERSION_NAME 	varchar(32) NOT NULL,
	CREATE_BEGIN 	datetime,
	CREATE_END		datetime,
	VERSION_STATUS  tinyint(1) NOT NULL DEFAULT 1,
	CREATE_INFO		varchar(1024),
	UPDATED 		datetime NOT NULL,
	UPDATOR			int(11) NOT NULL,
	primary key (MERCHANT_ID, VERSION_NAME)
);

create table search_hot_word
(
	HOT_ID		int(11) NOT NULL auto_increment,
	STORE_ID 	int(11) NOT NULL,
	HOT_WORD 	varchar(32) NOT NULL,
	HOT_PRIO	int not null,
	CREATED     timestamp not null default CURRENT_TIMESTAMP,
	primary key (HOT_ID)
);

create table search_last_updated
(
	MERCHANT_ID 		int(11) NOT NULL,
	PROG_LAST_UPDATED 	datetime,
	PROD_LAST_UPDATED 	datetime,
	primary key (MERCHANT_ID)
) comment='产品或促销最后变更时间';


/** TODO 存在更新，不存在插入, 超过10就删除,?异步处理 */
create table search_last_word
(
	OPENID		varchar(32) NOT NULL,
	LAST_WORD	varchar(32) NOT NULL,
	CREATED  	timestamp not null default CURRENT_TIMESTAMP,
	primary key (OPENID, LAST_WORD)
) comment='搜索历史';



