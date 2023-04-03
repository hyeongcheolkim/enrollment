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
    - 재수강의 경우, 기존 성적은 사라지고 새 점수로 대체된다
      - 재수강시, 과거 수강 내역은 DB에서 완전 삭제된다 

- 학생
  - 로그인 할 수 있다
  - 수강신청을 할 수 있다
  - 장바구니 기능을 이용할 수 있다 
    - 장바구니에 담긴 모든 과목을 일괄적으로 수강신청 할 수 있다
  - 수강완료된 과목들의 리스트와, 총 평점등을 조회할 수 있다
  - B0미만일경우 재수강을 신청할 수 있다. 이때, 동일한 과목 코드를 가지고 있어야한다 

## 📃작성할 테스트 목록
- 기능 
  - 교수는 기존 코스와 시간이 겹치는 코스를 만들 수 없다
  - 학생은 기존 수강신청한 코스의 시간과 겹치는 코스를 수강신청할 수 없다
  - 교수가 설정한 수강금지 조건에 걸리는 학생은 수강신청 할 수 없다
  - 교수가 학생에게 성적을 부여하면 성적이 기록되고 수강완료처리된다
  - 학생은 과목코드가 동일한 과목은 수강신청할 수 없다
    - 단, 성적이 B0미만이라면 신청할 수 있다
  - 교수가 재수강 과목에 성적을 부여하면, 동일 과거 수강내역은 완전히 삭제된다 
- 코드 
  - Enrollment Entity Builder로 객체 생성할때, 연관된 List에 Enrollment추가해주는지? 
