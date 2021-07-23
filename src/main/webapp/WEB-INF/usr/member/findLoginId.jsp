<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="로그인아이디 찾기" />
<%@ include file="../part/head.jspf"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<section class="section section-member-login flex-grow flex justify-center items-center">
	<div class="w-full max-w-md card-wrap">
		<div class="card bordered shadow-lg">
			<div class="card-title">
				<span> <i class="fas fa-sign-in-alt"></i>
				</span> <span>로그인 아이디 찾기</span>
			</div>

			<div class="find-login-id-form-box form-box px-4 py-4">
				<script>
					let DoFindLoginIdForm__submited = false;
					function DoFindLoginIdForm__submit(form) {
						if (DoFindLoginIdForm__submited) {
							alert('처리중 입니다.');
							return;
						}

						form.name.value = form.name.value.trim();

						if (form.name.value.length == 0) {
							alert('이름을 입력해주세요.');
							form.name.focus();

							return;
						}

						form.email.value = form.email.value.trim();

						if (form.email.value.length == 0) {
							alert('이메일을 입력해주세요.');
							form.email.focus();

							return;
						}

						form.loginPwReal.value = sha256(form.loginPw.value);

						form.loginPw.value = "";

						form.submit();
						DoFindLoginIdForm__submited = true;
					}
				</script>
				<form action="../member/doFindLoginId" method="POST" onsubmit="DoFindLoginIdForm__submit(this); return false;">
					<input type="hidden" name="loginPwReal" />

					<div class="form-control">
						<label class="label">
							<span class="label-text">이름</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="50" name="name" type="text" placeholder="이름을 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">이메일</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="50" name="email" type="email" placeholder="회원의 이메일주소를 입력해주세요." />
						</div>
					</div>

					<div class="btns">
						<button type="submit" class="btn btn-link">로그인아이디 찾기</button>
						<button class="btn btn-link" type="button" onclick="history.back();">뒤로가기</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<%@ include file="../part/foot.jspf"%>