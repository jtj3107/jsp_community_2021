<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 상세페이지" />
<%@ include file="../part/head.jspf"%>

<section class="section section-article-detail px-4">
  <div class="container mx-auto">
    <div class="card bordered shadow-lg">
      <div class="card-title">
        <a href="javascript:history.back();" class="cursor-pointer">
          <i class="fas fa-chevron-left"></i>
        </a>
        <span>게시물 상세페이지(${article.extra_boardName})</span>
      </div>

      <div class="px-4">

        <div class="py-4">
          <div class="grid gap-3" style="grid-template-columns: 100px 1fr;">
            <div>
              <img class="rounded-full w-full" src="${article.writerProfileImgUri}" alt="">
            </div>
            <div>
              <span class="badge badge-outline">제목</span>
              <div>${article.titleForPrint}</div>
            </div>
          </div>

          <div class="mt-3 grid sm:grid-cols-2 lg:grid-cols-4 gap-3">
            <div>
              <span class="badge badge-primary">번호</span>
              <span>${article.id}</span>
            </div>

            <div>
              <span class="badge badge-accent">작성자</span>
              <span>${article.extra__writerName}</span>
            </div>

            <div>
              <span class="badge">등록날짜</span>
              <span class="text-gray-600 text-light">${article.regDate}</span>
            </div>

            <div>
              <span class="badge">수정날짜</span>
              <span class="text-gray-600 text-light">${article.updateDate}</span>
            </div>

            <div>
              <span class="badge badge-secondary">좋아요</span>
              <span>${article.extra__likeOnlyPoint}</span>
            </div>

            <div>
              <span class="badge badge-secondary">싫어요</span>
              <span>${article.extra__dislikeOnlyPoint}</span>
            </div>
          </div>
          <div class="block mt-3 col-span-1 sm:col-span-2xl:col-span-3 f ">
            <span class="badge badge-outline">본문</span>
          </div>
          <div class="toast-ui-viewer">
            <script type="text/x-template">
                ${article.bodySummaryForPrintNlToBr}
              </script>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
<section class="section section-article-detail px-4 mt-4">
  <c:if test="${rq.notLogined}">
    <div class="container mx-auto">
      <div class="card bordered shadow-lg">
        <div>
          <i class="far fa-comment-dots"></i>
          <span>로그인후 사용 해주세요</span>
        </div>
      </div>
    </div>
  </c:if>
</section>

<section class="section section-article-detail px-4">
  <div class="btns container mx-auto">
    <c:if test="${article.extra__actorCanLike || article.extra__actorCanCancelDisLike}">
      <button type="button" class="btn btn-primary" id="likeUpDown">
        <span>
          <i class="far fa-thumbs-up"></i>
        </span>
        <span>좋아요</span>
      </button>
    </c:if>
    <c:if test="${article.extra__actorCanCancelLike}">
      <button type="button" class="btn btn-primary" id="likeUpDown">
        <span>
          <i class="fas fa-thumbs-up text-red-500"></i>
        </span>
        <span>좋아요</span>
      </button>
    </c:if>
    <c:if test="${article.extra__actorCanDisLike || article.extra__actorCanCancelLike}">
      <button type="button" class="btn btn-primary" id="dislikeUpDown">
        <span>
          <i class="far fa-thumbs-down"></i>
        </span>
        <span>싫어요</span>
      </button>
    </c:if>
    <c:if test="${article.extra__actorCanCancelDisLike}">
      <button type="button" class="btn btn-primary" id="dislikeUpDown">
        <span>
          <i class="fas fa-thumbs-down text-red-500"></i>
        </span>
        <span>싫어요</span>
      </button>
    </c:if>
    <c:if test="${article.extra__actorCanModify}">
      <a href="../article/modify?id=${article.id}" class="btn btn-link">
        <span>
          <i class="fas fa-edit"></i>
        </span>
        <span>수정</span>
      </a>
    </c:if>
    <c:if test="${article.extra__actorCanDelete}">
      <a onclick="if ( !confirm('정말로 삭제하시겠습니까?') ) return false;" href="../article/doDelete?id=${article.id}&boardId=${article.boardId}"
        class="btn btn-link">
        <span>
          <i class="fas fa-trash-alt"></i>
        </span>
        <span>삭제</span>
      </a>
    </c:if>
  </div>
  <script type="text/javascript">
			var relTypeCode = 'article';
			var relId = $
			{
				article.id
			};

			var data = '';
			data += "relTypeCode=" + relTypeCode;
			data += "&relId=" + relId;
			$("#likeUpDown").click(function() {
				$.ajax({
					type : "GET",
					url : "../like/doLike",
					data : data,
					dataType : "html",
					success : function(html) {
						location.reload();
					},

					error : function(xhr) {
						alert("Error Code : " + xhr.status);
					}
				});
			});

			$("#dislikeUpDown").click(function() {
				$.ajax({
					type : "GET",
					url : "../like/doDislike",
					data : data,
					dataType : "html",
					success : function(html) {
						location.reload();
					},

					error : function(xhr) {
						alert("Error Code : " + xhr.status);
					}
				});
			});
		</script>
</section>

<section class="section section-article-detail px-4 mt-4">
  <c:if test="${rq.logined}">
    <div class="container mx-auto">
      <div class="card bordered shadow-lg">
        <div class="card-title">
          <i class="far fa-comment-dots"></i>
          <span>댓글</span>
        </div>
        <div class="p-3">

          <div>
            <form method="POST">
              <input id="relId" type="hidden" name="relId" value="${article.id}" />

              <div class="form-control">
                <input class="input input-bordered w-full" id="reply" maxlength="100" name="reply" type="text" placeholder="댓글을 입력해주세요." />
              </div>
              <div class="btns">
                <button type="button" class="btn btn-link" id="btn">작성</button>
                <button type="button" class="btn btn-link">작성취소</button>
              </div>
              <script type="text/javascript">
															// jQuery는 JavaScript함수 사용가능 >> 그러나, jQuery메소드를 사용하는것을 권장!
															// [$] : jQuery, [#] : 아이디선택자, [.] : 클래스선택자
															$("#btn")
																	.click(
																			function() {
																				let Reply__DoWriteForm__submited = false;

																				if (Reply__DoWriteForm__submited) {
																					alert('처리중 입니다.');
																					return;
																				}

																				var reply = $(
																						"#reply")
																						.val()
																						.trim();
																				var relId = $(
																						"#relId")
																						.val()
																						.trim();
																				if (reply == "") {
																					alert('내용을 입력해주세요');
																					return;
																				}
																				$(
																						"#reply")
																						.val(
																								"");
																				$(
																						"#reply")
																						.focus();

																				var data = '';
																				data += "reply="
																						+ reply;
																				data += "&relId="
																						+ relId;
																				// [ajax기능을 구현하고싶은 경우 : 특정엘리먼트선택X >> jQuery 메소드 중 ajax를 호출할게요]
																				// $.ajax() : AJAX기능을 제공하는 jQuery메소드
																				//          : AJAX기능을 구현하기위해 필요한 정보(매개변수)를 object객체형식으로 전달
																				//          : ★ object객체의 특정 속성에 속성값과 함수를 설정하여 AJAX기능 구현
																				//          : 속성간의 구분[,] 속성값의 표현[:]

																				$
																						.ajax({
																							type : "POST", // 요청방식
																							url : "../reply/doWrite", // 요청문서의 url주소
																							data : data, // 전달할 변수와 값 설정
																							dataType : "html", // 요청에대한 응답결과의 데이터형식
																							// [응답결과를 어떻게 처리할 것인가? >> 익명함수설정]
																							// 1. 정상적인 응답결과를 제공받아 처리하는 함수를 설정
																							//     => 응답결과가 자동으로 함수의 매개변수에 전달되어 저장
																							success : function(
																									html) {
																								Reply__DoWriteForm__submited = true;
																								location
																										.reload();

																							},
																							// 2. 비정상적인 응답결과를 제공받아 처리하는 함수를 설정
																							//    => XMLHttpRequest객체가 자동으로 함수의 매개변수에 전달되어 저장
																							error : function(
																									xhr) {
																								alert("Error Code : "
																										+ xhr.status);
																							}
																						});

																			});
														</script>
            </form>
          </div>
        </div>
      </div>
    </div>
  </c:if>
  <div>
    <div class="container mx-auto p-2" id="contents">
      <span class="badge badge-primary">댓글</span>
      <span>${totalRepliesCount}개</span>
    </div>
    <c:forEach items="${replies}" var="reply">
      <div class="container mx-auto mt-1">
        <div class="card bordered shadow-lg">
          <div class="p-3">
            <div>
              <div>
                <div class="py-4">
                  <div class="px-4">
                    <span>${reply.extra__writerName}</span>
                  </div>
                  <div class="px-4">
                    <span>${reply.bodySummaryForPrint}</span>
                  </div>
                  <div class="px-4">
                    <span>${reply.regDate}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</section>

<%@ include file="../part/foot.jspf"%>