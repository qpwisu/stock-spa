from playwright.sync_api import sync_playwright
import pandas as pd
from datetime import datetime
from playwright.async_api import async_playwright
import asyncio
from datetime import timedelta, datetime
import time
import tqdm
from tqdm.asyncio import tqdm

class BlogCrawler:
    async def crawler(self, semaphore, context,  name, date):
        async with semaphore:
            try:
                page = await context.new_page()
                url = f'https://search.naver.com/search.naver?ssc=tab.blog.all&query={name} 관련주&sm=tab_opt&nso=so%3Ar%2Cp%3Afrom{date}to{date}'
                await page.goto(url, timeout=10000) # 너무 빠른 페이지 이동시 검색 제한 됨, 스크롤로 블로그 글들을 가져오는건 막히지 않는거 같음
                while True:
                    element = await page.query_selector('h3.title')
                    if element:
                        text = await element.text_content()
                        if "검색 서비스 이용이 제한되었습니다." in text:
                            # print("제한 메시지가 확인되었습니다."+name+date)
                            await page.goto(url, timeout=2000) # 검색 제한시 다시 검색
                    else:
                        break



                last_height = await page.evaluate('document.body.scrollHeight')
                last_time = time.time()

                try:
                    while True:
                        # 스크롤 다운
                        await page.evaluate('window.scrollTo(0, document.body.scrollHeight)')
                        # 현재 시간 가져오기
                        current_time = time.time()

                        # 마지막 높이 체크 이후 2초가 지났는지 확인
                        if current_time - last_time > 2:
                            # 새로운 스크롤 높이를 계산
                            new_height = await page.evaluate('document.body.scrollHeight')
                            if new_height == last_height:
                                # 높이 변화가 없으면 종료
                                break
                            else:
                                # 높이 변화가 있으면 업데이트
                                last_height = new_height
                                last_time = time.time()  # 마지막 체크 시간 업데이트
                        # time.sleep(0.1)  # 너무 빠른 스크롤 방지를 위해 짧은 대기 시간 추가
                except Exception as e:
                    print(e)

                title_elements = await page.query_selector_all('div.title_area > a')
                title_list = [await title.text_content() for title in title_elements]
                href_list = [await title.get_attribute('href') for title in title_elements]

                header_elements = await page.query_selector_all('div.dsc_area > a.dsc_link')
                header_list = [await header.text_content() for header in header_elements]

                if len(title_list) != len(header_list):
                    header_list = [None] * len(title_list)  # 헤더가 없는 경우 None으로 채움

                df = pd.DataFrame({
                    'name': [name] * len(title_list),
                    'title': title_list,
                    'header': header_list,
                    'href': href_list,
                    'date': [date] * len(title_list)
                })
                return df
            except Exception as e:
                # print(e)
                # print(name,date,"error")
                return

            finally:
                try:
                    await context.clear_cookies()
                    await page.evaluate("localStorage.clear()")
                    await page.evaluate("sessionStorage.clear()")
                    await page.close()
                except Exception as e:
                    print(f"자원 정리 중 오류 발생: {e}")  # 자원 정리 중 발생한 예외를 캐치하여 로깅
    async def blog_crawler(self, name_list, start_date, end_date):
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        elif isinstance(start_date, str):
            start_date = datetime.strptime(start_date, '%Y%m%d').date()

        if isinstance(end_date, datetime):
            end_date = end_date.date()
        elif isinstance(end_date, str):
            end_date = datetime.strptime(end_date, '%Y%m%d').date()
        date_range = [start_date + timedelta(days=x) for x in range((end_date - start_date).days + 1)]
        print(date_range)

        if not date_range:
            return False

        async with async_playwright() as p:
            # chrome gpu 가속에 메모리 사용량이 너무 커서 끔
            browser = await p.chromium.launch(headless=True, args=['--disable-gpu'])
            context = await browser.new_context(
                user_agent='Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36')
            df_list = []

            semaphore = asyncio.Semaphore(8) #4
            futures =[]
            for name in name_list:
                for date in date_range:
                    futures.append(self.crawler(semaphore, context, name, date.strftime('%Y%m%d')))

            for future in tqdm(asyncio.as_completed(futures), total=len(futures), disable=True):
                tmp_df = await future
                if tmp_df is not None:
                    df_list.append(tmp_df)

            df = pd.concat(df_list, ignore_index=True)
            await browser.close()
            return df
