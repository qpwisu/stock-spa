import time

import schedule

from create_aggregate_table import CreateAggregateTable
from mysql_connector import MySQLDataConnector
from stock_crawler import StockCrawler
from blog_crawler import BlogCrawler
from blog_analyzer  import BlogAnalyzer
import datetime
import pandas as pd
import asyncio


'''매일 00시 01분에 실행 '''
def update_1D():
    now_time = datetime.datetime.now()
    print("start update_1D : " + now_time.strftime('%Y-%m-%d %H:%M:%S'))
    crawler = StockCrawler()
    connector = MySQLDataConnector(user='root',
                                   password='1234',
                                   host='localhost',
                                   database='stockdb')

    now = datetime.datetime.now().date() - datetime.timedelta(days=1)
    now = now.strftime('%Y%m%d')

    """국내 종목 정보 업데이트"""
    print("1. 종목 업데이트")
    tickers = connector.select_columns("stocks", ["ticker"])["ticker"].tolist()
    stocks_df = crawler.stocks_crawler(tickers)
    connector.upload_dataframe(stocks_df, 'stocks',if_exists='append')
    print("2. 종목 정보 업데이트")
    stock_info_df = crawler.stock_info_crawler(stocks_df)
    connector.upload_dataframe(stock_info_df, 'stock_info',if_exists='append')

    """국내 종목 주가 업데이트"""
    print("3. 종목 주가 업데이트")
    stocks_df = connector.select_columns("stocks")
    last_date = connector.search_last_date("stock_price")
    start_date = str(last_date + datetime.timedelta(days=1)).replace("-", "")
    end_date = (datetime.datetime.now() - datetime.timedelta(days=1))
    df_kr_price = crawler.stock_price_crawler(stocks_df, start_date, end_date)
    connector.upload_dataframe(df_kr_price, 'stock_price')
    '''테마 크롤링 업데이트'''
    print("4. 테마 업데이트")
    exist_theme = connector.select_columns("themes", ["theme_name"])["theme_name"].values
    themes = crawler.theme_crawler(exist_theme)
    if not themes.empty:
        connector.upload_dataframe(themes, 'themes')

    '''테마의 포함되는 종목 크롤링 업데이트 '''
    print("5. 테마관련 종목 업데이트")
    stocks_df = connector.select_columns("stocks")
    themes_df = connector.select_columns("themes")
    df_theme = crawler.theme_stock_crawler(stocks_df,themes_df)
    connector.insert_new_data(df_theme, 'theme_stock')

    crawler_blog = BlogCrawler()
    analyzer_blog = BlogAnalyzer()
    last_date = connector.search_last_date("theme_blog")
    start_date = last_date + datetime.timedelta(days=1)
    end_date = datetime.datetime.now() - datetime.timedelta(days=1)

    '''네이버 테마 관련주 검색한 블로그글 크롤링 및 업로드'''
    print("6. 테마 검색 블로그 업데이트")
    themes_df =  connector.select_columns("themes")
    themes = themes_df["theme_name"].tolist()
    themes_blog_df = asyncio.run(crawler_blog.blog_crawler(themes, start_date, end_date))
    if isinstance(themes_blog_df, pd.DataFrame):
        themes_blog_df = themes_blog_df.merge(themes_df, left_on="name", right_on="theme_name")
        themes_blog_df.rename(columns={"id": "theme_id"}, inplace=True)
        themes_blog_df.drop(['name', 'theme_name'], axis=1, inplace=True)
        connector.upload_dataframe(themes_blog_df, 'theme_blog')

    print("7. 테마 검색 블로그에서 추출한 종목명 업데이트")
    max_theme_blog_id = connector.default_query("select max(theme_blog_id) from theme_blog_stock_name;").values[0][0]
    stocks_df = connector.select_columns("stocks")
    themas_blog_df = connector.default_query(f"select tb.id as blog_id, tb.title,tb.header,tb.theme_id,t.theme_name from theme_blog tb join themes t on tb.theme_id = t.id where tb.id > {max_theme_blog_id};")
    if not themas_blog_df.empty:
        themas_blog_analyzer_df = analyzer_blog.word_extractor(themas_blog_df, stocks_df)
        themas_blog_analyzer_df = themas_blog_analyzer_df.rename({"blog_id":"theme_blog_id","id":"stock_id"},axis=1)
        connector.upload_dataframe(themas_blog_analyzer_df, 'theme_blog_stock_name')



    '''네이버 종목명 관련주 검색한 블로그글 크롤링 및 업로드'''
    print("8. 종목명 검색 블로그 업데이트")
    last_date = connector.search_last_date("stock_blog")
    start_date = last_date + datetime.timedelta(days=1)
    end_date = datetime.datetime.now() - datetime.timedelta(days=1)

    stocks_df = connector.select_columns("stocks")
    company = stocks_df["company_name"].tolist()
    stock_blog_df = asyncio.run(crawler_blog.blog_crawler(company, start_date, end_date))
    if isinstance(stock_blog_df, pd.DataFrame):
        stock_blog_df = stock_blog_df.merge(stocks_df, left_on="name", right_on="company_name")
        stock_blog_df.rename(columns={"id": "stock_id"}, inplace=True)
        stock_blog_df.drop(['name', 'company_name',"ticker"], axis=1, inplace=True)
        connector.upload_dataframe(stock_blog_df, 'stock_blog')

    print("9. 종목명 검색 블로그에서 추출한 종목명 업데이트")
    max_stock_blog_id = connector.default_query("select max(stock_blog_id) from stock_blog_stock_name;").values[0][0]
    stocks_df = connector.select_columns("stocks")
    stock_blog_df = connector.default_query(f"select sb.id as blog_id, sb.title,sb.header,sb.stock_id,s.company_name from stock_blog sb join stocks s on sb.stock_id = s.id where sb.id > {max_stock_blog_id};")
    if not stock_blog_df.empty:
        stock_blog_analyzer_df = analyzer_blog.word_extractor(stock_blog_df, stocks_df)
        stock_blog_analyzer_df = stock_blog_analyzer_df.rename({"blog_id": "stock_blog_id", "id": "stock_id"}, axis=1)
        connector.upload_dataframe(stock_blog_analyzer_df, 'stock_blog_stock_name')

    '''
    집계 테이블
    # '''
    create_aggregate_table = CreateAggregateTable(connector)
    # # 테마 전체 언급 순위
    print("10. total_theme_aggregate update")
    connector.truncate_table("total_theme_aggregate")
    agg_df2 = create_aggregate_table.theme_topN(20)
    connector.upload_dataframe(agg_df2, 'total_theme_aggregate', if_exists="append")
    # 주식 종목명 전체 언급 순위
    print("11. total_stock_aggregate update")
    connector.truncate_table("total_stock_aggregate")
    agg_df2 = create_aggregate_table.stock_topN(20)
    connector.upload_dataframe(agg_df2, 'total_stock_aggregate', if_exists="append")
    print("12.each_aggregate update")
    connector.truncate_table("each_stock_aggregate")
    connector.truncate_table("each_theme_aggregate")
    df_c, df_s = create_aggregate_table.stock_category_aggregate(10)
    # 종목별 같이 언급된 테마 순위
    connector.upload_dataframe(df_c, 'each_stock_aggregate', if_exists="append")
    # 테마별 같이 언급된 종목 순위
    connector.upload_dataframe(df_s, 'each_theme_aggregate', if_exists="append")
    print("13. date_aggregate update")
    create_aggregate_table = CreateAggregateTable(connector)
    df_s, df_t = create_aggregate_table.date_stock_category_aggregate()
    connector.truncate_table("date_stock_aggregate")
    connector.truncate_table("date_theme_aggregate")
    connector.upload_dataframe(df_s, 'date_stock_aggregate', if_exists="append")
    connector.upload_dataframe(df_t, 'date_theme_aggregate', if_exists="append")
    print("14. related_stock_aggregate update")
    connector.truncate_table("related_stock_aggregate")
    df = create_aggregate_table.related_stock()
    connector.upload_dataframe(df, 'related_stock_aggregate', if_exists="append")
    print("15. mention_increments update ")
    connector.truncate_table("stock_mention_increments")
    connector.truncate_table("theme_mention_increments")
    df_s, df_t = create_aggregate_table.MentionIncrements()
    connector.upload_dataframe(df_s, 'stock_mention_increments', if_exists="append")
    connector.upload_dataframe(df_t, 'theme_mention_increments', if_exists="append")
    connector.close()
    end_time = datetime.datetime.now()
    print("end update_1D : " + end_time.strftime('%Y-%m-%d %H:%M:%S'))
    print("Execution time: " + str(end_time - now_time))

def update_1M():
    print("start update_1M")
    crawler = StockCrawler()
    connector = MySQLDataConnector(user='root',
                                   password='1234',
                                   host='localhost',
                                   database='stockdb')
    create_aggregate_table = CreateAggregateTable(connector)
    # # 현재 주식 가격
    agg_df7 = create_aggregate_table.get_now_stock_price()
    connector.update_or_insert_dataframe(agg_df7, 'stock_price_now')
    query = """
    SELECT ts.theme_id AS theme_id, AVG(spn.change_rate) AS average_change_rate, COUNT(spn.change_rate) AS cnt
    FROM theme_stock ts
    JOIN stocks s ON ts.stock_id = s.id
    JOIN stock_price_now spn ON spn.stock_id = s.id
    GROUP BY ts.theme_id;
    """
    df = pd.read_sql_query(query, connector.engine)
    connector.update_or_insert_dataframe(df, 'theme_average_change_rate')
    connector.close()


def schedule():
    print("start")
    while True:
        now = datetime.datetime.now()
        current_time = now.time()
        current_weekday = now.weekday()

        # 매일 00:01에 update_1D 실행
        if current_time >= datetime.time(0, 1) and current_time < datetime.time(0, 2):
            update_1D()
    
        # 월~금 09:00 ~ 16:00 사이에 update_1M 실행
        if current_weekday < 5 and current_time >= datetime.time(9, 0) and current_time <= datetime.time(16, 0):
            update_1M()
    
        time.sleep(60)


def test():
    update_1D()
    # update_1M()
test()
schedule()
