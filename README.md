## mysql 디비 초기 설정 
mysql -u root -p stockdb < stockdb_backup.sql

## 파이썬 크롤러
cd data

python3.9 -m venv myenv

source myenv/bin/activate

pip install requirements.txt


