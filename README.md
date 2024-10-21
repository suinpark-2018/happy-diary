# [Project - Spring] Happy Diary

</br>

## 🔎 프로젝트 소개
#### 스스로에게, 혹은 타인에게 매일 칭찬을 제공하도록 격려하기 위한 웹사이트입니다. </br>
성인이 되고, 점점 나이가 들어갈 수록 칭찬을 받을 일이나 칭찬을 해줄 사람이 줄어드는 것을 느껴보셨을 것입니다. </br> 
매일 스스로에게, 또는 다른 누군가에게 칭찬 한마디를 남겨보면 어떨까요? </br>
'Happy Diary'를 통해 본인이나 타인에게 받은 칭찬을 통해 행복한 하루를 보냈으면 하는 바람으로 기획하게 되었습니다.

</br>

## 🏃‍♂️ 프로젝트 기간
- 전체 개발기간: 2024.08.27 - 현재 (진행 중)
- 1차 개발: 2024.08.27 - 2024.10.21 (배포)

<br>
<br>

아래의 링크를 통해 웹사이트를 직접 확인하실 수 있습니다.
<br>
[Happy Diary Website](43.200.214.31:8080)

</br>

## 📁 ERD
### 3 Tables
#### 📌 상세 구조
<img width="1290" alt="ERD" src="https://github.com/user-attachments/assets/36433aeb-b66e-4345-b909-40f13e230b37">

#### 회원 테이블
<img width="500" alt="user" src="https://github.com/user-attachments/assets/eeffb2d8-c4c5-42c9-bb8a-cc6bd71898cd">

#### 게시판 테이블
<img width="500" alt="board" src="https://github.com/user-attachments/assets/e1fa16b1-b6e8-4610-baa4-c0869f1e6230">

#### 댓글 테이블
<img width="500" alt="comment" src="https://github.com/user-attachments/assets/8b8447e9-1aac-4680-a2c8-2a58d795d96f">

#### 회원정보 수정 테이블
<img width="500" alt="user_info_chg_hist" src="https://github.com/user-attachments/assets/3f7090b2-b924-4ebb-916f-ea6a0e9ca962">

#### 변경이력 코드 테이블
<img width="500" alt="chg_hist_code" src="https://github.com/user-attachments/assets/3fbc4bf1-b0f7-4b11-b658-038b8d7323da">

</br></br>

## ⚙ 기술 스택  
<div align=center>
  <img src="https://img.shields.io/badge/java-FF7800?style=for-the-badge&logo=OpenJDK&logoColor=white">
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white">
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <br>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/mybatis-EF2D5E?style=for-the-badge&logo=mybatis&logoColor=white">
  <img src="https://img.shields.io/badge/apachetomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
</div> 

</br></br>

## 🔮 주요 기능
* 회원가입
  - 이메일 본인인증
  - 아이디 중복 확인
* 로그인/로그아웃
  - SNS 간편 로그인
  - 아이디/비밀번호 찾기
* 게시판
  - 게시물 조회/작성/수정/삭제
  - 게시물 검색
  - 게시물 공개 범위 설정
  - 게시물 조회수
  - 페이징 처리
* 관리자
  - 회원 관리
  - 게시판 관리
  - 공지사항 관리

</br>
 
## 🏗 Project Structure
```
happy_diary
  ├── README.md
  ├── pom.xml
  └── src
      ├── main
      │   ├── java
      │   │   └── com
      │   │       └── happydiary
      │   │           ├── common
      │   │           │   ├── exception
      │   │           │   │   └── GlobalExceptionHandler.java
      │   │           │   └── validation
      │   │           │       ├── ValidationGroups.java
      │   │           │       └── ValidationSequence.java
      │   │           ├── controller
      │   │           │   ├── AccountDeletionController.java
      │   │           │   ├── BoardController.java
      │   │           │   ├── CommentController.java
      │   │           │   ├── FindIdPwdController.java
      │   │           │   ├── HomeController.java
      │   │           │   ├── LogController.java
      │   │           │   ├── LoginController.java
      │   │           │   ├── SignUpController.java
      │   │           │   └── UserInfoController.java
      │   │           ├── dao
      │   │           │   ├── BoardDao.java
      │   │           │   ├── BoardDaoImpl.java
      │   │           │   ├── CommentDao.java
      │   │           │   ├── CommentDaoImpl.java
      │   │           │   ├── UserDao.java
      │   │           │   └── UserDaoImpl.java
      │   │           ├── dto
      │   │           │   ├── BoardDto.java
      │   │           │   ├── CommentDto.java
      │   │           │   ├── PageRequestDto.java
      │   │           │   ├── PageResponseDto.java
      │   │           │   └── UserDto.java
      │   │           └── service
      │   │               ├── BoardService.java
      │   │               ├── BoardServiceImpl.java
      │   │               ├── CommentService.java
      │   │               ├── CommentServiceImpl.java
      │   │               ├── LogService.java      
      │   │               ├── UserService.java
      │   │               ├── UserServiceImpl.java
      │   │               └── mail
      │   │                   ├── MailHandler.java
      │   │                   ├── MailService.java
      │   │                   ├── MockMailSender.java
      │   │                   └── TempKey.java
      │   ├── resources
      │   │   ├── mapper
      │   │   │   ├── BoardMapper.xml
      │   │   │   ├── CommentMapper.xml
      │   │   │   └── UserMapper.xml
      │   │   ├── application.properties
      │   │   ├── logback-spring.xml
      │   │   └── mybatis-config.xml
      │   └── webapp
      │       └── WEB-INF
      │           ├── resources
      │           │   ├── css
      │           │   │   ├── board.css
      │           │   │   ├── boardDetail.css
      │           │   │   ├── createBoard.css
      │           │   │   ├── findIdPwd.css
      │           │   │   ├── foundBoard.css
      │           │   │   ├── identityVerification.css
      │           │   │   ├── index.css
      │           │   │   ├── login.css
      │           │   │   ├── main.css
      │           │   │   ├── modifyBoard.css
      │           │   │   ├── modifyPwd.css
      │           │   │   ├── modifyUserInfo.css
      │           │   │   ├── nav.css
      │           │   │   ├── notice.css
      │           │   │   └── register.css
      │           │   ├── img
      │           │   │   ├── kakao.png
      │           │   │   ├── kakao_login_logo.png
      │           │   │   ├── main.webp
      │           │   │   ├── naver.png
      │           │   │   ├── naver_login_logo.png
      │           │   │   └── notice.png
      │           │   └── js
      │           │       ├── boardDetail.js
      │           │       ├── findIdPwd.js
      │           │       ├── identityVerification.js
      │           │       ├── login.js
      │           │       ├── main.js
      │           │       ├── modifyUserInfo.js
      │           │       ├── nav.js
      │           │       └── register.js
      │           ├── spring
      │           │   ├── appServlet
      │           │   │   └── servlet-context.xml
      │           │   └── root-context.xml
      │           ├── views
      │           │   ├── include
      │           │   │   └── nav.jsp
      │           │   ├── board.jsp
      │           │   ├── boardDetail.jsp
      │           │   ├── createBoard.jsp
      │           │   ├── findIdPwd.jsp
      │           │   ├── foundBoards.jsp
      │           │   ├── identityVerification.jsp
      │           │   ├── index.jsp
      │           │   ├── login.jsp
      │           │   ├── main.jsp
      │           │   ├── modifyBoard.jsp
      │           │   ├── modifyPwd.jsp
      │           │   ├── modifyUserInfo.jsp    
      │           │   ├── notice.jsp    
      │           │   └── register.jsp
      │           └── web.xml
      └── test
          └── java
              └── com
                  └── happydiary
                      ├── dao
                      │   ├── BoardDaoImplTest.java
                      │   ├── CommentImplTest.java
                      │   └── UserDaoTest.java
                      └── service
                          ├── BoardServiceImplTest.java
                          ├── CommentServiceImplTest.java
                          └── UserServiceImplTest.java
```

</br>

## 📄 Commit Message Convention

#### 규칙
```
- 제목 첫글자는 대문자 사용
- 제목 마침표 미포함
- 제목과 본문은 빈 행으로 구분
- 제목 명령문으로 작성
- 제목 50자 이내로 간결하게 작성
```

#### 유형
```
Feat        :     새로운 기능 추가
Test        :     테스트 코드 완료
Docs        :     문서 추가 또는 수정 (ex. README 변경)
Chore       :     패키지 매니저(ex. gitignore 수정), 빌드 업무 수정 
Refactor    :     리팩토링, 코드 개선
Fix         :     버그 수정
Style       :     코드 스타일 변경 (포맷팅, 공백 제거, 세미콜론 누락 등 코드 변경이 없는 경우)
Comment     :     주석 추가 및 수정
Rename      :     파일 또는 폴더명을 수정하거나 이동하는 작업만 수행한 경우
Remove      :     파일을 삭제하는 작업만 수행한 경우
