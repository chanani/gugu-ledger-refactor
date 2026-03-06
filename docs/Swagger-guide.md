# Swagger (SpringDoc OpenAPI) 문서 작성 가이드

---

## 목차
1. [기본 구조](#1-기본-구조)
2. [@Operation](#2-operation)
3. [@Parameter](#3-parameter)
4. [requestBody](#4-requestbody)
5. [responses](#5-responses)
6. [제네릭 반환 타입](#6-제네릭-반환-타입)
7. [@AuthUser 숨기기](#7-authuser-숨기기)
8. [인터페이스 분리 패턴](#8-인터페이스-분리-패턴)
9. [전체 예시](#9-전체-예시)

---

## 1. 기본 구조

Swagger 문서는 **컨트롤러 인터페이스**에 작성하고, **컨트롤러 구현체**에는 Spring 어노테이션만 유지합니다.

```
Controller Interface  ← @Operation, @Parameter, @RequestBody, @ApiResponse
Controller Class      ← @RestController, @GetMapping, @PathVariable 등
```

---

## 2. @Operation

API의 기본 정보를 정의합니다.

```java
@Operation(
    summary = "자산그룹 수정",           // 짧은 제목
    description = "자산 그룹을 수정합니다." // 상세 설명
)
```

| 속성 | 설명 |
|------|------|
| `summary` | API 목록에 표시되는 짧은 제목 |
| `description` | 상세 설명 |
| `parameters` | Path/Query 파라미터 목록 |
| `requestBody` | 요청 바디 |
| `responses` | 응답 목록 |

---

## 3. @Parameter

### 파라미터 옆에 직접 선언

```java
ResponseEntity<ApiResponse> updateAssets(
    @Parameter(description = "자산그룹 ID", required = true, example = "1") Long assetsId,
    AssetsUpdateRequest request
);
```

### @Operation의 parameters 속성에 모아서 선언

```java
@Operation(
    summary = "자산그룹 수정",
    parameters = {
        @Parameter(
            name = "assetsId",       // 실제 파라미터명과 반드시 일치
            description = "자산그룹 ID",
            required = true,
            example = "1"
        )
    }
)
ResponseEntity<ApiResponse> updateAssets(
    Long assetsId,
    AssetsUpdateRequest request
);
```

> ⚠️ `parameters` 속성 방식은 `name` 값이 실제 파라미터명과 **반드시 일치**해야 합니다.

| 속성 | 설명 |
|------|------|
| `name` | 파라미터명 (parameters 속성 사용 시 필수) |
| `description` | 파라미터 설명 |
| `required` | 필수 여부 (기본값: false) |
| `example` | 예시 값 |
| `hidden` | Swagger UI에서 숨김 여부 |

---

## 4. requestBody

요청 바디 스키마를 정의합니다.

```java
@Operation(
    summary = "자산그룹 수정",
    requestBody = @RequestBody(
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AssetsUpdateRequest.class)
        )
    )
)
```

---

## 5. responses

응답 코드별 스키마를 정의합니다.

```java
@Operation(
    summary = "자산그룹 수정",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "자산그룹 수정 성공",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    }
)
```

---

## 6. 제네릭 반환 타입

`DataResponse<AssetsPageResponse>` 처럼 제네릭 타입은 `@Schema`에 직접 선언할 수 없습니다.
`implementation` + `subTypes`를 조합해서 선언합니다.

```java
responses = {
    @ApiResponse(
        responseCode = "200",
        description = "자산그룹 목록 조회 성공",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                implementation = DataResponse.class,       // 래퍼 클래스
                subTypes = { AssetsPageResponse.class }    // 내부 실제 타입
            )
        )
    )
}
```

---

## 7. @AuthUser 숨기기

`@AuthUser` 어노테이션에 `@Parameter(hidden = true)`를 포함시키면
컨트롤러마다 별도 선언 없이 자동으로 Swagger에서 숨겨집니다.

```java
// AuthUser.java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)   // ← 여기에 추가
public @interface AuthUser {}
```

```java
// 컨트롤러 인터페이스 - 별도 선언 불필요
ResponseEntity<DataResponse<AssetsPageResponse>> getAssets(User user);
```

---

## 8. 인터페이스 분리 패턴

Swagger 문서를 인터페이스에 분리하면 컨트롤러 코드가 깔끔해집니다.

```java
// AssetsControllerDocs.java (인터페이스)
public interface AssetsControllerDocs {

    @Operation(
        summary = "자산그룹 수정",
        description = "자산 그룹을 수정합니다.",
        parameters = {
            @Parameter(name = "assetsId", description = "자산그룹 ID", required = true, example = "1")
        },
        requestBody = @RequestBody(
            content = @Content(schema = @Schema(implementation = AssetsUpdateRequest.class))
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "자산그룹 수정 성공",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
        }
    )
    ResponseEntity<ApiResponse> updateAssets(Long assetsId, AssetsUpdateRequest request);
}
```

```java
// AssetsController.java (구현체)
@RestController
@RequiredArgsConstructor
public class AssetsController implements AssetsControllerDocs {

    private final AssetsService assetsService;

    @PutMapping("/api/v1/user/assets/{assetsId}")
    public ResponseEntity<ApiResponse> updateAssets(
            @PathVariable(name = "assetsId") Long assetsId,
            @Valid @RequestBody AssetsUpdateRequest request
    ) {
        assetsService.updateAssets(request, assetsId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
```

---

## 9. 전체 예시

### 자산그룹 API 전체 문서

```java
public interface AssetsControllerDocs {

    // ── 목록 조회 ──────────────────────────────────────────
    @Operation(
        summary = "자산그룹 목록 조회",
        description = "자산 그룹 목록을 조회합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "자산그룹 목록 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = DataResponse.class,
                        subTypes = { AssetsPageResponse.class }
                    )
                )
            )
        }
    )
    ResponseEntity<DataResponse<AssetsPageResponse>> getAssets(User user);


    // ── 생성 ───────────────────────────────────────────────
    @Operation(
        summary = "자산그룹 생성",
        description = "자산 그룹을 생성합니다.",
        requestBody = @RequestBody(
            content = @Content(schema = @Schema(implementation = AssetsCreateRequest.class))
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "자산그룹 생성 성공",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
        }
    )
    ResponseEntity<ApiResponse> addAssets(AssetsCreateRequest request, User user);


    // ── 수정 ───────────────────────────────────────────────
    @Operation(
        summary = "자산그룹 수정",
        description = "자산 그룹을 수정합니다.",
        parameters = {
            @Parameter(
                name = "assetsId",
                description = "자산그룹 ID",
                required = true,
                example = "1"
            )
        },
        requestBody = @RequestBody(
            content = @Content(schema = @Schema(implementation = AssetsUpdateRequest.class))
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "자산그룹 수정 성공",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
        }
    )
    ResponseEntity<ApiResponse> updateAssets(Long assetsId, AssetsUpdateRequest request);


    // ── 삭제 ───────────────────────────────────────────────
    @Operation(
        summary = "자산그룹 삭제",
        description = "자산 그룹을 삭제합니다.",
        parameters = {
            @Parameter(
                name = "assetsId",
                description = "자산그룹 ID",
                required = true,
                example = "1"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "자산그룹 삭제 성공",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
        }
    )
    ResponseEntity<ApiResponse> deleteAssets(Long assetsId);
}
```
