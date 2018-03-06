/** 创建表的时候写注释
create table test1
(
field_name int comment '字段的注释'
) comment='表的注释';
*/

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
) comment='首页轮播';
create index idx_home_swiper_store_id on home_swiper(STORE_ID);

create table home_level
(
	LEVEL_ID 	int(11) NOT NULL auto_increment,
	STORE_ID 	int(11) NOT NULL,
	LEVEL_NAME	varchar(32) NOT NULL,
	LEVEL_PRIO 	int not null,
	CREATED     timestamp not null default CURRENT_TIMESTAMP,
	primary key (LEVEL_ID)
) comment='首页楼层';
create index idx_home_level_store_id on home_level(STORE_ID);

create table home_level_product
(
	LEVEL_ID 	int(11) NOT NULL,
	PROD_ID 	int(11) NOT NULL,
	PROD_PRIO 	int not null,
	CREATED     timestamp not null default CURRENT_TIMESTAMP,
	primary key (LEVEL_ID, PROD_ID)
) comment='首页楼层产品';

create table home_img
(
	MERCHANT_ID int(11) NOT NULL,
	IMG_TYPE 	varchar(32) NOT NULL,
	IMG_PRIO	int(11) NOT NULL,
	IMG_SRC		varchar(256),
	primary key (MERCHANT_ID, IMG_TYPE)
) comment='首页图片,分类等合成图';






