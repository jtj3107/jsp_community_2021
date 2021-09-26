<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<c:set var="pageTitle" value="로그인" />
<%@ include file="../part/head.jspf"%>

<section class="section section-member-login flex-grow flex justify-center items-center">
	<div class="w-full max-w-md card-wrap">
		<div class="card bordered shadow-lg">
			<div class="card-title">
				<span>
					<i class="fas fa-sign-in-alt"></i>
				</span>
				<span>로그인</span>
			</div>

			<div class="px-4 py-4">
				<script>
					let MemberLogin__submitDone = false;
					function MemberLogin__submit(form) {
						if (MemberLogin__submitDone) {
							return;
						}

						form.loginId.value = form.loginId.value.trim();
						
						if (form.loginId.value.length == 0) {
							alert('아이디를 입력해주세요.');
							form.loginId.focus();

							return;
						}
						
						form.loginPw.value = form.loginPw.value.trim();

						if (form.loginPw.value.length == 0) {
							alert('비밀번호 입력해주세요.');
							form.loginPw.focus();

							return;
						}
						
						form.loginPwReal.value = sha256(form.loginPw.value);
						form.loginPw.value = "";

						form.submit();
						MemberLogin__submitDone = true;
					}
				</script>
				<form action="../member/doLogin" method="POST" onsubmit="MemberLogin__submit(this); return false;">
					<input type="hidden" name="redirectUri" value="${param.afterLoginUri}" />
					<input type="hidden" name="loginPwReal" />

					<div class="form-control">
						<label class="label">
							<span class="label-text">로그인아이디</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginId" type="text" placeholder="로그인아이디를 입력해주세요." value="${param.loginId}" />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">로그인비밀번호</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginPw" type="password" placeholder="로그인비밀번호를 입력해주세요." />
						</div>
					</div>

					<div class="btns">
						<button type="submit" class="btn btn-link">로그인</button>
						<a class="btn btn-link" href="../member/join">회원가입</a>
						<a class="btn btn-link" href="../member/findLoginId">아이디찾기</a>
						<a class="btn btn-link" href="../member/findLoginPw">비밀번호찾기</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<%@ include file="../part/foot.jspf"%>