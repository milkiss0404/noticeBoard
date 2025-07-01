# 1. 왜 블랙리스트 로그아웃이 필요한가?
JWT는 Stateless 구조이기 때문에 한 번 발급된 토큰은 만료되기 전까지 서버가 강제로 폐기할 수 없다.


따라서 로그아웃 시 다음과 같은 문제가 발생 할 수 있다


단순히 프론트에서 로컬스토리지/쿠키를 지우는 것은 보안상 완전한 로그아웃이 아님.


악의적인 사용자가 토큰을 복사해 두었다면 재사용 가능.


이를 막기 위해선 서버에서 강제로 토큰을 무효화시킬 방법이 필요함 → 레디스 TTL을 이용한 블랙리스트 방식 사용.


# 2. 왜 Redis를 RefreshToken 저장소로 사용했나?
Redis는 인메모리 기반 저장소이기 때문에 I/O 부하가 적고 조회 속도가 빠름.


블랙리스트 조회는 매 요청마다 Filter에서 수행되므로, 성능이 중요.


일반 RDBMS에 저장하면 I/O 병목 발생 가능성 있음.

## StringRedisTemplate 을이용한 로그인시 RefreshToken 키값save
```
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Response<String> login(@RequestBody RequestUserDto request, HttpServletResponse response) throws LoginException {

        UserEntity userEntity = userService.login(request);


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userName(), request.passwd())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        String accessToken = jwtTokenProvider.createAccessToken(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        String key = "refreshToken:userId %s".formatted(userEntity.getUsername());
        redisService.saveData(key, refreshToken);
```

## 로그아웃시 블랙리스트처리(redis TTL을 이용)

```
@RequiredArgsConstructor
@Component
public class LogoutHandlerImpl implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final RedisService redisService;

    @SneakyThrows // 체크 예외(checked exception)를 명시적으로 선언하거나 try-catch 없이 던질 수 있게 해줌
    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtTokenProvider.resolveToken(request);
        String userId = jwtTokenProvider.getUserIdAndIsValid(token);
        if (jwtTokenProvider.isBlackList(token)) {
            response.setStatus(400);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("토큰이 유효하지 않습니다");
        }

        String blackListKey = "blackList:%s".formatted(userId);
        redisService.setBlackList(blackListKey, token, 30L);

        String key = "refreshToken:userId %s".formatted(userId);
        redisService.deleteData(key);
//            jpaRefreshTokenRepository.deleteByUserId((userId));
    }
}
```


```
    public void setBlackList(String key, String value, Long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }
```


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


# 스프링시큐리티 jwt필터로 작성자 회원 인증하는법
---

RequestDTO로 userId를 받았던 방식에서 리팩터링을 하였습니다 jwtAhthenticationFilter를 UsernamePasswordAuthenticationFilter앞에 붙여주면서


access 토큰 검증이후에 SecurityContextHolder에 userId를 넣어준다 서비스레이어에서는 SecurityContextHolder.getContext().getAuthentication().getName()에서 인증된사용자 이름과


PostEntity 혹은 CommentEntity 내에 UserEntity의 userName과 비교해 작성자 확인을 한다

```
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```


```
 public String getUserIdAndIsValid(String token) throws CustomBadRequestException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new CustomBadRequestException("토큰이 유효하지 않습니다.");
        }
    }
```

```

 @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getToken(request);
            jwtTokenProvider.isBlackList(token);

            String userId = jwtTokenProvider.getUserIdAndIsValid(getToken(request));
            var userDetails = userDetailService.loadUserByUsername(userId);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (CustomBadRequestException e) {
            sendErrorResponse(response, "토큰이 유효하지 않습니다");
            return;
        } catch (Exception e) {
            sendErrorResponse(response, "회원을 찾을 수 없습니다");
            return;
        }
        filterChain.doFilter(request,response);
    }


```

PostEntity 일경우 예시

```
   private PostEntity getPostEntity(Long postId) {
        PostEntity post = findPost(postId);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String postUsername = post.getUser().getUsername();
        if (!userName.equals(postUsername)) {
            throw new CustomBadRequestException("작성자만 삭제/수정할 수 있습니다");
        }
        return post;
    }
```



# 예외처리
---
CustomBadRequestException를 만들어서 일괄 처리하였습니다.


```
@ControllerAdvice
public class CustomExceptionHandler {

        @ResponseBody
        @ExceptionHandler(CustomBadRequestException.class)
        public ResponseEntity<String> handleCustomBadRequestException(CustomBadRequestException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
}
```


```
public class CustomBadRequestException extends RuntimeException{
    public CustomBadRequestException(String message) {
        super(message);
    }
}

```


```
 throw new CustomBadRequestException("토큰이 유효하지 않습니다.");

```




