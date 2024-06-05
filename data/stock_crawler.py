import time
import pandas as pd
from playwright.sync_api import sync_playwright
from tqdm import tqdm
from pykrx import stock
from datetime import datetime
from playwright.async_api import async_playwright
import asyncio
import re
pd.set_option('display.max_rows', None)  # 모든 행을 출력하도록 설정
pd.set_option('display.max_columns', None)  # 모든 열을 출력하도록 설정
pd.set_option('display.width', 1000)  # 출력 너비를 넓게 설정
pd.set_option('display.max_colwidth', None)  # 열 내용 전체를 출력하도록 설정

class StockCrawler:
    # 국내 주식 정보 크롤러 티커, 종목명, 마켓, 기업개요 크롤링
    def df_strip(self, df):
        str_columns = df.select_dtypes(['object', 'string']).columns
        df[str_columns] = df[str_columns].apply(lambda col: col.str.strip() if col.dtypes == object else col)
        return df

    # 숫자와 '.' 추출
    def extract_numbers_and_dots(self, text):
        if text == "N/A":
            return None
        numbers_and_dots = re.findall(r'[\d.]+', text)
        # 찾아낸 숫자와 '.'들을 연결
        return float(''.join(numbers_and_dots))

    def stocks_crawler(self, exist_ticker=[]):
        df = stock.get_market_ticker_list(market="KOSPI")
        df_kp = pd.DataFrame({'ticker': df, 'company_name': [stock.get_market_ticker_name(ticker) for ticker in df]})
        df = stock.get_market_ticker_list(market="KOSDAQ")
        df_kd = pd.DataFrame({'ticker': df, 'company_name': [stock.get_market_ticker_name(ticker) for ticker in df]})
        df_combined = pd.concat([df_kp, df_kd], ignore_index=True)
        df_combined = self.df_strip(df_combined)
        # exist_ticker에 있는 티커를 제외
        df_combined = df_combined[~df_combined['ticker'].isin(exist_ticker)]
        return df_combined

    def stock_info_crawler(self,df_combined):
        # 기업 개요 크롤링
        ticker_list = df_combined["ticker"].to_list()
        company_description_list = []
        sector_list = []
        market_cap_list = []
        per_list = []
        eps_list = []
        pbr_list = []
        bps_list = []
        divided_list = []
        divided_rate_list = []
        count = 0
        with sync_playwright() as p:
            browser = p.chromium.launch(headless=True)
            context = browser.new_context()
            context.route('**/*.{png,jpg,jpeg}', lambda route: route.abort())
            page = context.new_page()
            for ticker in tqdm(ticker_list):
                try:
                    url = f"https://finance.naver.com/item/main.nhn?code={str(ticker)}"
                    page.goto(url, timeout=3000)
                    # 업종 크롤링
                    try:
                        page.wait_for_selector("#content > div.section.trade_compare > h4 > em > a", timeout=1000)
                        sector_ele = page.locator("#content > div.section.trade_compare > h4 > em > a")
                        sector_list.append(sector_ele.inner_text())
                    except Exception as e:
                        # print(f"섹터 크롤링 실패 {ticker}: {e}")
                        sector_list.append(None)
                    # 기업개요 크롤링
                    try:
                        page.wait_for_selector("#middle > div.h_company > div.wrap_company > div > em.summary > a",
                                               timeout=1001)
                        page.locator('#middle > div.h_company > div.wrap_company > div > em.summary > a').click()
                        elements_locator = page.locator('#summary_info > p')
                        elements_texts = elements_locator.all_text_contents()
                        company_description_list.append("\n".join(elements_texts))
                    except Exception as e:
                        # print(f"기업개요 크롤링 실패 {ticker}: {e}")
                        # 기업 개요 빈값
                        company_description_list.append(None)
                    # 시총, per 등 크롤링
                    try:
                        page.goto(f"https://m.stock.naver.com/domestic/stock/{str(ticker)}/total", timeout=10000)
                        page.wait_for_selector("#content > div > div > div > div.StockInfo_article__2fBr3 > a",
                                               timeout=1002)
                        btn = page.locator("#content > div > div > div > div.StockInfo_article__2fBr3 > a")
                        if btn.inner_text() == "종목 정보 더보기":
                            btn.click()
                            time.sleep(0.5)

                        elements = page.query_selector_all("li.StockInfo_item__H7Aor > div.StockInfo_inner__8plY1")
                        dic = {}
                        for ele in elements:
                            tmp = ele.inner_text()
                            tmp = tmp.split("\n")
                            dic[tmp[0]] = tmp[1]

                        market_cap_list.append(self.extract_numbers_and_dots(dic["시총"]) * (10 ** 8))
                        per_list.append(self.extract_numbers_and_dots(dic["PER"]))
                        eps_list.append(self.extract_numbers_and_dots(dic["EPS"]))
                        pbr_list.append(self.extract_numbers_and_dots(dic["PBR"]))
                        bps_list.append(self.extract_numbers_and_dots(dic["BPS"]))
                        divided_list.append(self.extract_numbers_and_dots(dic["주당배당금"]))
                        divided_rate_list.append(self.extract_numbers_and_dots(dic["배당수익률"]))

                    except Exception as e:
                        # print(f"이외 회사 정보 크로링 실패 {ticker}: {e}")
                        market_cap_list.append(None)
                        per_list.append(None)
                        eps_list.append(None)
                        pbr_list.append(None)
                        bps_list.append(None)
                        divided_list.append(None)
                        divided_rate_list.append(None)

                except Exception as e:
                    print(f"페이지 이동 실패 {ticker}: {e}")
                    sector_list.append(None)
                    company_description_list.append(None)
                    market_cap_list.append(None)
                    per_list.append(None)
                    eps_list.append(None)
                    pbr_list.append(None)
                    bps_list.append(None)
                    divided_list.append(None)
                    divided_rate_list.append(None)

                finally:
                    # 페이지를 주기적으로 닫아주지 않으면 AttributeError: 'dict' object has no attribute '_object 에러 발생
                    count += 1
                    if count == 100:
                        page.close()
                        count = 0
                        page = browser.new_page()
            browser.close()
        df_combined["company_description"] = company_description_list
        df_combined["sector"] = sector_list
        df_combined["market_cap"] = market_cap_list
        df_combined["per"] = per_list
        df_combined["eps"] = eps_list
        df_combined["pbr"] = pbr_list
        df_combined["bps"] = bps_list
        df_combined["divided"] = divided_list
        df_combined["divided_rate"] = divided_rate_list
        df_combined.rename(columns={"id": "stock_id"}, inplace=True)
        df_combined.drop('ticker', axis=1, inplace=True)
        df_combined.drop('company_name', axis=1, inplace=True)

        return df_combined

    '''
    국내 주식 가격 크롤링
    '''
    def stock_price_crawler(self, stocks_df, start_date, end_date):
        df_kr_price = pd.DataFrame()
        ticker_list = stocks_df["ticker"].tolist()
        # 국내 종목 티커
        for ticker in tqdm(ticker_list):
            df = stock.get_market_ohlcv(start_date, end_date, ticker).reset_index()
            df["티커"] = ticker
            df_kr_price = pd.concat([df_kr_price, df], axis=0)

        df_kr_price.rename(columns={
            '날짜': 'date',
            '시가': 'open',
            '고가': 'high',
            '저가': 'low',
            '종가': 'close',
            '거래량': 'volume',
            '등락률': 'change_rate',
            '티커': 'ticker'
        }, inplace=True)

        merged_df = stocks_df.merge(df_kr_price, on='ticker')
        merged_df.rename(columns={"id": "stock_id"}, inplace=True)
        merged_df = merged_df[['stock_id','date', 'open', 'high', 'low', 'close', 'volume', 'change_rate']]
        return merged_df



    def theme_crawler(self, exist_thema=[]):
        with sync_playwright() as p:
            browser = p.chromium.launch(headless=True)
            page = browser.new_page()

            page.goto("https://finance.naver.com/sise/theme.naver?&page=99")
            elements = page.query_selector_all("td.on")
            last_page = int(elements[0].text_content())
            thema_list = []
            for i in range(1, last_page + 1):
                page.goto(f"https://finance.naver.com/sise/theme.naver?&page={i}")
                elements = page.query_selector_all("tr > td.col_type1 > a")
                themas = [thema.text_content() for thema in elements]
                thema_list.extend(themas)
            browser.close()
        df = pd.DataFrame({"theme_name" : thema_list})
        df = df[~df['theme_name'].isin(exist_thema)]
        df = df.drop_duplicates()
        return df

    def theme_stock_crawler(self, stocks_df, themes_df):
        result = []
        with sync_playwright() as p:
            browser = p.chromium.launch(headless=True)
            context = browser.new_context()
            context.route('**/*.{png,jpg,jpeg}', lambda route: route.abort())
            page = context.new_page()
            page.goto("https://finance.naver.com/sise/theme.naver?&page=99")
            elements = page.query_selector_all("td.on")
            last_page = int(elements[0].text_content())
            hrefs = []
            for i in range(1, last_page + 1):
                page.goto(f"https://finance.naver.com/sise/theme.naver?&page={i}")
                elements = page.query_selector_all(
                    "#contentarea_left > table.type_1.theme > tbody > tr > td.col_type1 > a")
                hrefs.extend(["https://finance.naver.com" + element.get_attribute('href') for element in elements])
            for href in tqdm(hrefs):
                page.goto(href)
                theme = page.query_selector(
                    '#contentarea_left > table > tbody > tr:nth-child(4) > td:nth-child(1) > div > div > strong').text_content()
                elements = page.query_selector_all('#contentarea > div > table > tbody > tr > td.name > div > a')
                stock_names = [stock_name.text_content() for stock_name in elements]
                result.extend([[theme, name] for name in stock_names])

        df = pd.DataFrame(result, columns=["theme", "stock_name"])

        # theme_name 및 company_name을 사용하여 df에 있는 theme 및 stock_name과 매핑
        merged_df = df.merge(themes_df, left_on='theme', right_on='theme_name')
        merged_df = merged_df[["id","stock_name"]]
        merged_df.rename(columns={"id": "theme_id"}, inplace=True)
        merged_df = merged_df.merge(stocks_df, left_on='stock_name', right_on='company_name')
        merged_df.rename(columns={"id": "stock_id"}, inplace=True)
        merged_df = merged_df[["stock_id","theme_id"]]
        merged_df = merged_df.drop_duplicates()
        return merged_df
    '''
    실시간 국내 주식 가격 크롤링
    '''
    def now_stock_price_crawler(self, date):
        df_kr_price = pd.DataFrame()
        try:
            kospi_df = stock.get_market_ohlcv(date, market="KOSPI")
            kosdaq_df = stock.get_market_ohlcv(date, market="KOSDAQ")
            df_kr_price = pd.concat([kospi_df, kosdaq_df])
            df_kr_price["날짜"] = date
            df_kr_price.reset_index(inplace=True)

            df_kr_price.rename(columns={
                '날짜': 'date',
                '시가': 'open',
                '고가': 'high',
                '저가': 'low',
                '종가': 'close',
                '거래량': 'volume',
                '등락률': 'change_rate',
                '티커': 'ticker'
            }, inplace=True)
            df_kr_price = df_kr_price[['date', 'ticker', 'open', 'high', 'low', 'close', 'volume', 'change_rate']]
        except Exception as e:
            print(f"Error fetching stock data for date {date}: {e}")
        return df_kr_price
# s = StockCrawler()
# print(s.kr_stock_info_crawler())