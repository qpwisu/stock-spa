import time
import datetime

import pandas as pd
from stock_crawler import StockCrawler
from pykrx import stock


class CreateAggregateTable():

    def __init__(self,connector):
        self.connector = connector


    '''
    주식 테마 전체 언급 순위 
    '''
    def theme_topN(self, topN):
        df_list = []
        period_list = [1,3,7,30]
        for period in period_list:
            start_date = (datetime.datetime.now() - datetime.timedelta(days=1+period)).strftime('%Y-%m-%d')
            end_date = (datetime.datetime.now() - datetime.timedelta(days=1)).strftime('%Y-%m-%d')
            dff = self.connector.default_query(
                f"SELECT theme_id, COUNT(theme_id) AS cnt FROM theme_blog WHERE date >= '{start_date}' AND date <= '{end_date}' GROUP BY theme_id ORDER BY cnt DESC LIMIT {topN};")
            dff['ranking'] = dff.index + 1
            dff['period'] = period
            df_list.append(dff)
        df = pd.concat(df_list)
        return df

    def stock_topN(self, topN):
        df_list = []
        period_list = [1,3,7,30]
        for period in period_list:
            start_date = (datetime.datetime.now() - datetime.timedelta(days=1+period)).strftime('%Y-%m-%d')
            end_date = (datetime.datetime.now() - datetime.timedelta(days=1)).strftime('%Y-%m-%d')
            dff = self.connector.default_query(
                f"SELECT stock_id, COUNT(stock_id) AS cnt FROM stock_blog WHERE date >= '{start_date}' AND date <= '{end_date}' GROUP BY stock_id ORDER BY cnt DESC LIMIT {topN};")
            dff['ranking'] = dff.index + 1
            dff['period'] = period
            df_list.append(dff)
        df = pd.concat(df_list)
        return df


    def stock_category_aggregate(self,topN):
        category_df_list = []
        stock_df_list = []

        period_list = [1,3,7,30]
        for period in period_list:
            start_date = (datetime.datetime.now() - datetime.timedelta(days=1+period)).strftime('%Y-%m-%d')
            end_date = (datetime.datetime.now() - datetime.timedelta(days=1)).strftime('%Y-%m-%d')
            df = self.connector.default_query(
                f"""
                    select ts.stock_id,tb.theme_id,count(*) as cnt 
                    from theme_blog as tb 
                    join theme_blog_stock_name as ts on tb.id = ts.theme_blog_id  
                    WHERE date >= '{start_date}' AND date <= '{end_date}'   
                    group by ts.stock_id,tb.theme_id ;
                """
            )
            # stock_id별로 그룹화하고, 각 그룹 내에서 cnt 컬럼 값이 가장 큰 상위 topN개의 행을 선택합니다.
            df_c = df.groupby('stock_id').apply(lambda x: x.nlargest(topN, 'cnt')).reset_index(drop=True)
            df_c['ranking'] = df_c.groupby('stock_id').cumcount() + 1
            df_c['period'] = period
            category_df_list.append(df_c)

            df_s = df.groupby('theme_id').apply(lambda x: x.nlargest(topN, 'cnt')).reset_index(drop=True)
            df_s['ranking'] = df_s.groupby('theme_id').cumcount() + 1
            df_s['period'] = period
            stock_df_list.append(df_s)

        category_df = pd.concat(category_df_list)
        stock_df = pd.concat(stock_df_list)

        return category_df,stock_df



    def date_stock_category_aggregate(self):
        theme_df_list = []
        stock_df_list = []

        period = 30
        start_date = (datetime.datetime.now() - datetime.timedelta(days=1+period)).strftime('%Y-%m-%d')
        end_date = (datetime.datetime.now() - datetime.timedelta(days=1)).strftime('%Y-%m-%d')

        df = self.connector.default_query(
            f"""
            select sb.stock_id ,date ,count(*) as cnt 
            from stock_blog as sb 
            join stock_blog_stock_name as ss on sb.id = ss.stock_blog_id
            WHERE date >= '{start_date}' AND date <= '{end_date}' 
            group by stock_id,date ;
            """
        )
        stock_df_list.append(df)

        df2 = self.connector.default_query(
            f"""
            select tb.theme_id ,date ,count(*) as cnt 
            from theme_blog as tb 
            join theme_blog_stock_name as ts on tb.id = ts.theme_blog_id
            WHERE date >= '{start_date}' AND date <= '{end_date}' 
            group by theme_id,date ;
            """
        )
        theme_df_list.append(df2)

        stock_df = pd.concat(stock_df_list).groupby([ 'stock_id', 'date'])['cnt'].sum().reset_index()
        theme_df = pd.concat(theme_df_list).groupby(['theme_id', 'date'])['cnt'].sum().reset_index()


        return stock_df,theme_df

    '''
    현재 주식 가격 
    '''
    def get_now_stock_price(self):
        stock_info_df = self.connector.default_query(f"select id as stock_id, ticker from stocks;")
        """국내 종목 주가 업데이트"""
        crawler = StockCrawler()
        end_date = datetime.datetime.now().strftime('%Y-%m-%d')
        df_kr_price = crawler.now_stock_price_crawler(end_date)
        if df_kr_price.empty:
            print("No stock data fetched, returning empty DataFrame.")
            return df_kr_price
        df_kr_price = pd.merge(left=stock_info_df, right=df_kr_price, how="inner", on="ticker")
        df_kr_price.drop("ticker", axis=1, inplace=True)
        return df_kr_price

    def related_stock(self):
        period = 30
        start_date = (datetime.datetime.now() - datetime.timedelta(days=1+period)).strftime('%Y-%m-%d')
        end_date = (datetime.datetime.now() - datetime.timedelta(days=1)).strftime('%Y-%m-%d')

        df = self.connector.default_query(
        f'''
        WITH relatedStocks AS (
            SELECT 
                sb.stock_id AS search_stock_id,
                ss.stock_id AS mention_stock_id,
                COUNT(*) AS cnt,
                ROW_NUMBER() OVER (PARTITION BY sb.stock_id ORDER BY COUNT(*) DESC) AS rn
            FROM 
                stock_blog AS sb
            JOIN 
                stock_blog_stock_name AS ss ON sb.id = ss.stock_blog_id
            WHERE sb.date >= '{start_date}' AND sb.date <= '{end_date}' 
            GROUP BY 
                sb.stock_id, ss.stock_id
        )
        SELECT 
            search_stock_id, 
            mention_stock_id, 
            cnt
        FROM 
            relatedStocks
        WHERE 
            rn <= 10;
        ''')
        return df

    def MentionIncrements(self):

        df_s = self.connector.default_query(
        f'''
        WITH DailyMentions AS (
            SELECT
                date,
                stock_id,
                COUNT(*) AS mentions -- 하루 동안의 언급 횟수 집계
            FROM
                stock_blog
            GROUP BY
                date, stock_id
        ),
        MentionsChange AS (
            SELECT
                stock_id,
                date,
                mentions,
                LAG(mentions, 1) OVER (PARTITION BY stock_id ORDER BY date) AS previous_mentions, -- 이전 날의 언급 횟수
                (mentions - COALESCE(LAG(mentions, 1) OVER (PARTITION BY stock_id ORDER BY date), 0)) AS mention_change -- 전날 대비 변화량 계산
            FROM
                DailyMentions
        )
        SELECT
            stock_id,
            MAX(mention_change) AS increase -- 가장 큰 증가량
        FROM
            MentionsChange
        GROUP BY
            stock_id
        ORDER BY
            increase DESC
        LIMIT 20; -- 가장 큰 증가를 보인 stock_id 하나만 출력
        ''')

        df_t = self.connector.default_query(
        f'''
        WITH DailyMentions AS (
            SELECT
                date,
                theme_id,
                COUNT(*) AS mentions -- 하루 동안의 언급 횟수 집계
            FROM
                theme_blog
            GROUP BY
                date, theme_id
        ),
        MentionsChange AS (
            SELECT
                theme_id,
                date,
                mentions,
                LAG(mentions, 1) OVER (PARTITION BY theme_id ORDER BY date) AS previous_mentions, -- 이전 날의 언급 횟수
                (mentions - COALESCE(LAG(mentions, 1) OVER (PARTITION BY theme_id ORDER BY date), 0)) AS mention_change -- 전날 대비 변화량 계산
            FROM
                DailyMentions
        )
        SELECT
            theme_id,
            MAX(mention_change) AS increase -- 가장 큰 증가량
        FROM
            MentionsChange
        GROUP BY
            theme_id
        ORDER BY
            increase DESC
        LIMIT 20; -- 가장 큰 증가를 보인 stock_id 하나만 출력
        ''')

        return df_s,df_t