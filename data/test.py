import datetime
import time
import pandas as pd
from playwright.sync_api import sync_playwright
from tqdm import tqdm
from pykrx import stock

from create_aggregate_table import CreateAggregateTable
from mysql_connector import MySQLDataConnector
from stock_crawler import StockCrawler

crawler = StockCrawler()

connector = MySQLDataConnector(user='root',
                               password='1234',
                               host='localhost',
                               database='stockdb')
create_aggregate_table = CreateAggregateTable(connector)
connector.truncate_table("stock_price")
stocks_df = connector.select_columns("stocks")
last_date = connector.search_last_date("stock_price")
start_date = "20210101"
end_date = "20240611"
df_kr_price = crawler.stock_price_crawler(stocks_df, start_date, end_date)
connector.upload_dataframe(df_kr_price, 'stock_price')

# SQL 문 실행
#create_table_sql = """
#CREATE TABLE `suggest_month_stock` (
 #   `id` bigint NOT NULL AUTO_INCREMENT,
  #  `cnt` int NOT NULL,
   # `month` int NOT NULL,
    #`stock_id` BIGINT NOT NULL,
   # PRIMARY KEY (`id`),
   # CONSTRAINT `FK_stocks_TO_suggest_month_stock_1` FOREIGN KEY (`stock_id`) REFERENCES `stocks` (`id`)
#)"""
#connector.insert_default_query(create_table_sql)
connector.close()
