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
