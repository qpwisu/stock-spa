# 주식 정보 제공, 네이버 블로그 언급량 분석 웹 
- 배포 URL : http://hystock.site:8081/

## 포로젝트 소개 
- 국내 주식 투자시 필요한 정보를 제공하기 위한 WEB 입니다 
- 기본적인 주가, 기업정보 같은 주식 정보를 제공합니다
- 주식, 테마별 네이버 블로그 언급량 추이를 제공하고 분석합니다 
- 게시판을 통해 게시글, 댓글을 작성하고 좋아요를 누를 수 있습니다. 
- 주식, 테마를 즐겨찾기 할 수 있습니다. 

## 1. 개발 환경 
- Back-end + Front : Spring + thymeleaf (Single Page Application)
- Crawler : python(playwright)
    - selenium 아닌 playwright 사용
- database : Mysql 
- 코드 관리 : github
- 서비스 배포 환경 : 개인 노트북 서버(우분투)

## 2. 기능 
- ### 기본기능 
    - 회원가입, 로그인 (JWT,spring scurity)
    - 게시판, 댓글, 좋아요 CRUD
- ### 국내 주식 정보 
    - 기간별 주가, 실시간 주가, 테마, 시총, 기업개요, 지표(per,eps)
    - 주식별 네이버 블로그 언급량 추이
    - 관련 종목 
    - 주식 관련 네이버 블로그 글 
    - 실시간 급상승 종목 
    - 전날 대비 최대 언급 증가 종목

- ### 주식 테마 정보 
    - 테마에 포함된 종목 
    - 테마별 평균 상승률 
    - 테마별 네이버 블로그 언급량 추이
    - 테마 관련 네이버 블로그 글 
    - 실시간 급상승 테마
    - 전날 대비 최대 언급 증가 테마

## 3. 주요 페이지 

- 홈 
    ![home](https://github.com/qpwisu/stock-spa/assets/28581494/71875e50-853e-4e4e-bda0-62b04d14ffc7)

- 주식 
    ![stock](https://github.com/qpwisu/stock-spa/assets/28581494/f192610a-b49a-4833-b9a9-f496a2c50714)
- 테마
    ![theme](https://github.com/qpwisu/stock-spa/assets/28581494/2b145ee3-125e-459f-b9e2-d173430cd28e)



## mysql 디비 초기 설정 
1. mysql 설치 후 create database stockdb;
2. mysql -u root -p stockdb < stockdb_backup.sql  // 따로 다운로드

## 파이썬 크롤러 초기 설정 
- git clone https://github.com/qpwisu/stock-spa.git
1. cd data
2. python3.9 -m venv myenv
3. source myenv/bin/activate
4. pip install requirements.txt
5. playwright install 
6. python update.py 

## spring 초기 설정 
1. cd server 
2. ./gradlew build
3. ./gradlew bootRun


