import time
import pandas as pd
from playwright.sync_api import sync_playwright
from tqdm import tqdm
from pykrx import stock

from create_aggregate_table import CreateAggregateTable
from mysql_connector import MySQLDataConnector



connector = MySQLDataConnector(user='root',
                               password='1234',
                               host='localhost',
                               database='stockdb')
connector.close()
create_aggregate_table = CreateAggregateTable(connector)
df_s, df_t = create_aggregate_table.MentionIncrements()
print(df_t,df_s)
