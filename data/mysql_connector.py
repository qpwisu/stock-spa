import pandas as pd
from sqlalchemy import create_engine, MetaData, Table, insert
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.sql import text

class MySQLDataConnector:
    def __init__(self, user, password, host, database, port=3306):
        """ MySQL 데이터베이스 엔진을 초기화합니다. """
        self.engine = create_engine(f'mysql+pymysql://{user}:{password}@{host}:{port}/{database}')

    def truncate_table(self,table_name):
        try:
            with self.engine.connect() as conn:
                conn.execute(text(f"TRUNCATE TABLE {table_name};"))
        except Exception as e:
            print(f"Error truncating table: {e}")


    def upload_dataframe(self, df, table_name, if_exists='append', index=False):
        """ DataFrame 데이터를 MySQL 테이블에 업로드합니다. """
        try:
            df.to_sql(name=table_name, con=self.engine, if_exists=if_exists, index=index)
            print(f"Data uploaded successfully to {table_name}. shape:"+str(df.shape))
        except Exception as e:
            print(f"Error uploading data: {e}")

    # 데이터베이스에 데이터 업로드
    def update_politician_dataframe(self, df, table_name):
        for _, row in df.iterrows():
            name = row['name']
            party = row['party']
            # 쿼리 작성
            sql = text("""
                INSERT INTO {} (name, party)
                VALUES (:name, :party)
                ON DUPLICATE KEY UPDATE party=VALUES(party);
            """.format(table_name))  # table_name은 직접 포매팅

            # 쿼리 실행
            with self.engine.connect() as conn:
                conn.execute(sql, {"name": name, "party": party})  # 딕셔너리로 파라미터 전달
                conn.commit()  # 커밋 추가

        print(f"Data uploaded successfully to {table_name}.")

    def select_columns(self,table_name,column_names = "*"):
        if column_names != "*":
            column_names = ",".join(column_names)

        query = f"SELECT {column_names} FROM {table_name}"
        try:
            df = pd.read_sql_query(query, self.engine)
            return df
        except Exception as e:
            print(f"Error downloading data: {e}")
            return None

    def default_query(self,query):
        try:
            df = pd.read_sql_query(query, self.engine)
            return df
        except Exception as e:
            print(f"Error downloading data: {e}")
            return None
    def insert_default_query(self,query):
        try:
            with self.engine.connect() as connection:
                with connection.begin():  # 트랜잭션 시작
                    connection.execute(text(query))
        except Exception as e:
            print(f"Error executing query: {e}")
    def search_last_date(self,table_name):
        query = f"SELECT max(date) FROM {table_name};"
        try:
            df = pd.read_sql_query(query, self.engine)
            return pd.to_datetime(df.values[0][0])
        except Exception as e:
            print(f"Error downloading data: {e}")
            return None


    def insert_ignore_dataframe(self, df, table_name):
        """ 이미 값이 있으면 무시 DataFrame 데이터를 MySQL 테이블에 INSERT IGNORE를 사용하여 업로드합니다. """
        for _, row in df.iterrows():
            columns = ', '.join(df.columns)
            placeholders = ', '.join([f":{col}" for col in df.columns])
            sql = text(f"""
                INSERT IGNORE INTO {table_name} ({columns})
                VALUES ({placeholders});
            """)
            with self.engine.connect() as conn:
                conn.execute(sql, row.to_dict())
                conn.commit()  # 커밋 추가
        print(f"Data uploaded with INSERT IGNORE to {table_name}.")

    def update_or_insert_dataframe(self, df, table_name):
        """ 이미 값이 있으면 수정 DataFrame 데이터를 MySQL 테이블에 ON DUPLICATE KEY UPDATE를 사용하여 업로드합니다. """
        for _, row in df.iterrows():
            columns = ', '.join(df.columns)
            update_clause = ', '.join([f"{col}=VALUES({col})" for col in df.columns])
            placeholders = ', '.join([f":{col}" for col in df.columns])
            sql = text(f"""
                INSERT INTO {table_name} ({columns})
                VALUES ({placeholders})
                ON DUPLICATE KEY UPDATE {update_clause};
            """)
            with self.engine.connect() as conn:
                conn.execute(sql, row.to_dict())
                conn.commit()  # 커밋 추가
        print(f"Data uploaded with ON DUPLICATE KEY UPDATE to {table_name}.")

    def insert_new_data(self, new_df, table_name):
        """ 데이터베이스 테이블에서 기존 데이터를 읽고, 새로운 DataFrame의 중복되지 않은 데이터를 삽입합니다. """
        # 데이터베이스에서 기존 데이터를 읽어 DataFrame으로 변환
        existing_df = pd.read_sql_table(table_name, self.engine)

        # 기본키 'id'를 제외
        if 'id' in existing_df.columns:
            existing_df = existing_df.drop(columns=['id'])

        # 중복되지 않는 데이터만 필터링
        combined_df = pd.concat([existing_df, new_df]).drop_duplicates(keep=False)
        # 중복되지 않은 데이터 삽입
        for _, row in combined_df.iterrows():
            columns = ', '.join(combined_df.columns)
            placeholders = ', '.join([f":{col}" for col in combined_df.columns])
            sql = text(f"""
                INSERT INTO {table_name} ({columns})
                VALUES ({placeholders});
            """)
            with self.engine.connect() as conn:
                conn.execute(sql, row.to_dict())
                conn.commit()
        print(f"Non-duplicate data successfully added to {table_name}.")

    def close(self):
        """ 데이터베이스 연결을 종료합니다. """
        self.engine.dispose()


