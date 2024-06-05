'''
블로그글의 title + header에 포합되있는 주식 종목 추출
blog_id, 종목명
'''
from tqdm import tqdm
import pandas as pd
class BlogAnalyzer:
    def word_extractor(self, blog_df, stocks_df, w="company_name"):
        blog_df["content"] = blog_df["title"].str.cat(blog_df["header"], na_rep='', sep=' ')
        blog_df["content"] = blog_df["content"].fillna('')
        if "blog_id" in blog_df.columns:
            blog_df.rename(columns={"blog_id": "blog_id"}, inplace=True)

        # company_name_li에 있는 회사 이름이 content에 포함되어 있는지 확인
        # 각 회사 이름에 대해 content 컬럼에서 검색하고, 결과를 저장
        result = pd.DataFrame(columns=['blog_id', w])
        li = stocks_df["company_name"].tolist()
        for c in tqdm(li):
            mask = blog_df['content'].str.contains(c, regex=False)
            filtered_df = blog_df.loc[mask, ['blog_id']]
            filtered_df[w] = c
            result = pd.concat([result, filtered_df])

        result = result.merge(stocks_df, on="company_name")
        result.drop("ticker",inplace=True,axis=1)
        return result.reset_index(drop=True)

