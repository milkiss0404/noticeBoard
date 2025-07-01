# 클린아키텍처
---
클린 아키텍처란 무엇인가, 그리고 왜 중요한가?


소프트웨어 아키텍처에서 중요한 요소 중 하나는 비용 절감입니다


클린아키텍처는 유지보수와 확장에 드는 비용을 줄여주는 구조입니다.


그러면 어떻게 비용을 줄이는게 좋을까?


선택지를 넓혀서, 변화에 유연하게 대응할 수 있게 하기 때문입니다.


예를 들어, 비즈니스 로직을 작성했을 때 이를 다양한 환경( ex: 웹, 콘솔 ,  다른 DB)에 쉽게 이식할수 있어야 한다


이러한 유연함은 곧 클린 아키텍쳐의 핵심입니다


---
## 고수준과 저수준 컴포넌트의 분리<br>
클린 아키텍처에서는 컴포넌트를 다음과 같이 분류할수있음


고수준 컴포넌트: 비즈니스로직을 담당( ex: 서비스 레어이, 도메인 레이어)


저수준 컴포넌트: 외부와 가까움 기술적인 부불을 담당(ex: 컨드롤러 ,DB레파지토리)


가장 중요한 원칙은 **고수준 컴포넌트는 저수준 컴포넌트에 의존해서는 안된다** 입니다


왜냐 ! 저수준컴포넌트는 변화가 잦음,


예를 들어 게시판서비스에서 "글을 작성한다" 라는 핵심비즈니스로직은 잘안바뀝니다.


데이터베이스는 사용자수나 트래픽 증가에따라 캐싱을 도입하거나 테이블 구조가 바뀌는 등 잦은 변화가 발생합니다.


![layeredArchitecture2](https://github.com/user-attachments/assets/33aef40a-1647-4c8c-869b-f1c81ee128a6)<br>
![layeredArchitecture](https://github.com/user-attachments/assets/baf5dc72-621f-4138-882b-a3325c6cdb21)<br>

이러한 구조는 SOLID원칙중 DIP 의존성 역전의 원칙을 반영한것입니다.


고수준 모듈이 저수준 모듈에 의존하는것이 아니라 공통의 추상화에 의존함으로써 변경에 강한 구조를 만듭니다.


```
데이터 접근만 수행
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        return jpaUserRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUserName(String userName) {
        return jpaUserRepository.findByUsername(userName);
    }

    @Override
    public List<UserEntity> findAll() {
        return jpaUserRepository.findAll();
    }
}
```
RepositoryImpl 부분은 데이터 접근이라는 하나의 책임만 수행하고 내부적으로는 JpaUserRepository에 위임하고 로직은 전혀 섞이지않는 SRP(책임 단일의 원칙) 을 준수하고있습니다<br>



# ERD
---
![erd](https://github.com/user-attachments/assets/f6ffcfef-7920-4003-bb5a-9ea0f7df9a3f)


# API Spec
---
![swagger](https://github.com/user-attachments/assets/c56d24c8-a9f3-4316-8b9b-a268d3464205)
![swagger1](https://github.com/user-attachments/assets/5e605ca7-aa09-462d-b419-99a12bd63978)
![swagger2](https://github.com/user-attachments/assets/e02f89bd-1272-46ba-9eef-b89eb762140e)




# 게시물 생성 API

### Endpoint
``` http request
POST http://localhost:8080/post/save
```
### Request
```
{
    "userId":"1",
    "title":"글 제목 입니다!!!",
    "postPasswd": "1234!!dd",
    "content": "글내용 입니다!!!"
}
```
Headers

key :Authorization 

value: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMjIyMiIsImlhdCI6MTc1MTI5Mzg2OCwiZXhwIjoxNzUxMjk1NjY4fQ.W_cbF4zNvpVRmtb_jMJ7UVv-7rJb1qcsi_FrdeDrdeQ
### Response
```
{
    "code": 0,
    "message": "ok",
    "value": {
        "id": 7,
        "content": "글내용 입니다!!!",
        "postStatus": "새글",
        "user": {
            "userId": 1,
            "username": "22222",
            "createDateTime": [
                2025,
                6,
                25,
                18,
                30,
                19,
                361788000
            ],
            "modifiedDateTime": [
                2025,
                6,
                25,
                18,
                30,
                19,
                361788000
            ]
        }
    }
}
```
# 게시물 조회 API

### Endpoint
``` http request
POST http://localhost:8080/post/save
```
### Request
headers 

key: Authorization

value : Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMjIyMiIsImlhdCI6MTc1MTI5Mzg2OCwiZXhwIjoxNzUxMjk1NjY4fQ.W_cbF4zNvpVRmtb_jMJ7UVv-7rJb1qcsi_FrdeDrdeQ

### Response
```
{
    "code": 0,
    "message": "ok",
    "value": {
        "postId": 7,
        "user": {
            "userId": 1,
            "username": "22222",
            "createDateTime": [
                2025,
                6,
                25,
                18,
                30,
                19,
                361788000
            ],
            "modifiedDateTime": [
                2025,
                6,
                25,
                18,
                30,
                19,
                361788000
            ]
        },
        "title": "글 제목 입니다!!!",
        "content": "글내용 입니다!!!",
        "postStatus": "새글",
        "createDateTime": [
            2025,
            6,
            30,
            23,
            50,
            44,
            449930000
        ],
        "modifiedDateTime": [
            2025,
            6,
            30,
            23,
            50,
            44,
            449930000
        ],
        "comments": []
    }
}
```

# 댓글생성
### Endpoint
``` http request
POST http://localhost:8080/comment/save
```
### request
```
{
    "userId":"1",
    "postId":"7",
    "content":"댓글7번"
}
```


headers

key :Authorization

value: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMjIyMiIsImlhdCI6MTc1MTI5Mzg2OCwiZXhwIjoxNzUxMjk1NjY4fQ.W_cbF4zNvpVRmtb_jMJ7UVv-7rJb1qcsi_FrdeDrdeQ


### response
```
{
    "code": 0,
    "message": "ok",
    "value": {
        "user": {
            "userId": 1,
            "username": "22222",
            "createDateTime": [
                2025,
                6,
                25,
                18,
                30,
                19,
                361788000
            ],
            "modifiedDateTime": [
                2025,
                6,
                25,
                18,
                30,
                19,
                361788000
            ]
        },
        "commentId": 3,
        "postId": 7,
        "commentContent": "댓글7번",
        "createDateTime": [
            2025,
            6,
            30,
            23,
            54,
            1,
            120926600
        ],
        "modifiedDateTime": [
            2025,
            6,
            30,
            23,
            54,
            1,
            120926600
        ]
    }
}
```

