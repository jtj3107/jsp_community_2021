<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 작성" />
<%@ include file="../part/head.jspf"%>

<section class="section section-member-join px-4">
	<div class="container mx-auto">

		<div class="card bordered shadow-lg">
			<div class="card-title">
				<a href="javascript:history.back();" class="cursor-pointer">
					<i class="fas fa-chevron-left"></i>
				</a>
				<span>회원가입</span>
			</div>

			<div class="px-4 py-4">
				<script>
					let MemberJoin__submitDone = false;
					function MemberJoin__submitDone(form) {
						if (MemberJoin__submitDone) {
							return;
						}

						if (form.loginId.value.length == 0) {
							alert('아이디를 입력해주세요.');
							form.loginId.focus();

							return;
						}

						if (form.loginPw.value.length == 0) {
							alert('비밀번호를 입력해주세요.');
							form.loginPw.focus();

							return;
						}
						
						if (form.name.value.length == 0) {
							alert('성함을 입력해주세요.');
							form.name.focus();

							return;
						}
						if (form.nickname.value.length == 0) {
							alert('닉네임을 입력해주세요.');
							form.nickname.focus();

							return;
						}
						if (form.email.value.length == 0) {
							alert('이메일을 입력해주세요.');
							form.email.focus();

							return;
						}
						if (form.cellphoneNo.value.length == 0) {
							alert('전화번호를 입력해주세요.');
							form.cellphoneNo.focus();

							return;
						}
												
						form.submit();
						MemberJoin__submitDone = true;
					}
				</script>
				<form action="../member/doJoin" method="POST" onsubmit="MemberJoin__submit(this); return false;">
					<div class="form-control">
						<label class="label">
							<span class="label-text">아이디</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginId" type="text" placeholder="사용하실 아이디를 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">비밀번호</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginPw" type="password" placeholder="사용하실 비밀번호를 입력해주세요." />
						</div>
					</div>
					
					<div class="form-control">
						<label class="label">
							<span class="label-text">성함</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="name" type="text" placeholder="성함을 입력해주세요." />
						</div>
					</div>
					
					<div class="form-control">
						<label class="label">
							<span class="label-text">닉네임</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="nickname" type="text" placeholder="닉네임을 입력해주세요." />
						</div>
					</div>
					
					<div class="form-control">
						<label class="label">
							<span class="label-text">이메일</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="email" type="text" placeholder="이메일을 입력해주세요." />
						</div>
					</div>
					
					<div class="form-control">
						<label class="label">
							<span class="label-text">전화번호</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="cellphoneNo" type="text" placeholder="휴대전화번호를 입력해주세요." />
						</div>
					</div>

					<div class="btns">
						<button type="submit" class="btn btn-link">회원가입</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<%@ include file="../part/foot.jspf"%>