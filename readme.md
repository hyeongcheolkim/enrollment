# 수강신청 서비스 

## 🔍 목표

- Spring MVC를 이용하여 수강신청 서비스를 구현한다
- Spring WebFlux를 이용하여 비동기 처리를 구현한다(차후예정)

## 📮 요구사항
- 교수
  - 로그인 할 수 있다
  - 과목을 개설할 수 있다
    - 복수전공, 타전공, 부전공 여부에 따라 수강 가능 여부 설정 가능
    - 수강 가능 과 설정 가능 
  - 학생에게 성적을 부여할 수 있다
    - 성적에 부과되면, 수강중인 과목이 수강완료 처리된다

- 학생
  - 로그인 할 수 있다
  - 수강신청을 할 수 있다
  - 장바구니 기능을 이용할 수 있다 
    - 장바구니에 담긴 모든 과목을 일괄적으로 수강신청 할 수 있다
  - 수강완료된 과목들의 리스트와, 총 평점등을 조회할 수 있다
     
