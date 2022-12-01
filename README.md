# awsCondor
Assignment of Cloud Computing Class

CBNU / School of Computer Science / 강형빈

설정은 글로 남기고 코드만 Github로 관리

11/28(월)-----------------------------------

개발 환경 구축

- Windows10
  - VirtualBox (Ubuntu)
    - Eclipse
    - JAVA JDK
    - AWS SDK

문제점 : region 설정을 필요로 함

11/29(화)-----------------------------------

- region 설정 문제 해결
  - aws cli - configure를 통해 csv키 등록

- IAM 권한 문제 발생
  - EC2 권한만 줬었는데, Elastic 권한 부여
 
 11/30(수)-----------------------------------

- Master, Slave 인스턴스 생성 및 설정
  - Slave를 AMI 이미지화
 
- 네트워크 문제 발생
  - 보안 그룹 설정을 통해 인,아웃바운드 규칙 설정
  - 새로 생성될 instance에도 설정을 위해 JAVA 코드 수정
  
   12/1(목)-----------------------------------

- SSM 권한 추가를 통하여 인스턴스에서 쉘 스크립트 실행 가능
 
- 추가 기능을 Ver.2 로 UI를 분리하여 구현
  - Condor_status 구현 성공
 

