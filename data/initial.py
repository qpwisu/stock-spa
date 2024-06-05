import asyncio

from mysql_connector import MySQLDataConnector
from stock_crawler import StockCrawler
from blog_crawler import BlogCrawler
from blog_analyzer  import BlogAnalyzer
import datetime
import pandas as pd
crawler = StockCrawler()
connector = MySQLDataConnector(user='root',
                               password='1234',
                               host='localhost',
                               database='stockdb')

now = datetime.datetime.now().date() - datetime.timedelta(days=1)
now = now.strftime('%Y%m%d')

"""국내 종목 정보 25M 소요"""
# stocks_df = crawler.stocks_crawler()
# connector.upload_dataframe(stocks_df, 'stocks',if_exists='append')

# stocks_df = connector.select_columns("stocks")
# stock_info_df = crawler.stock_info_crawler(stocks_df)
# connector.upload_dataframe(stock_info_df, 'stock_info',if_exists='append')

# """국내 종목 주가 3M 소요"""
# stocks_df = connector.select_columns("stocks")
# df_kr_price = crawler.stock_price_crawler(stocks_df, "20230101", now)
# connector.upload_dataframe(df_kr_price, 'stock_price')
#
# '''국내 주식 테마 크롤링'''
# df_theme = crawler.theme_crawler()
# connector.upload_dataframe(df_theme, 'themes')
'''테마의 포함되는 종목 클롤링 '''
# stocks_df = connector.select_columns("stocks")
# themes_df = connector.select_columns("themes")
# df_theme = crawler.theme_stock_crawler(stocks_df,themes_df)
# connector.upload_dataframe(df_theme, 'theme_stock')

crawler_blog = BlogCrawler()
analyzer_blog = BlogAnalyzer()
'''
네이버 테마 관련주 검색한 블로그글 크롤링 및 업로드
# '''

# themes_df =  connector.select_columns("themes")
# themes = themes_df["theme_name"].tolist()
# themes_blog_df = asyncio.run(crawler_blog.blog_crawler(themes, "20240401", now))
# themes_blog_df = themes_blog_df.merge(themes_df,left_on="name",right_on="theme_name")
# themes_blog_df.rename(columns={"id": "theme_id"}, inplace=True)
# themes_blog_df.drop(['name', 'theme_name'], axis=1, inplace=True)
# connector.upload_dataframe(themes_blog_df, 'theme_blog')
# #

# stocks_df = connector.select_columns("stocks")
# themas_blog_df = connector.default_query("select tb.id as blog_id, tb.title,tb.header,tb.theme_id,t.theme_name from theme_blog tb join themes t on tb.theme_id = t.id ;")
# themas_blog_analyzer_df = analyzer_blog.word_extractor(themas_blog_df, stocks_df)
# themas_blog_analyzer_df = themas_blog_analyzer_df.rename({"blog_id":"theme_blog_id","id":"stock_id"},axis=1)
# connector.upload_dataframe(themas_blog_analyzer_df, 'theme_blog_stock_name')

# stocks_df =  connector.select_columns("stocks")
# company = stocks_df["company_name"].tolist()[:40]
# stock_blog_df = asyncio.run(crawler_blog.blog_crawler(company, "20240502", "20240502"))
# stock_blog_df = stock_blog_df.merge(stocks_df,left_on="name",right_on="company_name")
# stock_blog_df.rename(columns={"id": "stock_id"}, inplace=True)
# stock_blog_df.drop(['name', 'company_name','ticker'], axis=1, inplace=True)
# connector.upload_dataframe(stock_blog_df, 'stock_blog')


# stocks_df = connector.select_columns("stocks")
# stock_blog_df = connector.default_query("select sb.id as blog_id, sb.title,sb.header,sb.stock_id,s.company_name from stock_blog sb join stocks s on sb.stock_id = s.id ;")
# stock_blog_analyzer_df = analyzer_blog.word_extractor(stock_blog_df, stocks_df)
# stock_blog_analyzer_df = stock_blog_analyzer_df.rename({"blog_id":"stock_blog_id","id":"stock_id"},axis=1)
# connector.upload_dataframe(stock_blog_analyzer_df, 'stock_blog_stock_name')

# connector.upload_dataframe(themes_blog_df, 'theme_blog')# stock_df.rename(columns={"name": "company_name"}, inplace=True)






connector.close()
