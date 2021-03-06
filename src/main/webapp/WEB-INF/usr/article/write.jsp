<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 작성" />
<%@ include file="../part/head.jspf"%>

<section class="section section-article-write px-4">
	<div class="container mx-auto">

		<div class="card bordered shadow-lg">
			<div class="card-title">
				<a href="javascript:history.back();" class="cursor-pointer">
					<i class="fas fa-chevron-left"></i>
				</a>
				<span>게시물 작성</span>
			</div>

			<div class="px-4 py-4">
				<script>
					let ArticleWrite__submitDone = false;
					function ArticleWrite__submit(form) {
						if (ArticleWrite__submitDone) {
							return;

						}

						if (form.boardId.value == 0) {
							alert('게시판을 선택해주세요.');
							form.boardId.focus();

							return;
						}

						form.title.value = form.title.value.trim();

						if (form.title.value.length == 0) {
							alert('제목을 입력해주세요.');
							form.title.focus();

							return;
						}

						const editor = $(form).find('.toast-ui-editor').data('data-toast-editor');				
						const body = editor.getMarkdown().trim();
						
						if (body.length == 0) {
							alert('내용을 입력해주세요.');
							editor.focus();

							return;
						}
						
						form.body.value = body;

						form.submit();
						ArticleWrite__submitDone = true;
					}
				</script>
				<form action="../article/doWrite" method="POST" onsubmit="ArticleWrite__submit(this); return false;">
					<input type="hidden" name="redirectUri" value="../article/detail?id=[NEW_ID]" />
					<input type="hidden" name="body" />

					<div class="form-control">
						<select name="boardId" class="select select-bordered select-primary w-full max-w-md">
							<option value="0" disabled selected>게시판을 선택해주세요.</option>
							<option value="1">공지</option>
							<option value="2">자유</option>
						</select>
					</div>
					<div class="form-control">
						<label class="label">
							<span class="label-text">제목</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="title" type="text" placeholder="제목을 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">내용</span>
						</label>
						<div class="toast-ui-editor">
							<script type="text/x-template"></script>
						</div>
					</div>

					<div class="btns">
						<button type="submit" class="btn btn-link">작성</button>
						<button type="button" class="btn btn-link">작성취소</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<%@ include file="../part/foot.jspf"%>