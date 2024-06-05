DROP database IF EXISTS stockdb;
create database stockdb;
use stockdb;

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
	`id`	BIGINT	NOT NULL,
	`created_at`	DATETIME(6)	NULL,
	`last_modified_at`	DATETIME(6)	NULL,
	`body`	VARCHAR(255)	NULL,
	`board_id`	BIGINT	NULL,
	`user_id`	BIGINT	NULL
);

DROP TABLE IF EXISTS `upload_image`;

CREATE TABLE `upload_image` (
	`id`	BIGINT	NOT NULL,
	`original_filename`	VARCHAR(255)	NULL,
	`saved_filename`	VARCHAR(255)	NULL
);

DROP TABLE IF EXISTS `board`;

CREATE TABLE `board` (
	`id`	BIGINT	NOT NULL,
	`created_at`	DATETIME(6)	NULL,
	`last_modified_at`	DATETIME(6)	NULL,
	`body`	VARCHAR(255)	NULL,
	`category`	VARCHAR(255)	NULL,
	`comment_cnt`	INT	NULL,
	`like_cnt`	INT	NULL,
	`title`	VARCHAR(255)	NULL,
	`upload_image_id`	BIGINT	NULL,
	`user_id`	BIGINT	NULL
);

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
	`id`	BIGINT	NOT NULL,
	`created_at`	DATETIME(6)	NULL,
	`login_id`	VARCHAR(255)	NULL,
	`nickname`	VARCHAR(255)	NULL,
	`password`	VARCHAR(255)	NULL,
	`user_role`	INT	NULL
);

DROP TABLE IF EXISTS `like`;

CREATE TABLE `like` (
	`id`	BIGINT	NOT NULL,
	`board_id`	BIGINT	NULL,
	`user_id`	BIGINT	NULL
);




DROP TABLE IF EXISTS `stock_info`;

CREATE TABLE `stock_info` (
	`id`	BIGINT	NOT NULL,
	`bps`	FLOAT	NULL,
	`company_description`	VARCHAR(255)	NULL,
	`divided`	FLOAT	NULL,
	`divided_rate`	FLOAT	NULL,
	`eps`	FLOAT	NULL,
	`market`	VARCHAR(10)	NULL,
	`market_cap`	DECIMAL(19,2)	NULL,
	`pbr`	FLOAT	NULL,
	`per`	FLOAT	NULL,
	`sector`	VARCHAR(255)	NULL,
	`stock_id`	BIGINT	NOT NULL
);

DROP TABLE IF EXISTS `stocks`;

CREATE TABLE `stocks` (
	`id`	BIGINT	NOT NULL,
	`ticker`	varchar(10)	NOT NULL,
	`company_name`	varchar(50)	NOT NULL
);

DROP TABLE IF EXISTS `themes`;

CREATE TABLE `themes` (
	`id`	BIGINT	NOT NULL,
	`theme_name`	varchar(100)	NOT NULL
);

DROP TABLE IF EXISTS `theme_stock`;

CREATE TABLE `theme_stock` (
	`id`	BIGINT	NOT NULL,
	`theme_id`	BIGINT	NOT NULL,
	`stock_id`	BIGINT	NOT NULL
);

DROP TABLE IF EXISTS `stock_price_now`;

CREATE TABLE `stock_price_now` (
	`id`	BIGINT	NOT NULL,
	`date`	DATETIME(6)	NULL,
	`open`	DECIMAL(10,2)	NULL,
	`high`	DECIMAL(10,2)	NULL,
	`low`	DECIMAL(10,2)	NULL,
	`close`	DECIMAL(10,2)	NULL,
	`volume`	BIGINT	NULL,
	`change_rate`	DECIMAL(10,2)	NULL
);

DROP TABLE IF EXISTS `theme_blog`;
CREATE TABLE `theme_blog` (
	`id`	BIGINT	NOT NULL,
	`title`	TEXT	NULL,
	`header`	TEXT	NULL,
	`href`	TEXT	NULL,
	`date`	DATETIME(6)	NULL,
	`theme_id`	BIGINT	NOT NULL
);

DROP TABLE IF EXISTS `theme_blog_stock_name`;

CREATE TABLE `theme_blog_stock_name` (
	`id`	BIGINT	NOT NULL,
	`theme_blog_id`	BIGINT	NOT NULL,
	`stock_id`	BIGINT	NOT NULL,
	`company_name`	 varchar(50)	NOT NULL

);

DROP TABLE IF EXISTS `stock_blog`;

CREATE TABLE `stock_blog` (
	`id`	BIGINT	NOT NULL,
	`title`	TEXT	NULL,
	`header`	TEXT	NULL,
	`href`	TEXT	NULL,
	`date`	DATETIME(6)	NULL,
	`stock_id`	BIGINT	NOT NULL
);

DROP TABLE IF EXISTS `stock_blog_stock_name`;

CREATE TABLE `stock_blog_stock_name` (
	`id`	BIGINT	NOT NULL,
	`stock_blog_id`	BIGINT	NOT NULL,
	`stock_id`	BIGINT	NOT NULL,
	`company_name`	 varchar(50)	NOT NULL

);

DROP TABLE IF EXISTS `stock_price`;

CREATE TABLE `stock_price` (
	`id`	BIGINT	NOT NULL,
	`date`	DATE	NOT NULL,
	`open`	DECIMAL(20,4)	NULL,
	`high`	DECIMAL(20,4)	NULL,
	`low`	DECIMAL(20,4)	NULL,
	`close`	DECIMAL(20,4)	NULL,
	`volume`	BIGINT	NULL,
	`change_rate`	DECIMAL(10,2)	NULL,
	`stock_id`	BIGINT	NOT NULL
);

CREATE TABLE `total_theme_aggregate` (
	`id`	bigint	NOT NULL,
	`cnt`	int	NOT NULL,
	`ranking`	int	NOT NULL,
	`period`	int	NOT NULL,
	`theme_id`	BIGINT	NOT NULL
);

CREATE TABLE `total_stock_aggregate` (
	`id`	bigint	NOT NULL,
	`cnt`	int	NOT NULL,
	`ranking`	int	NOT NULL,
	`period`	int	NOT NULL,
	`stock_id`	BIGINT	NOT NULL
);
CREATE TABLE `each_theme_aggregate` (
	`id`	bigint	NOT NULL,
	`cnt`	bigint	NULL,
	`ranking`	bigint	NULL,
	`period`	bigint	NULL,
	`stock_id`	BIGINT	NOT NULL,
	`theme_id`	BIGINT	NOT NULL
);

CREATE TABLE `each_stock_aggregate` (
	`id`	bigint	NOT NULL,
	`cnt`	bigint	NULL,
	`ranking`	bigint	NULL,
	`period`	bigint	NULL,
	`stock_id`	BIGINT	NOT NULL,
	`theme_id`	BIGINT	NOT NULL
);



ALTER TABLE `comment` ADD CONSTRAINT `PK_COMMENT` PRIMARY KEY (
	`id`
);

ALTER TABLE `upload_image` ADD CONSTRAINT `PK_UPLOAD_IMAGE` PRIMARY KEY (
	`id`
);

ALTER TABLE `board` ADD CONSTRAINT `PK_BOARD` PRIMARY KEY (
	`id`
);

ALTER TABLE `user` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`id`
);

ALTER TABLE `like` ADD CONSTRAINT `PK_LIKE` PRIMARY KEY (
	`id`
);





ALTER TABLE `stock_info` ADD CONSTRAINT `PK_STOCK_INFO` PRIMARY KEY (
	`id`
);

ALTER TABLE `stocks` ADD CONSTRAINT `PK_STOCKS` PRIMARY KEY (
	`id`
);

ALTER TABLE `themes` ADD CONSTRAINT `PK_THEMES` PRIMARY KEY (
	`id`
);

ALTER TABLE `theme_stock` ADD CONSTRAINT `PK_THEME_STOCK` PRIMARY KEY (
	`id`
);

ALTER TABLE `stock_price_now` ADD CONSTRAINT `PK_stock_price_now` PRIMARY KEY (
	`id`
);

ALTER TABLE `theme_blog` ADD CONSTRAINT `PK_THEME_BLOG` PRIMARY KEY (
	`id`
);

ALTER TABLE `theme_blog_stock_name` ADD CONSTRAINT `PK_theme_BLOG_STOCK_NAME` PRIMARY KEY (
	`id`
);

ALTER TABLE `stock_blog` ADD CONSTRAINT `PK_STOCK_BLOG` PRIMARY KEY (
	`id`
);
ALTER TABLE `stock_blog_stock_name` ADD CONSTRAINT `PK_STOCK_BLOG_STOCK_NAME` PRIMARY KEY (
	`id`
);

ALTER TABLE `stock_price` ADD CONSTRAINT `PK_STOCK_PRICE` PRIMARY KEY (
	`id`
);
ALTER TABLE `total_theme_aggregate` ADD CONSTRAINT `PK_TOTAL_THEME_AGGREGATE` PRIMARY KEY (
	`id`
);

ALTER TABLE `total_stock_aggregate` ADD CONSTRAINT `PK_TOTAL_STOCK_AGGREGATE` PRIMARY KEY (
	`id`
);

ALTER TABLE `each_theme_aggregate` ADD CONSTRAINT `PK_EACH_THEME_AGGREGATE` PRIMARY KEY (
	`id`
);

ALTER TABLE `each_stock_aggregate` ADD CONSTRAINT `PK_EACH_STOCK_AGGREGATE` PRIMARY KEY (
	`id`
);


# 외래키 설정전에 auto_increment 설정 (erd_cloud에서 auto_increment 설정이 불가능해서 따로 처리해야함)
ALTER TABLE user MODIFY COLUMN id bigint auto_increment  NOT NULL;
ALTER TABLE comment  MODIFY COLUMN id bigint auto_increment  NOT NULL;
ALTER TABLE board  MODIFY COLUMN id bigint auto_increment  NOT NULL;
ALTER TABLE upload_image  MODIFY COLUMN id bigint auto_increment  NOT NULL;
ALTER TABLE `like`  MODIFY COLUMN id bigint auto_increment  NOT NULL;

ALTER TABLE stock_info  MODIFY COLUMN id bigint auto_increment  NOT NULL;
ALTER TABLE stocks MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE theme_stock MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE theme_blog_stock_name MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE theme_blog MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE themes MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE stock_blog MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE stock_blog_stock_name MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE stock_price MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE total_theme_aggregate MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE total_stock_aggregate MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE each_theme_aggregate MODIFY COLUMN id bigint auto_increment NOT NULL;
ALTER TABLE each_stock_aggregate MODIFY COLUMN id bigint auto_increment NOT NULL;



ALTER TABLE `comment` ADD CONSTRAINT `FK_board_TO_comment_1` FOREIGN KEY (
	`board_id`
)
REFERENCES `board` (
	`id`
);

ALTER TABLE `comment` ADD CONSTRAINT `FK_user_TO_comment_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`id`
);

ALTER TABLE `board` ADD CONSTRAINT `FK_upload_image_TO_board_1` FOREIGN KEY (
	`upload_image_id`
)
REFERENCES `upload_image` (
	`id`
);

ALTER TABLE `board` ADD CONSTRAINT `FK_user_TO_board_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`id`
);

ALTER TABLE `like` ADD CONSTRAINT `FK_board_TO_like_1` FOREIGN KEY (
	`board_id`
)
REFERENCES `board` (
	`id`
);

ALTER TABLE `like` ADD CONSTRAINT `FK_user_TO_like_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`id`
);

ALTER TABLE `stock_info` ADD CONSTRAINT `FK_stocks_TO_stock_info_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `theme_stock` ADD CONSTRAINT `FK_themes_TO_theme_stock_1` FOREIGN KEY (
	`theme_id`
)
REFERENCES `themes` (
	`id`
);

ALTER TABLE `theme_stock` ADD CONSTRAINT `FK_stocks_TO_theme_stock_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `stock_price_now` ADD CONSTRAINT `FK_stocks_TO_stock_price_now_1` FOREIGN KEY (
	`id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `theme_blog` ADD CONSTRAINT `FK_themes_TO_theme_blog_1` FOREIGN KEY (
	`theme_id`
)
REFERENCES `themes` (
	`id`
);

ALTER TABLE `theme_blog_stock_name` ADD CONSTRAINT `FK_theme_blog_TO_theme_blog_stock_name_1` FOREIGN KEY (
	`theme_blog_id`
)
REFERENCES `theme_blog` (
	`id`
);

ALTER TABLE `theme_blog_stock_name` ADD CONSTRAINT `FK_stocks_TO_theme_blog_stock_name_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);


ALTER TABLE `stock_blog` ADD CONSTRAINT `FK_stocks_TO_stock_blog_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);


ALTER TABLE `stock_blog_stock_name` ADD CONSTRAINT `FK_stock_blog_TO_stock_blog_stock_name_1` FOREIGN KEY (
	`stock_blog_id`
)
REFERENCES `stock_blog` (
	`id`
);

ALTER TABLE `stock_blog_stock_name` ADD CONSTRAINT `FK_stocks_TO_stock_blog_stock_name_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `stock_price` ADD CONSTRAINT `FK_stocks_TO_stock_price_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);


ALTER TABLE `total_theme_aggregate` ADD CONSTRAINT `FK_themes_TO_total_theme_aggregate_1` FOREIGN KEY (
	`theme_id`
)
REFERENCES `themes` (
	`id`
);

ALTER TABLE `total_stock_aggregate` ADD CONSTRAINT `FK_stocks_TO_total_stock_aggregate_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `each_stock_aggregate` ADD CONSTRAINT `FK_stocks_TO_each_stock_aggregate_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `each_stock_aggregate` ADD CONSTRAINT `FK_themes_TO_each_stock_aggregate_1` FOREIGN KEY (
	`theme_id`
)
REFERENCES `themes` (
	`id`
);




ALTER TABLE `each_theme_aggregate` ADD CONSTRAINT `FK_stocks_TO_each_theme_aggregate_1` FOREIGN KEY (
	`stock_id`
)
REFERENCES `stocks` (
	`id`
);

ALTER TABLE `each_theme_aggregate` ADD CONSTRAINT `FK_themes_TO_each_theme_aggregate_1` FOREIGN KEY (
	`theme_id`
)
REFERENCES `themes` (
	`id`
);

create index idx_ticker on stocks(ticker);